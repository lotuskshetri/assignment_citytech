package com.payment.service;

import com.payment.dto.merchant.*;
import com.payment.dto.merchant.MerchantListResponse.PaginationMetadata;
import com.payment.entity.Merchant;
import com.payment.repository.MerchantRepository;
import com.payment.repository.TransactionRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class MerchantServiceImpl implements MerchantService {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;

    public MerchantServiceImpl(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
    }

    @Override
    public MerchantListResponse listMerchants(int limit, int offset, Optional<String> search) {
        LOG.info("Listing merchants with limit={}, offset={}, search={}", limit, offset, search.orElse("none"));

        List<MerchantStatsDTO> results;
        Long totalCount;

        if (search.isPresent() && !search.get().trim().isEmpty()) {
            String searchTerm = search.get().trim();
            results = transactionRepository.searchMerchantsWithStats(searchTerm, limit, offset);
            totalCount = transactionRepository.countMerchantsBySearch(searchTerm);
        } else {
            results = transactionRepository.getMerchantsWithStats(limit, offset);
            totalCount = transactionRepository.countDistinctMerchants();
        }

        LOG.debug("Query returned {} results", results != null ? results.size() : 0);

        List<MerchantSummary> merchants = new ArrayList<>();

        if (results != null && !results.isEmpty()) {
            for (MerchantStatsDTO dto : results) {
                try {
                    if (dto == null) {
                        LOG.warn("DTO is null, skipping");
                        continue;
                    }

                    MerchantSummary merchant = mapDTOToMerchantSummary(dto);
                    merchants.add(merchant);
                } catch (Exception e) {
                    LOG.error("Error processing merchant {}: {}", dto != null ? dto.getMerchantId() : "null", e.getMessage(), e);
                    // Continue processing other rows
                }
            }
        }

        PaginationMetadata pagination = new PaginationMetadata(totalCount, limit, offset);

        LOG.info("Found {} merchants (total: {})", merchants.size(), totalCount);

        return new MerchantListResponse(merchants, pagination);
    }

    @Override
    public Optional<MerchantSummary> getMerchantById(String merchantId) {
        LOG.info("Getting merchant details for merchantId={}", merchantId);

        try {
            List<MerchantStatsDTO> results = transactionRepository.getMerchantStatistics(merchantId);

            if (results == null || results.isEmpty()) {
                LOG.warn("Merchant not found: {}", merchantId);
                return Optional.empty();
            }

            MerchantStatsDTO dto = results.get(0);
            if (dto == null) {
                LOG.warn("Merchant result DTO is null: {}", merchantId);
                return Optional.empty();
            }

            MerchantSummary merchant = mapDTOToMerchantSummary(dto);
            return Optional.of(merchant);
        } catch (Exception e) {
            LOG.error("Error getting merchant {}: {}", merchantId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Map database result row to MerchantSummary object
     *
     * CRITICAL: Handles Object[] from native query results with defensive type checking
     *
     * ISSUE RESOLVED: ClassCastException prevention
     * - Problem: Native queries with GROUP BY can return inconsistent types
     * - Solution: Always check if result is Object[] before casting
     * - Use explicit PostgreSQL type casts (::bigint, ::numeric) in queries
     * - Add null checks and safe type conversions with toString()
     * - Wrap in try-catch for unexpected type issues
     */
    private MerchantSummary mapRowToMerchantSummary(Object[] row) {
        MerchantSummary merchant = new MerchantSummary();

        // Row structure from query:
        // 0: merchant_id (String)
        // 1: total_transactions (Long/BigInteger)
        // 2: total_revenue (BigDecimal/Numeric)
        // 3: completed_count (Long/BigInteger)
        // 4: failed_count (Long/BigInteger)
        // 5: pending_count (Long/BigInteger)
        // 6: last_transaction_date (Date)
        // 7: first_transaction_date (Date)

        try {
            String merchantId = row[0] != null ? row[0].toString() : null;
            Long totalTransactions = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            BigDecimal totalRevenue = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
            Long completedCount = row[3] != null ? ((Number) row[3]).longValue() : 0L;
            Long failedCount = row[4] != null ? ((Number) row[4]).longValue() : 0L;
            Long pendingCount = row[5] != null ? ((Number) row[5]).longValue() : 0L;
            Date lastTxnDate = row[6] != null ? (Date) row[6] : null;
            Date firstTxnDate = row[7] != null ? (Date) row[7] : null;

        merchant.setMerchantId(merchantId);
        merchant.setMerchantName(generateMerchantName(merchantId));
        merchant.setTotalTransactions(totalTransactions);
        merchant.setTotalRevenue(totalRevenue);
        merchant.setCompletedCount(completedCount);
        merchant.setFailedCount(failedCount);
        merchant.setPendingCount(pendingCount);

        // Calculate success rate
        if (totalTransactions > 0) {
            double successRate = (completedCount.doubleValue() / totalTransactions.doubleValue()) * 100;
            merchant.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
        } else {
            merchant.setSuccessRate(0.0);
        }

        // Calculate average transaction amount
        if (totalTransactions > 0 && totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgAmount = totalRevenue.divide(new BigDecimal(totalTransactions), 2, RoundingMode.HALF_UP);
            merchant.setAverageTransactionAmount(avgAmount);
        } else {
            merchant.setAverageTransactionAmount(BigDecimal.ZERO);
        }

        // Set dates
        if (lastTxnDate != null) {
            merchant.setLastTransactionDate(lastTxnDate.toLocalDate());
        }
        if (firstTxnDate != null) {
            merchant.setFirstTransactionDate(firstTxnDate.toLocalDate());
        }

            // Determine merchant status based on recent activity
            merchant.setStatus(determineMerchantStatus(lastTxnDate));

        } catch (Exception e) {
            LOG.error("Error mapping row to MerchantSummary. Row data: {}", (Object) row, e);
            throw new RuntimeException("Failed to map merchant data", e);
        }

        return merchant;
    }

    /**
     * Generate a display name from merchant ID
     * For now, we use the merchant ID as display name
     * In a real system, this would come from a merchants table
     */
    private String generateMerchantName(String merchantId) {
        // Extract number from MCH-00001 format and create display name
        if (merchantId != null && merchantId.startsWith("MCH-")) {
            String number = merchantId.substring(4);
            return "Merchant " + number;
        }
        return merchantId;
    }

    /**
     * Determine merchant status based on last transaction date
     */
    private String determineMerchantStatus(Date lastTransactionDate) {
        if (lastTransactionDate == null) {
            return "inactive";
        }

        LocalDate lastTxn = lastTransactionDate.toLocalDate();
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        if (lastTxn.isAfter(thirtyDaysAgo)) {
            return "active";
        } else {
            return "inactive";
        }
    }

    /**
     * Map MerchantStatsDTO to MerchantSummary object
     */
    private MerchantSummary mapDTOToMerchantSummary(MerchantStatsDTO dto) {
        MerchantSummary merchant = new MerchantSummary();

        merchant.setMerchantId(dto.getMerchantId());
        merchant.setMerchantName(generateMerchantName(dto.getMerchantId()));
        merchant.setTotalTransactions(dto.getTotalTransactions() != null ? dto.getTotalTransactions() : 0L);
        merchant.setTotalRevenue(dto.getTotalRevenue() != null ? dto.getTotalRevenue() : BigDecimal.ZERO);
        merchant.setCompletedCount(dto.getCompletedCount() != null ? dto.getCompletedCount() : 0L);
        merchant.setFailedCount(dto.getFailedCount() != null ? dto.getFailedCount() : 0L);
        merchant.setPendingCount(dto.getPendingCount() != null ? dto.getPendingCount() : 0L);

        // Calculate success rate
        long totalTxns = merchant.getTotalTransactions();
        if (totalTxns > 0) {
            double successRate = (merchant.getCompletedCount().doubleValue() / totalTxns) * 100;
            merchant.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
        } else {
            merchant.setSuccessRate(0.0);
        }

        // Calculate average transaction amount
        if (totalTxns > 0 && merchant.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgAmount = merchant.getTotalRevenue().divide(new BigDecimal(totalTxns), 2, RoundingMode.HALF_UP);
            merchant.setAverageTransactionAmount(avgAmount);
        } else {
            merchant.setAverageTransactionAmount(BigDecimal.ZERO);
        }

        // Set dates
        if (dto.getLastTransactionDate() != null) {
            merchant.setLastTransactionDate(dto.getLastTransactionDate().toLocalDate());
        }
        if (dto.getFirstTransactionDate() != null) {
            merchant.setFirstTransactionDate(dto.getFirstTransactionDate().toLocalDate());
        }

        // Determine merchant status based on recent activity
        merchant.setStatus(determineMerchantStatus(dto.getLastTransactionDate()));

        return merchant;
    }

    @Override
    @Transactional
    public MerchantResponse createMerchant(CreateMerchantRequest request) {
        LOG.info("Creating new merchant: {}", request.getBusinessName());

        // Generate unique merchant ID
        String merchantId = generateNewMerchantId();

        // Check if merchant ID already exists (should not happen)
        if (merchantRepository.existsByMerchantId(merchantId)) {
            throw new RuntimeException("Generated merchant ID already exists: " + merchantId);
        }

        // Create merchant entity
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);
        merchant.setMerchantName(request.getMerchantName());
        merchant.setBusinessName(request.getBusinessName());
        merchant.setBusinessType(request.getBusinessType());
        merchant.setTaxId(request.getTaxId());
        merchant.setEmail(request.getEmail());
        merchant.setPhone(request.getPhone());
        merchant.setWebsite(request.getWebsite());
        merchant.setAddressLine1(request.getAddressLine1());
        merchant.setAddressLine2(request.getAddressLine2());
        merchant.setCity(request.getCity());
        merchant.setState(request.getState());
        merchant.setPostalCode(request.getPostalCode());
        merchant.setCountry(request.getCountry());
        merchant.setRegistrationDate(request.getRegistrationDate() != null ? request.getRegistrationDate() : LocalDate.now());
        merchant.setIndustry(request.getIndustry());
        merchant.setAnnualRevenueRange(request.getAnnualRevenueRange());
        merchant.setEmployeeCountRange(request.getEmployeeCountRange());
        merchant.setStatus(request.getStatus());
        merchant.setRiskLevel(request.getRiskLevel());
        merchant.setNotes(request.getNotes());

        // Save merchant
        Merchant savedMerchant = merchantRepository.save(merchant);

        LOG.info("Merchant created successfully: {}", savedMerchant.getMerchantId());

        return mapToMerchantResponse(savedMerchant);
    }

    @Override
    @Transactional
    public MerchantResponse updateMerchant(String merchantId, UpdateMerchantRequest request) {
        LOG.info("Updating merchant: {}", merchantId);

        // Find existing merchant
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant not found: " + merchantId));

        // Update fields if provided
        if (request.getMerchantName() != null) {
            merchant.setMerchantName(request.getMerchantName());
        }
        if (request.getBusinessName() != null) {
            merchant.setBusinessName(request.getBusinessName());
        }
        if (request.getBusinessType() != null) {
            merchant.setBusinessType(request.getBusinessType());
        }
        if (request.getTaxId() != null) {
            merchant.setTaxId(request.getTaxId());
        }
        if (request.getEmail() != null) {
            merchant.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            merchant.setPhone(request.getPhone());
        }
        if (request.getWebsite() != null) {
            merchant.setWebsite(request.getWebsite());
        }
        if (request.getAddressLine1() != null) {
            merchant.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            merchant.setAddressLine2(request.getAddressLine2());
        }
        if (request.getCity() != null) {
            merchant.setCity(request.getCity());
        }
        if (request.getState() != null) {
            merchant.setState(request.getState());
        }
        if (request.getPostalCode() != null) {
            merchant.setPostalCode(request.getPostalCode());
        }
        if (request.getCountry() != null) {
            merchant.setCountry(request.getCountry());
        }
        if (request.getRegistrationDate() != null) {
            merchant.setRegistrationDate(request.getRegistrationDate());
        }
        if (request.getIndustry() != null) {
            merchant.setIndustry(request.getIndustry());
        }
        if (request.getAnnualRevenueRange() != null) {
            merchant.setAnnualRevenueRange(request.getAnnualRevenueRange());
        }
        if (request.getEmployeeCountRange() != null) {
            merchant.setEmployeeCountRange(request.getEmployeeCountRange());
        }
        if (request.getStatus() != null) {
            merchant.setStatus(request.getStatus());
        }
        if (request.getRiskLevel() != null) {
            merchant.setRiskLevel(request.getRiskLevel());
        }
        if (request.getNotes() != null) {
            merchant.setNotes(request.getNotes());
        }

        // Update merchant
        Merchant updatedMerchant = merchantRepository.update(merchant);

        LOG.info("Merchant updated successfully: {}", updatedMerchant.getMerchantId());

        return mapToMerchantResponse(updatedMerchant);
    }

    @Override
    public Optional<MerchantResponse> getMerchantDetails(String merchantId) {
        LOG.info("Getting merchant details: {}", merchantId);

        Optional<Merchant> merchantOpt = merchantRepository.findById(merchantId);

        if (merchantOpt.isEmpty()) {
            return Optional.empty();
        }

        MerchantResponse response = mapToMerchantResponse(merchantOpt.get());

        // Also fetch transaction statistics and add to response
        try {
            Optional<MerchantSummary> stats = getMerchantById(merchantId);
            if (stats.isPresent()) {
                MerchantSummary summary = stats.get();
                response.setTotalTransactions(summary.getTotalTransactions());

                // Convert BigDecimal to Double
                response.setTotalRevenue(summary.getTotalRevenue() != null ?
                    summary.getTotalRevenue().doubleValue() : null);

                response.setCompletedCount(summary.getCompletedCount());
                response.setFailedCount(summary.getFailedCount());
                response.setPendingCount(summary.getPendingCount());
                response.setSuccessRate(summary.getSuccessRate());

                // Convert BigDecimal to Double
                response.setAverageTransactionAmount(summary.getAverageTransactionAmount() != null ?
                    summary.getAverageTransactionAmount().doubleValue() : null);

                // Convert LocalDate to String
                response.setLastTransactionDate(summary.getLastTransactionDate() != null ?
                    summary.getLastTransactionDate().toString() : null);
                response.setFirstTransactionDate(summary.getFirstTransactionDate() != null ?
                    summary.getFirstTransactionDate().toString() : null);
            }
        } catch (Exception e) {
            LOG.warn("Could not fetch transaction statistics for merchant {}: {}", merchantId, e.getMessage());
            // Continue without stats - profile data is still valid
        }

        return Optional.of(response);
    }

    /**
     * Generate a new unique merchant ID
     */
    private String generateNewMerchantId() {
        // Get count of existing merchants
        Long count = merchantRepository.count();

        // Generate MCH-XXXXX format
        return "MCH-" + String.format("%05d", count + 51); // Start from 51 since we have 50 existing
    }

    /**
     * Map Merchant entity to MerchantResponse DTO
     */
    private MerchantResponse mapToMerchantResponse(Merchant merchant) {
        MerchantResponse response = new MerchantResponse();
        response.setMerchantId(merchant.getMerchantId());
        response.setMerchantName(merchant.getMerchantName());
        response.setBusinessName(merchant.getBusinessName());
        response.setBusinessType(merchant.getBusinessType());
        response.setTaxId(merchant.getTaxId());
        response.setEmail(merchant.getEmail());
        response.setPhone(merchant.getPhone());
        response.setWebsite(merchant.getWebsite());
        response.setAddressLine1(merchant.getAddressLine1());
        response.setAddressLine2(merchant.getAddressLine2());
        response.setCity(merchant.getCity());
        response.setState(merchant.getState());
        response.setPostalCode(merchant.getPostalCode());
        response.setCountry(merchant.getCountry());
        response.setRegistrationDate(merchant.getRegistrationDate());
        response.setIndustry(merchant.getIndustry());
        response.setAnnualRevenueRange(merchant.getAnnualRevenueRange());
        response.setEmployeeCountRange(merchant.getEmployeeCountRange());
        response.setStatus(merchant.getStatus());
        response.setRiskLevel(merchant.getRiskLevel());
        response.setCreatedAt(merchant.getCreatedAt());
        response.setUpdatedAt(merchant.getUpdatedAt());
        response.setCreatedBy(merchant.getCreatedBy());
        response.setNotes(merchant.getNotes());
        return response;
    }
}


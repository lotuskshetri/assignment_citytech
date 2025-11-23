package com.payment.controller;

import com.payment.entity.TransactionMaster;
import com.payment.repository.TransactionRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Transaction-focused controller for querying all transactions with filters
 */
@Controller("/api/v1/transactions")
@Tag(name = "Transactions")
public class TransactionsController {

    private final TransactionRepository transactionRepository;

    public TransactionsController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Get
    @Operation(
        summary = "Get all transactions with filters",
        description = "Returns paginated list of transactions with optional filters for merchant, status, and date range"
    )
    public HttpResponse<Map<String, Object>> getAllTransactions(
            @QueryValue Optional<String> merchantId,
            @QueryValue Optional<String> status,
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate,
            @QueryValue(defaultValue = "0") Integer page,
            @QueryValue(defaultValue = "20") Integer size
    ) {
        Pageable pageable = Pageable.from(page, size);
        Page<TransactionMaster> transactionsPage;

        // Default date range if not provided
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());
        Date sqlStartDate = Date.valueOf(start);
        Date sqlEndDate = Date.valueOf(end);

        // Query based on filters
        if (merchantId.isPresent() && status.isPresent()) {
            // Filter by merchant, status, and date range
            transactionsPage = transactionRepository.findByMerchantIdAndStatusAndDateRange(
                merchantId.get(), status.get(), sqlStartDate, sqlEndDate, pageable);
        } else if (merchantId.isPresent()) {
            // Filter by merchant and date range
            transactionsPage = transactionRepository.findByMerchantIdAndDateRange(
                merchantId.get(), sqlStartDate, sqlEndDate, pageable);
        } else if (status.isPresent()) {
            // Filter by status only
            transactionsPage = transactionRepository.findByStatusAndDateRange(
                status.get(), sqlStartDate, sqlEndDate, pageable);
        } else {
            // All transactions in date range
            transactionsPage = transactionRepository.findByTxnDateBetween(
                sqlStartDate, sqlEndDate, pageable);
        }

        // Calculate summary statistics across ALL filtered transactions (not just current page)
        Map<String, Object> summary = calculateSummary(merchantId, status, sqlStartDate, sqlEndDate);

        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactionsPage.getContent());
        response.put("totalTransactions", transactionsPage.getTotalSize());
        response.put("page", transactionsPage.getPageNumber());
        response.put("size", transactionsPage.getSize());
        response.put("totalPages", transactionsPage.getTotalPages());
        response.put("summary", summary); // Add summary statistics

        return HttpResponse.ok(response);
    }

    /**
     * Calculate summary statistics for filtered transactions
     */
    private Map<String, Object> calculateSummary(Optional<String> merchantId, Optional<String> status,
                                                   Date startDate, Date endDate) {
        // Fetch all transactions matching the filter (not paginated) for statistics
        java.util.List<TransactionMaster> allTransactions;

        if (merchantId.isPresent() && status.isPresent()) {
            allTransactions = transactionRepository.findByMerchantIdAndStatusAndDateRange(
                merchantId.get(), status.get(), startDate, endDate);
        } else if (merchantId.isPresent()) {
            allTransactions = transactionRepository.findByMerchantIdAndDateRange(
                merchantId.get(), startDate, endDate);
        } else if (status.isPresent()) {
            allTransactions = transactionRepository.findByStatusAndDateRange(
                status.get(), startDate, endDate);
        } else {
            allTransactions = transactionRepository.findByDateRangeForAnalytics(startDate, endDate);
        }

        // Calculate statistics
        long totalCount = allTransactions.size();
        double totalAmount = allTransactions.stream()
            .mapToDouble(txn -> txn.getAmount().doubleValue())
            .sum();

        long completedCount = allTransactions.stream()
            .filter(txn -> "completed".equalsIgnoreCase(txn.getStatus()))
            .count();

        long pendingCount = allTransactions.stream()
            .filter(txn -> "pending".equalsIgnoreCase(txn.getStatus()))
            .count();

        long failedCount = allTransactions.stream()
            .filter(txn -> "failed".equalsIgnoreCase(txn.getStatus()))
            .count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCount", totalCount);
        summary.put("totalAmount", totalAmount);
        summary.put("completedCount", completedCount);
        summary.put("pendingCount", pendingCount);
        summary.put("failedCount", failedCount);

        return summary;
    }
}


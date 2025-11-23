package com.payment.repository;

import com.payment.entity.TransactionMaster;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

/**
 * Repository for TransactionMaster entities with analytics queries and pagination support.
 */
@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TransactionRepository extends PageableRepository<TransactionMaster, Long> {

    // Basic finder methods
    List<TransactionMaster> findByMerchantId(String merchantId);

    List<TransactionMaster> findByTxnDateBetween(Date startDate, Date endDate);

    // Count and aggregation methods
    @Query(value = "SELECT COUNT(*) FROM operators.transaction_master WHERE txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    Long countByDateRange(Date startDate, Date endDate);

    @Query(value = "SELECT SUM(amount) FROM operators.transaction_master WHERE txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    BigDecimal sumAmountByDateRange(Date startDate, Date endDate);

    @Query(value = "SELECT AVG(amount) FROM operators.transaction_master WHERE txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    BigDecimal avgAmountByDateRange(Date startDate, Date endDate);

    // Fetch transactions for analytics processing in service layer
    @Query(value = "SELECT * FROM operators.transaction_master WHERE txn_date BETWEEN :startDate AND :endDate ORDER BY txn_date",
           nativeQuery = true)
    List<TransactionMaster> findByDateRangeForAnalytics(Date startDate, Date endDate);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE merchant_id = :merchantId AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    List<TransactionMaster> findByMerchantIdAndDateRange(String merchantId, Date startDate, Date endDate);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE EXTRACT(YEAR FROM txn_date) IN (:year1, :year2)",
           nativeQuery = true)
    List<TransactionMaster> findByYears(int year1, int year2);


    // Recent transactions for real-time updates
    @Query(value = "SELECT * FROM operators.transaction_master " +
           "WHERE created_at > :since " +
           "ORDER BY created_at DESC " +
           "LIMIT :limit",
           nativeQuery = true)
    List<TransactionMaster> findRecentTransactions(Instant since, int limit);

    // Pageable queries for listing transactions (auto-generated, no @Query needed)
    Page<TransactionMaster> findByMerchantId(String merchantId, Pageable pageable);

    Page<TransactionMaster> findByTxnDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE status = :status ORDER BY txn_date DESC",
           countQuery = "SELECT COUNT(*) FROM operators.transaction_master WHERE status = :status",
           nativeQuery = true)
    Page<TransactionMaster> findByStatus(String status, Pageable pageable);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE merchant_id = :merchantId AND txn_date BETWEEN :startDate AND :endDate ORDER BY txn_date DESC",
           countQuery = "SELECT COUNT(*) FROM operators.transaction_master WHERE merchant_id = :merchantId AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    Page<TransactionMaster> findByMerchantIdAndDateRange(String merchantId, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE card_type = :cardType AND txn_date BETWEEN :startDate AND :endDate ORDER BY txn_date DESC",
           countQuery = "SELECT COUNT(*) FROM operators.transaction_master WHERE card_type = :cardType AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    Page<TransactionMaster> findByCardTypeAndDateRange(String cardType, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE status = :status AND txn_date BETWEEN :startDate AND :endDate ORDER BY txn_date DESC",
           countQuery = "SELECT COUNT(*) FROM operators.transaction_master WHERE status = :status AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    Page<TransactionMaster> findByStatusAndDateRange(String status, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE merchant_id = :merchantId AND status = :status AND txn_date BETWEEN :startDate AND :endDate ORDER BY txn_date DESC",
           countQuery = "SELECT COUNT(*) FROM operators.transaction_master WHERE merchant_id = :merchantId AND status = :status AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    Page<TransactionMaster> findByMerchantIdAndStatusAndDateRange(String merchantId, String status, Date startDate, Date endDate, Pageable pageable);

    // Non-paginated versions for summary calculations
    @Query(value = "SELECT * FROM operators.transaction_master WHERE status = :status AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    List<TransactionMaster> findByStatusAndDateRange(String status, Date startDate, Date endDate);

    @Query(value = "SELECT * FROM operators.transaction_master WHERE merchant_id = :merchantId AND status = :status AND txn_date BETWEEN :startDate AND :endDate",
           nativeQuery = true)
    List<TransactionMaster> findByMerchantIdAndStatusAndDateRange(String merchantId, String status, Date startDate, Date endDate);

    // Merchant aggregation queries - using raw JDBC query execution
    // IMPORTANT: Native queries with aggregations return List<Object[]>
    // Each Object[] contains row data that must be carefully cast to expected types
    // Use explicit PostgreSQL casts (::bigint, ::numeric) for consistent type handling
    @Query(value = "SELECT DISTINCT merchant_id FROM operators.transaction_master ORDER BY merchant_id",
           nativeQuery = true)
    List<String> findDistinctMerchantIds();

    @Query(value = "SELECT merchant_id, " +
           "COUNT(*)::bigint as total_transactions, " +
           "SUM(amount)::numeric as total_revenue, " +
           "SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::bigint as completed_count, " +
           "SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END)::bigint as failed_count, " +
           "SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END)::bigint as pending_count, " +
           "MAX(txn_date) as last_transaction_date, " +
           "MIN(txn_date) as first_transaction_date " +
           "FROM operators.transaction_master " +
           "WHERE merchant_id = :merchantId " +
           "GROUP BY merchant_id",
           nativeQuery = true,
           readOnly = true)
    List<com.payment.dto.merchant.MerchantStatsDTO> getMerchantStatistics(String merchantId);

    @Query(value = "SELECT merchant_id, " +
           "COUNT(*)::bigint as total_transactions, " +
           "SUM(amount)::numeric as total_revenue, " +
           "SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::bigint as completed_count, " +
           "SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END)::bigint as failed_count, " +
           "SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END)::bigint as pending_count, " +
           "MAX(txn_date) as last_transaction_date, " +
           "MIN(txn_date) as first_transaction_date " +
           "FROM operators.transaction_master " +
           "GROUP BY merchant_id " +
           "ORDER BY merchant_id " +
           "LIMIT :limit OFFSET :offset",
           nativeQuery = true,
           readOnly = true)
    List<com.payment.dto.merchant.MerchantStatsDTO> getMerchantsWithStats(int limit, int offset);

    @Query(value = "SELECT COUNT(DISTINCT merchant_id) FROM operators.transaction_master",
           nativeQuery = true)
    Long countDistinctMerchants();

    @Query(value = "SELECT merchant_id, " +
           "COUNT(*)::bigint as total_transactions, " +
           "SUM(amount)::numeric as total_revenue, " +
           "SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::bigint as completed_count, " +
           "SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END)::bigint as failed_count, " +
           "SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END)::bigint as pending_count, " +
           "MAX(txn_date) as last_transaction_date, " +
           "MIN(txn_date) as first_transaction_date " +
           "FROM operators.transaction_master " +
           "WHERE LOWER(merchant_id) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "GROUP BY merchant_id " +
           "ORDER BY merchant_id " +
           "LIMIT :limit OFFSET :offset",
           nativeQuery = true,
           readOnly = true)
    List<com.payment.dto.merchant.MerchantStatsDTO> searchMerchantsWithStats(String search, int limit, int offset);

    @Query(value = "SELECT COUNT(DISTINCT merchant_id) FROM operators.transaction_master " +
           "WHERE LOWER(merchant_id) LIKE LOWER(CONCAT('%', :search, '%'))",
           nativeQuery = true)
    Long countMerchantsBySearch(String search);
}

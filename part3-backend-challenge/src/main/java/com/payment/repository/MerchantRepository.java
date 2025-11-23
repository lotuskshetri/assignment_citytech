package com.payment.repository;

import com.payment.entity.Merchant;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.Optional;

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface MerchantRepository extends PageableRepository<Merchant, String> {

    Optional<Merchant> findByMerchantId(String merchantId);

    List<Merchant> findByStatus(String status);

    Page<Merchant> findByStatus(String status, Pageable pageable);

    List<Merchant> findByBusinessNameContainsIgnoreCase(String businessName);

    Page<Merchant> findByBusinessNameContainsIgnoreCase(String businessName, Pageable pageable);

    @Query(value = "SELECT * FROM operators.merchants WHERE " +
            "merchant_name ILIKE '%' || :search || '%' OR " +
            "business_name ILIKE '%' || :search || '%' OR " +
            "merchant_id ILIKE '%' || :search || '%'",
            countQuery = "SELECT COUNT(*) FROM operators.merchants WHERE " +
                    "merchant_name ILIKE '%' || :search || '%' OR " +
                    "business_name ILIKE '%' || :search || '%' OR " +
                    "merchant_id ILIKE '%' || :search || '%'",
            nativeQuery = true)
    Page<Merchant> searchMerchants(String search, Pageable pageable);

    @Query(value = "SELECT * FROM operators.merchants WHERE status = :status AND risk_level = :riskLevel",
            nativeQuery = true)
    List<Merchant> findByStatusAndRiskLevel(String status, String riskLevel);

    @Query(value = "SELECT COUNT(*) FROM operators.merchants WHERE status = :status",
            nativeQuery = true)
    Long countByStatus(String status);

    boolean existsByMerchantId(String merchantId);
}
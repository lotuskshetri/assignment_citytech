package com.payment.service;

import com.payment.dto.merchant.CreateMerchantRequest;
import com.payment.dto.merchant.MerchantListResponse;
import com.payment.dto.merchant.MerchantResponse;
import com.payment.dto.merchant.MerchantSummary;
import com.payment.dto.merchant.UpdateMerchantRequest;

import java.util.Optional;

public interface MerchantService {

    /**
     * Get paginated list of merchants with statistics
     *
     * @param limit Maximum number of merchants per page
     * @param offset Number of merchants to skip
     * @param search Optional search term to filter merchants by ID
     * @return Paginated merchant list with statistics
     */
    MerchantListResponse listMerchants(int limit, int offset, Optional<String> search);

    /**
     * Get detailed statistics for a specific merchant
     *
     * @param merchantId Merchant ID
     * @return Merchant summary with detailed statistics
     */
    Optional<MerchantSummary> getMerchantById(String merchantId);

    /**
     * Create a new merchant
     *
     * @param request Merchant creation request
     * @return Created merchant response
     */
    MerchantResponse createMerchant(CreateMerchantRequest request);

    /**
     * Update an existing merchant
     *
     * @param merchantId Merchant ID to update
     * @param request Merchant update request
     * @return Updated merchant response
     */
    MerchantResponse updateMerchant(String merchantId, UpdateMerchantRequest request);

    /**
     * Get merchant full details (from merchants table)
     *
     * @param merchantId Merchant ID
     * @return Merchant response with full details
     */
    Optional<MerchantResponse> getMerchantDetails(String merchantId);
}


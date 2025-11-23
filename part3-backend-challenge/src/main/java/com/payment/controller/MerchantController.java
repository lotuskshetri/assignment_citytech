package com.payment.controller;

import com.payment.dto.merchant.*;
import com.payment.service.MerchantService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

/**
 * REST controller for merchant management operations.
 * Provides endpoints to list merchants with statistics and view merchant details.
 */
@Controller("/api/v1/merchants")
@Tag(name = "Merchants", description = "Merchant management and statistics APIs")
public class MerchantController {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantController.class);

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * Get paginated list of merchants with statistics.
     *
     * @param limit Maximum number of merchants per page (default: 10)
     * @param offset Number of merchants to skip (default: 0)
     * @param search Optional search term to filter merchants by ID
     * @return Paginated list of merchants with statistics
     */
    @Get
    @Operation(
        summary = "List merchants with statistics",
        description = "Returns a paginated list of merchants with aggregated transaction statistics including total revenue, transaction counts, success rates, and activity status. Supports search by merchant ID."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = @Content(schema = @Schema(implementation = MerchantListResponse.class))
    )
    public HttpResponse<MerchantListResponse> listMerchants(
            @Parameter(description = "Maximum number of merchants to return per page", example = "10")
            @QueryValue(defaultValue = "10") int limit,

            @Parameter(description = "Number of merchants to skip for pagination", example = "0")
            @QueryValue(defaultValue = "0") int offset,

            @Parameter(description = "Search term to filter merchants by ID (case-insensitive)", example = "MCH-00001")
            @QueryValue Optional<String> search) {

        LOG.info("GET /api/v1/merchants - limit={}, offset={}, search={}",
                 limit, offset, search.orElse("none"));

        // Validate pagination parameters
        if (limit < 1 || limit > 100) {
            limit = 10;
        }
        if (offset < 0) {
            offset = 0;
        }

        MerchantListResponse response = merchantService.listMerchants(limit, offset, search);

        return HttpResponse.ok(response);
    }

    /**
     * Get detailed information for a specific merchant.
     *
     * @param id Merchant ID
     * @return Merchant details with statistics
     */
    @Get("/{id}")
    @Operation(
        summary = "Get merchant details",
        description = "Returns complete merchant profile with contact details, business information, and transaction statistics"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Merchant found",
        content = @Content(schema = @Schema(implementation = MerchantResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Merchant not found"
    )
    public HttpResponse<MerchantResponse> getMerchantById(
            @Parameter(description = "Merchant ID", example = "MCH-00001", required = true)
            @PathVariable String id) {

        LOG.info("GET /api/v1/merchants/{} - Getting full merchant details", id);

        // Get full merchant profile from merchants table
        Optional<MerchantResponse> merchant = merchantService.getMerchantDetails(id);

        if (merchant.isPresent()) {
            return HttpResponse.ok(merchant.get());
        } else {
            return HttpResponse.notFound();
        }
    }

    /**
     * Create a new merchant
     *
     * @param request Merchant creation request
     * @return Created merchant response
     */
    @Post
    @Operation(
        summary = "Create new merchant",
        description = "Register a new merchant with business and contact information"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Merchant created successfully",
        content = @Content(schema = @Schema(implementation = MerchantResponse.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request data"
    )
    public HttpResponse<MerchantResponse> createMerchant(@Valid @Body CreateMerchantRequest request) {
        LOG.info("POST /api/v1/merchants - Creating merchant: {}", request.getBusinessName());

        MerchantResponse response = merchantService.createMerchant(request);

        return HttpResponse.created(response, URI.create("/api/v1/merchants/" + response.getMerchantId()));
    }

    /**
     * Update an existing merchant
     *
     * @param id Merchant ID
     * @param request Merchant update request
     * @return Updated merchant response
     */
    @Put("/{id}")
    @Operation(
        summary = "Update merchant",
        description = "Update merchant information including contact details, address, and status"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Merchant updated successfully",
        content = @Content(schema = @Schema(implementation = MerchantResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Merchant not found"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request data"
    )
    public HttpResponse<MerchantResponse> updateMerchant(
            @Parameter(description = "Merchant ID", example = "MCH-00001", required = true)
            @PathVariable String id,
            @Valid @Body UpdateMerchantRequest request) {

        LOG.info("PUT /api/v1/merchants/{} - Updating merchant", id);

        try {
            MerchantResponse response = merchantService.updateMerchant(id, request);
            return HttpResponse.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return HttpResponse.notFound();
            }
            throw e;
        }
    }

    /**
     * Get full merchant details (from merchants table)
     *
     * @param id Merchant ID
     * @return Merchant response with full business information
     */
    @Get("/{id}/details")
    @Operation(
        summary = "Get full merchant details",
        description = "Returns complete merchant information from the merchants table including business and contact details"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Merchant found",
        content = @Content(schema = @Schema(implementation = MerchantResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Merchant not found"
    )
    public HttpResponse<MerchantResponse> getMerchantDetails(
            @Parameter(description = "Merchant ID", example = "MCH-00001", required = true)
            @PathVariable String id) {

        LOG.info("GET /api/v1/merchants/{}/details - Getting full merchant details", id);

        Optional<MerchantResponse> merchant = merchantService.getMerchantDetails(id);

        if (merchant.isPresent()) {
            return HttpResponse.ok(merchant.get());
        } else {
            return HttpResponse.notFound();
        }
    }
}


package com.payment.dto.merchant;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class MerchantListResponse {
    private List<MerchantSummary> merchants;
    private PaginationMetadata pagination;

    public MerchantListResponse() {
    }

    public MerchantListResponse(List<MerchantSummary> merchants, PaginationMetadata pagination) {
        this.merchants = merchants;
        this.pagination = pagination;
    }

    public List<MerchantSummary> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantSummary> merchants) {
        this.merchants = merchants;
    }

    public PaginationMetadata getPagination() {
        return pagination;
    }

    public void setPagination(PaginationMetadata pagination) {
        this.pagination = pagination;
    }

    @Serdeable
    public static class PaginationMetadata {
        private Long total;
        private Integer limit;
        private Integer offset;
        private Integer currentPage;
        private Integer totalPages;
        private Boolean hasNext;
        private Boolean hasPrevious;

        public PaginationMetadata() {
        }

        public PaginationMetadata(Long total, Integer limit, Integer offset) {
            this.total = total;
            this.limit = limit;
            this.offset = offset;
            this.currentPage = (offset / limit) + 1;
            this.totalPages = (int) Math.ceil((double) total / limit);
            this.hasNext = offset + limit < total;
            this.hasPrevious = offset > 0;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Boolean getHasNext() {
            return hasNext;
        }

        public void setHasNext(Boolean hasNext) {
            this.hasNext = hasNext;
        }

        public Boolean getHasPrevious() {
            return hasPrevious;
        }

        public void setHasPrevious(Boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }
    }
}


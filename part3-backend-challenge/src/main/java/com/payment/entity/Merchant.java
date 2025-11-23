package com.payment.entity;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.time.LocalDate;

@Serdeable
@MappedEntity(value = "merchants", schema = "operators")
public class Merchant {

    @Id
    @MappedProperty("merchant_id")
    private String merchantId;

    @MappedProperty("merchant_name")
    private String merchantName;

    @MappedProperty("business_name")
    private String businessName;

    @MappedProperty("business_type")
    private String businessType;

    @MappedProperty("tax_id")
    private String taxId;

    // Contact information
    @MappedProperty("email")
    private String email;

    @MappedProperty("phone")
    private String phone;

    @MappedProperty("website")
    private String website;

    // Address
    @MappedProperty("address_line1")
    private String addressLine1;

    @MappedProperty("address_line2")
    private String addressLine2;

    @MappedProperty("city")
    private String city;

    @MappedProperty("state")
    private String state;

    @MappedProperty("postal_code")
    private String postalCode;

    @MappedProperty("country")
    private String country;

    // Business details
    @MappedProperty("registration_date")
    private LocalDate registrationDate;

    @MappedProperty("industry")
    private String industry;

    @MappedProperty("annual_revenue_range")
    private String annualRevenueRange;

    @MappedProperty("employee_count_range")
    private String employeeCountRange;

    // Account status
    @MappedProperty("status")
    private String status;

    @MappedProperty("risk_level")
    private String riskLevel;

    // Metadata
    @DateCreated
    @MappedProperty("created_at")
    private Instant createdAt;

    @DateUpdated
    @MappedProperty("updated_at")
    private Instant updatedAt;

    @MappedProperty("created_by")
    private String createdBy;

    @MappedProperty("notes")
    private String notes;

    // Constructors
    public Merchant() {
    }

    public Merchant(String merchantId, String merchantName, String businessName) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.businessName = businessName;
    }

    // Getters and Setters
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAnnualRevenueRange() {
        return annualRevenueRange;
    }

    public void setAnnualRevenueRange(String annualRevenueRange) {
        this.annualRevenueRange = annualRevenueRange;
    }

    public String getEmployeeCountRange() {
        return employeeCountRange;
    }

    public void setEmployeeCountRange(String employeeCountRange) {
        this.employeeCountRange = employeeCountRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}


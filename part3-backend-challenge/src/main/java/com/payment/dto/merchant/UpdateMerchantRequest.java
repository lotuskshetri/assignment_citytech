package com.payment.dto.merchant;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Serdeable
public class UpdateMerchantRequest {

    @Size(min = 3, max = 255, message = "Merchant name must be between 3 and 255 characters")
    private String merchantName;

    @Size(min = 3, max = 255, message = "Business name must be between 3 and 255 characters")
    private String businessName;

    @Size(max = 100, message = "Business type must not exceed 100 characters")
    private String businessType;

    @Size(max = 50, message = "Tax ID must not exceed 50 characters")
    private String taxId;

    // Contact information
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    // Address
    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(min = 3, max = 3, message = "Country code must be exactly 3 characters")
    private String country;

    // Business details
    private LocalDate registrationDate;

    @Size(max = 100, message = "Industry must not exceed 100 characters")
    private String industry;

    @Size(max = 50, message = "Annual revenue range must not exceed 50 characters")
    private String annualRevenueRange;

    @Size(max = 50, message = "Employee count range must not exceed 50 characters")
    private String employeeCountRange;

    @Pattern(regexp = "active|inactive|suspended|pending", message = "Status must be one of: active, inactive, suspended, pending")
    private String status;

    @Pattern(regexp = "low|medium|high", message = "Risk level must be one of: low, medium, high")
    private String riskLevel;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    // Constructors
    public UpdateMerchantRequest() {
    }

    // Getters and Setters
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}


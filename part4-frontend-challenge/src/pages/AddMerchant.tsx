// Add New Merchant Form Component

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { merchantService } from '../services/merchantService';
import '../components/merchants/AddMerchantForm.css';

export const AddMerchantForm: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    businessName: '',
    email: '',
    phone: '',
    address: '',
    registrationNumber: '',
    merchantName: '',  // Changed from contactName to merchantName
    status: 'active',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    // Required fields
    if (!formData.businessName.trim()) {
      newErrors.businessName = 'Business name is required';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }

    if (!formData.phone.trim()) {
      newErrors.phone = 'Phone number is required';
    } else if (!/^\+?[\d\s\-()]+$/.test(formData.phone)) {
      newErrors.phone = 'Invalid phone format';
    }

    if (!formData.merchantName.trim()) {
      newErrors.merchantName = 'Merchant name is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setSubmitting(true);

    try {
      const response = await merchantService.createMerchant(formData);

      // Show success notification
      alert(`✅ Merchant created successfully! ID: ${response.merchantId}`);

      // Reset form
      resetForm();

      // Navigate to merchant list or details
      navigate('/merchants');
    } catch (error: any) {
      console.error('Error creating merchant:', error);
      alert(`❌ Failed to create merchant: ${error.message || 'Unknown error'}`);
    } finally {
      setSubmitting(false);
    }
  };

  const resetForm = () => {
    setFormData({
      businessName: '',
      email: '',
      phone: '',
      address: '',
      registrationNumber: '',
      merchantName: '',
      status: 'active',
    });
    setErrors({});
  };

  const handleCancel = () => {
    if (window.confirm('Are you sure you want to cancel? Any unsaved changes will be lost.')) {
      navigate('/merchants');
    }
  };

  return (
    <div className="add-merchant-container">
      <div className="form-header">
        <h1>➕ Add New Merchant</h1>
        <p className="form-subtitle">Register a new merchant account</p>
      </div>

      <form onSubmit={handleSubmit} className="add-merchant-form">
        {/* Business Information */}
        <div className="form-section">
          <h3 className="section-title">📋 Business Information</h3>

          <div className="form-row">
            <div className="form-group full-width">
              <label htmlFor="businessName">
                Business Name <span className="required">*</span>
              </label>
              <input
                type="text"
                id="businessName"
                name="businessName"
                value={formData.businessName}
                onChange={handleChange}
                className={errors.businessName ? 'input-error' : ''}
                placeholder="Enter legal business name"
                autoFocus
              />
              {errors.businessName && (
                <span className="error-message">{errors.businessName}</span>
              )}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="registrationNumber">Registration Number</label>
              <input
                type="text"
                id="registrationNumber"
                name="registrationNumber"
                value={formData.registrationNumber}
                onChange={handleChange}
                placeholder="Business registration number"
              />
              <small className="field-hint">Optional: Tax ID or business registration number</small>
            </div>

            <div className="form-group">
              <label htmlFor="status">Initial Status</label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleChange}
              >
                <option value="active">✅ Active</option>
                <option value="pending">⏳ Pending Approval</option>
                <option value="inactive">⛔ Inactive</option>
              </select>
            </div>
          </div>
        </div>

        {/* Contact Information */}
        <div className="form-section">
          <h3 className="section-title">📞 Contact Information</h3>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="merchantName">
                Merchant Name <span className="required">*</span>
              </label>
              <input
                type="text"
                id="merchantName"
                name="merchantName"
                value={formData.merchantName}
                onChange={handleChange}
                className={errors.merchantName ? 'input-error' : ''}
                placeholder="Merchant display name"
              />
              {errors.merchantName && (
                <span className="error-message">{errors.merchantName}</span>
              )}
              <small className="field-hint">This will be the display name for the merchant</small>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="email">
                Email Address <span className="required">*</span>
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className={errors.email ? 'input-error' : ''}
                placeholder="contact@business.com"
              />
              {errors.email && (
                <span className="error-message">{errors.email}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="phone">
                Phone Number <span className="required">*</span>
              </label>
              <input
                type="tel"
                id="phone"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                className={errors.phone ? 'input-error' : ''}
                placeholder="+1 (555) 123-4567"
              />
              {errors.phone && (
                <span className="error-message">{errors.phone}</span>
              )}
            </div>
          </div>

          <div className="form-group full-width">
            <label htmlFor="address">Business Address</label>
            <textarea
              id="address"
              name="address"
              value={formData.address}
              onChange={handleChange}
              rows={3}
              placeholder="Street address, city, state, postal code"
            />
            <small className="field-hint">Complete business address for correspondence</small>
          </div>
        </div>

        {/* Form Actions */}
        <div className="form-actions">
          <button
            type="button"
            onClick={handleCancel}
            className="btn-cancel"
            disabled={submitting}
          >
            Cancel
          </button>
          <button
            type="button"
            onClick={resetForm}
            className="btn-reset"
            disabled={submitting}
          >
            🔄 Reset Form
          </button>
          <button
            type="submit"
            className="btn-submit"
            disabled={submitting}
          >
            {submitting ? '⏳ Creating...' : '✅ Create Merchant'}
          </button>
        </div>
      </form>

      {/* Help Section */}
      <div className="help-section">
        <h4>💡 Quick Tips</h4>
        <ul>
          <li>Fields marked with <span className="required">*</span> are required</li>
          <li><strong>Merchant Name</strong> is the display name shown in the system</li>
          <li><strong>Business Name</strong> is the legal business entity name</li>
          <li>Email will be used for account notifications</li>
          <li>Merchant ID will be auto-generated (format: MCH-XXXXX)</li>
          <li>You can update information later from merchant details page</li>
        </ul>
      </div>
    </div>
  );
};


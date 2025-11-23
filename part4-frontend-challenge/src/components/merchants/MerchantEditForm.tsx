// Merchant Edit Form Component

import React, { useState } from 'react';
import { merchantService } from '../../services/merchantService';
import './MerchantEditForm.css';

interface MerchantEditFormProps {
  merchant: {
    merchantId: string;
    merchantName: string;
    email?: string;
    phone?: string;
    address?: string;
    businessName?: string;
    registrationNumber?: string;
    status: string;
  };
  onCancel: () => void;
  onSave: () => void;
}

export const MerchantEditForm: React.FC<MerchantEditFormProps> = ({
  merchant,
  onCancel,
  onSave,
}) => {
  const [formData, setFormData] = useState({
    businessName: merchant.businessName || '',
    email: merchant.email || '',
    phone: merchant.phone || '',
    address: merchant.address || '',
    registrationNumber: merchant.registrationNumber || '',
    status: merchant.status || 'active',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [saving, setSaving] = useState(false);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.businessName.trim()) {
      newErrors.businessName = 'Business name is required';
    }

    if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }

    if (formData.phone && !/^\+?[\d\s\-()]+$/.test(formData.phone)) {
      newErrors.phone = 'Invalid phone format';
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

    setShowConfirmDialog(true);
  };

  const confirmSave = async () => {
    setSaving(true);
    setShowConfirmDialog(false);

    try {
      await merchantService.updateMerchant(merchant.merchantId, formData);

      // Show success notification
      alert('✅ Merchant updated successfully!');

      onSave();
    } catch (error: any) {
      console.error('Error updating merchant:', error);
      alert(`❌ Failed to update merchant: ${error.message}`);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="merchant-edit-form-container">
      <div className="form-header">
        <h2>✏️ Edit Merchant Details</h2>
        <p className="form-subtitle">Update merchant information and contact details</p>
      </div>

      <form onSubmit={handleSubmit} className="merchant-edit-form">
        {/* Business Information Section */}
        <div className="form-section">
          <h3 className="section-title">Business Information</h3>

          <div className="form-row">
            <div className="form-group">
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
                placeholder="Enter business name"
              />
              {errors.businessName && (
                <span className="error-message">{errors.businessName}</span>
              )}
            </div>

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
            </div>
          </div>
        </div>

        {/* Contact Information Section */}
        <div className="form-section">
          <h3 className="section-title">Contact Information</h3>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="email">Email Address</label>
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
              <label htmlFor="phone">Phone Number</label>
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

          <div className="form-group">
            <label htmlFor="address">Address</label>
            <textarea
              id="address"
              name="address"
              value={formData.address}
              onChange={handleChange}
              rows={3}
              placeholder="Business address"
            />
          </div>
        </div>

        {/* Status Management Section */}
        <div className="form-section">
          <h3 className="section-title">Status Management</h3>

          <div className="form-group">
            <label htmlFor="status">Merchant Status</label>
            <select
              id="status"
              name="status"
              value={formData.status}
              onChange={handleChange}
              className="status-select"
            >
              <option value="active">✅ Active</option>
              <option value="inactive">⛔ Inactive</option>
              <option value="pending">⏳ Pending</option>
              <option value="suspended">🚫 Suspended</option>
            </select>
            <small className="field-hint">
              Changing status will affect merchant's ability to process transactions
            </small>
          </div>
        </div>

        {/* Read-only Info */}
        <div className="form-section info-section">
          <h3 className="section-title">Account Information (Read-only)</h3>
          <div className="info-grid">
            <div className="info-item">
              <span className="info-label">Merchant ID:</span>
              <span className="info-value">{merchant.merchantId}</span>
            </div>
            <div className="info-item">
              <span className="info-label">Merchant Name:</span>
              <span className="info-value">{merchant.merchantName}</span>
            </div>
          </div>
        </div>

        {/* Form Actions */}
        <div className="form-actions">
          <button
            type="button"
            onClick={onCancel}
            className="btn-cancel"
            disabled={saving}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn-save"
            disabled={saving}
          >
            {saving ? 'Saving...' : '💾 Save Changes'}
          </button>
        </div>
      </form>

      {/* Confirmation Dialog */}
      {showConfirmDialog && (
        <div className="dialog-overlay">
          <div className="confirm-dialog">
            <div className="dialog-icon">⚠️</div>
            <h3>Confirm Changes</h3>
            <p>Are you sure you want to update this merchant's information?</p>
            <div className="dialog-actions">
              <button
                onClick={() => setShowConfirmDialog(false)}
                className="btn-dialog-cancel"
              >
                Cancel
              </button>
              <button
                onClick={confirmSave}
                className="btn-dialog-confirm"
              >
                Yes, Update
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};


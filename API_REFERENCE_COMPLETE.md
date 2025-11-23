# 🔌 Payment Platform - Complete API Reference

**Base URL**: `http://localhost:8080/api/v1`  
**API Documentation**: `http://localhost:8080/swagger-ui`  
**Date**: November 23, 2025

---

## 📋 Table of Contents

1. [Merchants API](#merchants-api)
2. [Transactions API](#transactions-api)
3. [Analytics API](#analytics-api)
4. [Revenue Reports API](#revenue-reports-api)
5. [Chart Data API](#chart-data-api)
6. [Health Check](#health-check)
7. [Error Responses](#error-responses)
8. [Request/Response Examples](#request-response-examples)

---

## 🏢 Merchants API

### 1. List Merchants
Get paginated list of merchants with transaction statistics.

**Endpoint**: `GET /merchants`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `limit` | integer | No | 10 | Records per page (1-100) |
| `offset` | integer | No | 0 | Records to skip |
| `search` | string | No | - | Search by merchant ID |

**Example Request**:
```
GET /api/v1/merchants?limit=10&offset=0&search=MCH
```

**Response** (200 OK):
```json
{
  "merchants": [
    {
      "merchantId": "MCH-00001",
      "merchantName": "Merchant 00001",
      "totalTransactions": 60,
      "totalRevenue": 143272.31,
      "completedCount": 60,
      "failedCount": 0,
      "pendingCount": 0,
      "successRate": 100.0,
      "lastTransactionDate": "2025-11-18",
      "firstTransactionDate": "2025-11-16",
      "status": "active",
      "averageTransactionAmount": 2387.87
    }
  ],
  "pagination": {
    "total": 50,
    "limit": 10,
    "offset": 0
  }
}
```

---

### 2. Get Merchant Details
Get complete merchant profile with contact information and statistics.

**Endpoint**: `GET /merchants/{id}`

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | string | Yes | Merchant ID (e.g., MCH-00001) |

**Example Request**:
```
GET /api/v1/merchants/MCH-00001
```

**Response** (200 OK):
```json
{
  "merchantId": "MCH-00001",
  "merchantName": "Premier E-commerce",
  "businessName": "Premier E-commerce LLC",
  "businessType": "E-commerce",
  "taxId": "TAX-000000001",
  "email": "contact@merchant1.com",
  "phone": "+1-201-101-0100",
  "website": "https://www.merchant1.com",
  "addressLine1": "0100 Main Street",
  "addressLine2": null,
  "city": "Los Angeles",
  "state": "CA",
  "postalCode": "10100",
  "country": "USA",
  "registrationDate": "2025-04-27",
  "industry": "Food & Beverage",
  "annualRevenueRange": "$5M-$10M",
  "employeeCountRange": "11-50",
  "status": "active",
  "riskLevel": "low",
  "totalTransactions": 60,
  "totalRevenue": 143272.31,
  "completedCount": 60,
  "failedCount": 0,
  "pendingCount": 0,
  "successRate": 100.0,
  "averageTransactionAmount": 2387.87,
  "lastTransactionDate": "2025-11-18",
  "firstTransactionDate": "2025-11-16",
  "createdAt": "2025-11-16T10:00:10.401819Z",
  "updatedAt": "2025-11-23T10:00:10.401819Z"
}
```

**Response** (404 Not Found):
```json
{
  "message": "Merchant not found"
}
```

---

### 3. Create Merchant
Register a new merchant with business and contact information.

**Endpoint**: `POST /merchants`

**Request Body**:
```json
{
  "merchantName": "Gloria's Cafe",
  "businessName": "Gloria's Cafe LLC",
  "businessType": "Restaurant",
  "taxId": "TAX-12345678",
  "email": "contact@gloriascafe.com",
  "phone": "+1-555-123-4567",
  "website": "https://gloriascafe.com",
  "addressLine1": "123 Main Street",
  "addressLine2": "Suite 100",
  "city": "New York",
  "state": "NY",
  "postalCode": "10001",
  "country": "USA",
  "industry": "Food & Beverage",
  "annualRevenueRange": "$1M-$5M",
  "employeeCountRange": "11-50",
  "status": "active",
  "riskLevel": "low",
  "notes": "New merchant onboarding"
}
```

**Required Fields**:
- `merchantName` (3-255 characters)
- `businessName` (3-255 characters)

**Response** (201 Created):
```json
{
  "merchantId": "MCH-00051",
  "merchantName": "Gloria's Cafe",
  "businessName": "Gloria's Cafe LLC",
  "status": "active",
  "message": "Merchant created successfully"
}
```

**Response** (400 Bad Request):
```json
{
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

---

### 4. Update Merchant
Update existing merchant information.

**Endpoint**: `PUT /merchants/{id}`

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | string | Yes | Merchant ID |

**Request Body** (partial update supported):
```json
{
  "businessName": "Updated Business Name",
  "email": "newemail@example.com",
  "phone": "+1-555-999-8888",
  "addressLine1": "456 New Street",
  "city": "San Francisco",
  "state": "CA",
  "status": "active"
}
```

**Response** (200 OK):
```json
{
  "merchantId": "MCH-00001",
  "merchantName": "Updated Business Name",
  "message": "Merchant updated successfully"
}
```

**Response** (404 Not Found):
```json
{
  "message": "Merchant not found"
}
```

---

## 💳 Transactions API

### 5. List All Transactions
Get transactions with optional filters (transaction-focused, not merchant-scoped).

**Endpoint**: `GET /transactions`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `merchantId` | string | No | - | Filter by merchant ID |
| `status` | string | No | - | Filter by status (completed, pending, failed, reversed) |
| `startDate` | date | No | 30 days ago | Start date (YYYY-MM-DD) |
| `endDate` | date | No | today | End date (YYYY-MM-DD) |
| `page` | integer | No | 0 | Page number (0-based) |
| `size` | integer | No | 20 | Records per page |

**Example Requests**:
```
# All transactions
GET /api/v1/transactions?page=0&size=20

# Filter by merchant
GET /api/v1/transactions?merchantId=MCH-00001&page=0&size=20

# Filter by status
GET /api/v1/transactions?status=failed&page=0&size=20

# Combined filters
GET /api/v1/transactions?merchantId=MCH-00001&status=completed&startDate=2025-11-16&endDate=2025-11-18&page=0&size=20
```

**Response** (200 OK):
```json
{
  "transactions": [
    {
      "txnId": 1,
      "merchantId": "MCH-00001",
      "amount": 1234.56,
      "currency": "USD",
      "status": "completed",
      "cardType": "VISA",
      "cardLast4": "4532",
      "authCode": "AUTH123456",
      "txnDate": "2025-11-16",
      "createdAt": "2025-11-16T10:30:45.123Z"
    }
  ],
  "totalTransactions": 3000,
  "page": 0,
  "size": 20,
  "totalPages": 150,
  "summary": {
    "totalCount": 3000,
    "totalAmount": 7466625.34,
    "completedCount": 2400,
    "pendingCount": 300,
    "failedCount": 300
  }
}
```

**Note**: The `summary` object contains statistics for ALL filtered transactions, not just the current page.

---

### 6. Get Merchant Transactions (Legacy)
Get transactions for a specific merchant.

**Endpoint**: `GET /merchants/{merchantId}/transactions`

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `merchantId` | string | Yes | Merchant ID |

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `page` | integer | No | 0 | Page number |
| `size` | integer | No | 10 | Records per page |
| `startDate` | date | No | - | Start date |
| `endDate` | date | No | - | End date |
| `status` | string | No | - | Transaction status |

**Example Request**:
```
GET /api/v1/merchants/MCH-00001/transactions?page=0&size=10
```

**Response** (200 OK):
```json
{
  "merchantId": "MCH-00001",
  "page": 0,
  "size": 10,
  "totalTransactions": 60,
  "transactions": [...]
}
```

---

## 📊 Analytics API

### 7. Transaction Volume Analytics
Get transaction volume statistics by time period.

**Endpoint**: `GET /analytics/transactions/volume`

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | date | Yes | Start date (YYYY-MM-DD) |
| `endDate` | date | Yes | End date (YYYY-MM-DD) |

**Example Request**:
```
GET /api/v1/analytics/transactions/volume?startDate=2025-11-16&endDate=2025-11-23
```

**Response** (200 OK):
```json
{
  "totalTransactions": 3000,
  "totalAmount": 7466625.34,
  "averageAmount": 2488.88,
  "dailyBreakdown": [
    {
      "date": "2025-11-16",
      "count": 1000,
      "amount": 2488875.45
    },
    {
      "date": "2025-11-17",
      "count": 1000,
      "amount": 2488875.45
    },
    {
      "date": "2025-11-18",
      "count": 1000,
      "amount": 2488874.44
    }
  ]
}
```

---

### 8. Success Rate Analysis
Get transaction success vs failure analysis.

**Endpoint**: `GET /analytics/transactions/success-rate`

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | date | Yes | Start date |
| `endDate` | date | Yes | End date |

**Example Request**:
```
GET /api/v1/analytics/transactions/success-rate?startDate=2025-11-16&endDate=2025-11-23
```

**Response** (200 OK):
```json
{
  "totalTransactions": 3000,
  "completedCount": 2400,
  "failedCount": 300,
  "pendingCount": 300,
  "successRate": 80.0,
  "failureRate": 10.0,
  "pendingRate": 10.0
}
```

---

### 9. Transaction Trends
Get transaction trends over time.

**Endpoint**: `GET /analytics/transactions/trends`

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | date | Yes | Start date |
| `endDate` | date | Yes | End date |

**Example Request**:
```
GET /api/v1/analytics/transactions/trends?startDate=2025-11-16&endDate=2025-11-23
```

**Response** (200 OK):
```json
{
  "period": "2025-11-16 to 2025-11-23",
  "averageTransactionAmount": 2488.88,
  "medianTransactionAmount": 2350.00,
  "trends": [
    {
      "date": "2025-11-16",
      "averageAmount": 2488.88,
      "count": 1000
    }
  ]
}
```

---

### 10. Peak Times Heatmap Data
Get transaction volume by hour and day of week.

**Endpoint**: `GET /analytics/transactions/peak-times`

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | date | Yes | Start date |
| `endDate` | date | Yes | End date |

**Example Request**:
```
GET /api/v1/analytics/transactions/peak-times?startDate=2025-11-16&endDate=2025-11-23
```

**Response** (200 OK):
```json
{
  "heatmapData": [
    {
      "hour": 10,
      "dayOfWeek": "SATURDAY",
      "transactionCount": 125,
      "totalAmount": 311110.00
    },
    {
      "hour": 14,
      "dayOfWeek": "SATURDAY",
      "transactionCount": 130,
      "totalAmount": 323553.40
    }
  ],
  "peakHour": 14,
  "peakDay": "SATURDAY"
}
```

---

### 11. Card Type Distribution
Get distribution of transactions by card type.

**Endpoint**: `GET /analytics/transactions/card-distribution`

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | date | Yes | Start date |
| `endDate` | date | Yes | End date |

**Example Request**:
```
GET /api/v1/analytics/transactions/card-distribution?startDate=2025-11-16&endDate=2025-11-23
```

**Response** (200 OK):
```json
{
  "distribution": [
    {
      "cardType": "VISA",
      "count": 750,
      "percentage": 25.0,
      "totalAmount": 1818820.52
    },
    {
      "cardType": "MASTERCARD",
      "count": 750,
      "percentage": 25.0,
      "totalAmount": 1888867.00
    },
    {
      "cardType": "AMEX",
      "count": 750,
      "percentage": 25.0,
      "totalAmount": 1900441.67
    },
    {
      "cardType": "DISCOVER",
      "count": 750,
      "percentage": 25.0,
      "totalAmount": 1858496.15
    }
  ],
  "totalTransactions": 3000
}
```

---

## 💰 Revenue Reports API

### 12. Revenue by Period
Get revenue breakdown by time period (daily, weekly, monthly).

**Endpoint**: `GET /reports/revenue/by-period`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `startDate` | date | Yes | - | Start date |
| `endDate` | date | Yes | - | End date |
| `period` | string | No | daily | Period type (daily, weekly, monthly) |

**Example Request**:
```
GET /api/v1/reports/revenue/by-period?startDate=2025-11-16&endDate=2025-11-23&period=daily
```

**Response** (200 OK):
```json
{
  "period": "daily",
  "data": [
    {
      "period": "2025-11-16",
      "revenue": 2488875.45,
      "transactionCount": 1000,
      "averageTransactionAmount": 2488.88
    },
    {
      "period": "2025-11-17",
      "revenue": 2488875.45,
      "transactionCount": 1000,
      "averageTransactionAmount": 2488.88
    },
    {
      "period": "2025-11-18",
      "revenue": 2488874.44,
      "transactionCount": 1000,
      "averageTransactionAmount": 2488.87
    }
  ],
  "totalRevenue": 7466625.34,
  "totalTransactions": 3000
}
```

---

### 13. Revenue by Merchant
Get revenue breakdown by merchant.

**Endpoint**: `GET /reports/revenue/by-merchant`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `startDate` | date | Yes | - | Start date |
| `endDate` | date | Yes | - | End date |
| `limit` | integer | No | 10 | Number of merchants |

**Example Request**:
```
GET /api/v1/reports/revenue/by-merchant?startDate=2025-11-16&endDate=2025-11-23&limit=10
```

**Response** (200 OK):
```json
{
  "merchants": [
    {
      "merchantId": "MCH-00001",
      "merchantName": "Merchant 00001",
      "revenue": 143272.31,
      "transactionCount": 60,
      "averageTransactionAmount": 2387.87,
      "percentageOfTotal": 1.92
    }
  ],
  "totalRevenue": 7466625.34,
  "totalMerchants": 50
}
```

---

### 14. Revenue Forecast
Get revenue forecast based on historical trends.

**Endpoint**: `GET /reports/revenue/forecast`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `periods` | integer | No | 7 | Number of periods to forecast |

**Example Request**:
```
GET /api/v1/reports/revenue/forecast?periods=7
```

**Response** (200 OK):
```json
{
  "forecasts": [
    {
      "period": "2025-11-24",
      "predictedRevenue": 2488875.45,
      "lowerBound": 2239187.91,
      "upperBound": 2738562.99,
      "confidence": 95.0
    },
    {
      "period": "2025-11-25",
      "predictedRevenue": 2488875.45,
      "lowerBound": 2239187.91,
      "upperBound": 2738562.99,
      "confidence": 95.0
    }
  ],
  "model": "linear-regression",
  "basedonDays": 3
}
```

---

### 15. Year-over-Year Growth
Get year-over-year revenue growth analysis.

**Endpoint**: `GET /reports/revenue/growth`

**Query Parameters**: None required

**Example Request**:
```
GET /api/v1/reports/revenue/growth
```

**Response** (200 OK):
```json
{
  "currentYear": 2025,
  "comparisonYear": 2024,
  "currentYearTotal": 7466625.34,
  "comparisonYearTotal": 0.0,
  "growthAmount": 7466625.34,
  "growthPercentage": 100.0,
  "monthlyComparison": [
    {
      "month": 11,
      "monthName": "November",
      "currentYearRevenue": 7466625.34,
      "comparisonYearRevenue": 0.0,
      "growth": 100.0
    }
  ]
}
```

---

### 16. Top Performing Merchants
Get ranking of top merchants by revenue.

**Endpoint**: `GET /reports/merchants/top-performers`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `startDate` | date | Yes | - | Start date |
| `endDate` | date | Yes | - | End date |
| `limit` | integer | No | 5 | Number of merchants |

**Example Request**:
```
GET /api/v1/reports/merchants/top-performers?startDate=2025-11-16&endDate=2025-11-23&limit=5
```

**Response** (200 OK):
```json
{
  "topPerformers": [
    {
      "rank": 1,
      "merchantId": "MCH-00002",
      "merchantName": "Merchant 00002",
      "revenue": 180533.94,
      "transactionCount": 60,
      "averageTransactionAmount": 3008.90,
      "percentageOfTotal": 2.42
    },
    {
      "rank": 2,
      "merchantId": "MCH-00034",
      "merchantName": "Merchant 00034",
      "revenue": 177950.42,
      "transactionCount": 60,
      "averageTransactionAmount": 2965.84,
      "percentageOfTotal": 2.38
    }
  ],
  "totalRevenue": 7466625.34,
  "period": "2025-11-16 to 2025-11-23"
}
```

---

## 📈 Chart Data API

### 17. Line Chart Data
Get data formatted for line charts (trends over time).

**Endpoint**: `GET /charts/line/trends`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `startDate` | date | Yes | - | Start date |
| `endDate` | date | Yes | - | End date |
| `metric` | string | No | revenue | Metric to chart (revenue, count, average) |
| `groupBy` | string | No | day | Group by (hour, day, week, month) |

**Example Request**:
```
GET /api/v1/charts/line/trends?startDate=2025-11-16&endDate=2025-11-23&metric=revenue&groupBy=day
```

**Response** (200 OK):
```json
{
  "labels": ["2025-11-16", "2025-11-17", "2025-11-18"],
  "datasets": [
    {
      "label": "Revenue",
      "data": [2488875.45, 2488875.45, 2488874.44],
      "borderColor": "#007bff",
      "fill": false
    }
  ],
  "chartType": "line"
}
```

---

### 18. Pie Chart Data
Get data formatted for pie charts (distribution).

**Endpoint**: `GET /charts/pie/distribution`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `startDate` | date | Yes | - | Start date |
| `endDate` | date | Yes | - | End date |
| `distributeBy` | string | No | status | Distribute by (status, cardType, merchant) |

**Example Request**:
```
GET /api/v1/charts/pie/distribution?startDate=2025-11-16&endDate=2025-11-23&distributeBy=status
```

**Response** (200 OK):
```json
{
  "labels": ["Completed", "Pending", "Failed"],
  "datasets": [
    {
      "label": "Transaction Status",
      "data": [2400, 300, 300],
      "backgroundColor": ["#28a745", "#ffc107", "#dc3545"]
    }
  ],
  "chartType": "pie"
}
```

---

### 19. Bar Chart Data
Get data formatted for bar charts (comparisons).

**Endpoint**: `GET /charts/bar/comparison`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `startDate` | date | Yes | - | Start date |
| `endDate` | date | Yes | - | End date |
| `compareBy` | string | No | merchant | Compare by (merchant, cardType, day) |
| `limit` | integer | No | 10 | Number of items |

**Example Request**:
```
GET /api/v1/charts/bar/comparison?startDate=2025-11-16&endDate=2025-11-23&compareBy=merchant&limit=5
```

**Response** (200 OK):
```json
{
  "labels": ["MCH-00001", "MCH-00002", "MCH-00003"],
  "datasets": [
    {
      "label": "Revenue",
      "data": [143272.31, 180533.94, 150000.00],
      "backgroundColor": "#007bff"
    }
  ],
  "chartType": "bar"
}
```

---

### 20. Real-time Recent Data
Get most recent transactions for real-time updates.

**Endpoint**: `GET /charts/data/recent`

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `since` | datetime | No | 5 min ago | Get transactions since this time |
| `limit` | integer | No | 10 | Number of transactions |

**Example Request**:
```
GET /api/v1/charts/data/recent?since=2025-11-23T10:00:00Z&limit=10
```

**Response** (200 OK):
```json
{
  "transactions": [
    {
      "txnId": 3000,
      "merchantId": "MCH-00050",
      "amount": 2500.00,
      "status": "completed",
      "timestamp": "2025-11-23T10:05:30Z"
    }
  ],
  "count": 10,
  "lastUpdated": "2025-11-23T10:05:30Z"
}
```

---

### 21. Drill-down Data
Get detailed breakdown for a specific category.

**Endpoint**: `GET /charts/drill-down/{category}`

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `category` | string | Yes | Category to drill into (merchant, card, status) |

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `value` | string | Yes | Value to filter by |
| `startDate` | date | Yes | Start date |
| `endDate` | date | Yes | End date |

**Example Request**:
```
GET /api/v1/charts/drill-down/merchant?value=MCH-00001&startDate=2025-11-16&endDate=2025-11-23
```

**Response** (200 OK):
```json
{
  "category": "merchant",
  "value": "MCH-00001",
  "summary": {
    "totalTransactions": 60,
    "totalRevenue": 143272.31,
    "averageAmount": 2387.87
  },
  "breakdown": {
    "byStatus": [
      {"status": "completed", "count": 60, "amount": 143272.31}
    ],
    "byCardType": [
      {"cardType": "VISA", "count": 15, "amount": 35818.08}
    ]
  }
}
```

---

## 🏥 Health Check

### 22. Health Status
Check API and database health.

**Endpoint**: `GET /health`

**Example Request**:
```
GET /api/v1/health
```

**Response** (200 OK):
```json
{
  "name": "payment-api",
  "status": "UP",
  "details": {
    "compositeDiscoveryClient()": {
      "name": "payment-api",
      "status": "UP"
    },
    "diskSpace": {
      "name": "payment-api",
      "status": "UP",
      "details": {
        "total": 294649851904,
        "free": 46877257728,
        "threshold": 10485760
      }
    },
    "jdbc": {
      "name": "payment-api",
      "status": "UP",
      "details": {
        "jdbc:postgresql://localhost:5432/payment_platform": {
          "name": "payment-api",
          "status": "UP",
          "details": {
            "database": "PostgreSQL",
            "version": "17.5"
          }
        }
      }
    }
  }
}
```

---

## ❌ Error Responses

### Standard Error Format
All API errors follow this format:

```json
{
  "timestamp": "2025-11-23T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/merchants"
}
```

### HTTP Status Codes

| Status | Meaning | When Used |
|--------|---------|-----------|
| 200 | OK | Successful GET request |
| 201 | Created | Successful POST (resource created) |
| 400 | Bad Request | Validation error or invalid parameters |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Unexpected server error |

### Validation Errors
When validation fails, the response includes field-specific errors:

```json
{
  "timestamp": "2025-11-23T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/merchants",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format",
      "rejectedValue": "invalid-email"
    },
    {
      "field": "merchantName",
      "message": "Merchant name is required",
      "rejectedValue": null
    }
  ]
}
```

---

## 📝 Request/Response Examples

### Example 1: Create Merchant and View Details

**Step 1: Create Merchant**
```http
POST /api/v1/merchants
Content-Type: application/json

{
  "merchantName": "Test Merchant",
  "businessName": "Test Business LLC",
  "email": "test@example.com",
  "phone": "+1-555-123-4567",
  "status": "active"
}
```

**Response**:
```json
{
  "merchantId": "MCH-00051",
  "merchantName": "Test Merchant",
  "status": "active",
  "message": "Merchant created successfully"
}
```

**Step 2: View Merchant Details**
```http
GET /api/v1/merchants/MCH-00051
```

**Response**:
```json
{
  "merchantId": "MCH-00051",
  "merchantName": "Test Merchant",
  "businessName": "Test Business LLC",
  "email": "test@example.com",
  "phone": "+1-555-123-4567",
  "status": "active",
  "totalTransactions": 0,
  "totalRevenue": 0.0
}
```

---

### Example 2: Filter Transactions by Merchant and Status

**Request**:
```http
GET /api/v1/transactions?merchantId=MCH-00001&status=completed&startDate=2025-11-16&endDate=2025-11-18&page=0&size=10
```

**Response**:
```json
{
  "transactions": [
    {
      "txnId": 1,
      "merchantId": "MCH-00001",
      "amount": 1234.56,
      "status": "completed",
      "cardType": "VISA",
      "txnDate": "2025-11-16"
    }
  ],
  "totalTransactions": 60,
  "page": 0,
  "size": 10,
  "totalPages": 6,
  "summary": {
    "totalCount": 60,
    "totalAmount": 143272.31,
    "completedCount": 60,
    "pendingCount": 0,
    "failedCount": 0
  }
}
```

---

### Example 3: Get Revenue Report with Forecast

**Step 1: Get Current Revenue**
```http
GET /api/v1/reports/revenue/by-period?startDate=2025-11-16&endDate=2025-11-18&period=daily
```

**Step 2: Get 7-Day Forecast**
```http
GET /api/v1/reports/revenue/forecast?periods=7
```

**Step 3: Get Top Performers**
```http
GET /api/v1/reports/merchants/top-performers?startDate=2025-11-16&endDate=2025-11-18&limit=5
```

---

## 🔐 Authentication & Authorization

**Current Status**: No authentication required (development mode)

**Production Notes**:
- Add JWT or OAuth2 authentication
- Implement role-based access control (RBAC)
- Secure sensitive endpoints
- Add API rate limiting
- Implement audit logging

---

## 📊 Rate Limiting

**Current Status**: No rate limiting (development mode)

**Recommended Production Limits**:
- Read operations: 100 requests/minute
- Write operations: 20 requests/minute
- Report generation: 10 requests/minute

---

## 🌐 CORS Configuration

**Current Status**: Open CORS for development

**Allowed Origins**:
- `http://localhost:3000` (Frontend dev server)
- `http://localhost:8080` (Backend dev server)

**Production**: Configure specific allowed origins

---

## 📖 Additional Resources

- **Swagger UI**: `http://localhost:8080/swagger-ui`
- **OpenAPI Spec**: `http://localhost:8080/swagger/payment-platform-api-1.0.yml`
- **RapiDoc**: `http://localhost:8080/rapidoc`
- **ReDoc**: `http://localhost:8080/redoc`

---

**Last Updated**: November 23, 2025  
**API Version**: 1.0.0  
**Backend Framework**: Micronaut 4.2.0  
**Database**: PostgreSQL 17


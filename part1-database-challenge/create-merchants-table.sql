-- ============================================================================
-- MERCHANT TABLE CREATION AND DATA MIGRATION
-- ============================================================================
-- This script creates a proper merchants table and populates it with data
-- extracted from existing transaction_master records
-- ============================================================================

-- Create merchants table
DROP TABLE IF EXISTS operators.merchants CASCADE;

CREATE TABLE operators.merchants (
    merchant_id VARCHAR(50) PRIMARY KEY,
    merchant_name VARCHAR(255) NOT NULL,
    business_name VARCHAR(255) NOT NULL,
    business_type VARCHAR(100),
    tax_id VARCHAR(50),

    -- Contact information
    email VARCHAR(255),
    phone VARCHAR(20),
    website VARCHAR(255),

    -- Address
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(50),
    postal_code VARCHAR(20),
    country VARCHAR(3) DEFAULT 'USA',

    -- Business details
    registration_date DATE,
    industry VARCHAR(100),
    annual_revenue_range VARCHAR(50),
    employee_count_range VARCHAR(50),

    -- Account status
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'suspended', 'pending')),
    risk_level VARCHAR(20) DEFAULT 'medium' CHECK (risk_level IN ('low', 'medium', 'high')),

    -- Metadata
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(100),
    notes TEXT
);

-- Create indexes
CREATE INDEX idx_merchants_status ON operators.merchants(status);
CREATE INDEX idx_merchants_business_name ON operators.merchants(business_name);
CREATE INDEX idx_merchants_country ON operators.merchants(country);
CREATE INDEX idx_merchants_created_at ON operators.merchants(created_at);

-- Add comment
COMMENT ON TABLE operators.merchants IS 'Merchant master data with business and contact information';

-- ============================================================================
-- POPULATE MERCHANTS TABLE FROM EXISTING TRANSACTION DATA
-- ============================================================================

DO $$
DECLARE
    v_merchant_id VARCHAR(50);
    v_merchant_number INTEGER;
    v_business_types VARCHAR[] := ARRAY[
        'Retail Store', 'E-commerce', 'Restaurant', 'Gas Station',
        'Grocery Store', 'Hotel', 'Healthcare', 'Professional Services',
        'Entertainment', 'Education', 'Technology', 'Manufacturing'
    ];
    v_industries VARCHAR[] := ARRAY[
        'Retail', 'Food & Beverage', 'Hospitality', 'Healthcare',
        'Technology', 'Education', 'Entertainment', 'Services',
        'Manufacturing', 'Transportation', 'Finance', 'Real Estate'
    ];
    v_cities VARCHAR[] := ARRAY[
        'New York', 'Los Angeles', 'Chicago', 'Houston', 'Phoenix',
        'Philadelphia', 'San Antonio', 'San Diego', 'Dallas', 'San Jose',
        'Austin', 'Jacksonville', 'Fort Worth', 'Columbus', 'Charlotte',
        'San Francisco', 'Indianapolis', 'Seattle', 'Denver', 'Washington'
    ];
    v_states VARCHAR[] := ARRAY[
        'NY', 'CA', 'IL', 'TX', 'AZ', 'PA', 'TX', 'CA', 'TX', 'CA',
        'TX', 'FL', 'TX', 'OH', 'NC', 'CA', 'IN', 'WA', 'CO', 'DC'
    ];
    v_business_name VARCHAR(255);
    v_email VARCHAR(255);
    v_phone VARCHAR(20);
    v_city VARCHAR(100);
    v_state VARCHAR(50);
    v_business_type VARCHAR(100);
    v_industry VARCHAR(100);
    v_status VARCHAR(20);
    v_risk_level VARCHAR(20);
    v_reg_date DATE;
    i INTEGER;
BEGIN
    -- Extract distinct merchant IDs from transaction_master
    FOR v_merchant_id IN
        SELECT DISTINCT merchant_id
        FROM operators.transaction_master
        ORDER BY merchant_id
    LOOP
        -- Extract merchant number from ID (MCH-00001 -> 1)
        v_merchant_number := SUBSTRING(v_merchant_id FROM 5)::INTEGER;

        -- Generate business details based on merchant number
        i := v_merchant_number % 20 + 1; -- Index for arrays (1-20)

        v_business_type := v_business_types[(v_merchant_number % 12) + 1];
        v_industry := v_industries[(v_merchant_number % 12) + 1];
        v_city := v_cities[i];
        v_state := v_states[i];

        -- Generate business name
        v_business_name := CASE (v_merchant_number % 5)
            WHEN 0 THEN 'Ace ' || v_business_type || ' Co'
            WHEN 1 THEN 'Premier ' || v_business_type
            WHEN 2 THEN v_city || ' ' || v_business_type
            WHEN 3 THEN 'United ' || v_business_type || ' Group'
            ELSE 'Global ' || v_business_type || ' Inc'
        END;

        -- Generate contact details
        v_email := 'contact@merchant' || v_merchant_number || '.com';
        v_phone := '+1-' || LPAD((200 + v_merchant_number)::TEXT, 3, '0') ||
                   '-' || LPAD((1000 + v_merchant_number * 10)::TEXT, 3, '0') ||
                   '-' || LPAD((v_merchant_number * 100)::TEXT, 4, '0');

        -- Determine status based on recent transaction activity
        SELECT CASE
            WHEN MAX(txn_date) >= CURRENT_DATE - INTERVAL '30 days' THEN 'active'
            WHEN MAX(txn_date) >= CURRENT_DATE - INTERVAL '90 days' THEN 'inactive'
            ELSE 'inactive'
        END INTO v_status
        FROM operators.transaction_master
        WHERE merchant_id = v_merchant_id;

        -- Calculate risk level based on transaction patterns
        SELECT CASE
            WHEN (SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END)::FLOAT / COUNT(*)) > 0.15 THEN 'high'
            WHEN (SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END)::FLOAT / COUNT(*)) > 0.05 THEN 'medium'
            ELSE 'low'
        END INTO v_risk_level
        FROM operators.transaction_master
        WHERE merchant_id = v_merchant_id;

        -- Generate registration date (6 months to 5 years ago)
        v_reg_date := CURRENT_DATE - ((180 + (v_merchant_number * 30)) % 1825)::INTEGER;

        -- Insert merchant record
        INSERT INTO operators.merchants (
            merchant_id,
            merchant_name,
            business_name,
            business_type,
            tax_id,
            email,
            phone,
            website,
            address_line1,
            city,
            state,
            postal_code,
            country,
            registration_date,
            industry,
            annual_revenue_range,
            employee_count_range,
            status,
            risk_level,
            created_at,
            notes
        ) VALUES (
            v_merchant_id,
            v_business_name,
            v_business_name,
            v_business_type,
            'TAX-' || LPAD(v_merchant_number::TEXT, 9, '0'),
            v_email,
            v_phone,
            'https://www.merchant' || v_merchant_number || '.com',
            LPAD(((v_merchant_number * 100) % 9999)::TEXT, 4, '0') || ' Main Street',
            v_city,
            v_state,
            LPAD(((10000 + v_merchant_number * 100) % 99999)::TEXT, 5, '0'),
            'USA',
            v_reg_date,
            v_industry,
            CASE
                WHEN v_merchant_number % 4 = 0 THEN '$1M-$5M'
                WHEN v_merchant_number % 4 = 1 THEN '$5M-$10M'
                WHEN v_merchant_number % 4 = 2 THEN '$10M-$50M'
                ELSE '$500K-$1M'
            END,
            CASE
                WHEN v_merchant_number % 3 = 0 THEN '1-10'
                WHEN v_merchant_number % 3 = 1 THEN '11-50'
                ELSE '51-200'
            END,
            v_status,
            v_risk_level,
            NOW() - ((v_merchant_number * 7)::TEXT || ' days')::INTERVAL,
            'Migrated from transaction data on ' || NOW()::DATE
        );

    END LOOP;

    RAISE NOTICE 'Successfully migrated % merchants', (SELECT COUNT(*) FROM operators.merchants);
END $$;

-- ============================================================================
-- ADD FOREIGN KEY TO TRANSACTION_MASTER (OPTIONAL)
-- ============================================================================
-- Uncomment if you want to enforce referential integrity

-- ALTER TABLE operators.transaction_master
-- ADD CONSTRAINT fk_merchant
-- FOREIGN KEY (merchant_id) REFERENCES operators.merchants(merchant_id);

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================

-- Count merchants by status
SELECT status, COUNT(*) as merchant_count
FROM operators.merchants
GROUP BY status
ORDER BY status;

-- Count merchants by risk level
SELECT risk_level, COUNT(*) as merchant_count
FROM operators.merchants
GROUP BY risk_level
ORDER BY risk_level;

-- Sample merchant data
SELECT
    merchant_id,
    merchant_name,
    business_type,
    city,
    state,
    status,
    risk_level,
    registration_date
FROM operators.merchants
ORDER BY merchant_id
LIMIT 10;

-- Verify all transaction merchants exist in merchant table
SELECT
    COUNT(DISTINCT tm.merchant_id) as txn_merchants,
    (SELECT COUNT(*) FROM operators.merchants) as table_merchants,
    COUNT(DISTINCT tm.merchant_id) = (SELECT COUNT(*) FROM operators.merchants) as match
FROM operators.transaction_master tm;

-- Show merchants with transaction summary
SELECT
    m.merchant_id,
    m.merchant_name,
    m.status,
    m.risk_level,
    COUNT(t.txn_id) as total_transactions,
    SUM(t.amount) as total_revenue,
    MIN(t.txn_date) as first_transaction,
    MAX(t.txn_date) as last_transaction
FROM operators.merchants m
LEFT JOIN operators.transaction_master t ON m.merchant_id = t.merchant_id
GROUP BY m.merchant_id, m.merchant_name, m.status, m.risk_level
ORDER BY total_revenue DESC
LIMIT 10;

-- ============================================================================
-- UPDATE STATISTICS
-- ============================================================================

VACUUM ANALYZE operators.merchants;

SELECT '✅ Merchants table created and populated successfully!' AS status;
SELECT '🏪 Total merchants: ' || COUNT(*) FROM operators.merchants;


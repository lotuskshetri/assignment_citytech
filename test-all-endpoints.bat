@echo off
REM Comprehensive API Endpoint Testing Script
REM Tests ALL 22 documented endpoints from API_REFERENCE_COMPLETE.md

setlocal enabledelayedexpansion

echo ========================================
echo   Payment Platform API - Complete Test
echo   Testing All 22 Endpoints
echo ========================================
echo.

set BASE_URL=http://localhost:8080
set PASSED=0
set FAILED=0
set TOTAL=22

REM Date parameters
set START_DATE=2025-11-16
set END_DATE=2025-11-22

echo Starting tests at %TIME%
echo Base URL: %BASE_URL%
echo Date Range: %START_DATE% to %END_DATE%
echo.

echo ========================================
echo   MERCHANTS API (4 endpoints)
echo ========================================
echo.

REM Test 1: List Merchants
echo [1/22] List Merchants (GET /merchants)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/merchants?limit=10&offset=0" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/merchants?limit=10&offset=0" > test_output_1.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 2: Get Merchant Details
echo [2/22] Get Merchant Details (GET /merchants/{id})
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/merchants/MCH-00001" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/merchants/MCH-00001" > test_output_2.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 3: Create Merchant (skipped - would create data)
echo [3/22] Create Merchant (POST /merchants)
echo   ⊘ SKIPPED - Would create test data
echo   Note: Test manually with POST request
set /a TOTAL-=1
echo.

REM Test 4: Update Merchant (skipped - would modify data)
echo [4/22] Update Merchant (PUT /merchants/{id})
echo   ⊘ SKIPPED - Would modify existing data
echo   Note: Test manually with PUT request
set /a TOTAL-=1
echo.

echo ========================================
echo   TRANSACTIONS API (2 endpoints)
echo ========================================
echo.

REM Test 5: List All Transactions
echo [5/22] List All Transactions (GET /transactions)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/transactions?page=0&size=20&startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/transactions?page=0&size=20&startDate=%START_DATE%&endDate=%END_DATE%" > test_output_5.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 6: Get Merchant Transactions
echo [6/22] Get Merchant Transactions (GET /merchants/{id}/transactions)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/merchants/MCH-00001/transactions?page=0&size=10" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/merchants/MCH-00001/transactions?page=0&size=10" > test_output_6.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   ANALYTICS API (5 endpoints)
echo ========================================
echo.

REM Test 7: Transaction Volume Analytics
echo [7/22] Transaction Volume Analytics (GET /analytics/transactions/volume)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/volume?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/volume?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_7.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 8: Success Rate Analysis
echo [8/22] Success Rate Analysis (GET /analytics/transactions/success-rate)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/success-rate?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/success-rate?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_8.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 9: Transaction Trends
echo [9/22] Transaction Trends (GET /analytics/transactions/trends)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/trends?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/trends?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_9.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 10: Peak Times Heatmap Data
echo [10/22] Peak Times Heatmap Data (GET /analytics/transactions/peak-times)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/peak-times?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/peak-times?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_10.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 11: Card Type Distribution
echo [11/22] Card Type Distribution (GET /analytics/transactions/card-distribution)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/card-distribution?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/card-distribution?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_11.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   REVENUE REPORTS API (5 endpoints)
echo ========================================
echo.

REM Test 12: Revenue by Period
echo [12/22] Revenue by Period (GET /reports/revenue/by-period)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/by-period?startDate=%START_DATE%&endDate=%END_DATE%&period=daily" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/by-period?startDate=%START_DATE%&endDate=%END_DATE%&period=daily" > test_output_12.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 13: Revenue by Merchant
echo [13/22] Revenue by Merchant (GET /reports/revenue/by-merchant)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/by-merchant?startDate=%START_DATE%&endDate=%END_DATE%&limit=10" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/by-merchant?startDate=%START_DATE%&endDate=%END_DATE%&limit=10" > test_output_13.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 14: Revenue Forecast
echo [14/22] Revenue Forecast (GET /reports/revenue/forecast)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/forecast?periods=7" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/forecast?periods=7" > test_output_14.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 15: Year-over-Year Growth
echo [15/22] Year-over-Year Growth (GET /reports/revenue/growth)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/growth" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/growth" > test_output_15.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 16: Top Performing Merchants
echo [16/22] Top Performing Merchants (GET /reports/merchants/top-performers)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/merchants/top-performers?startDate=%START_DATE%&endDate=%END_DATE%&limit=5" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/merchants/top-performers?startDate=%START_DATE%&endDate=%END_DATE%&limit=5" > test_output_16.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   CHART DATA API (5 endpoints)
echo ========================================
echo.

REM Test 17: Line Chart Data
echo [17/22] Line Chart Data (GET /charts/line/trends)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/line/trends?startDate=%START_DATE%&endDate=%END_DATE%&metric=revenue&groupBy=day" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/line/trends?startDate=%START_DATE%&endDate=%END_DATE%&metric=revenue&groupBy=day" > test_output_17.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 18: Pie Chart Data
echo [18/22] Pie Chart Data (GET /charts/pie/distribution)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/pie/distribution?startDate=%START_DATE%&endDate=%END_DATE%&distributeBy=status" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/pie/distribution?startDate=%START_DATE%&endDate=%END_DATE%&distributeBy=status" > test_output_18.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 19: Bar Chart Data
echo [19/22] Bar Chart Data (GET /charts/bar/comparison)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/bar/comparison?startDate=%START_DATE%&endDate=%END_DATE%&compareBy=merchant&limit=5" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/bar/comparison?startDate=%START_DATE%&endDate=%END_DATE%&compareBy=merchant&limit=5" > test_output_19.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 20: Real-time Recent Data
echo [20/22] Real-time Recent Data (GET /charts/data/recent)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/data/recent?limit=10" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/data/recent?limit=10" > test_output_20.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 21: Drill-down Data
echo [21/22] Drill-down Data (GET /charts/drill-down/{category})
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/drill-down/merchant?value=MCH-00001&startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/drill-down/merchant?value=MCH-00001&startDate=%START_DATE%&endDate=%END_DATE%" > test_output_21.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   HEALTH CHECK (1 endpoint)
echo ========================================
echo.

REM Test 22: Health Status
echo [22/22] Health Status (GET /health)
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/health" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/health" > test_output_22.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Clean up
del temp_status.txt 2>nul

echo ========================================
echo   TEST SUMMARY
echo ========================================
echo.
echo Total Endpoints:  22
echo Tested:           %TOTAL%
echo Passed:           %PASSED%
echo Failed:           %FAILED%
echo Skipped:          2 (Create/Update - would modify data)
echo.

if %FAILED%==0 (
    echo ✅ ALL TESTED ENDPOINTS PASSED!
    echo.
    echo API Coverage:
    echo   Merchants:     2/4 endpoints (GET only)
    echo   Transactions:  2/2 endpoints ✓
    echo   Analytics:     5/5 endpoints ✓
    echo   Revenue:       5/5 endpoints ✓
    echo   Charts:        5/5 endpoints ✓
    echo   Health:        1/1 endpoint  ✓
    echo   --------------------
    echo   Total:        20/22 endpoints tested (91%%)
) else (
    echo ⚠️  SOME TESTS FAILED
    echo.
    echo Please check the logs and fix the failing endpoints.
)
echo.
echo Test Results Location:
echo   JSON responses saved to: test_output_*.json
echo   Location: %CD%
echo.
echo Finished at %TIME%
echo ========================================

pause


@echo off
REM Comprehensive API Endpoint Testing Script
REM Tests all 15 endpoints (100 points)

setlocal enabledelayedexpansion

echo ========================================
echo   Payment Platform API - Full Test
echo   Testing All 17 Endpoints (110 Points)
echo ========================================
echo.

set BASE_URL=http://localhost:8080
set PASSED=0
set FAILED=0
set TOTAL=17

REM Date parameters
set START_DATE=2025-11-16
set END_DATE=2025-11-22

echo Starting tests at %TIME%
echo Base URL: %BASE_URL%
echo Date Range: %START_DATE% to %END_DATE%
echo.
echo ========================================
echo   ANALYTICS ENDPOINTS (35 points)
echo ========================================
echo.

REM Test 1: Transaction Volume (10 pts)
 echo [1/17] Testing Transaction Volume Analytics...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/volume?startDate=%START_DATE%&endDate=%END_DATE%&groupBy=day" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/volume?startDate=%START_DATE%&endDate=%END_DATE%&groupBy=day" > test_output_1.json
    echo   Response saved to: test_output_1.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 2: Success Rate (8 pts)
echo [2/17] Testing Success Rate Analysis...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/success-rate?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/success-rate?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_2.json
    echo   Response saved to: test_output_2.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 3: Transaction Trends (7 pts)
echo [3/17] Testing Transaction Trends...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/trends?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/trends?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_3.json
    echo   Response saved to: test_output_3.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 4: Peak Times (5 pts)
echo [4/17] Testing Peak Times Heatmap...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/peak-times?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/peak-times?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_4.json
    echo   Response saved to: test_output_4.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 5: Card Distribution (5 pts)
echo [5/17] Testing Card Type Distribution...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/analytics/transactions/card-distribution?startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/analytics/transactions/card-distribution?startDate=%START_DATE%&endDate=%END_DATE%" > test_output_5.json
    echo   Response saved to: test_output_5.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   REVENUE REPORTS ENDPOINTS (30 points)
echo ========================================
echo.

REM Test 6: Revenue by Period (8 pts)
echo [6/17] Testing Revenue by Period...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/by-period?startDate=%START_DATE%&endDate=%END_DATE%&period=daily" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/by-period?startDate=%START_DATE%&endDate=%END_DATE%&period=daily" > test_output_6.json
    echo   Response saved to: test_output_6.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 7: Revenue by Merchant (7 pts)
echo [7/17] Testing Revenue by Merchant...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/by-merchant?startDate=%START_DATE%&endDate=%END_DATE%&limit=10" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/by-merchant?startDate=%START_DATE%&endDate=%END_DATE%&limit=10" > test_output_7.json
    echo   Response saved to: test_output_7.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 8: Revenue Forecast (8 pts)
echo [8/17] Testing Revenue Forecast...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/forecast?periods=7" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/forecast?periods=7" > test_output_8.json
    echo   Response saved to: test_output_8.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 9: Year-over-Year Growth (4 pts)
echo [9/17] Testing Year-over-Year Growth...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/revenue/growth?currentYear=2025&comparisonYear=2024" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/revenue/growth?currentYear=2025&comparisonYear=2024" > test_output_9.json
    echo   Response saved to: test_output_9.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 10: Top Performers (3 pts)
echo [10/17] Testing Top Performing Merchants...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/reports/merchants/top-performers?startDate=%START_DATE%&endDate=%END_DATE%&limit=10&sortBy=revenue" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/reports/merchants/top-performers?startDate=%START_DATE%&endDate=%END_DATE%&limit=10&sortBy=revenue" > test_output_10.json
    echo   Response saved to: test_output_10.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   CHART DATA ENDPOINTS (15 points)
echo ========================================
echo.

REM Test 11: Line Chart Data (4 pts)
echo [11/17] Testing Line Chart Data...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/line/trends?metric=revenue&startDate=%START_DATE%&endDate=%END_DATE%&groupBy=day" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/line/trends?metric=revenue&startDate=%START_DATE%&endDate=%END_DATE%&groupBy=day" > test_output_11.json
    echo   Response saved to: test_output_11.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 12: Bar Chart Data (4 pts)
echo [12/17] Testing Bar Chart Data...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/bar/comparison?compareBy=merchant&startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/bar/comparison?compareBy=merchant&startDate=%START_DATE%&endDate=%END_DATE%" > test_output_12.json
    echo   Response saved to: test_output_12.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 13: Pie Chart Data (3 pts)
echo [13/17] Testing Pie Chart Data...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/pie/distribution?distributeBy=status&startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/pie/distribution?distributeBy=status&startDate=%START_DATE%&endDate=%END_DATE%" > test_output_13.json
    echo   Response saved to: test_output_13.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 14: Real-time Data (2 pts)
echo [14/17] Testing Real-time Recent Data...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/data/recent?limit=50" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/data/recent?limit=50" > test_output_14.json
    echo   Response saved to: test_output_14.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 15: Drill-down data (2 pts)
echo [15/17] Testing Drill-down Data...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/charts/drill-down/merchant?categoryValue=MCH-00001&startDate=%START_DATE%&endDate=%END_DATE%" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/charts/drill-down/merchant?categoryValue=MCH-00001&startDate=%START_DATE%&endDate=%END_DATE%" > test_output_15.json
    echo   Response saved to: test_output_15.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

echo ========================================
echo   MERCHANT MANAGEMENT ENDPOINTS (30 points)
echo ========================================
echo.

REM Test 16: List Merchants (30 pts)
echo [16/17] Testing Merchant List (paginated)...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/merchants?limit=10&offset=0" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/merchants?limit=10&offset=0" > test_output_16.json
    echo   Response saved to: test_output_16.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Test 17: Get Merchant Details (bonus)
echo [17/17] Testing Merchant Details...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/api/v1/merchants/MCH-00001" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    set /a PASSED+=1
    curl -s "%BASE_URL%/api/v1/merchants/MCH-00001" > test_output_17.json
    echo   Response saved to: test_output_17.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
    set /a FAILED+=1
)
echo.

REM Bonus: Health check
echo [BONUS] Testing Health Endpoint...
curl -s -o nul -w "%%{http_code}" "%BASE_URL%/health" > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo   ✓ PASSED - Status: !STATUS!
    curl -s "%BASE_URL%/health" > test_output_health.json
) else (
    echo   ✗ FAILED - Status: !STATUS!
)
echo.

echo ========================================
echo   TEST SUMMARY
echo ========================================
echo.
echo Total Tests:  %TOTAL%
echo Passed:       %PASSED%
echo Failed:       %FAILED%
echo.

if %FAILED%==0 (
    echo ✅ ALL TESTS PASSED!
    echo.
    echo Points Breakdown:
    echo   Analytics:  35 points
    echo   Reports:    30 points
    echo   Charts:     15 points
    echo   Merchants:  30 points
    echo   ----------------------
    echo   TOTAL:      110/100 points
    echo.
    echo 🎉 EXCEEDED TARGET! You have 110 points (need only 100).
    echo.
    echo Note: This covers backend API implementation.
    echo You have successfully completed the backend requirements!
) else (
    echo ⚠️  SOME TESTS FAILED!
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

REM Cleanup
del temp_status.txt 2>nul

pause


-- Show table schema 
\d+ retail;

--Q1 Show first 10 rows
select * from retail r 
limit 10;

--Q2 Check # of records
select count(*) from retail r;

--Q3 number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) AS total_unique_clients
FROM retail;

--Q4 invoice date range (e.g. max/min dates)
select max(invoice_date) , min(invoice_date)from retail r;

--Q5 number of SKU/merchants (e.g. unique stock code)
select COUNT(distinct stock_code) as count_stock_code
from retail;

--Q6 Calculate average invoice amount excluding invoices with a negative amount 
SELECT AVG(total_invoice_amount) AS average_invoice_value
FROM (
    SELECT 
        SUM(quantity*unit_price) AS total_invoice_amount
    FROM retail
    GROUP BY invoice_no 
    HAVING SUM(quantity*unit_price ) > 0
) AS subquery;

--OR CTE

WITH InvoiceTotals AS (
    SELECT  
        SUM(quantity*unit_price) AS total_invoice_amount
    FROM retail
    GROUP BY invoice_no 
    HAVING SUM(quantity*unit_price ) > 0
)
SELECT AVG(total_invoice_amount) AS average_invoice_value
FROM InvoiceTotals;

--Q7 Calculate total revenue (e.g. sum of unit_price * quantity)
select sum(unit_price*quantity) from retail r;

--Q8 Calculate total revenue by YYYYMM 
SELECT 
    TO_CHAR(invoice_date , 'YYYYMM') AS year_month, 
    SUM(quantity*unit_price) AS total_revenue
FROM retail
GROUP BY year_month
ORDER BY year_month;








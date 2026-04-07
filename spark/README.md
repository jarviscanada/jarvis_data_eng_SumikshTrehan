# Retail Data Analytics with PySpark (Databricks)

## Project Overview

The *Retail Data Analytics with PySpark* project is focused on analyzing large-scale retail datasets to extract valuable insights that can guide decision-making in the retail industry. Using PySpark, a Python library for Apache Spark, this project efficiently handles big data, providing insights into sales performance, customer behavior, and key business metrics.

## Key Goals of the Project

1. **Data Exploration and Preprocessing**:
   - The project starts by loading retail data into PySpark's DataFrame format, exploring the structure and content of the data (e.g., checking for missing values, duplicates, and inconsistencies).
   - Data cleaning is performed by handling missing values, converting data types, and removing irrelevant or incorrect records to ensure accurate analysis.

2. **Sales Analysis**:
   - A key focus of the project is to analyze sales data across various dimensions (e.g., time periods, geographic locations, product categories).
   - Metrics such as **total sales**, **average sales per product**, **total revenue**, and **sales trends over time** are calculated, helping to identify high-performing products and sales patterns.

3. **Product and Customer Insights**:
   - Product performance is calculated by aggregating sales data, identifying top-selling products, their categories, and sales behavior.
   - Customer behavior is analyzed by grouping data based on purchasing patterns, frequency of purchases, and product preferences, helping to identify trends in customer choices and loyalty.

4. **Business Metrics**:
   - Key performance indicators (KPIs) such as **Sales Growth**, **Revenue per Customer**, and **Profit Margins** are calculated.
   - These KPIs help businesses understand financial health and optimize inventory management, pricing strategies, and customer engagement.

5. **Data Visualization**:
   - The project generates visualizations to summarize the findings in a clear and interpretable way, including sales distribution by category, seasonal trends, and customer demographics. This helps stakeholders make data-driven decisions.

## Installation

To get started with this project, you need to install PySpark and other required libraries. Follow the steps below to set up your environment:

### Prerequisites

- Python 3.x
- Java 8 or higher
- Apache Spark (PySpark)
- Jupyter Notebook (optional but recommended for easier interaction)

### Zeppelin notebook queries 
## PySpark Query

## Fetching GDP Growth Data using PySpark

```python
df = spark.sql("""
    SELECT * FROM wdi_csv_parquet 
    WHERE countryname='Canada' 
    AND indicatorcode='NY.GDP.MKTP.KD.ZG'
""")

z.show(df)
```

## Fetching canada growth year by year

```python
wdi_canada_df = spark.sql("""
SELECT year, indicatorvalue
FROM wdi_csv_parquet
WHERE TRIM(indicatorcode) = 'NY.GDP.MKTP.KD.ZG' 
AND LOWER(TRIM(countryname)) = 'canada'
ORDER BY year
""")

z.show(wdi_canada_df.select("year", "indicatorvalue"))
``` 

## Fetching GDP Growth Data for All Countries (Distributed and Sorted)

```python
wdi_all_countries_df = spark.sql("""
SELECT countryname,
       year,
       indicatorcode,
       indicatorvalue
FROM wdi_csv_parquet
WHERE TRIM(indicatorcode) = 'NY.GDP.MKTP.KD.ZG'
DISTRIBUTE BY countryname
SORT BY countryname, year
""")

z.show(wdi_all_countries_df.select("countryname", "year", "indicatorcode", "indicatorvalue"))
```

## Finding Maximum GDP Growth Year for Each Country

```python
wdi_max_gdp_df = spark.sql("""
SELECT wdi_csv_parquet.indicatorvalue AS value, 
       wdi_csv_parquet.year AS year, 
       wdi_csv_parquet.countryname AS country 
FROM (
    SELECT MAX(indicatorvalue) AS ind, countryname 
    FROM wdi_csv_parquet 
    WHERE indicatorcode = 'NY.GDP.MKTP.KD.ZG' 
    AND indicatorvalue <> 0 
    GROUP BY countryname
) t1 
INNER JOIN wdi_csv_parquet 
ON t1.ind = wdi_csv_parquet.indicatorvalue 
AND t1.countryname = wdi_csv_parquet.countryname
""")

z.show(wdi_max_gdp_df.select("country", "year", "value"))
```


# Introduction
As a Data Engineer at Jarvis Consulting, I am leading a Proof of Concept (PoC) for London Gift Shop (LGS), a UK-based online retailer facing stagnant revenue growth despite ten years of operation. My role is to bridge the gap between their existing transactional data and the marketing team's need for advanced insights, especially since their internal IT department lacks the resources to take on new projects.

LGS will utilize my analytical results to move away from generic sales tactics toward data-driven, targeted marketing. Specifically, they will use my findings to develop precision campaignssuch as tailored email promotions and exclusive eventsaimed at acquiring new customers and retaining high-value wholesalers.

Work and Technologies: I am developing an ETL (Extract, Transform, Load) and analytics pipeline using Python and Pandas for data manipulation, and Numpy for numerical analysis. My entire workflow is managed within Jupyter Notebooks, and I am using Docker to maintain a consistent environment across the project.

# Implementation
## Project Architecture
The architecture is designed to be decoupled from the LGS production environment to meet security requirements.It utilizes a Docker-based setup where a PostgreSQL container stores the sanitized transaction data, and a Jupyter container serves as the development interface. These containers communicate via a bridge network, allowing me to process the SQL dump provided by the LGS IT team without touching their live Azure cloud.

## Data Analytics and Wrangling
I am using the data to perform RFM (Recency, Frequency, Monetary) Segmentation, which is the primary strategy for increasing revenue.
 
#### Recency:
Recency measures the time elapsed since a customer's last purchase. It is a critical indicator of customer engagement; the more recently a customer has shopped, the more likely they are to respond to current marketing messages and make a follow-up purchase.

#### Recency Calculation:
Recency is determined through a two-step process:

Defining the Snapshot Date: First, a snapshot_date is established by finding the most recent invoice_date in the entire dataset and adding one day (to represent "today").

Calculating the Gap: For each unique customer, the code identifies their latest purchase date (x.max()) and subtracts it from the snapshot_date. The result is converted into the number of days.


#### Frequency
In my code, Frequency is calculated using the 'nunique' function on the invoice column.

#### Frequency calculation
A single order often contains multiple items, appearing as multiple rows in the database. By using nunique, I ensure we only count the number of times a customer successfully completed a checkout, rather than the total number of items they bought.

#### Monetary
The line_total is the building block for the Monetary value in the RFM model.
I first calculate the sub-total for every item line using the formula:

#### Monetary calculation
High Monetary: These are the "Whale" customers. Even if they shop less frequently than others, their large order volumes keep the business profitable.

Low Monetary: These may be small retail hobbyists or customers who only buy clearance items.

## Our Case RFM 

1. Champions
RFM Score: 545
Count: 837 customers
Recency (Mean): 7.56 days
Frequency (Mean): 19.34 purchases
Monetary (Mean): £10,901.13

Marketing Strategy: These are your most frequent and highest-spending customers who have shopped in the last week. Focus on Retention, not acquisition. Use VIP rewards, early access to new products, and personalized "thank you" gestures. They are your brand advocates; treat them like partners.

2. Can't Lose
RFM score: 255
Count: 71 customers
Recency (Mean): 330.23 days
Frequency (Mean): 15.93 purchases
Monetary (Mean): £8,355.68

Marketing Strategy: These were power users who have gone silent for nearly a year. Focus on Reactivation. Because they have a high lifetime value, they deserve aggressive win-back offers (deep discounts or "we miss you" gifts). You need to identify why they leftwas it a bad service experience or a competitor?

3. Hibernating
Exact Stats:
RFM score: 212
Count: 1,523 customers
Recency (Mean): 458.28 days
Frequency (Mean): 1.25 purchases
Monetary (Mean): £438.03

Marketing Strategy: This is your largest group, but they are low-value and have been inactive for over 15 months. Focus on Resource Efficiency. Don't waste your primary marketing budget here. Use automated, low-cost email sequences or bulk seasonal promotions. If they don't respond to a "last chance" offer, they are effectively lost.

#### Marketing Signficance:
Low Recency Value: Indicates an active, "fresh" customer who has purchased very recently. They should be targeted with loyalty rewards or new product announcements.

High Recency Value: Indicates a customer who has not purchased in a long time. These customers are categorized as "At-Risk" or "Hibernating" and require different strategies, such as aggressive re-engagement discounts, to bring them back to the store.

High Frequency: Suggests a "Habitual Buyer" or a reliable wholesaler who has integrated LGS into their own supply chain. The marketing team should offer "Frequency Incentives," such as a loyalty card or a "Buy 5, Get 10% Off Your 6th Order" promotion to push them into the high-frequency category.

Low Frequency: Suggests "One-off" gift buyers or new customers who have not yet committed to the brand. These customers need "Welcome Back" education. Send them content about the variety of products LGS offers to show that the shop isn't just for one specific occasion.

For High Monetary: LGS should provide "White Glove Service." This includes dedicated account managers, early access to new stock, or bulk-buy discounts that aren't available to the general public.

For Low Monetary: The goal here is to increase the Average Order Value (AOV). The marketing team can use "Upsell" strategies, such as "Free shipping on orders over £50" or "Recommended Add-ons" during the checkout process.

By categorizing customers into segments like "Champions" or "At-Risk," I provide the marketing team with the exact list of users who need either loyalty rewards or re-engagement discounts. Additionally, my analysis of monthly sales growth and user retention patterns helps LGS identify which seasons or product types are underperforming.

## Improvements
Real-time Integration: Transition from static SQL/CSV dumps to a live data stream from the LGS Azure environment using Azure Data Factory for up-to-the-minute marketing insights.


Predictive Analytics: Implement Machine Learning models to forecast future sales and predict customer churn, allowing the marketing team to act before a high-value wholesaler leaves.

Automated Visualization: Deploy an interactive Power BI or Streamlit dashboard so the LGS marketing team can explore the data themselves without needing to run code.

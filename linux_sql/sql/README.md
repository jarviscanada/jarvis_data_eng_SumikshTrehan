# SQL Query Practice

# Introduction
This project contains a collection of SQL queries developed as part of the Jarvis Consulting Data Engineering training. The queries are designed to solve various data retrieval and analysis problems using a sample relational database.

# Database Schema
The queries are written for a database involving:
- **Members**: Personal details and registration info.
- **Facilities**: Information about various gym/club facilities.
- **Bookings**: Records of facility usage by members.

#### SQL Queries Included
The `queries.sql` file contains solutions for:
1. **Basic Data Retrieval**: Filtering and sorting records.
2. **Joins and Subqueries**: Combining data across multiple tables.
3. **Aggregations**: Using `GROUP BY`, `SUM`, and `COUNT` for reporting.
4. **Window Functions**: Performing complex analysis like ranking or running totals (if applicable).

#### How to Run the Queries
1. Ensure you have a PostgreSQL instance or docker postgres server running.
2. Connect to your database using `psql`.
3. Run the script. First run the postgres server on docker on expose on port 5432 and then from local linux machine run psql client side:
   psql-h locahost -U postgres -d exercises -p 5432 -f queries.sql 


# CREATE TABLES 
```sql
CREATE TABLE cd.members (
  memid integer NOT NULL, 
  surname character varying(200) NOT NULL, 
  firstname character varying(200) NOT NULL, 
  address character varying(300) NOT NULL, 
  zipcode integer NOT NULL, 
  telephone character varying(20) NOT NULL, 
  recommendedby integer, 
  joindate timestamp NOT NULL, 
  CONSTRAINT members_pk PRIMARY KEY (memid), 
  CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE 
  SET 
    NULL
);
```

###### CREATE TABLE 
```sql
CREATE TABLE cd.facilities (
  facid integer NOT NULL, 
  name character varying(100) NOT NULL, 
  membercost numeric NOT NULL, 
  guestcost numeric NOT NULL, 
  initialoutlay numeric NOT NULL, 
  monthlymaintenance numeric NOT NULL, 
  CONSTRAINT facilities_pk PRIMARY KEY (facid)
);
```

###### CREATE TABLE
```sql
CREATE TABLE cd.bookings (
  bookid integer NOT NULL, 
  facid integer NOT NULL, 
  memid integer NOT NULL, 
  starttime timestamp NOT NULL, 
  slots integer NOT NULL, 
  CONSTRAINT bookings_pk PRIMARY KEY (bookid), 
  CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid), 
  CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
``` 

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
###### CREATE TABLE 
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

###### 1. Insert a new facility
```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);
```

###### 2. Insert using a subquery to calculate the next ID
```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
    ((SELECT max(facid) + 1 FROM cd.facilities), 'Spa', 20, 30, 100000, 800);
```

###### 3. Update a single record
```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;
```

###### 4. Update a record using values calculated from another record
```sql
UPDATE cd.facilities facs
SET
    membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE facid = 0),
    guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE facid = 0)
WHERE facs.facid = 1;
```

###### 5. Delete all records from the bookings table
```sql
DELETE FROM cd.bookings;
```

###### 6. Delete a specific member based on a condition
```sql
DELETE FROM cd.members
WHERE memid = 37;
```

###### 7. Filter using basic arithmetic and multiple conditions
```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0
  AND membercost < (monthlymaintenance / 50);
```

###### 8. Pattern matching using LIKE
```sql
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';
```

###### 9. Filter using the IN operator
```sql
SELECT * FROM cd.facilities
WHERE facid IN (1, 5);
```

###### 10. Filtering by date
```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';
```

###### 11. Combining results with UNION
```sql
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;
```

###### 12. Retrieve start times for bookings by 'David Farrell'
```sql
SELECT bks.starttime
FROM cd.bookings bks
JOIN cd.members meb
  ON bks.memid = meb.memid
WHERE meb.firstname = 'David'
  AND meb.surname = 'Farrell';
```

###### 13. Filter by facility name and specific date range
```sql
SELECT bks.starttime, fac.name
FROM cd.bookings bks
JOIN cd.facilities fac
  ON bks.facid = fac.facid
WHERE fac.name IN ('Tennis Court 1', 'Tennis Court 2')
  AND bks.starttime >= '2012-09-21'
  AND bks.starttime < '2012-09-22'
ORDER BY bks.starttime;
```

###### 14. Self-join to find who recommended whom
```sql
SELECT
    mems.firstname AS mem_fname,
    mems.surname AS mem_sname,
    recs.firstname AS rec_fname,
    recs.surname AS rec_sname
FROM
    cd.members mems
LEFT JOIN cd.members recs
    ON recs.memid = mems.recommendedby
ORDER BY
    mems.surname, mems.firstname;
```

###### 15. Find all members who have recommended someone else
```sql
SELECT DISTINCT
    recs.firstname AS firstname,
    recs.surname AS surname
FROM
    cd.members mems
INNER JOIN cd.members recs
    ON recs.memid = mems.recommendedby
ORDER BY
    surname, firstname;
```

###### 16. List members and their recommenders using a subquery
```sql
SELECT DISTINCT
    mems.firstname || ' ' || mems.surname AS member,
    (SELECT recs.firstname || ' ' || recs.surname AS recommender
     FROM cd.members recs
     WHERE recs.memid = mems.recommendedby)
FROM
    cd.members mems
ORDER BY
    member;
```

###### 17. Count recommendations per member
```sql
SELECT recommendedby, COUNT(recommendedby)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
```

###### 18. Total slots booked per facility
```sql
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
```

###### 19. Total slots per facility for September 2012
```sql
SELECT facid, SUM(slots)
FROM cd.bookings
WHERE starttime >= '2012-09-01'
  AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);
```

###### 20. Total slots per facility per month in 2012
```sql
SELECT
    facid,
    EXTRACT(MONTH FROM starttime) AS month,
    SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-01-01'
  AND starttime < '2013-01-01'
GROUP BY facid, month
ORDER BY facid, month;
```

###### 21. Count unique members who have made a booking
```sql
SELECT COUNT(DISTINCT memid)
FROM cd.bookings;
```

###### 22. First booking after Sept 1st per member
```sql
SELECT
    mems.surname,
    mems.firstname,
    mems.memid,
    MIN(bks.starttime) AS starttime
FROM cd.bookings bks
INNER JOIN cd.members mems
    ON mems.memid = bks.memid
WHERE bks.starttime >= '2012-09-01'
GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY mems.memid;
```

###### 23. List members with a global count (Scalar Subquery)
```sql
SELECT
    (SELECT COUNT(*) FROM cd.members) AS count,
    firstname,
    surname
FROM cd.members
ORDER BY joindate;
```

###### 24. Generate a sequential row number by join date
```sql
SELECT
    ROW_NUMBER() OVER (ORDER BY joindate),
    firstname,
    surname
FROM cd.members
ORDER BY joindate;
```

###### 25. Facility with the highest number of slots
```sql
SELECT facid, SUM(slots) AS total
FROM cd.bookings
GROUP BY facid
ORDER BY total DESC
LIMIT 1;
```

###### 26. FORMATTING NAMES (Concatenation)
```sql
SELECT
    surname || ', ' || firstname AS name
FROM
    cd.members;
```

###### 27. FINDING FORMATTING PATTERNS (Regular Expressions)
```sql
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone ~ '[()]';
```

###### 28. FREQUENCY ANALYSIS (Substrings & Grouping)
```sql
SELECT
    SUBSTR(surname, 1, 1) AS letter,
    COUNT(*) AS count
FROM
    cd.members
GROUP BY
    letter
ORDER BY
    letter;
```

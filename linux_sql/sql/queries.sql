-- 1. Insert a new facility
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);

-- 2. Insert using a subquery to calculate the next ID
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
    ((SELECT max(facid) + 1 FROM cd.facilities), 'Spa', 20, 30, 100000, 800);

-- 3. Update a single record
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;

-- 4. Update a record using values calculated from another record
UPDATE cd.facilities facs
SET 
    membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE facid = 0),
    guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE facid = 0)
WHERE facs.facid = 1;

-- 5. Delete all records from the bookings table
-- Importance: ????
DELETE FROM cd.bookings;

-- 6. Delete a specific member based on a condition
-- Importance: ????
DELETE FROM cd.members
WHERE memid = 37;

-- 7. Filter using basic arithmetic and multiple conditions
-- Importance: ??????
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0
  AND membercost < (monthlymaintenance / 50);

-- 8. Pattern matching using LIKE
-- Importance: ??????
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';

-- 9. Filter using the IN operator
-- Importance: ????????
SELECT * FROM cd.facilities
WHERE facid IN (1, 5);

-- 10. Filtering by date
-- Importance: ??????
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';

-- 11. Combining results with UNION
-- Importance: ????????
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;

-- 12. Retrieve start times for bookings by 'David Farrell'
-- Importance: ????????
SELECT bks.starttime
FROM cd.bookings bks
JOIN cd.members meb
  ON bks.memid = meb.memid
WHERE meb.firstname = 'David'
  AND meb.surname = 'Farrell';

-- 13. Filter by facility name and specific date range
-- Importance: ????????
SELECT bks.starttime, fac.name
FROM cd.bookings bks
JOIN cd.facilities fac
  ON bks.facid = fac.facid
WHERE fac.name IN ('Tennis Court 1', 'Tennis Court 2')
  AND bks.starttime >= '2012-09-21'
  AND bks.starttime < '2012-09-22'
ORDER BY bks.starttime;

-- 14. Self-join to find who recommended whom
-- Importance: ????????
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

-- 15. Find all members who have recommended someone else
-- Importance: ????
SELECT DISTINCT
    recs.firstname AS firstname,
    recs.surname AS surname
FROM
    cd.members mems
INNER JOIN cd.members recs
    ON recs.memid = mems.recommendedby
ORDER BY
    surname, firstname;

-- 16. List members and their recommenders using a subquery
-- Importance: ????
SELECT DISTINCT
    mems.firstname || ' ' || mems.surname AS member,
    (SELECT recs.firstname || ' ' || recs.surname AS recommender
     FROM cd.members recs
     WHERE recs.memid = mems.recommendedby)
FROM
    cd.members mems
ORDER BY
    member;

-- 17. Count recommendations per member
-- Importance: ????????
SELECT recommendedby, COUNT(recommendedby)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

-- 18. Total slots booked per facility
-- Importance: ????????
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

-- 19. Total slots per facility for September 2012
-- Importance: ????????
SELECT facid, SUM(slots)
FROM cd.bookings
WHERE starttime >= '2012-09-01'
  AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);

-- 20. Total slots per facility per month in 2012
-- Importance: ????????
SELECT
    facid,
    EXTRACT(MONTH FROM starttime) AS month,
    SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-01-01'
  AND starttime < '2013-01-01'
GROUP BY facid, month
ORDER BY facid, month;

-- 21. Count unique members who have made a booking
-- Importance: ????????
SELECT COUNT(DISTINCT memid)
FROM cd.bookings;

-- 22. First booking after Sept 1st per member
-- Importance: ??????
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

-- 23. List members with a global count (Scalar Subquery)
-- Importance: ??????
SELECT
    (SELECT COUNT(*) FROM cd.members) AS count,
    firstname,
    surname
FROM cd.members
ORDER BY joindate;

-- 24. Generate a sequential row number by join date
-- Importance: ??????
SELECT
    ROW_NUMBER() OVER (ORDER BY joindate),
    firstname,
    surname
FROM cd.members
ORDER BY joindate;

-- 25. Facility with the highest number of slots
-- Importance: ??????
SELECT facid, SUM(slots) AS total
FROM cd.bookings
GROUP BY facid
ORDER BY total DESC
LIMIT 1;


-- 26. FORMATTING NAMES (Concatenation)
-- The '||' operator joins strings together. 
-- Here we are creating a "Last, First" format.
SELECT 
    surname || ', ' || firstname AS name 
FROM 
    cd.members;


-- 27. FINDING FORMATTING PATTERNS (Regular Expressions)
SELECT 
    memid, 
    telephone 
FROM 
    cd.members 
WHERE 
    telephone ~ '[()]';


-- 28. FREQUENCY ANALYSIS (Substrings & Grouping)
-- substr(string, start, length) chops the string.
SELECT 
    SUBSTR(surname, 1, 1) AS letter, 
    COUNT(*) AS count 
FROM 
    cd.members
GROUP BY 
    letter
ORDER BY 
    letter;

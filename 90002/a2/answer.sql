-- Q1
SELECT DISTINCT
    '1856981' AS StuID,
    d.dept_Id AS DepartmentID,
    d.dept_Name AS DepartmentName,
    r.room_Type AS RoomType
FROM Department6981 AS d
INNER JOIN Room6981 AS r
    ON d.dept_Id = r.dept_Id
ORDER BY d.dept_Name, r.room_Type;


-- Q2
SELECT
	'1856981' AS StuID,
    d.dept_ID,
    d.dept_Name,
    w.ward_No,
    w.ward_Name,
    COUNT(b.bed_No) AS Total_Bed 
FROM Department6981 AS d
INNER JOIN Ward6981 AS w
	ON d.dept_ID = w.dept_ID
INNER JOIN Bed6981 AS b
    ON w.ward_No = b.ward_No
GROUP BY d.dept_ID,
		d.dept_Name,
		w.ward_No,
        w.ward_Name
HAVING COUNT(bed_No) >= 10
ORDER BY Total_Bed DESC;
    
    
-- Q3
SELECT
	'1856981' AS StuID,
    CONCAT(d.FName,' ',d.LName) AS DoctorName,
    a.appointment_Date AS AppointmentDate,
    p.LName AS Patient_LastName,
    p.patient_Id AS PatientID
FROM Appointment6981 AS a
INNER JOIN Patients6981 AS p
	ON a.patient_Id = p.patient_Id
INNER JOIN Doctor6981 AS d
	ON a.doct_Id = d.doct_Id
WHERE YEAR(CURDATE()) - 1 = YEAR(a.appointment_Date) AND MONTH(a.appointment_Date) = 1
GROUP BY d.doct_Id, 
		CONCAT(d.FName,' ',d.LName),
        p.patient_Id,
        p.LName,
        a.appointment_Date
ORDER BY d.doct_Id, a.appointment_Date;
		
    
    
    
-- Q4
SELECT
	'1856981' AS StuID,
    CONCAT(d.LName,',',d.FName) AS Doctor_Name,
    a.appointment_Date AS Appointment_Date,
    CONCAT(p.FName,' ',p.LName) AS Patient_Name,
    a.payment_amount AS The_Most_Expensive_Amount
FROM Appointment6981 AS a
INNER JOIN Patients6981 as p
	ON a.patient_Id = p.patient_Id
INNER JOIN Doctor6981 AS d
	ON d.doct_Id = a.doct_Id
WHERE 
	a.payment_amount = (
		SELECT MAX(a.payment_amount)
		FROM Appointment6981 AS a
    )
ORDER BY a.payment_amount DESC;
    


-- Q5
SELECT
	'1856981' AS StuID,
    w.ward_No AS Ward_No,
    w.ward_Name AS Ward_Name
FROM Ward6981 AS w
LEFT JOIN Bed6981 AS b
	ON w.ward_No = b.ward_No
GROUP BY 
	w.ward_No,
	w.ward_Name
HAVING COUNT(b.bed_No) = 0
ORDER BY w.ward_Name;

-- Q6
SELECT
	'1856981' AS StuID,
	d.dept_Id AS Department_Id,
    d.dept_Name AS Department_Name,
    COUNT(r.room_No) AS Unused_Rooms
FROM Department6981 AS d
INNER JOIN Room6981 AS r
	ON r.dept_Id = d.dept_Id
LEFT JOIN RoomRecords6981 AS rr
	ON r.room_No = rr.room_no
WHERE r.room_Type <> 'Consultation Room'
	AND rr.room_no IS NULL
GROUP BY
	d.dept_Id,
    d.dept_Name
HAVING COUNT(r.room_No) > 0
ORDER BY Unused_Rooms DESC;
    

-- Q7
SELECT
	'1856981' AS StuID,
    AVG(TIMESTAMPDIFF(
    MINUTE,
    TIMESTAMP(s.surgery_Date, s.start_Time),
    TIMESTAMP(s.surgery_Date, s.end_Time))) AS Average_Surgery_Length_Minutes
FROM Patients6981 AS p
INNER JOIN SurgeryRecord6981 AS s
	ON s.patient_Id = p.patient_Id
INNER JOIN MedicalRecord6981 AS m
	ON m.patient_Id = p.patient_Id
WHERE LOWER(m.diagnosis) LIKE '%diabetes%'
	AND s.surgery_Date > m.visit_Date;


-- Q8
SELECT
	'1856981' AS StuID,
	d.doct_Id AS DoctorID,
    CONCAT(d.FName,' ',d.LName) AS DoctorName,
    CONCAT(n.FName,' ',n.LName) AS NurseName,
    CONCAT(h.FName,' ',h.LName) AS HelperName
FROM SurgeryRecord6981 AS s
INNER JOIN Helpers6981 AS h
	ON h.helper_Id = s.helper_Id
INNER JOIN Nurse6981 AS n
	ON n.nurse_Id = s.nurse_Id
INNER JOIN Doctor6981 AS d
	ON d.doct_Id = s.surgeon_ID
WHERE d.LName = n.LName
		OR d.LName = h.LName
        OR n.LName = h.LName
ORDER BY CONCAT(d.FName,' ',d.LName);


-- Q9
SELECT
	'1856981' AS StuID,
    dp.dept_Name AS DepartmentName,
    'Doctor' AS MedStaff,
    COUNT(dt.doct_Id) AS Number
FROM Department6981 AS dp
INNER JOIN Doctor6981 AS dt
	ON dt.dept_Id = dp.dept_Id
GROUP BY dp.dept_Name
UNION ALL
SELECT 
	'1856981' AS StuID,
    dp.dept_Name AS DepartmentName,
    'Nurse' AS MedStaff,
    COUNT(n.nurse_Id) AS Number
FROM Department6981 AS dp
INNER JOIN Nurse6981 AS n
	ON dp.dept_Id = n.dept_Id
GROUP BY dp.dept_Name
UNION ALL
SELECT
	'1856981' AS StuID,
    dp.dept_Name AS DepartmentName,
    'Helper' AS MedStaff,
    COUNT(h.helper_Id) AS Number
FROM Department6981 AS dp
INNER JOIN Helpers6981 AS h
	ON dp.dept_Id = h.dept_Id
GROUP BY dp.dept_Name
UNION ALL
SELECT
	'1856981' AS StuID,
    '' AS DepartmentName,
    'Total' AS MedStaff,
    (SELECT COUNT(*) FROM Doctor6981)
        + (SELECT COUNT(*) FROM Nurse6981)
        + (SELECT COUNT(*) FROM Helpers6981) AS Number;


-- Q10.a
ALTER TABLE Room6981
ADD COLUMN capacity INT;

DESCRIBE Room6981;


-- Q10.b
UPDATE Room6981
SET capacity = 4
WHERE room_Type = 'Standard Room'
	AND room_No > 0;

SELECT
	'1856981' AS StuID,
	room_No,
	dept_Id,
	room_Type,
	capacity
FROM Room6981
WHERE room_Type = 'Standard Room';


-- Q11.a
CREATE VIEW SurgeonSurgeryCount6981 AS
SELECT
	d.doct_Id AS Surgeon_Id,
	CONCAT(d.FName, ' ', d.LName) AS Surgeon_Name,
	COUNT(s.surgery_Id) AS Number_Of_Surgeries
FROM Doctor6981 AS d
INNER JOIN SurgeryRecord6981 AS s
	ON d.doct_Id = s.surgeon_Id
GROUP BY
	d.doct_Id,
	CONCAT(d.FName, ' ', d.LName);

SELECT *
FROM SurgeonSurgeryCount6981;


-- Q11.b
SELECT
	'1856981' AS StuID,
	Surgeon_Id,
	Surgeon_Name,
	Number_Of_Surgeries
FROM SurgeonSurgeryCount6981
WHERE Number_Of_Surgeries = (
	SELECT MAX(Number_Of_Surgeries)
	FROM SurgeonSurgeryCount6981);




























    

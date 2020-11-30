CREATE DATABASE Insight_Demo;

create user Insight_Admin password 'shinz9474';

Create table Test_Run (
TestRunID int PRIMARY KEY,
config JSON NOT NULL,
Created_Date DATE NOT NULL DEFAULT CURRENT_DATE,
Modified_date DATE
);
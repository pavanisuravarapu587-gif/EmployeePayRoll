CREATE DATABASE IF NOT EXISTS payroll_db;
USE payroll_db;

CREATE TABLE IF NOT EXISTS employee (
    emp_id         INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(60)  NOT NULL,
    department     VARCHAR(30),
    designation    VARCHAR(40),
    gender         VARCHAR(10),
    basic_salary   DECIMAL(10,2) NOT NULL,
    bonus_eligible BOOLEAN DEFAULT FALSE,
    bonus_percent  INT DEFAULT 0
);

INSERT INTO employee (name, department, designation, gender, basic_salary, bonus_eligible, bonus_percent) VALUES
('Asha Rao',   'IT',      'Developer',  'Female', 45000, TRUE, 10),
('Ravi Kumar', 'Finance', 'Accountant', 'Male',   32000, FALSE, 0),
('Meena Iyer', 'HR',      'HR Manager', 'Female', 60000, TRUE, 15);

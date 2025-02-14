CREATE TABLE payroll (
    payroll_id INTEGER PRIMARY KEY AUTOINCREMENT,
    employee_id INTEGER NOT NULL,
    hours_worked REAL NOT NULL,
    pto_hours REAL NOT NULL,
    gross_pay REAL NOT NULL,
    net_pay REAL NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees (employee_id)
);
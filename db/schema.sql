-- Schema bootstrapping script for the springtest1 application.
-- Run this in MySQL to prepare the `test1` database used by Spring Boot.

CREATE DATABASE IF NOT EXISTS test1
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE test1;

-- Drop the table when refreshing the schema. Remove this block if you prefer migrations.
DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  age INT NOT NULL,
  salary DECIMAL(15, 2) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO employees (name, age, salary) VALUES
  ('Alice Johnson', 28, 52000.00),
  ('Brian King', 35, 68000.00),
  ('Carla Mendez', 41, 74500.00);

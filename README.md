# ITFac_Batch21_Group14
IS4600-In21-S7-IT-Quality-Assurance | Qualitix Group 14 | Assignment Repository

# QA Training App Automation

## Project Overview
This project is developed as part of the IS3440 – IT Quality Assurance group assignment.
The objective is to automate functional test scenarios for the QA Training App using
Behavior Driven Development (BDD) principles.

## Automation Framework
- Serenity BDD
- Cucumber (Gherkin)
- Selenium WebDriver (UI Automation)
- Rest Assured (API Automation)
- Maven (Build Tool)

## Application Under Test
QA Training App supports two user roles:
- Admin User
- Normal User

Functional areas tested:
- Category Management
- Plant Management
- Sales Management
- Role-based access control

## Test Coverage
- UI Automation (Admin & User)
- API Automation (Admin & User)

## How to Run Tests
1. Ensure the QA Training App is running locally
2. Clone the repository
3. Run the following command:

```bash
mvn clean verify

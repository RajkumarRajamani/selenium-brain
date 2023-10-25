@Test-Feature
Feature: Login into web app
  Scenario: Validate user login
    Given user launch app
    When they login with valid credentials
    Then they are directed to home page

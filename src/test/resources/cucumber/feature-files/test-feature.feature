@FID-Demo @Test-Feature
Feature: Login into web app

  @Test-feature @NoBrowser @DataTable-Test
  Scenario: Validate user login1.0
    Given Step 1
      | eventType   | code   | umr          | policy         |
      | firmOrder   | code01 | B2024OCT2401 | POL09OCT202401 |
      | writtenLine | code02 | B2024OCT2402 | POL09OCT202402 |
Feature: User - Low stock indicator

  Background:
    Given the QA Training App is running
    And I am on the login page
    When I login as regular user
    Then I should be redirected to the dashboard

  @Smoke @User @Plants
  Scenario: TC_UI_USER_PLANT_008 Verify Low badge when quantity < 5
    When I am on the Plants list page
    Then I should see at least one plant with quantity less than 5
    And I should see Low badge displayed next to that quantity

  @Smoke @User @Plants
  Scenario: TC_UI_USER_PLANT_009 Verify Low badge NOT shown when quantity >= 5
    When I am on the Plants list page
    Then I should see no Low badge for plants with quantity 5 or more

  @Smoke @User @Plants
  Scenario: TC_UI_USER_PLANT_011 Verify pagination visibility
    When I am on the Plants list page
    Then I should see pagination controls

  @Smoke @User @Plants
  Scenario: TC_UI_USER_PLANT_013 Verify empty plant list message
    When I am on the Plants list page
    Then I should see the empty plant list message

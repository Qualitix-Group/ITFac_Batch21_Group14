Feature: Category Create Authorization

  @user
  Scenario: TC_API_USER_CAT_002 Verify normal user is not allowed to add a category
    When user creates a main category with name "Anthurium"
    Then create category access should be forbidden

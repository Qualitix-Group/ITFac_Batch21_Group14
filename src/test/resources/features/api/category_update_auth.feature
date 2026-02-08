Feature: Category Update Authorization

  @user
  Scenario: TC_API_USER_CAT_001 Verify normal user is not allowed to update a category
    When user updates category id 1 with name "Anthurium" and parentId 2
    Then access should be forbidden

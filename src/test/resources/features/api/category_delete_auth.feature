Feature: Category Delete Authorization

  @user
  Scenario: TC_API_USER_CAT_003 Verify normal user is not allowed to delete a category
    When user deletes category with id 1
    Then delete access should be forbidden

Feature: Category Search

  @user
  Scenario: TC_API_USER_CAT_005 Verify category search by category name
    When user searches categories by name "Anthurium" with page 0 and size 10
    Then search should return 200 and all category names should contain "Anthurium"

  @user
  Scenario: TC_API_USER_CAT_006 Verify categories are filtered by parent category ID
    When user filters categories by parentId 1 with page 0 and size 10
    Then filter should return 200 and some category data

  @user
  Scenario: TC_API_USER_CAT_007 Verify response when page number contains no category data
    When user requests only page 999 and size 10
    Then page should return 200 and no category data

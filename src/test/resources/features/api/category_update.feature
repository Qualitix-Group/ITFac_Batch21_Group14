Feature: Update Category

  @admin
  Scenario: TC_API_ADMIN_CAT_UPD_001 Verify update category with valid name and valid parentId
    When admin updates category id 3 with name "EditNew3" and parentId 1
    Then update should be successful and returned name should be "EditNew3"

  @admin
  Scenario: TC_API_ADMIN_CAT_UPD_002 Verify update category as main category (parentId = null)
    When admin updates category id 1 with name "Anthurium1" and parentId is null
    Then update should be successful and returned name should be "Anthurium1"


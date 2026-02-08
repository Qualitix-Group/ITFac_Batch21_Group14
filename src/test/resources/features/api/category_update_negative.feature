Feature: Update Category - Negative validations

  @admin
  Scenario: TC_API_ADMIN_CAT_UPD_003 Verify category cannot be its own parent
    When admin updates category id 1 with name "Anthurium" and parentId 1
    Then self parent validation error should be returned with message "Category cannot be its own parent"

  @admin
  Scenario: TC_API_ADMIN_CAT_UPD_004 Verify update category with invalid parentId
    When admin updates category id 1 with name "NewEdit" and parentId 999999
    Then not found error should be returned with message "Category not found"


package com.group14.qa.api.tests;

import com.group14.qa.api.steps.CategorySearchApiSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategorySearchApiTest {

    private final CategorySearchApiSteps steps = new CategorySearchApiSteps();

    @When("user searches categories by name {string} with page {int} and size {int}")
    public void user_searches_categories_by_name(String name, Integer page, Integer size) {
        steps.userSearchesByName(name, page, size);
    }

    @Then("search should return 200 and all category names should contain {string}")
    public void search_should_return_200_and_names_should_match(String name) {
        steps.verifySearchResultsMatchName(name);
    }

    @When("user filters categories by parentId {int} with page {int} and size {int}")
    public void user_filters_categories_by_parentId(Integer parentId, Integer page, Integer size) {
        steps.userFiltersByParentId(parentId.longValue(), page, size);
    }

    @Then("filter should return 200 and some category data")
    public void filter_should_return_200_and_some_data() {
        steps.verifyFilterHasData();
    }

    @When("user requests only page {int} and size {int}")
    public void user_requests_only_page(Integer page, Integer size) {
        steps.userRequestsOnlyPage(page, size);
    }

    @Then("page should return 200 and no category data")
    public void page_should_return_200_and_no_data() {
        steps.verifyEmptyPage();
    }


}

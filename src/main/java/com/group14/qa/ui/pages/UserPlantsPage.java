package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import java.util.List;

public class UserPlantsPage extends PageObject {

    @FindBy(xpath = "//a[@href='/ui/plants']")
    private WebElementFacade plantsSidebarLink;

    @FindBy(xpath = "//a[@href='/ui/dashboard']")
    private WebElementFacade dashboardSidebarLink;

    @FindBy(xpath = "//input[@name='name' or @placeholder='Search plant']")
    private WebElementFacade searchInput;

    @FindBy(xpath = "//button[text()='Search' or @type='submit' or contains(@class, 'btn-primary')]")
    private WebElementFacade searchButton;

    @FindBy(xpath = "//a[@href='/ui/plants' and contains(@class, 'btn-outline-secondary')]")
    private WebElementFacade clearSearchButton;

    @FindBy(xpath = "//td[contains(@class, 'text-center') and contains(text(), 'No plants found')]")
    private WebElementFacade noPlantsFoundMessage;

    public void navigateToPlantsPage() {
        getDriver().get("http://localhost:8080/ui/plants");
        waitForPageToLoad();
    }

    public void navigateToPlantsByCategory(String categoryId) {
        getDriver().get("http://localhost:8080/ui/plants/category/" + categoryId);
        waitForPageToLoad();
    }

    public void clickPlantsFromSidebar() {
        try {
            plantsSidebarLink.click();
            waitForPageToLoad();
        } catch (Exception e) {
            System.out.println("Error clicking plants from sidebar: " + e.getMessage());
            navigateToPlantsPage();
        }
    }

    public boolean isPlantsPageDisplayed() {
        waitForPageToLoad();
        try {
            String currentUrl = getDriver().getCurrentUrl();
            return currentUrl.contains("/plants");
        } catch (Exception e) {
            return false;
        }
    }

    public int getPlantCount() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> plantRows = findAll(By.xpath("//table/tbody/tr[count(td) > 1]"));
            return plantRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getActualPlantCount() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> rows = findAll(By.xpath("//table/tbody/tr[not(contains(td, 'No plants found'))]"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoPlantsMessageDisplayed() {
        waitForPageToLoad();
        try {
            return find(By.xpath("//td[contains(@class, 'text-center') and contains(text(), 'No plants found')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyDeleteButtonVisible() {
        waitForPageToLoad();
        try {
            return findAll(By.xpath("//table/tbody/tr//button[contains(@class, 'btn-outline-danger')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDeleteButtonVisibleForAnyPlant() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> plantRows = findAll(By.xpath("//table/tbody/tr[count(td) > 1]"));

            if (plantRows.isEmpty()) {
                System.out.println("No plant rows found in table");
                return false;
            }

            System.out.println("Found " + plantRows.size() + " plant rows");

            for (int i = 0; i < plantRows.size(); i++) {
                WebElementFacade row = plantRows.get(i);

                boolean hasDeleteButton = false;
                try {
                    hasDeleteButton = row.find(By.xpath(".//button[contains(@class, 'btn-outline-danger')]")).isDisplayed();
                } catch (Exception e) {
                    hasDeleteButton = false;
                }

                if (hasDeleteButton) {
                    System.out.println("Row " + (i+1) + " HAS a delete button - user should not see this!");
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.out.println("Error checking delete buttons: " + e.getMessage());
            return false;
        }
    }

    public int getDeleteButtonCount() {
        waitForPageToLoad();
        try {
            return findAll(By.xpath("//table/tbody/tr//button[contains(@class, 'btn-outline-danger')]")).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getAllPlantNames() {
        waitForPageToLoad();
        List<String> plantNames = new java.util.ArrayList<>();
        try {
            List<WebElementFacade> nameCells = findAll(By.xpath("//table/tbody/tr[count(td) > 1]/td[1]"));
            for (WebElementFacade cell : nameCells) {
                String plantName = cell.getText().trim();
                if (!plantName.isEmpty()) {
                    plantNames.add(plantName);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting all plant names: " + e.getMessage());
        }
        return plantNames;
    }

    public String getFirstPlantNameWithSpaces() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> nameCells = findAll(By.xpath("//table/tbody/tr[count(td) > 1]/td[1]"));
            for (WebElementFacade cell : nameCells) {
                String plantName = cell.getText().trim();
                if (plantName.contains(" ")) {
                    return plantName;
                }
            }

            if (!nameCells.isEmpty()) {
                return nameCells.get(0).getText().trim();
            }
            return "";
        } catch (Exception e) {
            System.out.println("Error finding plant name with spaces: " + e.getMessage());
            return "";
        }
    }

    public void searchForPlant(String plantName) {
        waitForPageToLoad();
        try {
            System.out.println("Attempting to search for plant: " + plantName);

            WebElementFacade inputField = null;
            try {
                inputField = find(By.xpath("//input[@name='name']"));
            } catch (Exception e) {
                try {
                    inputField = find(By.xpath("//input[@placeholder='Search plant']"));
                } catch (Exception ex) {
                    try {
                        inputField = find(By.xpath("//form//input[@type='text']"));
                    } catch (Exception ex2) {
                        System.out.println("Could not find search input field");
                        return;
                    }
                }
            }

            inputField.clear();
            waitFor(300);

            if (plantName.contains(" ")) {
                inputField.type(plantName);
            } else {
                inputField.type(plantName);
            }

            waitFor(500);

            WebElementFacade searchBtn = null;
            try {
                searchBtn = find(By.xpath("//button[text()='Search']"));
            } catch (Exception e) {
                try {
                    searchBtn = find(By.xpath("//form//button[@type='submit']"));
                } catch (Exception ex) {
                    try {
                        searchBtn = find(By.xpath("//form//button[contains(@class, 'btn-primary')]"));
                    } catch (Exception ex2) {
                        System.out.println("Could not find search button");
                        return;
                    }
                }
            }

            searchBtn.click();
            waitForPageToLoad();
            System.out.println("Search completed for: " + plantName);

        } catch (Exception e) {
            System.out.println("Error searching for plant: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void searchByPlantNameWithSpaces() {
        String plantNameWithSpaces = getFirstPlantNameWithSpaces();
        if (!plantNameWithSpaces.isEmpty()) {
            System.out.println("Searching for plant with spaces: " + plantNameWithSpaces);
            searchForPlant(plantNameWithSpaces);
        } else {
            System.out.println("No plant with spaces found in the table");
        }
    }


    public void searchByPartialName(String partialName) {
        System.out.println("Searching by partial name: " + partialName);
        searchForPlant(partialName);
    }


    public void searchByNonExistentName() {
        String nonExistentName = "NonexistentPlant";
        System.out.println("Searching by non-existent name: " + nonExistentName);
        searchForPlant(nonExistentName);
    }


    public String getFirstPlantName() {
        waitForPageToLoad();
        try {
            return find(By.xpath("(//table/tbody/tr[count(td) > 1]/td[1])[1]")).getText().trim();
        } catch (Exception e) {
            System.out.println("Error getting first plant name: " + e.getMessage());
            return "";
        }
    }


    public String getPartialNameFromFirstPlant() {
        String fullName = getFirstPlantName();
        if (fullName.length() >= 3) {
            return fullName.substring(0, 3);
        } else if (fullName.length() > 0) {
            return fullName.substring(0, 1);
        }
        return "";
    }


    public boolean isPlantExactMatchInTable(String plantName) {
        waitForPageToLoad();
        try {
            if (isNoPlantsMessageDisplayed()) {
                return false;
            }

            List<WebElementFacade> nameCells = findAll(By.xpath("//table/tbody/tr[count(td) > 1]/td[1]"));
            for (WebElementFacade cell : nameCells) {
                String cellText = cell.getText().trim();
                if (cellText.equalsIgnoreCase(plantName.trim())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error checking exact plant match: " + e.getMessage());
            return false;
        }
    }


    public boolean isPlantInPlantsTable(String plantName) {
        waitForPageToLoad();
        try {
            if (isNoPlantsMessageDisplayed()) {
                return false;
            }

            if (isPlantExactMatchInTable(plantName)) {
                return true;
            }

            return find(By.xpath("//table/tbody/tr[count(td) > 1][td[contains(text(), '" + plantName + "')]]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyPlantContainsPartialName(String partialName) {
        waitForPageToLoad();
        try {
            List<WebElementFacade> plantCells = findAll(By.xpath("//table/tbody/tr[count(td) > 1]/td[1]"));
            for (WebElementFacade cell : plantCells) {
                String plantName = cell.getText().toLowerCase();
                if (plantName.contains(partialName.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error checking partial name match: " + e.getMessage());
            return false;
        }
    }

    public int getCountOfPlantsMatchingPartialName(String partialName) {
        waitForPageToLoad();
        int count = 0;
        try {
            List<WebElementFacade> plantCells = findAll(By.xpath("//table/tbody/tr[count(td) > 1]/td[1]"));
            for (WebElementFacade cell : plantCells) {
                String plantName = cell.getText().toLowerCase();
                if (plantName.contains(partialName.toLowerCase())) {
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error counting plants matching partial name: " + e.getMessage());
        }
        return count;
    }

    public void scrollThroughPlantList() {
        evaluateJavascript("window.scrollTo(0, document.body.scrollHeight);");
        waitFor(1000);
        evaluateJavascript("window.scrollTo(0, 0);");
        waitFor(500);
    }

    public void printDebugInfo() {
        System.out.println("=== USER DEBUG INFO ===");
        System.out.println("Current URL: " + getDriver().getCurrentUrl());
        System.out.println("Page Title: " + getDriver().getTitle());

        int plantCount = getPlantCount();
        int deleteButtonsCount = getDeleteButtonCount();

        System.out.println("Plant count: " + plantCount);
        System.out.println("Actual plant count (debug): " + getActualPlantCount());
        System.out.println("Delete buttons count (should be 0): " + deleteButtonsCount);
        System.out.println("Any delete button visible: " + isAnyDeleteButtonVisible());
        System.out.println("No plants message displayed: " + isNoPlantsMessageDisplayed());

        try {
            List<String> allPlantNames = getAllPlantNames();
            System.out.println("All plant names in table (" + allPlantNames.size() + " plants):");
            for (int i = 0; i < allPlantNames.size(); i++) {
                System.out.println("  " + (i+1) + ". " + allPlantNames.get(i));
            }

            String plantWithSpaces = getFirstPlantNameWithSpaces();
            if (!plantWithSpaces.isEmpty()) {
                System.out.println("First plant with spaces found: " + plantWithSpaces);
            }
        } catch (Exception e) {
            System.out.println("Could not get plant names: " + e.getMessage());
        }

        try {
            boolean formExists = find(By.xpath("//form")).isDisplayed();
            System.out.println("Search form exists: " + formExists);

            if (formExists) {
                boolean searchInputExists = find(By.xpath("//input[@name='name']")).isDisplayed();
                boolean searchButtonExists = find(By.xpath("//button[text()='Search']")).isDisplayed();
                System.out.println("Search input exists: " + searchInputExists);
                System.out.println("Search button exists: " + searchButtonExists);

                String currentSearchValue = find(By.xpath("//input[@name='name']")).getValue();
                System.out.println("Current search input value: '" + currentSearchValue + "'");
            }
        } catch (Exception e) {
            System.out.println("Error checking search form: " + e.getMessage());
        }
    }

    private void waitForPageToLoad() {
        waitFor(2500);
    }

    public void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
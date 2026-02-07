package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import java.util.List;

public class AdminPlantsPage extends PageObject {

    @FindBy(xpath = "//div[contains(@class, 'alert-success')]//span")
    private WebElementFacade successMessageSpan;

    @FindBy(xpath = "//div[contains(@class, 'alert-success')]")
    private WebElementFacade successMessageDiv;

    @FindBy(xpath = "//div[contains(@class, 'alert-danger')]")
    private WebElementFacade errorMessageDiv;

    @FindBy(xpath = "//div[contains(@class, 'alert-warning')]")
    private WebElementFacade warningMessageDiv;

    @FindBy(xpath = "//div[contains(@class, 'alert-info')]")
    private WebElementFacade infoMessageDiv;

    @FindBy(xpath = "//h1[contains(text(), 'Whitelabel Error Page')]")
    private WebElementFacade whitelabelErrorPage;

    @FindBy(xpath = "//div[contains(text(), 'There was an unexpected error')]")
    private WebElementFacade unexpectedErrorDiv;

    @FindBy(xpath = "//body[contains(text(), 'Whitelabel Error Page')]")
    private WebElementFacade bodyWhitelabelError;

    @FindBy(xpath = "//div[contains(text(), 'Cannot delete') or contains(text(), 'cannot delete')]")
    private WebElementFacade cannotDeleteMessage;

    @FindBy(xpath = "//div[contains(text(), 'active sale') or contains(text(), 'Active sale') or contains(text(), 'referenced')]")
    private WebElementFacade activeSaleMessage;

    @FindBy(xpath = "//*[contains(text(), 'error') or contains(text(), 'Error')]")
    private List<WebElementFacade> anyErrorElements;

    @FindBy(xpath = "//a[@href='/ui/sales']")
    private WebElementFacade salesSidebarLink;

    @FindBy(xpath = "//a[@href='/ui/plants']")
    private WebElementFacade plantsSidebarLink;

    @FindBy(xpath = "//a[@href='/ui/dashboard']")
    private WebElementFacade dashboardSidebarLink;

    @FindBy(xpath = "//input[@type='search' or @placeholder='Search' or @name='search']")
    private WebElementFacade searchInput;

    @FindBy(xpath = "//button[contains(text(), 'Search') or @type='submit' or contains(@class, 'btn-search')]")
    private WebElementFacade searchButton;

    public void navigateToPlantsPage() {
        getDriver().get("http://localhost:8080/ui/plants");
        waitForPageToLoad();
    }

    public void navigateToSalesPage() {
        getDriver().get("http://localhost:8080/ui/sales");
        waitForPageToLoad();
    }

    public void navigateToPlantsByCategory(String categoryId) {
        getDriver().get("http://localhost:8080/ui/plants/category/" + categoryId);
        waitForPageToLoad();
    }

    public void clickSalesFromSidebar() {
        try {
            salesSidebarLink.click();
            waitForPageToLoad();
        } catch (Exception e) {
            System.out.println("Error clicking sales from sidebar: " + e.getMessage());
            navigateToSalesPage();
        }
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

    public boolean isSalesPageDisplayed() {
        waitForPageToLoad();
        try {
            String currentUrl = getDriver().getCurrentUrl();
            return currentUrl.contains("/sales");
        } catch (Exception e) {
            return false;
        }
    }

    public int getPlantCount() {
        waitForPageToLoad();
        try {
            return findAll(By.xpath("//table/tbody/tr[td]")).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoPlantsMessageDisplayed() {
        try {
            return find(By.xpath("//*[contains(text(), 'No plants found') or contains(text(), 'No data available')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddPlantButtonVisible() {
        try {
            return find(By.xpath("//a[contains(@href, '/ui/plants/add')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLowBadgeDisplayedForAnyPlant() {
        waitForPageToLoad();
        try {
            return find(By.xpath("//table//tr[td]//span[contains(@class, 'badge') and contains(text(), 'Low')]")).isDisplayed();
        } catch (Exception e) {
            System.out.println("No Low badge found for any plant: " + e.getMessage());
            return false;
        }
    }

    public boolean isLowBadgeDisplayedForRow(int rowNumber) {
        waitForPageToLoad();
        try {
            return find(By.xpath("(//table/tbody/tr[td])[" + rowNumber + "]//span[contains(@class, 'badge') and contains(text(), 'Low')]")).isDisplayed();
        } catch (Exception e) {
            System.out.println("No Low badge found for row " + rowNumber + ": " + e.getMessage());
            return false;
        }
    }

    public String getQuantityForRow(int rowNumber) {
        waitForPageToLoad();
        try {
            return find(By.xpath("(//table/tbody/tr[td])[" + rowNumber + "]/td[4]")).getText();
        } catch (Exception e) {
            try {
                String rowText = find(By.xpath("(//table/tbody/tr[td])[" + rowNumber + "]")).getText();
                return rowText.replaceAll("[^0-9]", "").substring(0, 1);
            } catch (Exception ex) {
                System.out.println("Could not get quantity for row " + rowNumber + ": " + ex.getMessage());
                return "0";
            }
        }
    }

    public int findFirstPlantWithLowQuantity() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> plantRows = findAll(By.xpath("//table/tbody/tr[td]"));

            for (int i = 0; i < plantRows.size(); i++) {
                try {
                    String quantityText = plantRows.get(i).find(By.xpath(".//td[contains(text(), 'Low')]/preceding-sibling::td[1]")).getText();
                    int quantity = Integer.parseInt(quantityText.trim());
                    if (quantity < 5) {
                        return i + 1;
                    }
                } catch (Exception e) {
                    String rowText = plantRows.get(i).getText();
                    String[] parts = rowText.split("\\s+");
                    for (String part : parts) {
                        try {
                            int quantity = Integer.parseInt(part);
                            if (quantity < 5) {
                                return i + 1;
                            }
                        } catch (NumberFormatException nfe) {
                            continue;
                        }
                    }
                }
            }
            System.out.println("No plant found with quantity less than 5");
            return -1;
        } catch (Exception e) {
            System.out.println("Error finding plant with low quantity: " + e.getMessage());
            return -1;
        }
    }

    public boolean verifyLowBadgeForLowQuantityPlants() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> plantRows = findAll(By.xpath("//table/tbody/tr[td]"));
            boolean allCorrect = true;

            for (int i = 0; i < plantRows.size(); i++) {
                int rowNum = i + 1;
                String quantityText = getQuantityForRow(rowNum);
                try {
                    int quantity = Integer.parseInt(quantityText.trim());
                    boolean hasLowBadge = isLowBadgeDisplayedForRow(rowNum);

                    if (quantity < 5 && !hasLowBadge) {
                        System.out.println("ERROR: Plant at row " + rowNum + " has quantity " + quantity + " but NO Low badge");
                        allCorrect = false;
                    } else if (quantity >= 5 && hasLowBadge) {
                        System.out.println("ERROR: Plant at row " + rowNum + " has quantity " + quantity + " but HAS Low badge (shouldn't)");
                        allCorrect = false;
                    } else {
                        System.out.println("OK: Plant at row " + rowNum + " has quantity " + quantity +
                                " and " + (hasLowBadge ? "HAS" : "NO") + " Low badge");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Could not parse quantity for row " + rowNum + ": " + quantityText);
                }
            }
            return allCorrect;
        } catch (Exception e) {
            System.out.println("Error verifying low badges: " + e.getMessage());
            return false;
        }
    }

    public boolean isWhitelabelErrorPageDisplayed() {
        try {
            boolean whitelabelVisible = false;
            try {
                whitelabelVisible = whitelabelErrorPage.isDisplayed();
            } catch (Exception e) {
                try {
                    whitelabelVisible = bodyWhitelabelError.isDisplayed();
                } catch (Exception ex) {
                    try {
                        String pageSource = getDriver().getPageSource();
                        whitelabelVisible = pageSource.contains("Whitelabel Error Page");
                    } catch (Exception ex2) {
                        whitelabelVisible = false;
                    }
                }
            }

            return whitelabelVisible;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getAllPlantNamesInSales() {
        waitForPageToLoad();
        List<String> plantNames = new java.util.ArrayList<>();
        try {
            List<WebElementFacade> rows = findAll(By.xpath("//table/tbody/tr/td[1]"));
            for (WebElementFacade row : rows) {
                plantNames.add(row.getText().trim());
            }
        } catch (Exception e) {
            System.out.println("Error getting plant names from sales: " + e.getMessage());
        }
        return plantNames;
    }

    public boolean isPlantInSales(String plantName) {
        waitForPageToLoad();
        try {
            try {
                return find(By.xpath("//table/tbody/tr/td[1][text()='" + plantName + "']")).isDisplayed();
            } catch (Exception e) {
                return find(By.xpath("//table/tbody/tr/td[1][contains(text(), '" + plantName + "')]")).isDisplayed();
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstPlantNameInSales() {
        waitForPageToLoad();
        try {
            return find(By.xpath("(//table/tbody/tr/td[1])[1]")).getText().trim();
        } catch (Exception e) {
            System.out.println("Error getting first plant name from sales: " + e.getMessage());
            return "";
        }
    }

    // Search methods
    public void searchForPlant(String plantName) {
        waitForPageToLoad();
        try {
            try {
                searchInput.type(plantName);
                waitFor(500);
                searchButton.click();
            } catch (Exception e) {
                try {
                    WebElementFacade input = find(By.xpath("//input[@type='text' or @type='search']"));
                    input.type(plantName);
                    waitFor(500);
                    WebElementFacade button = find(By.xpath("//button[@type='submit']"));
                    button.click();
                } catch (Exception ex) {
                    System.out.println("Could not perform search, continuing without search");
                }
            }
            waitForPageToLoad();
        } catch (Exception e) {
            System.out.println("Error searching for plant: " + e.getMessage());
        }
    }

    public boolean isPlantInPlantsTable(String plantName) {
        waitForPageToLoad();
        try {
            return find(By.xpath("//table/tbody/tr[td[contains(text(), '" + plantName + "')]]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPlantIdByName(String plantName) {
        waitForPageToLoad();
        try {
            WebElementFacade plantRow = find(By.xpath("//table/tbody/tr[td[contains(text(), '" + plantName + "')]]"));
            WebElementFacade editLink = plantRow.find(By.xpath(".//a[contains(@href, '/plants/edit/')]"));
            String href = editLink.getAttribute("href");
            String[] parts = href.split("/");
            return parts[parts.length - 1];
        } catch (Exception e) {
            System.out.println("Error getting plant ID for '" + plantName + "': " + e.getMessage());
            return "";
        }
    }

    // Delete button methods
    public int getDeleteButtonCount() {
        waitForPageToLoad();
        try {
            return findAll(By.xpath("//table/tbody/tr//button[contains(@class, 'btn-outline-danger')]")).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isDeleteButtonVisibleForEachPlant() {
        waitForPageToLoad();
        try {
            List<WebElementFacade> plantRows = findAll(By.xpath("//table/tbody/tr[td]"));

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

                if (!hasDeleteButton) {
                    System.out.println("Row " + (i+1) + " does NOT have a delete button");
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error checking delete buttons: " + e.getMessage());
            return false;
        }
    }

    public void clickDeleteButtonForPlantByName(String plantName) {
        waitForPageToLoad();
        try {
            System.out.println("Looking for delete button for plant: " + plantName);
            WebElementFacade plantRow = find(By.xpath("//table/tbody/tr[td[contains(text(), '" + plantName + "')]]"));
            WebElementFacade deleteButton = plantRow.find(By.xpath(".//button[contains(@class, 'btn-outline-danger')]"));
            deleteButton.click();
            waitFor(1500);
        } catch (Exception e) {
            System.out.println("Error clicking delete button for plant '" + plantName + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clickDeleteButtonForFirstPlant() {
        waitForPageToLoad();
        try {
            WebElementFacade firstDeleteButton = find(By.xpath("(//table/tbody/tr//button[contains(@class, 'btn-outline-danger')])[1]"));
            System.out.println("Clicking first delete button");
            firstDeleteButton.click();
            waitFor(1500);
        } catch (Exception e) {
            System.out.println("Error clicking delete button: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void confirmDeleteInAlert() {
        try {
            System.out.println("Checking for alert...");
            waitFor(500);

            if (isAlertPresent()) {
                System.out.println("Alert found, accepting it...");
                getDriver().switchTo().alert().accept();
                System.out.println("Alert accepted");
                waitForPageToLoad();
            } else {
                System.out.println("No alert found after clicking delete button");
            }
        } catch (Exception e) {
            System.out.println("Error confirming delete in alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelDeleteInAlert() {
        try {
            System.out.println("Cancelling delete in browser alert");
            getDriver().switchTo().alert().dismiss();
            waitFor(1000);
        } catch (Exception e) {
            System.out.println("Error cancelling delete in alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            waitFor(2000);

            boolean found = false;

            try {
                if (successMessageSpan.isDisplayed()) {
                    found = true;
                }
            } catch (Exception e) {
                try {
                    if (successMessageDiv.isDisplayed()) {
                        found = true;
                    }
                } catch (Exception ex) {
                    try {
                        WebElementFacade alert = find(By.xpath("//div[contains(@class, 'alert') and contains(@class, 'alert-success')]"));
                        if (alert.isDisplayed()) {
                            found = true;
                        }
                    } catch (Exception ex2) {

                    }
                }
            }

            if (found) {
                System.out.println("SUCCESS: Success message found");
            } else {
                System.out.println("DEBUG: No success message found after delete");
            }

            return found;
        } catch (Exception e) {
            System.out.println("Exception checking success message: " + e.getMessage());
            return false;
        }
    }

    public boolean doesSuccessMessageContain(String text) {
        try {
            List<WebElementFacade> alerts = findAll(By.xpath("//div[contains(@class, 'alert')]"));
            for (WebElementFacade alert : alerts) {
                if (alert.isDisplayed()) {
                    String alertText = alert.getText();
                    System.out.println("Found alert with text: '" + alertText + "'");
                    if (alertText.toLowerCase().contains(text.toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error checking success message: " + e.getMessage());
            return false;
        }
    }

    public boolean isAlertPresent() {
        try {
            getDriver().switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollThroughPlantList() {
        evaluateJavascript("window.scrollTo(0, document.body.scrollHeight);");
        waitFor(1000);
        evaluateJavascript("window.scrollTo(0, 0);");
        waitFor(500);
    }

    public void printDebugInfo() {
        System.out.println("=== DEBUG INFO ===");
        System.out.println("Current URL: " + getDriver().getCurrentUrl());
        System.out.println("Page Title: " + getDriver().getTitle());

        int plantCount = getPlantCount();
        int deleteButtonsCount = getDeleteButtonCount();

        System.out.println("Plant count: " + plantCount);
        System.out.println("Delete buttons count: " + deleteButtonsCount);

        System.out.println("Alert present: " + isAlertPresent());

        System.out.println("Whitelabel error page: " + isWhitelabelErrorPageDisplayed());


        System.out.println("Any Low badge displayed: " + isLowBadgeDisplayedForAnyPlant());
        System.out.println("Low badge verification: " + verifyLowBadgeForLowQuantityPlants());


        try {
            String pageSource = getDriver().getPageSource();
            System.out.println("Page source (first 500 chars): " + pageSource.substring(0, Math.min(500, pageSource.length())));
        } catch (Exception e) {
            System.out.println("Could not get page source: " + e.getMessage());
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
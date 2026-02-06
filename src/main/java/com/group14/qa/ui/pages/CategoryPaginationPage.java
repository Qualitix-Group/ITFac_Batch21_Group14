//package com.group14.qa.ui.pages;
//
//import net.serenitybdd.core.pages.PageObject;
//import net.serenitybdd.core.pages.WebElementFacade;
//import org.openqa.selenium.By;
//
//import java.util.List;
//
//public class CategoryPaginationPage extends PageObject {
//
//    private final By paginationContainer = By.cssSelector("ul.pagination");
//    private final By pageNumbers = By.cssSelector("ul.pagination li.page-item:not(.disabled) a.page-link");
//    private final By nextButton = By.xpath("//ul[contains(@class,'pagination')]//li[a[text()='Next']]");
//
//    // Check pagination container is visible
//    public boolean isPaginationVisible() {
//        return find(paginationContainer).waitUntilVisible().isDisplayed();
//    }
//
//    // Click Next button if visible and not disabled
//    public boolean clickNextButton() {
//        try {
//            WebElementFacade next = find(nextButton).waitUntilVisible().waitUntilClickable();
//
//            // Check if parent <li> is disabled
//            String liClass = next.findBy("..").getAttribute("class");
//            if (liClass.contains("disabled")) {
//                System.out.println("|---- Next button is disabled (already last page)");
//                return false; // gracefully skip
//            }
//
//            next.click();
//            System.out.println("|---- Next button clicked successfully");
//            return true;
//
//        } catch (Exception e) {
//            System.out.println("|---- Next button not clickable: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // Get page numbers
//    public List<WebElementFacade> getPageNumbers() {
//        return findAll(pageNumbers);
//    }
//
//    // Click page number by index (0-based)
//    public void clickPageNumber(int index) {
//        List<WebElementFacade> pages = getPageNumbers();
//        if (pages.isEmpty()) {
//            System.out.println("|---- No page numbers found");
//            return;
//        }
//        if (index >= 0 && index < pages.size()) {
//            pages.get(index).waitUntilClickable().click();
//            System.out.println("|---- Clicked page number: " + (index + 1));
//        } else {
//            System.out.println("|---- Invalid page index: " + index);
//        }
//    }
//}

package qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base page class providing common functionality for all page objects.
 * Implements best practices for:
 * - Explicit waits (no Thread.sleep)
 * - Safe element interactions
 * - Retry mechanisms for flaky elements
 * - Consistent timeout management
 */
public abstract class BasePage extends PageObject {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // Configurable timeouts
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration SHORT_TIMEOUT = Duration.ofSeconds(3);
    protected static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);
    protected static final int MAX_RETRY_ATTEMPTS = 3;

    /**
     * Wait for element to be visible with default timeout.
     */
    protected WebElementFacade waitForVisible(By locator) {
        return waitForVisible(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be visible with custom timeout.
     */
    protected WebElementFacade waitForVisible(By locator, Duration timeout) {
        return $(locator).withTimeoutOf(timeout).waitUntilVisible();
    }

    /**
     * Wait for element to be clickable with default timeout.
     */
    protected WebElementFacade waitForClickable(By locator) {
        return waitForClickable(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable with custom timeout.
     */
    protected WebElementFacade waitForClickable(By locator, Duration timeout) {
        return $(locator).withTimeoutOf(timeout).waitUntilClickable();
    }

    /**
     * Wait for URL to contain a specific path.
     */
    protected void waitForUrlContains(String urlPart) {
        waitForUrlContains(urlPart, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for URL to contain a specific path with custom timeout.
     */
    protected void waitForUrlContains(String urlPart, Duration timeout) {
        new WebDriverWait(getDriver(), timeout)
            .until(ExpectedConditions.urlContains(urlPart));
    }

    /**
     * Wait for page to fully load (document ready state).
     */
    protected void waitForPageLoad() {
        waitForPageLoad(DEFAULT_TIMEOUT);
    }

    /**
     * Wait for page to fully load with custom timeout.
     */
    protected void waitForPageLoad(Duration timeout) {
        new WebDriverWait(getDriver(), timeout).until(driver -> {
            String readyState = ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString();
            return "complete".equals(readyState);
        });
    }

    /**
     * Safe click with retry mechanism and JavaScript fallback.
     * Handles StaleElementReferenceException and ElementClickInterceptedException.
     */
    protected void safeClick(WebElement element) {
        safeClick(element, MAX_RETRY_ATTEMPTS);
    }

    /**
     * Safe click with configurable retry attempts.
     */
    protected void safeClick(WebElement element, int maxAttempts) {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                element.click();
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                lastException = e;
                log.debug("Click attempt {} failed: {}", attempt, e.getMessage());

                if (attempt < maxAttempts) {
                    // Try JavaScript click as fallback
                    try {
                        JavascriptExecutor js = (JavascriptExecutor) getDriver();
                        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
                        js.executeScript("arguments[0].click();", element);
                        return;
                    } catch (Exception jsException) {
                        log.debug("JS click fallback failed: {}", jsException.getMessage());
                    }
                }
            }
        }

        throw new RuntimeException("Failed to click element after " + maxAttempts + " attempts", lastException);
    }

    /**
     * Safe click on element located by selector.
     */
    protected void safeClick(By locator) {
        WebElementFacade element = waitForClickable(locator);
        safeClick(element);
    }

    /**
     * Type text into element with clear first.
     */
    protected void typeInto(By locator, String text) {
        WebElementFacade element = waitForVisible(locator);
        element.clear();
        element.type(text);
    }

    /**
     * Get text from element with retry for stale elements.
     */
    protected String getTextSafely(By locator) {
        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                return waitForVisible(locator).getText().trim();
            } catch (StaleElementReferenceException e) {
                log.debug("Stale element on getText attempt {}", attempt + 1);
            }
        }
        return "";
    }

    /**
     * Check if element is visible without throwing exception.
     */
    protected boolean isVisibleSafely(By locator) {
        try {
            return $(locator).withTimeoutOf(SHORT_TIMEOUT).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is present in DOM without throwing exception.
     */
    protected boolean isElementPresent(By locator) {
        try {
            return $(locator).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scroll element into view.
     */
    protected void scrollIntoView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({block:'center', behavior:'smooth'});", element);
    }

    /**
     * Scroll to top of page.
     */
    protected void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Scroll to bottom of page.
     */
    protected void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Highlight element for debugging (useful for visual verification).
     */
    protected void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(
            "arguments[0].style.border='3px solid red'; arguments[0].style.backgroundColor='yellow';",
            element
        );
    }

    /**
     * Get current page URL.
     */
    protected String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Get current page title.
     */
    protected String getPageTitle() {
        return getDriver().getTitle();
    }

    /**
     * Refresh the current page.
     */
    protected void refreshPage() {
        getDriver().navigate().refresh();
        waitForPageLoad();
    }

    /**
     * Navigate back in browser history.
     */
    protected void navigateBack() {
        getDriver().navigate().back();
        waitForPageLoad();
    }
}

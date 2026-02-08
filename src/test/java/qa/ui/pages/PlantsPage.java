package qa.ui.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Plants list page shared by Member3 (list/search/sort/filter) and Member4
 * (stock lookups).
 * Locator priority: id/name > data attributes > css > text/xpath.
 */
@DefaultUrl("/ui/plants")
public class PlantsPage extends PageObject {

	private final By table = By.cssSelector("table.table");
	private final By tableRows = By.cssSelector("table.table tbody tr");
	private final By tableHeaderCells = By.cssSelector("table.table thead th");
	private final By emptyState = By.xpath("//td[contains(normalize-space(),'No plant') or contains(normalize-space(),'No data')]");

	private final By searchInput = By.cssSelector("input[name='name'], input#name, input[placeholder*='Search']");
	private final By categorySelect = By.cssSelector("select[name='categoryId'], select#categoryId");
	private final By searchButton = By.xpath("//form[contains(@class,'g-2') and .//input[@name='name']]//button[contains(normalize-space(),'Search')]");

	private final By sortByPriceLink = By.cssSelector("a[href*='sortField=price'], th a[href*='price']");
	private final By sortByPriceLinkXPath = By.xpath("//th[a[contains(translate(.,'PRICE','price'),'price')]]//a");
	private final By sortByNameLink = By.cssSelector("a[href*='sortField=name'], th a[href*='name']");
	private final By sortByNameLinkXPath = By.xpath("//th[a[contains(translate(.,'NAME','name'),'name')]]//a");
	private final By sortByQuantityLink = By.cssSelector("a[href*='sortField=quantity'], th a[href*='quantity']");
	private final By sortByQuantityLinkXPath = By.xpath("//th[a[contains(translate(.,'QUANTITY','quantity'),'quantity')]]//a");

	private final By addPlantLink = By.cssSelector("a[href$='/ui/plants/add']");
	private final By rowDeleteButton = By.cssSelector("form[action*='/ui/plants/delete'] button.btn-outline-danger, button.btn-outline-danger");
	private final By rowEditLink = By.cssSelector("a[href*='/ui/plants/edit']");
	private final By pagination = By.cssSelector("ul.pagination");
	private final By paginationLinks = By.cssSelector("ul.pagination li.page-item a.page-link");
	private final By activePageLink = By.cssSelector("ul.pagination li.page-item.active a.page-link");
	private final By lowBadge = By.cssSelector("span.badge");
	private final By successAlert = By.cssSelector(".alert.alert-success");
	private final By errorAlert = By.cssSelector(".alert.alert-danger");
	private final By deleteModal = By.id("deleteModal");
	private final By deleteModalCancel = By.cssSelector("#deleteModal .btn-secondary, #deleteModal [data-bs-dismiss='modal']");

	public void openPage() {
		open();
		waitForPage();
	}

	private void waitForPage() {
		new WebDriverWait(getDriver(), Duration.ofSeconds(10))
				.until(driver -> {
					try {
						return $(table).isVisible() || $(emptyState).isVisible() || $(searchInput).isVisible();
					} catch (StaleElementReferenceException e) {
						return false;
					}
				});
	}

	public boolean isTableVisible() {
		return $(table).isVisible();
	}

	/** Returns column values using 1-based column index (as used in Member3 tests). */
	public List<String> getColumnValues(int columnIndex1Based) {
		int columnIndex = columnIndex1Based - 1; // convert to zero-based
		List<String> values = new ArrayList<>();
		for (WebElementFacade row : findAll(tableRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (columnIndex >= 0 && columnIndex < cells.size()) {
				values.add(cells.get(columnIndex).getText().trim());
			}
		}
		return values;
	}

	public void searchFor(String text) {
		int attempts = 0;
		while (attempts < 2) {
			try {
				WebElementFacade input = $(searchInput);
				if (input.isPresent()) {
					input.waitUntilVisible();
					input.clear();
					input.type(text);
				}
				return;
			} catch (StaleElementReferenceException e) {
				waitForPage();
				attempts++;
			}
		}
	}

	public void clickSearchButton() {
		int attempts = 0;
		while (attempts < 2) {
			try {
				WebElementFacade btn = $(searchButton);
				if (!btn.isPresent()) {
					return;
				}
				btn.waitUntilVisible();
				btn.waitUntilClickable();
				evaluateJavascript("arguments[0].scrollIntoView({block:'center'});", btn);
				btn.click();
				return;
			} catch (StaleElementReferenceException e) {
				waitForPage();
				attempts++;
			} catch (Exception ignored) {
				// fall through to JS fallback below
			}
		}
		// JS fallback
		try {
			WebElementFacade btn = $(searchButton);
			evaluateJavascript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
			return;
		} catch (Exception ignored) {
			// final fallback: submit form
		}
		try {
			WebElementFacade btn = $(searchButton);
			evaluateJavascript("arguments[0].form && arguments[0].form.submit();", btn);
		} catch (Exception ignored) {
			// give up; assertion later will fail if needed
		}
	}

	public void searchAndSubmit(String query) {
		searchFor(query);
		refreshResults(this::clickSearchButton);
	}

	/** Run an action (like clicking Search) and wait for table to refresh. */
	private void refreshResults(Runnable action) {
		WebElement oldTable = null;
		try {
			oldTable = getDriver().findElement(table);
		} catch (Exception ignored) {}

		action.run();

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
		if (oldTable != null) {
			try {
				wait.until(ExpectedConditions.stalenessOf(oldTable));
			} catch (Exception ignored) {}
		}
		waitForResults();
	}

	public void waitForResults() {
		new WebDriverWait(getDriver(), Duration.ofSeconds(8))
				.until(d -> {
					try {
						return $(tableRows).isPresent() || $(emptyState).isPresent();
					} catch (StaleElementReferenceException e) {
						return false;
					}
				});
	}

	public boolean rowsContainText(String text) {
		String needle = text.toLowerCase(Locale.ROOT);
		int attempts = 0;
		while (attempts < 2) {
			try {
				for (WebElementFacade row : findAll(tableRows)) {
					if (row.getText().toLowerCase(Locale.ROOT).contains(needle)) {
						return true;
					}
				}
				return false;
			} catch (StaleElementReferenceException e) {
				waitForResults();
				attempts++;
			}
		}
		return false;
	}

	public boolean allRowsMatchPlantNameContains(String text) {
		String needle = text.toLowerCase(Locale.ROOT);
		for (WebElementFacade row : findAll(tableRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.isEmpty()) {
				continue;
			}
			String name = cells.get(0).getText().toLowerCase(Locale.ROOT);
			if (!name.contains(needle)) {
				return false;
			}
		}
		return true;
	}

	public String selectFirstCategoryOption() {
		new WebDriverWait(getDriver(), Duration.ofSeconds(10))
			.until(ExpectedConditions.presenceOfElementLocated(categorySelect));

		int attempts = 0;
		while (attempts < 2) {
			try {
				WebElementFacade selectEl = $(categorySelect);
				if (!selectEl.isPresent()) {
					return "";
				}
				Select select = new Select(selectEl);
				List<WebElement> options = select.getOptions();
				for (WebElement opt : options) {
					String value = opt.getAttribute("value");
					if (value != null && !value.isBlank()) {
						String optionText = opt.getText().trim();
						select.selectByValue(value);
						// apply filter by submitting the form (search button)
						refreshResults(this::clickSearchButton);
						return optionText;
					}
				}
				return "";
			} catch (StaleElementReferenceException e) {
				waitForPage();
				attempts++;
			}
		}
		return "";
	}

	public boolean allRowsMatchCategory(String categoryName) {
		String needle = categoryName.toLowerCase(Locale.ROOT);
		for (WebElementFacade row : findAll(tableRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.size() < 2) {
				continue;
			}
			String category = cells.get(1).getText().toLowerCase(Locale.ROOT);
			if (!category.contains(needle)) {
				return false;
			}
		}
		return true;
	}

	public void clickSortByPrice() {
		findAndClick(sortByPriceLink, sortByPriceLinkXPath);
	}

	public void clickSortByName() {
		findAndClick(sortByNameLink, sortByNameLinkXPath);
	}

	public void clickSortByQuantity() {
		findAndClick(sortByQuantityLink, sortByQuantityLinkXPath);
	}

	private void findAndClick(By primary, By fallback) {
		WebElementFacade el = $(primary);
		if (el.isPresent()) {
			el.click();
			return;
		}
		WebElementFacade alt = $(fallback);
		if (alt.isPresent()) {
			alt.click();
		}
	}

	public String getFirstPlantName() {
		waitForResults();
		int attempts = 0;
		while (attempts < 2) {
			try {
				var rows = findAll(tableRows);
				if (rows.isEmpty()) {
					return "";
				}
				List<WebElement> cells = rows.get(0).findElements(By.tagName("td"));
				if (cells.isEmpty()) {
					return "";
				}
				return cells.get(0).getText().trim();
			} catch (StaleElementReferenceException e) {
				waitForResults();
				attempts++;
			}
		}
		return "";
	}

	public boolean isPaginationVisible() {
		return $(pagination).isVisible();
	}

	public int getActivePageNumber() {
		try {
			String text = $(activePageLink).getText().trim();
			return Integer.parseInt(text);
		} catch (Exception e) {
			return -1;
		}
	}

	public void clickNextPage() {
		clickPaginationLink("Next");
	}

	public void clickPreviousPage() {
		clickPaginationLink("Previous");
	}

	public void clickPageNumber(int pageNumber) {
		String target = String.valueOf(pageNumber);
		for (var link : findAll(paginationLinks)) {
			if (target.equals(link.getText().trim())) {
				link.click();
				return;
			}
		}
	}

	private void clickPaginationLink(String label) {
		for (var link : findAll(paginationLinks)) {
			if (label.equalsIgnoreCase(link.getText().trim())) {
				link.click();
				return;
			}
		}
	}

	public boolean isAddPlantVisible() {
		return $(addPlantLink).isVisible();
	}

	public boolean isAnyEditVisible() {
		return findAll(rowEditLink).stream().anyMatch(WebElement::isDisplayed);
	}

	public boolean isAnyDeleteVisible() {
		return findAll(rowDeleteButton).stream().anyMatch(WebElement::isDisplayed);
	}

	public boolean hasNoPlantsMessage() {
		return $(emptyState).isVisible();
	}

	public boolean anyRowHasLowBadge() {
		for (WebElementFacade row : findAll(tableRows)) {
			if (!row.findElements(lowBadge).isEmpty()
					&& row.findElements(lowBadge).stream().anyMatch(b -> b.getText().trim().equalsIgnoreCase("low"))) {
				return true;
			}
		}
		return false;
	}

	public boolean anyRowWithQtyAtLeastHasNoLowBadge(int minimumQty) {
		int qtyIndex = findQuantityColumnIndex();
		if (qtyIndex < 0) {
			qtyIndex = 3;
		}
		for (WebElementFacade row : findAll(tableRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.isEmpty() || qtyIndex >= cells.size()) continue;
			String qtyText = cells.get(qtyIndex).getText().replaceAll("[^0-9]", "");
			if (qtyText.isBlank()) continue;
			int qty = Integer.parseInt(qtyText);
			if (qty >= minimumQty) {
				boolean hasLow = row.findElements(lowBadge).stream()
						.anyMatch(b -> b.getText().trim().equalsIgnoreCase("low"));
				return !hasLow;
			}
		}
		return false;
	}
	/**
	 * Find the first plant row with quantity/stock > 0.
	 * Returns empty string when no such plant is found.
	 */
	public String findPlantWithPositiveStock() {
		waitForResults();
		int attempts = 0;
		while (attempts < 2) {
			try {
				int qtyIndex = findQuantityColumnIndex();
				if (qtyIndex < 0) {
					qtyIndex = 3; // fallback: 4th column in current UI
				}
				for (WebElementFacade row : findAll(tableRows)) {
					List<WebElement> cells = row.findElements(By.tagName("td"));
					if (cells.isEmpty()) {
						continue;
					}
					if (qtyIndex >= cells.size()) {
						continue;
					}
					String qtyText = cells.get(qtyIndex).getText().replaceAll("[^0-9]", "");
					if (qtyText.isBlank()) {
						continue;
					}
					int qty = Integer.parseInt(qtyText);
					if (qty > 0) {
						return cells.get(0).getText().trim();
					}
				}
				return "";
			} catch (StaleElementReferenceException e) {
				waitForResults();
				attempts++;
			} catch (NumberFormatException ignored) {
				// fall through to retry or return empty
				attempts++;
			}
		}
		return "";
	}

	public void clickDeleteForPlantName(String plantName) {
		String needle = plantName.toLowerCase(Locale.ROOT);
		for (WebElementFacade row : findAll(tableRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.isEmpty()) continue;
			if (cells.get(0).getText().toLowerCase(Locale.ROOT).contains(needle)) {
				row.findElement(rowDeleteButton).click();
				return;
			}
		}
	}

	public String clickEditFirstPlant() {
		waitForResults();
		var rows = findAll(tableRows);
		if (rows.isEmpty()) {
			return "";
		}
		List<WebElement> cells = rows.get(0).findElements(By.tagName("td"));
		String name = cells.isEmpty() ? "" : cells.get(0).getText().trim();
		rows.get(0).findElement(rowEditLink).click();
		return name;
	}

	public boolean hasSuccessMessage() {
		return $(successAlert).isVisible();
	}

	public boolean hasErrorMessage() {
		return $(errorAlert).isVisible();
	}

	public void acceptBrowserConfirmIfPresent() {
		try {
			getAlert().accept();
		} catch (Exception ignored) {
			// no alert present
		}
	}

	public boolean hasDeleteConfirmationOnFirstRow() {
		var buttons = findAll(rowDeleteButton);
		if (buttons.isEmpty()) return false;
		buttons.get(0).click();
		try {
			String alertText = getAlert().getText();
			getAlert().dismiss();
			return alertText != null && alertText.toLowerCase().contains("delete");
		} catch (Exception ignored) {
			// fallback to modal
		}
		try {
			return $(deleteModal).isVisible();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean cancelDeleteOnFirstRow() {
		var buttons = findAll(rowDeleteButton);
		if (buttons.isEmpty()) return false;
		int before = findAll(tableRows).size();
		buttons.get(0).click();
		try {
			getAlert().dismiss();
		} catch (Exception ignored) {
			try {
				$(deleteModalCancel).click();
			} catch (Exception ignored2) {
			}
		}
		waitForResults();
		int after = findAll(tableRows).size();
		return after == before;
	}

	public boolean adminActionsHiddenForUser() {
		boolean addHidden = !$(addPlantLink).isVisible();
		boolean editHidden = findAll(rowEditLink).isEmpty();
		boolean deleteHidden = findAll(rowDeleteButton).isEmpty();
		return addHidden && editHidden && deleteHidden;
	}

	/**
	 * Return the quantity/stock value for a given plant name. Returns -1 when not found.
	 * Detects the stock column by header text with a safe fallback to column 4 (zero-based index 3).
	 */
	public int getStockFor(String plantName) {
		int qtyIndex = findQuantityColumnIndex();
		if (qtyIndex < 0) {
			qtyIndex = 3; // fallback: 4th column in current UI
		}

		for (WebElementFacade row : findAll(tableRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.isEmpty()) continue;

			String name = cells.get(0).getText().trim();
			if (name.equalsIgnoreCase(plantName)) {
				if (qtyIndex < cells.size()) {
					String qtyText = cells.get(qtyIndex).getText().replaceAll("[^0-9]", "");
					if (!qtyText.isBlank()) {
						return Integer.parseInt(qtyText);
					}
				}
			}
		}
		return -1;
	}

	private int findQuantityColumnIndex() {
		var headers = findAll(tableHeaderCells);
		for (int i = 0; i < headers.size(); i++) {
			String text = headers.get(i).getText().toLowerCase(Locale.ROOT).trim();
			if (text.contains("qty") || text.contains("quantity") || text.contains("stock")) {
				return i;
			}
		}
		return -1;
	}
}

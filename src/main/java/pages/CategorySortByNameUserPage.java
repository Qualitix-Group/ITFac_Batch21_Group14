package pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class CategorySortByNameUserPage extends PageObject {

    private final By nameHeader = By.xpath("//table/thead//th[2]//a");
    private final By tableRows = By.cssSelector("table tbody tr");

    // Click the Name column header and wait for table to reload
    public void clickNameColumnHeader() {
        List<String> oldNames = getNames(); // capture current names
        find(nameHeader).waitUntilClickable().click();

        // Wait until table updates (first row text changes)
        waitForCondition().until(driver -> {
            List<String> newNames = getNames();
            return !newNames.equals(oldNames);
        });
    }

    // Ascending alphabetical check
    public boolean isSortedAscendingByName() {
        List<String> names = getNames();
        System.out.println("Names for ascending check: " + names);
        return isAscending(names);
    }

    // Descending alphabetical check
    public boolean isSortedDescendingByName() {
        List<String> names = getNames();
        System.out.println("Names for descending check: " + names);
        return isDescending(names);
    }

    // Extract Name column values
    private List<String> getNames() {
        return findAll(tableRows).stream()
                .map(row -> row.findBy("./td[2]").getText()
                        .replace("\u00A0", "")      // remove non-breaking spaces
                        .replaceAll("\\s+", " ")    // normalize spaces
                        .trim()
                )
                .collect(Collectors.toList());
    }

    // Ascending check
    private boolean isAscending(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareToIgnoreCase(list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    // Descending check
    private boolean isDescending(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareToIgnoreCase(list.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }
}

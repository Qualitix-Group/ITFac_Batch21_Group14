package pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class CategorySortByIDUserPage extends PageObject {

    private final By idHeader = By.xpath("//table/thead//th[1]//a");
    private final By tableRows = By.cssSelector("table tbody tr");

    public void clickIDColumnHeader() {
        find(idHeader).waitUntilClickable().click();
        waitForRenderedElements(tableRows);
    }

    public boolean isSortedAscendingByID() {
        return isAscending(getIds());
    }

    public boolean isSortedDescendingByID() {
        return isDescending(getIds());
    }

    private List<Integer> getIds() {
        return findAll(tableRows).stream()
                .map(row -> Integer.parseInt(
                        row.findBy("./td[1]").getText()   // ✅ FIX
                ))
                .collect(Collectors.toList());
    }

    private boolean isAscending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) return false;
        }
        return true;
    }

    private boolean isDescending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) < list.get(i + 1)) return false;
        }
        return true;
    }
}

package views.screen.product;

import java.util.ArrayList;
import java.util.List;

public class CheckboxIdManager {

    private static CheckboxIdManager instance;
    private List<Integer> selectedCheckboxIds;

    private CheckboxIdManager() {
        selectedCheckboxIds = new ArrayList<>();
    }

    public static CheckboxIdManager getInstance() {
        if (instance == null) {
            instance = new CheckboxIdManager();
        }
        return instance;
    }

    public List<Integer> getSelectedCheckboxIds() {
        return selectedCheckboxIds;
    }

    public int getSizeCheckboxId() {
        return selectedCheckboxIds.size();
    }

    public void addSelectedCheckboxId(Integer checkboxId) {
        selectedCheckboxIds.add(checkboxId);
    }

    public void removeSelectedCheckboxId(Integer checkboxId) {
        selectedCheckboxIds.remove(checkboxId);
    }

    public void clearSelectedCheckboxId() {
        selectedCheckboxIds.clear();
    }
}


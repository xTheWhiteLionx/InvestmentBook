package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logic.Investment;


/**
 * View-Controller for the person table.
 *
 * @author Marco Jakob
 */
public class FilterByName {

    @FXML
    private TextField filterField;
    @FXML
    private TableView<Investment> personTable;

    private ObservableList<Investment> masterData = FXCollections.observableArrayList();

    /**
     * Just add some sample data in the constructor.
     */
    public FilterByName() {
//        masterData.add(new Person("Hans", "Muster"));
//        masterData.add(new Person("Ruth", "Mueller"));
//        masterData.add(new Person("Heinz", "Kurz"));
//        masterData.add(new Person("Cornelia", "Meier"));
//        masterData.add(new Person("Werner", "Meyer"));
//        masterData.add(new Person("Lydia", "Kunz"));
//        masterData.add(new Person("Anna", "Best"));
//        masterData.add(new Person("Stefan", "Meier"));
//        masterData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Investment> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(investment -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare name of every investment with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return investment.getStockName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Investment> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);
    }
}

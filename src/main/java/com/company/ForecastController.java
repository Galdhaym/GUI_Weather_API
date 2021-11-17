package com.company;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ForecastController {
    @FXML
    private Label filterLabel;
    @FXML
    private Label daysLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane containerPane;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField daysTextField;
    @FXML
    private Button clearFilterButton;
    @FXML
    private ComboBox<String> signButton;
    @FXML
    private AnchorPane filterPanel;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private Button filterButton;
    @FXML
    private AnchorPane TableContainer;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private Button sortButton;
    @FXML
    private TableView<Day> forecastTable;
    @FXML
    private TextField filterValue;

    private ProgressIndicator progressIndicator;

    private ObservableList<Day> items;

    @FXML
    public void initialize() {
        initSortComboBox();
        initFilterComboBox();
        initSignComboBox();

        sortButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            sortTableByParam(sortComboBox, forecastTable);
        });

        filterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            filterTableByParam(filterComboBox, forecastTable);
        });

        clearFilterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            clearFilter();
        });
    }

    private void initSortComboBox() {
        ObservableList<String> sortingParams = FXCollections.observableArrayList("Max Temp_C", "Min Temp_C", "Average humidity");
        sortComboBox.setItems(sortingParams);
        sortComboBox.setValue("Max Temp_C");
    }

    private void initFilterComboBox() {
        ObservableList<String> filterParams = FXCollections.observableArrayList("Max Temp_C", "Min Temp_C", "Average humidity");
        filterComboBox.setItems(filterParams);
        filterComboBox.setValue("Max Temp_C");
    }

    private void initSignComboBox(){
        ObservableList<String> signs = FXCollections.observableArrayList(">", "<");
        signButton.setItems(signs);
        signButton.setValue(">");
    }

    private ProgressIndicator loadProgressIndicator() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ProgressIndicator.fxml")));
        ProgressIndicator progressIndicator = (ProgressIndicator) anchorPane.lookup("#progressBar");

        Platform.runLater(() -> containerPane.getChildren().add(progressIndicator));
        return progressIndicator;
    }

    private void validateIntInput(TextField field, Label label){
        try {
            Integer.parseInt(field.getText());
        }
        catch (NumberFormatException e){
            Platform.runLater(() -> {
                errorLabel.setText(String.format("Поле '%s' должно быть целочисленным значением!", label.getText()));
                progressIndicator.setVisible(false);
            });
        }
    }

    private void validateDoubleInput(TextField field, Label label){
        try {
            Double.parseDouble(field.getText());
        }
        catch (NumberFormatException e){
            Platform.runLater(() -> {
                errorLabel.setText(String.format("Поле '%s' должно быть дробным значением!", label.getText()));
            });
        }
    }

    @FXML
    private void getForecast(ActionEvent event) throws IOException {

        Thread thread = new Thread(() -> {
            try {
                progressIndicator = loadProgressIndicator();
                validateIntInput(daysTextField, daysLabel);
                String cityValue = cityTextField.getText();
                int daysValue = Integer.parseInt(daysTextField.getText());
                Forecast forecast = WeatherForecastLoader.getWeatherForecastByURL("f720c35b125e4c18a38191356201110", cityValue, daysValue);
                ArrayList<ForecastDay> forecastDays = forecast.getForecast().getForecastDays();
                List<Day> days = forecastDays.stream()
                        .map(ForecastDay::getDay)
                        .collect(Collectors.toList());
                items = FXCollections.observableList(days);
                forecastTable.setItems(items);
                Platform.runLater(() -> progressIndicator.setVisible(false));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    errorLabel.setText(e.getLocalizedMessage());
                    progressIndicator.setVisible(false);
                });
            }
        });
        thread.start();
    }

    private void sortTableByParam(ComboBox<String> sortComboBox, TableView<Day> table){
        ObservableList<Day> items = table.getItems();
        sortByParam(items, sortComboBox.getValue());
        table.setItems(items);
    }

    private void filterTableByParam(ComboBox<String> filterComboBox, TableView<Day> table){
        validateDoubleInput(filterValue, filterLabel);
        Double value = Double.parseDouble(filterValue.getText());
        table.setItems(filterByParam(items, signButton.getValue(), filterComboBox.getValue(), value));
    }

    private void clearFilter(){
        forecastTable.setItems(items);
    }

    private static void sortByParam(ObservableList<Day> days, String param){
        switch (param){
            case "Max Temp_C":
                days.sort(Day.byMaxtemp_c);
                break;
            case "Min Temp_C":
                days.sort(Day.byMintemp_c);
                break;
            case "Average humidity":
                days.sort(Day.byAvghumidity);
                break;
        }
    }

    private ObservableList<Day> filterByParam(ObservableList<Day> days, String sign, String param, Double value){
            if(param.equals("Max Temp_C") && sign.equals("<")) {
                return FXCollections.observableList(days.stream()
                        .filter(day -> day.getMaxtemp_c() <= value)
                        .collect(Collectors.toList()));
            }
            else if(param.equals("Min Temp_C") && sign.equals("<")) {
                return FXCollections.observableList(days.stream()
                        .filter(day -> day.getMintemp_c() <= value)
                        .collect(Collectors.toList()));
            }
            else if(param.equals("Average humidity") && sign.equals("<")) {
                    return FXCollections.observableList(days.stream()
                            .filter(day -> day.getAvghumidity() <= value)
                            .collect(Collectors.toList()));
            }
            if(param.equals("Max Temp_C") && sign.equals(">")) {
                return FXCollections.observableList(days.stream()
                        .filter(day -> day.getMaxtemp_c() >= value)
                        .collect(Collectors.toList()));
            }
            else if(param.equals("Min Temp_C") && sign.equals(">")) {
                return FXCollections.observableList(days.stream()
                        .filter(day -> day.getMintemp_c() >= value)
                        .collect(Collectors.toList()));
            }
            else if(param.equals("Average humidity") && sign.equals(">")) {
                return FXCollections.observableList(days.stream()
                        .filter(day -> day.getAvghumidity() >= value)
                        .collect(Collectors.toList()));
            }
        return days;
    }

}

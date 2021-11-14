package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ForecastController {
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

    @FXML
    private void getForecast(ActionEvent event) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                Forecast forecast = WeatherForecastLoader.getWeatherForecastByURL("f720c35b125e4c18a38191356201110", "Одесса", Integer.parseInt(daysTextField.getText()));
                ArrayList<ForecastDay> forecastDays = forecast.getForecast().getForecastDays();
                List<Day> days = forecastDays.stream()
                        .map(ForecastDay::getDay)
                        .collect(Collectors.toList());
                items = FXCollections.observableList(days);
                forecastTable.setItems(items);
            } catch (IOException  e) {
                System.out.println(e.getLocalizedMessage());
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

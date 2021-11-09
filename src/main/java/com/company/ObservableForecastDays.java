package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class ObservableForecastDays {
    private ObservableList<Day> forecastDays;

    public void fillTableWithArray(List<Day> days, AnchorPane container){
        forecastDays = FXCollections.observableList(days);
        TableView<Day> table = new TableView<>(forecastDays);

        TableColumn<Day, String> maxTemp_C_Column = new TableColumn<>("MaxTemp_C");
        maxTemp_C_Column.setCellValueFactory(new PropertyValueFactory<>("maxtemp_c"));
        table.getColumns().add(maxTemp_C_Column);

        TableColumn<Day, String> maxTemp_F_Column = new TableColumn<>("MaxTemp_F");
        maxTemp_F_Column.setCellValueFactory(new PropertyValueFactory<>("maxtemp_c"));
        table.getColumns().add(maxTemp_F_Column);

        TableColumn<Day, String> minTemp_C_Column = new TableColumn<>("MinTemp_C");
        minTemp_C_Column.setCellValueFactory(new PropertyValueFactory<>("mintemp_c"));
        table.getColumns().add(minTemp_C_Column);

        TableColumn<Day, String> minTemp_F_Column = new TableColumn<>("MinTemp_F");
        minTemp_F_Column.setCellValueFactory(new PropertyValueFactory<>("mintemp_c"));
        table.getColumns().add(minTemp_F_Column);

        TableColumn<Day, String> avgTemp_C_Column = new TableColumn<>("AvgTemp_C");
        avgTemp_C_Column.setCellValueFactory(new PropertyValueFactory<>("avgtemp_c"));
        table.getColumns().add(avgTemp_C_Column);

        TableColumn<Day, String> avgTemp_F_Column = new TableColumn<>("AvgTemp_F");
        avgTemp_F_Column.setCellValueFactory(new PropertyValueFactory<>("avgtemp_f"));
        table.getColumns().add(avgTemp_F_Column);

        TableColumn<Day, String> MaxWind_kph_Column = new TableColumn<>("MaxWind_kph");
        MaxWind_kph_Column.setCellValueFactory(new PropertyValueFactory<>("maxwind_kph"));
        table.getColumns().add(MaxWind_kph_Column);

        TableColumn<Day, String> AvgHumidityColumn = new TableColumn<>("AvgHumidity");
        AvgHumidityColumn.setCellValueFactory(new PropertyValueFactory<>("avghumidity"));
        table.getColumns().add(AvgHumidityColumn);

        TableColumn<Day, String> TotalPrecip_mm_Column = new TableColumn<>("TotalPrecip_mm");
        TotalPrecip_mm_Column.setCellValueFactory(new PropertyValueFactory<>("totalprecip_mm"));
        table.getColumns().add(TotalPrecip_mm_Column);

        container.getChildren().add(table);
    }
}

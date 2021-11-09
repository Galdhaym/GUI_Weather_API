package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForecastController {

    @FXML
    private AnchorPane TableContainer;

    @FXML
    private void getForecast(ActionEvent event) throws IOException {

        Forecast forecast = Forecast.getWeatherForecastByURL("https://api.weatherapi.com/v1/forecast.json?key=f720c35b125e4c18a38191356201110&q=Одесса&days=3");
        List<Day> forecastDays = forecast.getForecast().getForecastDays().stream()
                .map(ForecastDay::getDay)
                .collect(Collectors.toList());
        ObservableForecastDays observableForecastDays = new ObservableForecastDays();
        observableForecastDays.fillTableWithArray(forecastDays, TableContainer);

//        System.out.println("Прогноз погоды(сортировка):");
//        forecast.getForecast().getForecastDays().sort(ForecastDay.byMaxtemp_c);
//        System.out.println(forecast);
//
//        System.out.println("Прогноз погоды(фильтрация):");
//        List<Day> days = forecast.getForecast().filterByAvgTempGreaterThan(14.2);
//        System.out.println(days);
    }
}

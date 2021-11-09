package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Main.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


        System.out.println("Прогноз погоды(получение из json колекцию):");
        Forecast forecast = Forecast.getWeatherForecastByURL("https://api.weatherapi.com/v1/forecast.json?key=f720c35b125e4c18a38191356201110&q=Одесса&days=3");
        System.out.println(forecast);

        System.out.println("Прогноз погоды(сортировка):");
        forecast.getForecast().getForecastDays().sort(ForecastDay.byMaxtemp_c);
        System.out.println(forecast);

        System.out.println("Прогноз погоды(фильтрация):");
        List<Day> days = forecast.getForecast().filterByAvgTempGreaterThan(14.2);
        System.out.println(days);
    }
}

package com.example.demo1.Controller;

import com.example.demo1.HelloApplication;
import com.example.demo1.Model.BookingSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    @FXML
    private DatePicker date;

    @FXML
    private ChoiceBox<String> goingto;

    @FXML
    private ChoiceBox<String> leavingform;

    @FXML
    private Button search;

    @FXML
    ObservableList<String> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = FXCollections.observableArrayList();
        list.addAll("Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Comilla", "Narayanganj", "Tangail","noakhali");
        leavingform.setItems(list);
        goingto.setItems(list);
    }

    @FXML
    void gotoavaablepage(ActionEvent event) throws IOException {
        String from = leavingform.getValue();
        String to = goingto.getValue();

        if (from == null || to == null || from.equals(to)) {
            System.out.println("⚠️ Invalid route selected.");
            return;
        }

        BookingSession.setRoute(from, to);
        HelloApplication.changeScene("AvailableBusesPage.fxml");
    }

    @FXML
    void ticektcanselhandle(MouseEvent event) throws IOException {
        HelloApplication.changeScene("Ticekt cansel .fxml");
    }

    @FXML
    void canselticekt(MouseEvent event) throws IOException {
        HelloApplication.changeScene("Ticekt cansel .fxml");
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        HelloApplication.changeScene("login.fxml");
    }

    @FXML
    void schdedule(MouseEvent event) throws IOException {
        HelloApplication.changeScene("Schedule.fxml");
    }

    @FXML
    void viewhistory(MouseEvent event) {
        System.out.println("working viewhistory");
    }

    @FXML
    void cheackTicketstasus(MouseEvent event) throws IOException {
        HelloApplication.changeScene("Check Ticket status .fxml");
    }

    @FXML
    void feedback(MouseEvent event) throws IOException {
        HelloApplication.changeScene("feedback_form.fxml");
        //HelloApplication.changeScene("rating_feedback_form..fxml");
        System.out.println("working");
    }
}

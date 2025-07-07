package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.BusInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updatebus implements Initializable {

    @FXML
    private TextField busid;

    @FXML
    private ChoiceBox<String> fromid;

    @FXML
    private TextField seatpirceid;

    @FXML
    private TextField timeid;

    @FXML
    private ChoiceBox<String> toid;

    private ObservableList<String> cityList;

    public static ObservableList<BusInfo> busData = FXCollections.observableArrayList(); // Shared list (if needed elsewhere)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cityList = FXCollections.observableArrayList(
                "Dhaka", "Chittagong", "Khulna", "Rajshahi",
                "Barisal", "Sylhet", "Comilla", "Narayanganj", "Tangail"
        );
        fromid.setItems(cityList);
        toid.setItems(cityList);
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        System.out.println("Back button clicked");
        HelloApplication.changeScene("adminpanel.fxml");
    }

    @FXML
    void updatebus(ActionEvent event) {
       loadBusDetails();
    }

    private void clearForm() {
        busid.clear();
        fromid.setValue(null);
        toid.setValue(null);
        seatpirceid.clear();
        timeid.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    void loadBusDetails() {
        String id = busid.getText().trim();

        if (id.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing ID", "Please enter a Bus ID to load details.");
            return;
        }

        try {
            Connection conn = ConnectionSingleton.getConnection();
            String query = "SELECT from_city, to_city, departure_time, price FROM buses WHERE bus_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, id);

            var rs = pst.executeQuery();

            if (rs.next()) {
                fromid.setValue(rs.getString("from_city"));
                toid.setValue(rs.getString("to_city"));
                timeid.setText(rs.getString("departure_time"));
                seatpirceid.setText(String.valueOf(rs.getDouble("price")));
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No bus found with that ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

}

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

public class Addnewbus implements Initializable {

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

    public static ObservableList<BusInfo> busData = FXCollections.observableArrayList(); // ⬅ shared list

    @FXML
    void back(ActionEvent event) throws IOException {
        System.out.println("Back button clicked");
        HelloApplication.changeScene("adminpanel.fxml");
    }


    @FXML



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cityList = FXCollections.observableArrayList("Dhaka", "Chittagong", "Khulna", "Rajshahi",
                "Barisal", "Sylhet", "Comilla", "Narayanganj", "Tangail");
        fromid.setItems(cityList);
        toid.setItems(cityList);
    }
    @FXML
    void save_new_bus() {
        String id = busid.getText().trim();
        String from = fromid.getValue();
        String to = toid.getValue();
        String price = seatpirceid.getText().trim();
        String time = timeid.getText().trim();

        if (id.isEmpty() || from == null || to == null || price.isEmpty() || time.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please fill out all fields.");
            return;
        }

        try {
            double parsedPrice = Double.parseDouble(price);  // Ensure price is valid

            Connection conn = ConnectionSingleton.getConnection();
            conn.setAutoCommit(false);  // ✅ Begin transaction

            // 1️⃣ Insert into buses
            String insertBusSQL = "INSERT INTO buses (bus_id, from_city, to_city, departure_time, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(insertBusSQL);
            pst.setString(1, id);
            pst.setString(2, from);
            pst.setString(3, to);
            pst.setString(4, time);
            pst.setDouble(5, parsedPrice);
            pst.executeUpdate();

            // 2️⃣ Insert 40 seats
            String insertSeatSQL = "INSERT INTO seats (seat_id, bus_id) VALUES (?, ?)";
            PreparedStatement seatStmt = conn.prepareStatement(insertSeatSQL);
            for (int i = 1; i <= 40; i++) {
                seatStmt.setInt(1, i);
                seatStmt.setString(2, id);
                seatStmt.addBatch();
            }
            seatStmt.executeBatch();

            conn.commit();  //
            showAlert(Alert.AlertType.INFORMATION, "Success", "Bus and seats added successfully!");

            // Clear form
            busid.clear();
            seatpirceid.clear();
            timeid.clear();
            fromid.setValue(null);
            toid.setValue(null);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter a valid numeric price.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save bus data. Reason: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void updatebus(ActionEvent actionEvent) {
        save_new_bus();
    }
}

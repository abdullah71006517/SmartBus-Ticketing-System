package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deletepage {

    @FXML
    private TextField busid;

    @FXML
    void back(ActionEvent event) throws IOException {
        HelloApplication.changeScene("adminpanel.fxml");
    }

    @FXML
    void deltectbus(MouseEvent event) {
        String id = busid.getText().trim();

        if (id.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Input", "Please enter Bus ID to delete.");
            return;
        }

        try {
            Connection conn = ConnectionSingleton.getConnection();
            String deleteSQL = "DELETE FROM buses WHERE bus_id = ?";
            PreparedStatement pst = conn.prepareStatement(deleteSQL);
            pst.setString(1, id);

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Bus and associated seats deleted successfully!");
                busid.clear();
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No bus found with the provided Bus ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not delete bus. Reason: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicektCansel {

    @FXML
    private TextField ticketid;

    @FXML
    void back(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Dashboard.fxml");
    }

    @FXML
    void sumitapplication(ActionEvent event) {
        String ticketIdStr = ticketid.getText().trim();

        if (ticketIdStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Input", "Please enter a valid Ticket ID.");
            return;
        }

        try {
            int ticketId = Integer.parseInt(ticketIdStr);
            Connection conn = ConnectionSingleton.getConnection();

            // Step 1: Check if ticket exists
            String selectSQL = "SELECT bus_id, seat_id FROM bookings WHERE ticket_id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setInt(1, ticketId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String busId = rs.getString("bus_id");
                int seatId = rs.getInt("seat_id");

                // Step 2: Delete booking
                String deleteSQL = "DELETE FROM bookings WHERE ticket_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
                deleteStmt.setInt(1, ticketId);
                deleteStmt.executeUpdate();

                // Step 3: Update seat status
                String updateSeatSQL = "UPDATE seats SET status = 'available', booked_by = NULL WHERE seat_id = ? AND bus_id = ?";
                PreparedStatement seatStmt = conn.prepareStatement(updateSeatSQL);
                seatStmt.setInt(1, seatId);
                seatStmt.setString(2, busId);
                seatStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket cancelled and seat is now available.");
                ticketid.clear();

            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No booking found with the given Ticket ID.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ticket ID must be a number.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
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

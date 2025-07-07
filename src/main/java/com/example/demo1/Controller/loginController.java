package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginController {

    @FXML
    private PasswordField logpassword;

    @FXML
    private TextField logusername;

    @FXML
    void forgatepassword(MouseEvent event) {
        String username = logusername.getText().trim();
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter your username to retrieve password.");
            return;
        }

        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                // For demo only - NEVER show passwords in production apps
                showAlert(Alert.AlertType.INFORMATION, "Your password is: " + password);
            } else {
                showAlert(Alert.AlertType.ERROR, "Username not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void gotodeashbord(ActionEvent event) throws IOException {
        HelloApplication.user = "guest";
        HelloApplication.changeScene("Dashboard.fxml");
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        String username = logusername.getText().trim();
        String password = logpassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter both username and password.");
            return;
        }

        // ✅ Hardcoded Admin Access
        if (username.equals("**") && password.equals("**")) {
            HelloApplication.user = "admin";
            HelloApplication.changeScene("adminpanel.fxml");
            return;
        }

        String query = "SELECT password, user_type FROM users WHERE username = ?";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String userType = rs.getString("user_type");

                if (password.equals(storedPassword)) {
                    HelloApplication.user = username;

                    if (userType.equalsIgnoreCase("ADMIN")) {
                        HelloApplication.changeScene("adminpanel.fxml");
                    } else {
                        HelloApplication.changeScene("Dashboard.fxml");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Incorrect password.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Username does not exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void gotoresister(MouseEvent event) throws IOException {
        HelloApplication.changeScene("Registration.fxml");
    }
}

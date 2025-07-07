package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;

public class RegistrationController {
    @FXML
    private CheckBox agreechekbox;

    @FXML
    private PasswordField regconframpassword;

    @FXML
    private TextField regemail;

    @FXML
    private TextField regname;

    @FXML
    private PasswordField regpassword;

    @FXML
    private TextField regphone;

    @FXML
    void gotologin(MouseEvent event) throws IOException {
        HelloApplication.changeScene("login.fxml");
    }

    @FXML
    void submit(ActionEvent event) {
        String name = regname.getText();
        String email = regemail.getText();
        String phone = regphone.getText();
        String username = name;
        String password = regpassword.getText();
        String confirmPassword = regconframpassword.getText();

        if (!agreechekbox.isSelected()) {
            showAlert("Agreement Required", "You must accept the terms to register.", Alert.AlertType.WARNING);
            return;
        }

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Incomplete Form", "Please fill in all fields.", Alert.AlertType.WARNING);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "The password and confirm password must match.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Connection conn = ConnectionSingleton.getConnection();

            var checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            var result = checkStmt.executeQuery();
            if (result.next()) {
                showAlert("Duplicate Email", "This email is already registered. Try logging in.", Alert.AlertType.INFORMATION);
                return;
            }

            String sql = "INSERT INTO users (username, full_name, phone, email, password, user_type) VALUES (?, ?, ?, ?, ?, 'USER')";
            var stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, password);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Your account has been created. Please log in.", Alert.AlertType.INFORMATION);
                HelloApplication.changeScene("login.fxml");
            } else {
                showAlert("Error", "Registration failed. Please try again.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Exception", "An unexpected error occurred.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

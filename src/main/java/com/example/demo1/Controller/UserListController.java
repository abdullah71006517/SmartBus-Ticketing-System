package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserListController implements Initializable {

    @FXML
    private TableView<UserData> userTable;

    @FXML
    private TableColumn<UserData, String> usernameCol;

    @FXML
    private TableColumn<UserData, String> fullNameCol;

    @FXML
    private TableColumn<UserData, String> phoneCol;

    @FXML
    private TableColumn<UserData, String> emailCol;

    @FXML
    private TableColumn<UserData, String> userTypeCol;

    private ObservableList<UserData> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));

        loadUsers();
    }

    private void loadUsers() {
        try {
            Connection conn = ConnectionSingleton.getConnection();
            String query = "SELECT username, full_name, phone, email, user_type FROM users";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            userList.clear();
            while (rs.next()) {
                userList.add(new UserData(
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("user_type")
                ));
            }
            userTable.setItems(userList);

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

    @FXML
    void back(ActionEvent event) throws IOException {
        HelloApplication.changeScene("adminpanel.fxml");
    }

}

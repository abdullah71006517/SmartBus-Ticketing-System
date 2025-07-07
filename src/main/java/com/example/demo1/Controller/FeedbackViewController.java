package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.Feedback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FeedbackViewController {

    @FXML
    private TableView<Feedback> feedbackTable;

    @FXML
    private TableColumn<Feedback, String> usernameCol;

    @FXML
    private TableColumn<Feedback, String> cleanlinessCol;

    @FXML
    private TableColumn<Feedback, String> behaviorCol;

    @FXML
    private TableColumn<Feedback, String> serviceCol;

    @FXML
    private TableColumn<Feedback, String> messageCol;

    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        cleanlinessCol.setCellValueFactory(new PropertyValueFactory<>("cleanlinessRating"));
        behaviorCol.setCellValueFactory(new PropertyValueFactory<>("behaviorRating"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceRating"));
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        loadFeedbackFromDatabase();
    }

    private void loadFeedbackFromDatabase() {
        ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();

        String sql = "SELECT username, cleanliness_rating, behavior_rating, service_rating, message FROM feedbacks";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String cleanliness = rs.getString("cleanliness_rating");
                String behavior = rs.getString("behavior_rating");
                String service = rs.getString("service_rating");
                String message = rs.getString("message");

                Feedback feedback = new Feedback(username, cleanliness, behavior, service, message);
                feedbackList.add(feedback);
            }

            feedbackTable.setItems(feedbackList);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @FXML
    void back(ActionEvent event) throws IOException {
        HelloApplication.changeScene("adminpanel.fxml");
    }
}

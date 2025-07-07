package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.Feedback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class FeedbackController implements Initializable {

    @FXML private RadioButton cleanBad, cleanAverage, cleanGood;
    @FXML private RadioButton behaviorBad, behaviorAverage, behaviorGood;
    @FXML private RadioButton ratingBad, ratingAverage, ratingGood;

    @FXML private TextArea feedbackTextArea;
    @FXML private Button submitButton;

    private ToggleGroup cleanGroup = new ToggleGroup();
    private ToggleGroup behaviorGroup = new ToggleGroup();
    private ToggleGroup ratingGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Assign RadioButtons to groups
        cleanBad.setToggleGroup(cleanGroup);
        cleanAverage.setToggleGroup(cleanGroup);
        cleanGood.setToggleGroup(cleanGroup);

        behaviorBad.setToggleGroup(behaviorGroup);
        behaviorAverage.setToggleGroup(behaviorGroup);
        behaviorGood.setToggleGroup(behaviorGroup);

        ratingBad.setToggleGroup(ratingGroup);
        ratingAverage.setToggleGroup(ratingGroup);
        ratingGood.setToggleGroup(ratingGroup);
    }

    @FXML
    void cancelFeedback(ActionEvent event) throws IOException {
        cleanGroup.selectToggle(null);
        behaviorGroup.selectToggle(null);
        ratingGroup.selectToggle(null);
        feedbackTextArea.clear();
        HelloApplication.changeScene("Dashboard.fxml");
    }

    @FXML
    void submitFeedback(ActionEvent event) {
        String username = HelloApplication.user;// Replace with actual user

        String cleanliness = getSelectedValue(cleanGroup);
        String behavior = getSelectedValue(behaviorGroup);
        String rating = getSelectedValue(ratingGroup);
        String message = feedbackTextArea.getText().trim();

        if (cleanliness == null || behavior == null || rating == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete", "Please rate all 3 sections.");
            return;
        }

        try (Connection conn = ConnectionSingleton.getConnection()) {
            String sql = "INSERT INTO feedbacks (username, cleanliness_rating, behavior_rating, service_rating, message) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, cleanliness);
            stmt.setString(3, behavior);
            stmt.setString(4, rating);
            stmt.setString(5, message);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Thank you! Your feedback has been submitted.");
            cancelFeedback(null);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit feedback.");
        }
    }

    private String getSelectedValue(ToggleGroup group) {
        Toggle selected = group.getSelectedToggle();
        return selected != null ? ((RadioButton) selected).getText() : null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

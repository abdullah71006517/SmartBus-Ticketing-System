package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class IncomeReport implements Initializable {

    @FXML private BarChart<String, Number> barchrt;
    @FXML private Label totalincome;
    @FXML private Label totalticektsell;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadIncomeByPaymentMethod2();
        loadSummaryStats();
    }

    private void loadIncomeByPaymentMethod() {
        String sql = "SELECT method, SUM(amount) AS total_income FROM payments WHERE status='SUCCESS' GROUP BY method";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Income by Payment Type");

            while (rs.next()) {
                String method = rs.getString("method");
                double income = rs.getDouble("total_income");

                XYChart.Data<String, Number> data = new XYChart.Data<>(method, income);

                // ✅ Apply custom color style per method
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        switch (method.toLowerCase()) {
                            case "bkash" -> newNode.getStyleClass().add("bkash-bar");
                            case "rocket" -> newNode.getStyleClass().add("rocket-bar");
                            case "nagad" -> newNode.getStyleClass().add("nagad-bar");
                            default -> newNode.getStyleClass().add("default-bar");
                        }
                    }
                });

                series.getData().add(data);
            }

            barchrt.getData().clear();
            barchrt.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadIncomeByPaymentMethod2() {
        String sql = "SELECT method, SUM(amount) AS total_income FROM payments WHERE status='SUCCESS' GROUP BY method";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Income by Payment Type");

            while (rs.next()) {
                String method = rs.getString("method");
                double income = rs.getDouble("total_income");

                XYChart.Data<String, Number> data = new XYChart.Data<>(method, income);
                series.getData().add(data);

                // 🎨 Apply custom color directly from Java
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        String color;
                        switch (method.toLowerCase()) {
                            case "bkash" -> color = "#e6006e"; // pink
                            case "rocket" -> color = "#7f00ff"; // purple
                            case "nagad" -> color = "#ff6f00"; // orange
                            default -> color = "#4CAF50"; // default green
                        }
                        newNode.setStyle("-fx-bar-fill: " + color + ";");
                    }
                });
            }

            barchrt.getData().clear();
            barchrt.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSummaryStats() {
        String totalIncomeQuery = "SELECT SUM(amount) AS total FROM payments WHERE status='SUCCESS'";
        String totalTicketsQuery = "SELECT COUNT(*) AS count FROM bookings";

        try (Connection conn = ConnectionSingleton.getConnection()) {
            // 🔹 Total Income
            try (PreparedStatement pst1 = conn.prepareStatement(totalIncomeQuery);
                 ResultSet rs1 = pst1.executeQuery()) {
                if (rs1.next()) {
                    double total = rs1.getDouble("total");
                    totalincome.setText("Total income :"+"৳ " + total);
                } else {
                    totalincome.setText("৳ 0.00");
                }
            }

            // 🔹 Total Tickets
            try (PreparedStatement pst2 = conn.prepareStatement(totalTicketsQuery);
                 ResultSet rs2 = pst2.executeQuery()) {
                if (rs2.next()) {
                    int count = rs2.getInt("count");
                    totalticektsell.setText("Total sales: "+count + " tickets");
                } else {
                    totalticektsell.setText("0 tickets");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("adminpanel.fxml");
    }
}

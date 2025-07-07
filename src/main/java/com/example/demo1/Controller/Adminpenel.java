package com.example.demo1.Controller;


import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Adminpenel implements Initializable {

    @FXML
    private PieChart piechart;

    @FXML
    void addbus(MouseEvent event) throws IOException {
HelloApplication.changeScene("Add BUS.fxml");
    }

    @FXML
    void addoprator(MouseEvent event) {

    }

    @FXML
    void deletebus(MouseEvent event) throws IOException {
HelloApplication.changeScene("deletepage.fxml");
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        HelloApplication.changeScene("login.fxml");
    }

    @FXML
    void totalincome(MouseEvent event) throws IOException {
HelloApplication.changeScene("IncomeReport.fxml");

    }

    @FXML
    void updatebus(MouseEvent event) throws IOException {
        HelloApplication.changeScene("updatebus.fxml");

    }

    @FXML
    void viewfeedback(MouseEvent event) throws IOException {
        HelloApplication.changeScene("FeedbackViewController.fxml");

    }
    private void loadPieChartData() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        String sql = """
        SELECT b.from_city, COUNT(*) AS total
        FROM bookings bk
        JOIN buses b ON bk.bus_id = b.bus_id
        GROUP BY b.from_city
        ORDER BY total DESC
        LIMIT 4
        """;

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String city = rs.getString("from_city");
                int total = rs.getInt("total");
                pieData.add(new PieChart.Data(city, total));
            }

            piechart.setData(pieData);
            piechart.setTitle("Top 4 Departure Cities");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void userinformation(MouseEvent event) throws IOException {
HelloApplication.changeScene("userlist.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPieChartData();
    }
}

package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.BusInfo;
import javafx.beans.property.SimpleStringProperty;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Schedule implements Initializable {

    @FXML
    private TableColumn<BusInfo, String> Fromid;

    @FXML
    private TableColumn<BusInfo, String> busid;

    @FXML
    private TableView<BusInfo> bustable;

    @FXML
    private TableColumn<BusInfo, String> pirceid;

    @FXML
    private Button search;

    @FXML
    private TextField serchfeildtext;

    @FXML
    private TableColumn<BusInfo, String> timeid;

    @FXML
    private TableColumn<BusInfo, String> toid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        busid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBusId()));
        Fromid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFrom()));
        toid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTo()));
        pirceid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrice()));
        timeid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTime()));
        busid.setCellFactory(column -> createColoredCell("#f8d7da"));    // Light Red
        Fromid.setCellFactory(column -> createColoredCell("#d4edda"));  // Light Green
        toid.setCellFactory(column -> createColoredCell("#d1ecf1"));    // Light Blue
        pirceid.setCellFactory(column -> createColoredCell("#fff3cd")); // Light Yellow
        timeid.setCellFactory(column -> createColoredCell("#e2e3e5"));  // Light Gray

        loadBusesFromDatabase();
    }
    private void loadBusesFromDatabase() {
        ObservableList<BusInfo> buses = FXCollections.observableArrayList();

        String sql = "SELECT * FROM buses";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String busId = rs.getString("bus_id");
                String from = rs.getString("from_city");
                String to = rs.getString("to_city");
                String price = rs.getString("price");
                String time = rs.getString("departure_time");

                buses.add(new BusInfo(busId, from, to, price, time));

            }

            bustable.setItems(buses);

        } catch (SQLException e) {
            e.printStackTrace();
           // showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load buses from the database.");
        }
    }
    @FXML
    void back(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Dashboard.fxml");
    }
    private TableCell<BusInfo, String> createColoredCell(String bgColor) {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset
                } else {
                    setText(item);
                    setStyle("-fx-background-color: " + bgColor + "; -fx-alignment: CENTER;");
                }
            }
        };
    }

}

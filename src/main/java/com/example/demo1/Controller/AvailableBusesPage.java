package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.BookingSession;
import com.example.demo1.Model.BusInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AvailableBusesPage {

    @FXML
    private TableView<BusInfo> bustable;

    @FXML
    private TableColumn<BusInfo, String> busid;

    @FXML
    private TableColumn<BusInfo, String> Fromid;

    @FXML
    private TableColumn<BusInfo, String> toid;

    @FXML
    private TableColumn<BusInfo, String> pirceid;

    @FXML
    private TableColumn<BusInfo, String> timeid;

    @FXML
    private TableColumn<BusInfo, Void> actionid;
    @FXML
    ObservableList<BusInfo>busInfoObservableList=FXCollections.observableArrayList();

    @FXML
    void back(ActionEvent event) {
        System.out.println("Back button clicked");
    }

    @FXML
    public void initialize() {

        busid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBusId()));
        Fromid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFrom()));
        toid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTo()));
        pirceid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrice()));
        timeid.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTime()));

        busid.setCellFactory(column -> createColoredCell("#f8d7da"));    // Light Red
        Fromid.setCellFactory(column -> createColoredCell("#d4edda"));  // Light Green
        toid.setCellFactory(column -> createColoredCell("#d1ecf1"));    // Light Blue
        pirceid.setCellFactory(column -> createColoredCell("#fff3cd")); // Light Yellow
        timeid.setCellFactory(column -> createColoredCell("#e2e3e5"));

        loadBusesFromDatabase();

        addBookButtonToTable();
    }
    
    private void loadBusesFromDatabase() {
        ObservableList<BusInfo> buses = FXCollections.observableArrayList();

        String from = BookingSession.getRouteFrom();
        String to = BookingSession.getRouteTo();

        String sql = "SELECT * FROM buses WHERE from_city = ? AND to_city = ?";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, from);
            pst.setString(2, to);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String busId = rs.getString("bus_id");
                String price = rs.getString("price");
                String time = rs.getString("departure_time");

                buses.add(new BusInfo(busId, from, to, price, time));
            }

            bustable.setItems(buses);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load buses from the database.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String databaseError, String s) {
    }

    private void addBookButtonToTable() {
        actionid.setCellFactory(col -> new TableCell<>() {
            private final Button bookButton = new Button("Book");
           //  bookButton.s("-fx-background-color: green; -fx-text-fill: white;");


            {
                bookButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                bookButton.setOnAction(event -> {
                  //  bookButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    BusInfo selectedBus = getTableView().getItems().get(getIndex());
                    showSeatLayout(selectedBus);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : bookButton);
            }
        });
    }

    // 40 seat layout logic
    private void showSeatLayout(BusInfo bus) {
        Stage seatStage = new Stage();
        seatStage.setTitle("Seat Layout for Bus: " + bus.getBusId());

        GridPane seatGrid = new GridPane();
        seatGrid.setPadding(new Insets(20));
        seatGrid.setHgap(50);
        seatGrid.setVgap(50);

        int totalSeats = 40;
        int cols = 4;


        Set<Integer> bookedSeats = new HashSet<>();
        try (Connection conn = ConnectionSingleton.getConnection()) {
            String query = "SELECT seat_id FROM bookings WHERE bus_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, bus.getBusId());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getInt("seat_id")); // Assuming seat_id is int like 1, 2, 3, ...
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < totalSeats; i++) {
            int seatId = i + 1;
            String seatNo = "S" + seatId;
            Button seatBtn = new Button(seatNo);
            seatBtn.setPrefWidth(60);

            if (bookedSeats.contains(seatId)) {

                seatBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
                seatBtn.setDisable(true);
            } else {
                // 🟩 Available
                seatBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                seatBtn.setOnAction(e -> {
                    // 🟨 Selected seat
                    seatBtn.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black;");
                    BookingSession.setBookingInfo(bus, seatNo, Double.parseDouble(bus.getPrice()));

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Required");
                    alert.setHeaderText("Confirm Your Seat");
                    alert.setContentText("Do you want to continue to payment for seat " + seatNo + "?");

                    ButtonType confirmBtn = new ButtonType("Payment Confirm");
                    ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(confirmBtn, cancelBtn);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == confirmBtn) {
                            try {
                                HelloApplication.changeScene("Paymentpenel.fxml");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            seatBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                            System.out.println("Payment cancelled by user.");
                        }
                    });
                });
            }

            seatGrid.add(seatBtn, i % cols, i / cols);
        }

        Scene scene = new Scene(seatGrid, 350, 500);
        seatStage.setScene(scene);
        seatStage.show();
    }
    private TableCell<BusInfo, String> createColoredCell(String bgColor) {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: " + bgColor + "; -fx-alignment: CENTER;");
                }
            }
        };
    }


}

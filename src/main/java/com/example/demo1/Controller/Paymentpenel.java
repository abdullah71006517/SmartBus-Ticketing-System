package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import com.example.demo1.Model.BookingSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Paymentpenel implements Initializable {

    @FXML
    private RadioButton Bkash;

    @FXML
    private RadioButton Nagad;

    @FXML
    private RadioButton card;

    @FXML
    private ToggleGroup paymenttype;

    @FXML
    private Label pessngername;

    @FXML
    private Label totalamout;

    @FXML
    private TextField discountcode;

    @FXML
    private Label applydicount;

    @FXML
    private Label digitalbookingtax;
    @FXML
    private Label name;
    @FXML
    private Label servicecharge;

    @FXML
    private Label toalamout1;
    @FXML
    private Button PAY;

    @FXML
    private Label Ecotax;

    private double originalFare = 900.00;
    private double currentFare = 900.00;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (BookingSession.getSelectedBus() != null) {
            originalFare = BookingSession.getPrice();
            currentFare = originalFare;
            name.setText(HelloApplication.user);
            calculateCharges(originalFare);
            //totalamout.setText("Tk             : " + originalFare);
        } else {
            pessngername.setText("N/A");
            totalamout.setText("Tk 0");
        }
    }

    @FXML
    void discountappy(ActionEvent event) {
        String code = discountcode.getText().trim();

        if (Bkash.isSelected()) {
            if (code.equalsIgnoreCase("BKASH10")) {
                double discount = originalFare * 0.10;
                currentFare = originalFare - discount;
                totalamout.setText("Tk " + currentFare);
                applydicount.setText("✔ 10% discount applied: Tk " + discount);
                applydicount.setStyle("-fx-text-fill: green;");
            } else {
                applydicount.setText(" Invalid discount code");
                applydicount.setStyle("-fx-text-fill: red;");
            }
        } else {
            applydicount.setText("Discount only available for Bkash payment");
            applydicount.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML


    public void gototicket(ActionEvent actionEvent) throws IOException {
        calculateCharges(originalFare);
        RadioButton selectedMethod = (RadioButton) paymenttype.getSelectedToggle();

        if (selectedMethod == null) {
            showAlert(Alert.AlertType.WARNING, "No payment method selected!");
            return;
        }

        String method = selectedMethod.getText();

        if (BookingSession.getSelectedBus() == null || BookingSession.getSeatNo() == null) {
            showAlert(Alert.AlertType.ERROR, "Booking info is incomplete.");
            return;
        }

        try (Connection conn = ConnectionSingleton.getConnection()) {
            // Insert into bookings table
            String bookingSql = "INSERT INTO bookings (username, bus_id, seat_id, amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(bookingSql)) {
                pst.setString(1,HelloApplication.user );
                pst.setString(2, BookingSession.getSelectedBus().getBusId());
                pst.setInt(3, Integer.parseInt(BookingSession.getSeatNo().substring(1))); // assuming seat format S1
                pst.setDouble(4, currentFare);
                pst.executeUpdate();
            }

            // Insert into payments table
            String paymentSql = "INSERT INTO payments (username, method, amount, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst2 = conn.prepareStatement(paymentSql)) {
                pst2.setString(1,HelloApplication.user);
                pst2.setString(2, method);
                pst2.setDouble(3, currentFare);
                pst2.setString(4, "SUCCESS");
                pst2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Payment failed. Database error.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Payment Successful!\nPaid Tk " + currentFare + " via " + method);

        // Clear session after booking
        BookingSession.clear();

        HelloApplication.changeScene("Check Ticket status .fxml");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Payment");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void calculateCharges(double baseAmount) {
        double ecoTax = baseAmount * 0.012;
        double serviceChargeAmount = baseAmount * 0.02;
        double digitalBookingTaxAmount = baseAmount * 0.03;
        double totalAmount = baseAmount + ecoTax + serviceChargeAmount + digitalBookingTaxAmount;

       // name.setText(String.format("%.2f", baseAmount));
        servicecharge.setText(String.format("%.2f", serviceChargeAmount));
        digitalbookingtax.setText(String.format("%.2f", digitalBookingTaxAmount));
        toalamout1.setText(String.format("%.2f", totalAmount));
        Ecotax.setText(String.format("%.2f",ecoTax));
        PAY.setText("Pay "+String.format("%.2f", totalAmount));
        currentFare=totalAmount;
    }

}

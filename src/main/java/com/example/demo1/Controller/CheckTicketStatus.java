package com.example.demo1.Controller;

import com.example.demo1.DBConnection.ConnectionSingleton;
import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.scene.control.TextField;

public class CheckTicketStatus implements Initializable {

    @FXML private Label ticketno;
    @FXML private Label passngername;
    @FXML private Label busid;
    @FXML private Label seat;
    @FXML private Label time;
    @FXML private Label date;
    @FXML private Label price;
    @FXML private TextField searchField;
    @FXML private Button searchButton, pdfButton, backButton;

    private int currentTicketId;
    private double currentFare;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRecentData();
    }

    private void loadRecentData() {
        String sql = """
            SELECT b.ticket_id, u.full_name, b.bus_id, b.seat_id, b.booking_time, bs.departure_time, b.amount AS price
            FROM bookings b
            JOIN users u ON b.username = u.username
            JOIN buses bs ON b.bus_id = bs.bus_id
            ORDER BY b.ticket_id DESC LIMIT 1
        """;

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                fillTicketInfo(rs);
            } else {
                showAlert(Alert.AlertType.WARNING, "No recent ticket found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load ticket: " + e.getMessage());
        }
    }

    private void fillTicketInfo(ResultSet rs) throws SQLException {
        currentTicketId = rs.getInt("ticket_id");
        currentFare = rs.getDouble("price");

        ticketno.setText("TK" + currentTicketId);
        passngername.setText(rs.getString("full_name"));
        busid.setText(rs.getString("bus_id"));
        seat.setText("S-"+String.valueOf(rs.getInt("seat_id")));
        Timestamp bookingTime = rs.getTimestamp("booking_time");
        date.setText(bookingTime.toLocalDateTime().toLocalDate().toString());
        time.setText(rs.getString("departure_time"));
        price.setText("Tk " + String.format("%.2f", currentFare));
    }

    @FXML
    void serachtikectno(ActionEvent event) {
        String input = searchField.getText().trim();

        if (input.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a ticket number.");
            return;
        }

        String ticketIdStr = input.replaceAll("[^0-9]", "");
        if (ticketIdStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid ticket number format.");
            return;
        }

        int ticketId = Integer.parseInt(ticketIdStr);

        String sql = """
            SELECT b.ticket_id, u.full_name, b.bus_id, b.seat_id, b.booking_time, bs.departure_time, b.amount AS price
            FROM bookings b
            JOIN users u ON b.username = u.username
            JOIN buses bs ON b.bus_id = bs.bus_id
            WHERE b.ticket_id = ?
        """;

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fillTicketInfo(rs);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No ticket found for number: TK" + ticketId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void dowloandaspdf(ActionEvent event) {
        if (ticketno.getText().equals("") || price.getText().equals("")) {
            showAlert(Alert.AlertType.WARNING, "No ticket data available.");
            return;
        }

        try {
            Document document = new Document();
            String fileName = "SmartBus_Ticket_" + ticketno.getText() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph title = new Paragraph("🎫 SmartBus - Digital Ticket", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[]{2, 4});
            table.setSpacingBefore(10f);
            table.setWidthPercentage(100);

            addRow(table, "Ticket No", ticketno.getText(), labelFont, valueFont);
            addRow(table, "Passenger Name", passngername.getText(), labelFont, valueFont);
            addRow(table, "Bus ID", busid.getText(), labelFont, valueFont);
            addRow(table, "Seat No", seat.getText(), labelFont, valueFont);
            addRow(table, "Date", date.getText(), labelFont, valueFont);
            addRow(table, "Time", time.getText(), labelFont, valueFont);
            addRow(table, "Fare", price.getText(), labelFont, valueFont);

            document.add(table);

            Paragraph thanks = new Paragraph("\nThank you for choosing SmartBus.\nWishing you a safe and pleasant journey!", valueFont);
            thanks.setAlignment(Element.ALIGN_CENTER);
            thanks.setSpacingBefore(30f);
            document.add(thanks);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "PDF downloaded successfully as:\n" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "PDF generation failed:\n" + e.getMessage());
        }
    }

    private void addRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        HelloApplication.changeScene("Dashboard.fxml");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("SmartBus");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

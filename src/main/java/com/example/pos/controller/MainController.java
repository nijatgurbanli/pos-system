package com.example.pos.controller;

import com.example.pos.repository.DatabaseConnection;
import com.example.pos.session.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button salesBtn;
    @FXML
    private Button stockBtn;
    @FXML
    private Button userBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Label roleLabel;
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        String role = Session.getRole();
        String username = Session.getUsername();

        // Rol və istifadəçi adı UI-da göstərilir
        roleLabel.setText("Rol: " + role);
        welcomeLabel.setText("Xoş gəlmisiniz, " + username + "!");

        // Rol əsasında düymələrin görünməsini idarə etmək
        applyRolePermissions(role);
    }

    private void applyRolePermissions(String role) {
        switch (role) {
            case "ADMIN":
                break; // hər şey açıq
            case "CASHIER":
                stockBtn.setVisible(false);
                userBtn.setVisible(false);
                break;
            case "MANAGER":
                userBtn.setVisible(false);
                break;
        }
    }

    @FXML
    public void showStockPane() {
        loadPane("/fxml/stock.fxml");
    }

    @FXML
    public void showSalesPane() {
        loadPane("/fxml/sales.fxml");
    }

    @FXML
    public void showUserPane() {
        loadPane("/fxml/user.fxml");
    }

    private void loadPane(String fxmlPath) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔴 Logout funksiyası
    @FXML
    public void handleLogout() {
        // Audit log əlavə et
        auditLogout(Session.getUsername());

        // Sessiyanı təmizlə
        Session.clear();

        // Login ekranına geri qayıt
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Login - Market POS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Audit logging: istifadəçi logout olduqda DB-yə yazılır
    private void auditLogout(String username) {
        String sql = "INSERT INTO audit_log(username, action, timestamp) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, "LOGOUT");
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

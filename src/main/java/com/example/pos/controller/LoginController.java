package com.example.pos.controller;

import com.example.pos.repository.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent event) {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Məlumatları tam doldurun!");
            return;
        }

        if (checkCredentials(username, password)) {
            openMainScreen();
        } else {
            errorLabel.setText("İstifadəçi adı və ya şifrə yanlışdır!");
        }
    }

    private boolean checkCredentials(String username, String password) {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();   // qeyd tapıldısa TRUE

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Market POS Sistemi - Əsas ekran");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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

        // Sadə yoxlama (demo məqsədli)
        if ("admin".equals(username) && "1234".equals(password)) {
            openMainScreen();
        } else {
            errorLabel.setText("İstifadəçi adı və ya şifrə yanlışdır!");
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


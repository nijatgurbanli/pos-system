package com.example.pos.controller;

import com.example.pos.session.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

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
    private Label roleLabel;

    @FXML
    public void initialize() {
        String role = Session.getRole();

        // Rolun UI-da göstərilməsi
        roleLabel.setText("Rol: " + role);

        // Rol əsasında düymələrin görünməsini idarə etmək
        applyRolePermissions(role);
    }

    private void applyRolePermissions(String role) {

        switch (role) {

            case "Admin":
                // hər şey açıq qalır
                break;

            case "Kassir":
                stockBtn.setVisible(false);
                userBtn.setVisible(false);
                break;

            case "Menecer":
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
}

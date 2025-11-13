package com.example.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private TableView<ProductItem> cartTable;

    @FXML
    private TableColumn<ProductItem, String> nameColumn;

    @FXML
    private TableColumn<ProductItem, Double> priceColumn;

    @FXML
    private TableColumn<ProductItem, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductItem, Double> totalColumn;

    @FXML
    private TextField barcodeField;

    @FXML
    private Label totalLabel;

    private final ObservableList<ProductItem> cartItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        totalColumn.setCellValueFactory(data -> data.getValue().totalProperty().asObject());

        cartTable.setItems(cartItems);
    }

    @FXML
    private void handleAddProduct() {
        String barcode = barcodeField.getText();

        if (barcode.isEmpty()) return;

        // Demo məqsədli — əgər "111" yazılsa, "Cola" əlavə etsin
        if (barcode.equals("111")) {
            addToCart("Coca-Cola 0.5L", 1.20);
        } else if (barcode.equals("222")) {
            addToCart("Lay's Chips", 2.50);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Məhsul tapılmadı!", ButtonType.OK);
            alert.showAndWait();
        }

        barcodeField.clear();
    }

    private void addToCart(String name, double price) {
        // Əgər məhsul artıq səbətdədirsə, miqdarı artır
        for (ProductItem item : cartItems) {
            if (item.getName().equals(name)) {
                item.setQuantity(item.getQuantity() + 1);
                updateTotal();
                cartTable.refresh();
                return;
            }
        }

        cartItems.add(new ProductItem(name, price, 1));
        updateTotal();
    }

    private void updateTotal() {
        double total = cartItems.stream().mapToDouble(ProductItem::getTotal).sum();
        totalLabel.setText(String.format("%.2f ₼", total));
    }

    @FXML
    private void handleClear() {
        cartItems.clear();
        updateTotal();
    }

    @FXML
    private void handleConfirmSale() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Satış uğurla tamamlandı!", ButtonType.OK);
        alert.showAndWait();
        handleClear();
    }

    @FXML
    private void handleLogout() {
        // Giriş ekranına qayıtmaq
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            javafx.stage.Stage stage = (javafx.stage.Stage) barcodeField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Market POS Sistemi - Giriş");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

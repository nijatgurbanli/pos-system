package com.example.pos.controller;

import com.example.pos.model.Product;
import com.example.pos.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StockController {

    @FXML
    private TextField idField;
    @FXML
    private TextField barcodeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField searchField;   // 🔍 AXTARIŞ ÜÇÜN
    @FXML
    private TableView<Product> stockTable;
    @FXML
    private TableColumn<Product, Integer> colId;
    @FXML
    private TableColumn<Product, String> colBarcode;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colStock;

    private final ProductService productService = new ProductService();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Product> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        colBarcode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBarcode()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStock()));

        loadProducts();

        // 🔍 REAL-TIME SEARCH (Canlı axtarış)
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterProducts(newValue);
        });
    }

    private void loadProducts() {
        productList.setAll(productService.getAllProducts());
        filteredList.setAll(productList);
        stockTable.setItems(filteredList);
    }

    // 🔍 Axtarış filtri
    private void filterProducts(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            filteredList.setAll(productList);
            return;
        }

        String lower = keyword.toLowerCase();
        filteredList.setAll(
                productList.filtered(p ->
                        p.getName().toLowerCase().contains(lower) ||
                        p.getBarcode().toLowerCase().contains(lower)
                )
        );
    }

    @FXML
    public void handleAddProduct() {
//        int id = Integer.parseInt(idField.getText());
        String barcode = barcodeField.getText();
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());

        Product existing = productService.getProductByBarcode(barcode);

        if (existing != null) {
            int newStock = existing.getStock() + stock;
            productService.updateStock(existing.getId(), newStock);
        } else {
            Product p = new Product(1, barcode, name, price, stock);
            productService.addProduct(p);
        }

        loadProducts();

        barcodeField.clear();
        nameField.clear();
        priceField.clear();
        stockField.clear();
    }
}

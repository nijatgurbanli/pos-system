package com.example.pos.controller;

import com.example.pos.model.Product;
import com.example.pos.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StockController {

    @FXML
    private TextField barcodeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private TableView<Product> stockTable;
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

    @FXML
    public void initialize() {
        colBarcode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBarcode()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStock()));
        loadProducts();
    }

    private void loadProducts() {
        productList.setAll(productService.getAllProducts());
        stockTable.setItems(productList);
    }

    @FXML
    public void handleAddProduct() {
        String barcode = barcodeField.getText();
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());

        // Mövcud barkodu yoxla
        Product existing = productService.getProductByBarcode(barcode);

        if (existing != null) {
            // Əgər məhsul varsa → stok artır
            int newStock = existing.getStock() + stock;
            productService.updateStock(existing.getId(), newStock);
        } else {
            // Əks halda yeni məhsul əlavə et
            Product p = new Product(0, barcode, name, price, stock);
            productService.addProduct(p);
        }

        // Cədvəli yenilə və sahələri təmizlə
        loadProducts();
        barcodeField.clear();
        nameField.clear();
        priceField.clear();
        stockField.clear();
    }
}
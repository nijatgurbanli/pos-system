package com.example.pos.controller;

import com.example.pos.model.Product;
import com.example.pos.service.ProductService;
import com.example.pos.service.SaleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SalesController {

    @FXML private TextField barcodeField;
    @FXML private TextField quantityField;
    @FXML private TableView<Product> cartTable;
    @FXML private TableColumn<Product, String> colBarcode;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, Double> colTotal;
    @FXML private Label totalLabel;

    private final ProductService productService = new ProductService();
    private final SaleService saleService = new SaleService();
    private final ObservableList<Product> cartList = FXCollections.observableArrayList();
    private double total = 0.0;

    @FXML
    public void initialize() {
        colBarcode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBarcode()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        colQuantity.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getQuantity()));
        colTotal.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));
        cartTable.setItems(cartList);
    }

    @FXML
    public void handleAddToCart() {
        String barcode = barcodeField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        Product p = productService.getProductByBarcode(barcode);
        if (p != null && p.getStock() >= quantity) {

            // Əgər bu məhsul artıq səbətdədirsə:
            Product existing = cartList.stream()
                    .filter(item -> item.getBarcode().equals(barcode))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + quantity);
            } else {
                Product cartItem = new Product(
                        p.getId(),
                        p.getBarcode(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock(),
                        quantity
                );
                cartList.add(cartItem);
            }

            updateTotal();
            barcodeField.clear();
            quantityField.clear();
        }
    }

    private void updateTotal() {
        total = cartList.stream().mapToDouble(Product::getTotal).sum();
        totalLabel.setText(String.format("%.2f ₼", total));
        cartTable.refresh();
    }

    @FXML
    public void handleConfirmSale() {
        for (Product p : cartList) {
            saleService.recordSale(p.getId(), p.getQuantity(), p.getTotal());
            Product dbProduct = productService.getProductByBarcode(p.getBarcode());
            productService.updateStock(dbProduct.getId(), dbProduct.getStock() - p.getQuantity());
        }
        handleClear();
    }

    @FXML
    public void handleClear() {
        cartList.clear();
        total = 0;
        totalLabel.setText("0.00 ₼");
    }
}

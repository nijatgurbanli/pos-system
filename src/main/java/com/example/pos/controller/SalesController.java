package com.example.pos.controller;

import com.example.pos.model.Product;
import com.example.pos.service.ProductService;
import com.example.pos.service.SaleService;
import com.example.pos.session.Session;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class SalesController {

    @FXML
    private TextField barcodeField;
    @FXML
    private TextField quantityField;
    @FXML
    private TableView<Product> cartTable;
    @FXML
    private TableColumn<Product, String> colBarcode;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colQuantity;
    @FXML
    private TableColumn<Product, Double> colTotal;
    @FXML
    private Label totalLabel;
    @FXML
    private RadioButton cashRadio;
    @FXML
    private RadioButton cardRadio;
    @FXML
    private ToggleGroup paymentGroup;
    @FXML
    private Label statusLabel;


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

        paymentGroup = new ToggleGroup();
        cashRadio.setToggleGroup(paymentGroup);
        cardRadio.setToggleGroup(paymentGroup);

        cashRadio.setSelected(true); // Default nağd olsun
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

//    @FXML
//    public void handleConfirmSale() {
//        for (Product p : cartList) {
//            saleService.recordSale(p.getId(), p.getQuantity(), p.getTotal());
//            Product dbProduct = productService.getProductByBarcode(p.getBarcode());
//            productService.updateStock(dbProduct.getId(), dbProduct.getStock() - p.getQuantity());
//        }
//        handleClear();
//    }

    @FXML
    public void handleClear() {
        cartList.clear();
        total = 0;
        totalLabel.setText("0.00 ₼");
    }

    @FXML
    public void handleConfirmSale() {
        if (cartList.isEmpty()) {
            showStatus("Səbət boşdur! Satış uğursuz oldu.", false);
            return;
        }

        String paymentType;
        if (cashRadio.isSelected()) paymentType = "CASH";
        else if (cardRadio.isSelected()) paymentType = "CARD";
        else {
            showStatus("Ödəniş növü seçilməyib!", false);
            return;
        }

        boolean success = true;
        String username = Session.getUsername();

        for (Product p : cartList) {
            try {
                saleService.recordSale(
                    p.getId(),
                    p.getQuantity(),
                    p.getTotal(),
                    paymentType,
                    username
            );

            Product dbProduct = productService.getProductByBarcode(p.getBarcode());
            productService.updateStock(dbProduct.getId(), dbProduct.getStock() - p.getQuantity());
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
                break;
            }
        }

        if (success) {
            showStatus("Satış uğurla tamamlandı!", true);
            handleClear();
        } else {
            showStatus("Satış zamanı xəta baş verdi!", false);
        }
    }

    // Yeni metod: status göstər və 2 saniyədən sonra itir
    private void showStatus(String message, boolean success) {
        statusLabel.setText(message);
        statusLabel.setStyle(success
                ? "-fx-text-fill: green; -fx-font-weight: bold;"
                : "-fx-text-fill: red; -fx-font-weight: bold;");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> statusLabel.setText(""));
        pause.play();
    }


//    @FXML
//    public void handleConfirmSale() {
//
//        String paymentType = cashRadio.isSelected() ? "CASH" : "CARD";
//        String username = Session.getUsername();
//
//        for (Product p : cartList) {
//            saleService.recordSale(
//                    p.getId(),
//                    p.getQuantity(),
//                    p.getTotal(),
//                    paymentType,
//                    username
//            );
//
//            Product dbProduct = productService.getProductByBarcode(p.getBarcode());
//            productService.updateStock(dbProduct.getId(), dbProduct.getStock() - p.getQuantity());
//        }
//
//        handleClear();
//    }


}

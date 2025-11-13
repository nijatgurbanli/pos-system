package com.example.pos.model;

public class Product {
    private int id;
    private String barcode;
    private String name;
    private double price;
    private int stock;     // anbardakı stok miqdarı
    private int quantity;  // satış zamanı seçilən say (səbətdəki miqdar)

    public Product(int id, String barcode, String name, double price, int stock) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.quantity = 0; // default olaraq 0
    }

    // 🔹 Satış üçün əlavə konstruktor (quantity ilə)
    public Product(int id, String barcode, String name, double price, int stock, int quantity) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stock = 0;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Hesablama üçün köməkçi metod
    public double getTotal() {
        return price * quantity;
    }
}

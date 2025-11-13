package com.example.pos.controller;

import javafx.beans.property.*;

public class ProductItem {
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final DoubleProperty total;

    public ProductItem(String name, double price, int quantity) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.total = new SimpleDoubleProperty(price * quantity);

        // Miqdar dəyişəndə total yenilənsin
        this.quantity.addListener((obs, oldVal, newVal) ->
                this.total.set(this.price.get() * newVal.intValue()));
    }

    public String getName() { return name.get(); }
    public double getPrice() { return price.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getTotal() { return total.get(); }

    public void setQuantity(int q) { this.quantity.set(q); }

    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty totalProperty() { return total; }
}

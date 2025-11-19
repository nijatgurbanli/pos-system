package com.example.pos.service;

import com.example.pos.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaleService {

    public void recordSale(int productId, int quantity, double total, String paymentType, String username) {
        String sql = "INSERT INTO sales(product_id, quantity, total, payment_type, username) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, total);
            stmt.setString(4, paymentType);
            stmt.setString(5, username);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

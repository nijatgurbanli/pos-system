package com.example.pos.controller;

import com.example.pos.model.User;
import com.example.pos.repository.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleCombo;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colCreated;
    @FXML private TableColumn<User, Void> colActions;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        roleCombo.getItems().addAll("Admin", "Kassir", "Menecer");

        addActionButtons();
        loadUsers();
    }

    /** ACTION BUTTONS (EDIT + DELETE) **/
    private void addActionButtons() {
        colActions.setCellFactory(col -> new TableCell<>() {

            private final Button editBtn = new Button("Redaktə");
            private final Button deleteBtn = new Button("Sil");

            {
                editBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });

                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #E53935; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(10, editBtn, deleteBtn);
                    setGraphic(hBox);
                }
            }
        });
    }

    /** LOAD USERS **/
    private void loadUsers() {
        ObservableList<User> list = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users ORDER BY id DESC")) {

            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("created_at")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        userTable.setItems(list);
    }

    /** CREATE USER **/
    @FXML
    private void handleCreateUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleCombo.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) return;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users(username, password, role) VALUES (?, ?, ?)"
             )) {

            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.setString(3, role);

            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }

        loadUsers();
        usernameField.clear();
        passwordField.clear();
        roleCombo.setValue(null);
    }

    /** EDIT USER **/
    private void handleEditUser(User user) {
        usernameField.setText(user.getUsername());
        roleCombo.setValue(user.getRole());
        passwordField.setPromptText("Yeni şifrə daxil et");

        userTable.getSelectionModel().select(user);

        // Save new data
        Button saveBtn = new Button("Yadda saxla");
        saveBtn.setOnAction(e -> {

            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();
            String newRole = roleCombo.getValue();

            try (Connection conn = DatabaseConnection.getConnection()) {

                if (!newPassword.isEmpty()) {
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE users SET username=?, role=?, password=? WHERE id=?"
                    );
                    ps.setString(1, newUsername);
                    ps.setString(2, newRole);
                    ps.setString(3, newPassword);
                    ps.setInt(4, user.getId());
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE users SET username=?, role=? WHERE id=?"
                    );
                    ps.setString(1, newUsername);
                    ps.setString(2, newRole);
                    ps.setInt(3, user.getId());
                    ps.executeUpdate();
                }

            } catch (Exception ex) { ex.printStackTrace(); }

            loadUsers();
            usernameField.clear();
            passwordField.clear();
            roleCombo.setValue(null);
        });

        // Automatically add save button to your form if needed
    }

    /** DELETE USER **/
    private void handleDeleteUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM users WHERE id=?"
             )) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

        loadUsers();
    }
}



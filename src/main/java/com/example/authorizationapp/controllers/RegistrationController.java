package com.example.authorizationapp.controllers;

import com.example.authorizationapp.supportsClass.PasswordUtils;
import com.example.authorizationapp.supportsClass.SalaryGenerator;
import com.example.authorizationapp.supportsClass.Translate;
import com.example.authorizationapp.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationController {
    public TextField lastnameRegistration;
    public TextField firstnameRegistration;
    public TextField patronymicRegistration;
    public PasswordField passwordRegistration;
    private DatabaseManager databaseManager;
    private String userId;
    double salary;

    public RegistrationController() {
        databaseManager = new DatabaseManager();
    }

    private String generateUserId(Connection connection, String role) throws SQLException {
        String prefix;
        switch (role) {
            case "Менеджер":
                prefix = "M-";
                break;
            case "Администратор":
                prefix = "A-";
                break;
            case "Инженер":
                prefix = "E-";
                break;
            default:
                prefix = "U"; // Префикс для пользователей
                break;
        }
        String query = "SELECT COUNT(*) FROM users WHERE user_id LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, prefix + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int nextId = resultSet.getInt(1) + 1; // Увеличиваем количество
                    return String.format("%s%06d", prefix, nextId); // Форматируем ID как A000001, U000001 и т.д.
                }
            }
        }
        return String.format("%s%06d", prefix, 1); // Если пользователей нет, возвращаем префикс с 1
    }

    public void onRegistrationClick(ActionEvent actionEvent) {
        String lastname = lastnameRegistration.getText();
        String firstname = firstnameRegistration.getText();
        String patronymic = patronymicRegistration.getText();
        String password = passwordRegistration.getText();
        var role = roleChoiceBox.getValue();
        registerUser(lastname, firstname, patronymic, password, role);

    }

    private void setSalary(String role) {
        SalaryGenerator salaryGenerator = new SalaryGenerator();
        if (role.equals("Менеджер")) {
            salary = salaryGenerator.managerSalary();
        }
        if (role.equals("Администратор")) {
            salary = salaryGenerator.administratorSalary();
        }
        if (role.equals("Инженер")) {
            salary = salaryGenerator.engineSalary();
        }
    }

    public void registerUser(String lastname, String firstname, String patronymic, String password, String role) {
        String addUserQuery = "INSERT INTO users (user_id, login, lastname, firstname, patronymic, password, role, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;

        try {
            connection = databaseManager.getConnection();

            if (connection == null) {
                System.err.println("Не удалось установить соединение с базой данных.");
                return; // Прекращаем выполнение метода, если соединение не установлено
            }
            var userId = generateUserId(connection, role);
            connection = databaseManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(addUserQuery);
            var hashedPassword = PasswordUtils.hashPassword(password);
            var newRole = roleChoiceBox.getValue();
            setSalary(newRole);
            Translate translate = new Translate();
            String login =  translate.transcription(firstname, lastname, patronymic);

            pstmt.setString(1, userId);
            pstmt.setString(2, login);
            pstmt.setString(3, lastname);
            pstmt.setString(4, firstname);
            pstmt.setString(5, patronymic);
            pstmt.setString(6, hashedPassword);
            pstmt.setString(7, role);
            pstmt.setDouble(8, salary);

            pstmt.executeUpdate();
            System.out.println("Пользователь добавлен успешно с ID: " + userId);


        } catch (SQLException e) {
            System.err.println("Ошибка регистрации пользователя: " + e.getMessage());
        } finally {
            databaseManager.closeConnection(connection); // Закрытие соединения в блоке finally
        }
    }

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList("Администратор", "Инженер", "Менеджер");
        roleChoiceBox.setItems(roles);
    }
}

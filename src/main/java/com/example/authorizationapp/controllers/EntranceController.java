package com.example.authorizationapp.controllers;

import com.example.authorizationapp.database.DatabaseManager;
import com.example.authorizationapp.supportsClass.Time;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.*;

public class EntranceController {
    Time time = new Time();

    public Label info;
    private String userId;
    private DatabaseManager databaseManager;

    public void setUserId(String userId) {
        this.userId = userId;
        showUserInfo();
    }

    @FXML
    private void showUserInfo() {

        String userInfoQuery = "SELECT lastname, firstname, patronymic, salary, role FROM users WHERE user_id = ?";
        Connection connection = null;
        try {
            connection = databaseManager.getConnection();
            if (connection == null) {
                System.err.println("Не удалось установить соединение с базой данных.");
                return;
            }
            PreparedStatement pstmt = connection.prepareStatement(userInfoQuery);
            pstmt.setString(1, userId);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String patronymic = resultSet.getString("patronymic");
                String role = resultSet.getString("role");
                double salary = resultSet.getDouble("salary");

                String currentTime = time.setTime();

                String userInfo = currentTime + ", " + lastname + " " + firstname + " " + patronymic + ", Роль: " +
                        role + ", Зарплата: " + salary;

                info.setText(userInfo);
            } else {
                System.out.println("Пользователь не найден");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении информации о пользователе: " + e.getMessage());
        } finally {
            if (connection != null) {
                databaseManager.closeConnection(connection);
            }
        }
    }

    @FXML
    public void initialize() {
        databaseManager = new DatabaseManager();
    }
}
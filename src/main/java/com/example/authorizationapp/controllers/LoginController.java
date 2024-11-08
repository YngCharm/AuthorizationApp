package com.example.authorizationapp.controllers;

import com.example.authorizationapp.supportsClass.PasswordUtils;
import com.example.authorizationapp.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public TextField loginLogin;
    public PasswordField passwordLogin;
    public DatabaseManager databaseManager;
    public Label noUser;
    public AnchorPane anchorPane;
    @Setter
    private String userId;

    @FXML
    public void onLoginClick(ActionEvent actionEvent) {
        String login = loginLogin.getText();
        String password = passwordLogin.getText();
        loginUser(login, password);
    }

    private boolean loginUser(String login, String password) {
        String loginUserQuery = "SELECT user_id, login, password FROM users  WHERE login = ?";
        Connection connection = null;

        try {
            connection = databaseManager.getConnection();

            if (connection == null) {
                System.err.println("Не удалось соединиться с базой данный");
                return false;
            }
            PreparedStatement pstmt = connection.prepareStatement(loginUserQuery);
            pstmt.setString(1, login);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String storedHashedPassword = resultSet.getString("password");
                if (PasswordUtils.checkPassword(password, storedHashedPassword)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authorizationapp/enterance-view.fxml"));
                    Parent root = loader.load();

                    // Получаем контроллер главного окна
                    EntranceController entranceController = loader.getController();
                    entranceController.setUserId(userId);

                    // Переключаем сцену
                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    return true;
                }
            } else {
                noUser.setVisible(true);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке пользователя: " + e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.closeConnection(connection);
        }
        return false;
    }
    public void setUserId(String userId) {
        this.userId = userId;
        System.out.println("User ID received1: " + userId); // Выводим значение userId в консоль

    }

    @FXML
    public void initialize() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
    }
}

package com.eyren24.screenshare.datamanager;

import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

public class DataBase {
    private Connection connection;

    public DataBase(String file) throws SQLException, ClassNotFoundException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:"+file);
        Class.forName("org.sqlite.JDBC");
        createTable();
    }

    private void createTable() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY, username VARCHAR(255) NOT NULL, uuid CHAR(36) NOT NULL, isControl BOOLEAN NOT NULL);";
        Statement statement = this.connection.createStatement();
        statement.execute(sql);
    }

    public void closeConnection(){
        if (this.connection == null){
            return;
        }
        try {
            this.connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.connection = null;
    }

    public void initPlayer(Player player) throws SQLException {
        String checkPlayer = "SELECT * FROM users WHERE uuid = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(checkPlayer);
        preparedStatement.setString(1, String.valueOf(player.getUniqueId()));
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()){
            String sql = "INSERT INTO users (username, uuid, isControl) VALUES (?,?,?)";
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, player.getName());
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.setBoolean(3, false);
            statement.execute();
        }
    }

    public void setControl(Player target, boolean i) throws SQLException {
        String sql = "UPDATE users SET isControl = ? WHERE uuid = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, i);
        preparedStatement.setString(2, String.valueOf(target.getUniqueId()));
        preparedStatement.executeUpdate();
    }

    public boolean checkControl(Player target) throws SQLException {
        String sql = "SELECT * FROM users WHERE uuid = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(target.getUniqueId()));
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getBoolean("isControl");
        }
        return false;
    }

}

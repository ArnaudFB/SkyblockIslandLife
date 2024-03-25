package fr.nono74210.skyblockislandlife.database;

import java.sql.*;
import java.util.UUID;

public class Database {

    private Connection connection;

    String host;
    String prefix;
    int port;
    String user;
    String password;
    String database;

    public Database(String host, String prefix, Integer port, String user, String password, String database) {
        this.host = host;
        this.prefix = prefix;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public Connection init() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database  , user, password);
        return this.connection;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null && connection.isValid(0)) {
            return connection;
        }

        return init();
    }

    public void load() throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + prefix + "islandLives (" +
                "id         INTEGER     PRIMARY KEY AUTO_INCREMENT, " +
                "islandUuid     VARCHAR(36), " +
                "livesleft  NUMERIC)");

        ps.execute();
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public int getLivesLeftByUUID(UUID islandUuid) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("SELECT livesleft FROM " + prefix + "islandlives " +
                "WHERE islandUuid = ?");
        ps.setString(1, islandUuid.toString());

        ResultSet resultSet = ps.executeQuery();

        int livesLeft = -1;

        if (resultSet.next()) {
            livesLeft = resultSet.getInt("livesleft");
        }

        return livesLeft;
    }

    public int setNewLivesAmountByIslandUuid(UUID islandUuid, int newAmount) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE " + prefix + "islandlives " +
                "SET livesleft = ? " +
                "WHERE islandUuid = ? ");

        preparedStatement.setInt(1, newAmount);
        preparedStatement.setString(2, islandUuid.toString());

        preparedStatement.executeUpdate();

        return newAmount;

    }

    public void addIslandToDatabase(UUID islanduuid, int defaultAmount) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO " + prefix + "islandlives (islandUuid, livesleft) " +
                "VALUES (?, ?)");

        preparedStatement.setString(1, islanduuid.toString());
        preparedStatement.setInt(2, defaultAmount);

        preparedStatement.executeUpdate();
    }

    public void deleteIslandFromDatabase(UUID islanduuid) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM " + prefix + "islandlives " +
                "WHERE islandUuid = ?");

        preparedStatement.setString(1, islanduuid.toString());

        preparedStatement.executeUpdate();
    }
}

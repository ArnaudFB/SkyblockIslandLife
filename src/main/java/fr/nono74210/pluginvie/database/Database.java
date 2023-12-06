package fr.nono74210.pluginvie.database;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;

import java.sql.*;
import java.util.UUID;

public class Database {

    private Connection connection;

    public SuperiorSkyblock superiorSkyblock;
    String host;
    String user;
    String password;
    String database;

    String skyblockDB = superiorSkyblock.getConfig().getString("database.db-name");

    public Database(String host, String user, String password, String database) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public Connection init() throws SQLException {
        this.connection = DriverManager.getConnection(host, user, password);
        return this.connection;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        return init();
    }

    public void load() throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS islandLives (" +
                "id         INTEGER     PRIMARY KEY AUTO_INCREMENT, " +
                "islandUuid     VARCHAR(36), " +
                "livesleft  NUMERIC)");

        ps.execute();
    }

    public int getLivesLeftByUUID(UUID islandUuid) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("SELECT livesleft FROM islandlives " +
                "WHERE islandUuid = ?");
        ps.setString(1, islandUuid.toString());

        ResultSet resultSet = ps.executeQuery();

        int livesLeft = -1;

        if (resultSet.next()) {
            livesLeft = resultSet.getInt("livesleft");
        }

        //todo: Quid si -1 ?
        return livesLeft;
    }

    public int decrementLivesByIslandUuid(UUID islandUuid, int newAmount) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE islandlives " +
                "SET livesleft = ?" +
                "WHERE island = ?");

        preparedStatement.setInt(1, newAmount);
        preparedStatement.setString(2, islandUuid.toString());

        preparedStatement.executeUpdate();

        //todo return vie qui reste
    }

    public void addIslandToDatabase(UUID islanduuid, int defaultAmount) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO islandlives " +
                "VALUES (?, ?)");

        preparedStatement.setString(1, islanduuid.toString());
        preparedStatement.setInt(2, defaultAmount);

        preparedStatement.executeUpdate();
    }

    public void deleteIslandFromDatabase(UUID islanduuid) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM islandlives " +
                "WHERE island = ?");

        preparedStatement.setString(1, islanduuid.toString());

        preparedStatement.executeUpdate();
    }
}

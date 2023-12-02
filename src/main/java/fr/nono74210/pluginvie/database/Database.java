package fr.nono74210.pluginvie.database;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import fr.nono74210.pluginvie.PluginVie;

import java.sql.*;
import java.util.UUID;

public class Database {

    private Connection connection;
    public PluginVie plugin;

    public SuperiorSkyblock superiorSkyblock;
    String url = plugin.getConfig().getString("database.url");
    String user = plugin.getConfig().getString("database.user");
    String password = plugin.getConfig().getString("database.password");

    String skyblockDB = superiorSkyblock.getConfig().getString("database.db-name");

    public Connection getConnection() throws SQLException {

        if(connection != null) {
            return connection;
        }

        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected successfully to PluginVie database !");
        return this.connection;

    }

    public void initializeDataBase() throws SQLException {

        Statement statement = getConnection().createStatement();
        String initializeDatabase = "CREATE DATABASE pluginvie IF NOT EXISTS;";
        String useDatabase = "USE pluginvie;";
        String initializeTable = "CREATE TABLE IF NOT EXISTS islandlives (\n" +
                "    island CHAR(36),\n" +
                "    livesleft NUMERIC\n" +
                "    FOREIGN KEY (islands) REFERENCES " + skyblockDB + ".islands (uuid)" +
                ");";

        statement.execute(initializeDatabase);
        statement.execute(useDatabase);
        statement.execute(initializeTable);

    }

    public UUID getIslandByPlayerUUID(UUID playeruuid) throws SQLException {

        String uuid = playeruuid.toString();

        PreparedStatement preparedStatement = getConnection().
                prepareStatement("USE " + skyblockDB + "; \n" +
                        "SELECT islands FROM islands_members \n" +
                        "WHERE player =  ?;");

        preparedStatement.setString(1, uuid);

        ResultSet resultSet = preparedStatement.executeQuery();

        return UUID.fromString(resultSet.getString("islands"));

    }

    public int getLivesLeftByUUID(UUID islanduuid) throws SQLException {

        String uuid = islanduuid.toString();

        PreparedStatement preparedStatement = getConnection().
                prepareStatement("USE pluginvie ; \n" +
                        "SELECT livesleft FROM islandlives \n" +
                        "WHERE island = ?;");

        preparedStatement.setString(1, uuid);

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.getInt("livesleft");

    }

    public void decrementLivesByUUID(UUID playeruuid, int amount) throws SQLException {

        UUID islanduuid = getIslandByPlayerUUID(playeruuid);
        int livesleft = getLivesLeftByUUID(islanduuid);

        PreparedStatement preparedStatement = getConnection().
                prepareStatement("USE pluginvie ; \n" +
                        "UPDATE islandlives \n" +
                        "SET livesleft = ? \n" +
                        "WHERE island = ? ;");

        preparedStatement.setInt(1, livesleft-amount);
        preparedStatement.setObject(2, islanduuid);

        preparedStatement.executeUpdate();
        preparedStatement.getConnection().commit();

    }

    public void addIslandToDatabase(UUID islanduuid) throws SQLException {

        PreparedStatement preparedStatement = getConnection().
                prepareStatement("USE pluginvie ; \n" +
                        "INSERT INTO islandlives \n" +
                        "VALUES (?, ?);");

        preparedStatement.setObject(1, islanduuid);
        preparedStatement.setInt(2, plugin.getConfig().getInt("Lives.StartingAmount"));

        preparedStatement.executeUpdate();
        preparedStatement.getConnection().commit();

    }

    public void deleteIslandFromDatabase(UUID islanduuid) throws SQLException {

        PreparedStatement preparedStatement = getConnection().
                prepareStatement("USE pluginvie ; \n" +
                        "DELETE FROM islandlives \n" +
                        "WHERE island = ?;");

        preparedStatement.setObject(1, islanduuid);

        preparedStatement.executeUpdate();
        preparedStatement.getConnection().commit();

    }

}

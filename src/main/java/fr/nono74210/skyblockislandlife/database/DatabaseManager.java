package fr.nono74210.skyblockislandlife.database;

import fr.nono74210.skyblockislandlife.SkyblockIslandLife;
import fr.nono74210.skyblockislandlife.hooks.SuperiorsSkyBlockHook;
import fr.nono74210.skyblockislandlife.utils.ResultT;

import java.sql.SQLException;
import java.util.UUID;

import static java.lang.Math.min;

public class DatabaseManager {

    private static Database db;

    private static boolean isError;

    public static void init() {
        isError = false;

        SkyblockIslandLife plugin = SkyblockIslandLife.getInstance();

        String host = plugin.getConfig().getString("database.url");
        String user = plugin.getConfig().getString("database.user");
        String password = plugin.getConfig().getString("database.password");
        String database = plugin.getConfig().getString("database.database");
        int port = plugin.getConfig().getInt("database.port", 3306);

        db = new Database(host, port, user, password, database);

        try {
            db.init();
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cCritical error when trying to init database, please check your credentials and reload the plugin: " + ex.getMessage());
            isError = true;
        }
    }

    public static void load() {
        if (isError) {
            SkyblockIslandLife.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please go up in the logs.");
            return;
        }

        try {
            db.load();
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cCritical error when trying to load database: " + ex.getMessage());
            isError = true;
        }
    }

    public static void addIsland(UUID islandId) {
        if (isError) {
            SkyblockIslandLife.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please goes up in the logs.");
            return;
        }

        try {
            db.addIslandToDatabase(islandId, SkyblockIslandLife.getInstance().getConfig().getInt("Lives.StartingAmount"));
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cError when trying to add island " + islandId + " to database: " + ex.getMessage());
            isError = true;
        }
    }

    public static void deleteIsland(UUID islandId) {
        if (isError) {
            SkyblockIslandLife.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please go up in the logs.");
            return;
        }

        try {
            db.deleteIslandFromDatabase(islandId);
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cError when trying to delete island " + islandId + " to database: " + ex.getMessage());
            isError = true;
        }

    }

    public static ResultT<Integer> addLivesByIslandUuid(UUID islandUuid, Integer amount) {
        if (isError) {
            return ResultT.error("§cInteractions with storage disable to prevent issue, an error occurred before this, please go up in the logs.");
        }

        if (amount < 0) {
            return  ResultT.error("The specified amount must be positive !");
        }

        try {
            int islandLives = db.getLivesLeftByUUID(islandUuid);
            if (islandLives == -1) {
                return ResultT.error("No island match found with uuid = " + islandUuid);
            }
            int maxAmount = SkyblockIslandLife.getInstance().getConfig().getInt("Lives.MaxAmount", 100);
            int newAmount = min(islandLives + amount, maxAmount);
            db.setNewLivesAmountByIslandUuid(islandUuid, newAmount);
            return ResultT.success(newAmount);
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cSomething went wrong when trying to add lives on island " + islandUuid + ex.getMessage());
            isError = true;
            return ResultT.error("§cUnable to communicate with database, please contact an administrator");
        }
    }

    public static ResultT<Integer> decrementLivesByIslandUuid(UUID islandUuid, Integer penalty) {
        if (isError) {
            return ResultT.error("§cInteractions with storage disable to prevent issue, an error occurred before this, please go up in the logs.");
        }

        if (penalty < 0) {
            return ResultT.error("The specified penalty must be positive !");
        }

        try {
            int islandLives = db.getLivesLeftByUUID(islandUuid);
            if (islandLives == -1) {
                return ResultT.error("No island match found with uuid = " + islandUuid);
            }
            int newAmount = islandLives - penalty;
            db.setNewLivesAmountByIslandUuid(islandUuid, newAmount);
            return ResultT.success(newAmount);
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cSomething went wrong when trying to remove lives on island " + islandUuid + ex.getMessage());
            isError = true;
            return ResultT.error("§cUnable to communicate with database, please contact an administrator");
        }
    }

    public static ResultT<Integer> setLivesByIslandUuid(UUID islandUuid, Integer amount) {
        if (isError) {
            return ResultT.error("§cInteractions with storage disable to prevent issue, an error occurred before this, please go up in the logs.");
        }

        if (amount < 0) {
            return ResultT.error("The specified amount must be positive !");
        }

        try {
            db.setNewLivesAmountByIslandUuid(islandUuid, amount);
            return ResultT.success(amount);
        } catch (SQLException ex) {
            SkyblockIslandLife.log.sendMessage("§cSomething went wrong when trying to set a new amount on island " + islandUuid + ex.getMessage());
            isError = true;
            return ResultT.error("§cUnable to communicate with database, please contact an administrator");
        }
    }


    public static ResultT<Integer> getLivesByIslandUuid(UUID islandUuid) {
        if (isError) {
            SkyblockIslandLife.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please go up in the logs.");
            return ResultT.error("§cUnable to communicate with database, please contact an administrator");
        }

        try{
            int livesLeft = db.getLivesLeftByUUID(islandUuid);
            return ResultT.success(livesLeft);
        } catch (SQLException ex) {
            isError = true;
            return ResultT.error("§cUnable to communicate with database, please contact an administrator");
        }

    }

    public static boolean isDatabaseInError() {
        return isError;
    }

    public static void close() {
        if (isError) {
            return;
        }
        try {
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

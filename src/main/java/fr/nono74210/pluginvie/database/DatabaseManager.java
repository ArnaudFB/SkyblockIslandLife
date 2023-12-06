package fr.nono74210.pluginvie.database;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.utils.results.Result;
import fr.nono74210.pluginvie.utils.results.ResultT;

import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    private static Database db;

    private static boolean isError;

    public static void init() {
        PluginVie plugin = PluginVie.getInstance();

        String host = plugin.getConfig().getString("database.url");
        String user = plugin.getConfig().getString("database.user");
        String password = plugin.getConfig().getString("database.password");
        String database = plugin.getConfig().getString("database.database");

        db = new Database(host, user, password, database);

        try {
            db.init();
        } catch (SQLException ex) {
            PluginVie.log.sendMessage("§cCritical error when trying to init database, please check your credentials and reload the plugin: " + ex.getMessage());
            isError = true;
        }
    }

    public static void load() {
        if (isError) {
            PluginVie.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please goes up in the logs.");
            return;
        }

        try {
            db.load();
        } catch (SQLException ex) {
            PluginVie.log.sendMessage("§cCritical error when trying to load database: " + ex.getMessage());
            isError = true;
        }
    }

    public static void addIsland(UUID islandId) {
        if (isError) {
            PluginVie.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please goes up in the logs.");
            return;
        }

        try {
            db.addIslandToDatabase(islandId, PluginVie.getInstance().getConfig().getInt("Lives.StartingAmount"));
        } catch (SQLException ex) {
            PluginVie.log.sendMessage("§cError when trying to add island " + islandId + " to database: " + ex.getMessage());
            isError = true;
        }
    }

    public static void deleteIsland(UUID islandId) {
        if (isError) {
            PluginVie.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please goes up in the logs.");
            return;
        }

        //todo
    }

    public static Result addLivesByIslandUuid(UUID islandUuid, Integer amount) {
        //todo: exemple
        Result.error("");
        return Result.success();
    }

    public static ResultT<Integer> decrementLivesByIslandUuid(UUID islandUuid) {
        if (isError) {
            return ResultT.error("§cInteractions with storage disable to prevent issue, an error occurred before this, please goes up in the logs.");
        }

        //todo calcul amount à soustraire avec vérif
        //todo
        return ResultT.success(0);
    }

    public static int getLivesByIslandUuid(UUID islandUuid) {
        if (isError) {
            PluginVie.log.sendMessage("§cInteractions with storage disable to prevent issue, an error occurred before this, please goes up in the logs.");
            return;
        }

        //todo
        return 0;
    }

    public static void close() {
        if (isError) {
            return;
        }

        //db.close();
    }
}

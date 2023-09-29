package fr.nono74210.pluginvie;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import fr.nono74210.pluginvie.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class PluginVie extends JavaPlugin {

    private final PluginVie pluginVie;
    private Database database;
    private final SuperiorSkyblock superiorSkyblock;
    public PluginVie getInstance(){ return pluginVie; }

    public PluginVie(PluginVie pluginVie, SuperiorSkyblock superiorSkyblock) {
        this.pluginVie = pluginVie;
        this.superiorSkyblock = superiorSkyblock;
    }

    @Override
    public void onEnable() {

        this.database = new Database();

        try {
            database.initializeDataBase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

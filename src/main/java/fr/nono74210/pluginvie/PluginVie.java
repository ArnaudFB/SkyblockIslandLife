package fr.nono74210.pluginvie;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import fr.nono74210.pluginvie.commands.CommandVie;
import fr.nono74210.pluginvie.database.Database;
import fr.nono74210.pluginvie.listeners.IslandCreatedListener;
import fr.nono74210.pluginvie.listeners.IslandDeletedListener;
import fr.nono74210.pluginvie.listeners.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public final class PluginVie extends JavaPlugin {

    private static PluginVie pluginVie;

    public static PluginVie getInstance(){ return pluginVie; }

    public PluginVie(PluginVie pluginVie, SuperiorSkyblock superiorSkyblock) {
        PluginVie.pluginVie = pluginVie;
    }

    @Override
    public void onEnable() {

        Database database = new Database();

        try {
            database.initializeDataBase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        saveDefaultConfig();
        Bukkit.getLogger();

        getServer().getPluginManager().registerEvents(new IslandCreatedListener(database), this);
        getServer().getPluginManager().registerEvents(new IslandDeletedListener(database), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(database), this);

        Objects.requireNonNull(this.getCommand("vie")).setExecutor(new CommandVie(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

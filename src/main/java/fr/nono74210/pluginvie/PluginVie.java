package fr.nono74210.pluginvie;

import fr.nono74210.pluginvie.commands.CommandVie;
import fr.nono74210.pluginvie.database.DatabaseManager;
import fr.nono74210.pluginvie.hooks.PlaceholderHook;
import fr.nono74210.pluginvie.hooks.SuperiorsSkyBlockHook;
import fr.nono74210.pluginvie.listeners.IslandCreatedListener;
import fr.nono74210.pluginvie.listeners.IslandDeletedListener;
import fr.nono74210.pluginvie.listeners.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public final class PluginVie extends JavaPlugin {

    private static PluginVie instance;
    public static ConsoleCommandSender log;

    private FileConfiguration languageConfig;

    private SuperiorsSkyBlockHook superiorsSkyBlockHook;

    @Override
    public void onEnable() {
        instance = this;
        log = Bukkit.getConsoleSender();

        saveDefaultConfig();
        loadLanguage();

        DatabaseManager.init();

        getServer().getPluginManager().registerEvents(new IslandCreatedListener(), this);
        getServer().getPluginManager().registerEvents(new IslandDeletedListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            superiorsSkyBlockHook = new SuperiorsSkyBlockHook();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook().register();
        }

        getCommand("vie").setExecutor(new CommandVie());
    }

    private void loadLanguage() {
        String pathPluginLangFile = "lang/fr_messages.yml";
        Reader defConfigStream;

        try {
            defConfigStream = new InputStreamReader(getResource(pathPluginLangFile), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.sendMessage("§cCritical error loading message file from the plugin, please provide a correct path to load default messages.");
            this.setEnabled(false);
            return;
        }
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

        File languageConfFile = new File(getDataFolder(), pathPluginLangFile);
        languageConfig = YamlConfiguration.loadConfiguration(languageConfFile);

        languageConfig.setDefaults(defConfig);
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();
    }

    public static PluginVie getInstance() {
        return instance;
    }

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public SuperiorsSkyBlockHook getSuperiorsSkyBlockHook() {
        return superiorsSkyBlockHook;
    }
}

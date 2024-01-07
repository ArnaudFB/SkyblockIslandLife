package fr.nono74210.skyblockislandlife;

import fr.nono74210.skyblockislandlife.commands.CommandVie;
import fr.nono74210.skyblockislandlife.commands.TabCompletion;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.hooks.PlaceholderHook;
import fr.nono74210.skyblockislandlife.hooks.SuperiorsSkyBlockHook;
import fr.nono74210.skyblockislandlife.listeners.IslandCreatedListener;
import fr.nono74210.skyblockislandlife.listeners.IslandDeletedListener;
import fr.nono74210.skyblockislandlife.listeners.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public final class SkyblockIslandLife extends JavaPlugin {

    private static SkyblockIslandLife instance;
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
        DatabaseManager.load();

        getServer().getPluginManager().registerEvents(new IslandCreatedListener(), this);
        getServer().getPluginManager().registerEvents(new IslandDeletedListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        this.getCommand("vie").setExecutor(new CommandVie());
        this.getCommand("vie").setTabCompleter(new TabCompletion());

        if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
            superiorsSkyBlockHook = new SuperiorsSkyBlockHook();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook().register();
        }

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

    public static SkyblockIslandLife getInstance() {
        return instance;
    }

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public SuperiorsSkyBlockHook getSuperiorsSkyBlockHook() {
        return superiorsSkyBlockHook;
    }
}

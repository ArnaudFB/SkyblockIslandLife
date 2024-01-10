package fr.nono74210.skyblockislandlife;

import fr.nono74210.skyblockislandlife.commands.CommandVie;
import fr.nono74210.skyblockislandlife.commands.TabCompletion;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.hooks.PlaceholderHook;
import fr.nono74210.skyblockislandlife.hooks.SuperiorsSkyBlockHook;
import fr.nono74210.skyblockislandlife.listeners.IslandCreatedListener;
import fr.nono74210.skyblockislandlife.listeners.IslandDeletedListener;
import fr.nono74210.skyblockislandlife.listeners.PlayerDeathListener;
import fr.nono74210.skyblockislandlife.utils.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class SkyblockIslandLife extends JavaPlugin {

    private static SkyblockIslandLife instance;
    public static ConsoleCommandSender log;

    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    private SuperiorsSkyBlockHook superiorsSkyBlockHook;

    @Override
    public void onEnable() {
        instance = this;
        log = Bukkit.getConsoleSender();

        saveDefaultConfig();
        loadLang();

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
            new PlaceholderHook(this).register();
        }

    }

    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        Reader defConfigStream;
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                defConfigStream = new InputStreamReader(getResource("lang.yml"), StandardCharsets.UTF_8);
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                }
            } catch(IOException e) {
                e.printStackTrace();
                log.sendMessage("[SkyIslandLife] Couldn't create language file.");
                log.sendMessage("[SkyIslandLife] This is a fatal error. Now disabling");
                this.setEnabled(false);
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        SkyblockIslandLife.LANG = conf;
        SkyblockIslandLife.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            log.sendMessage("SkyIslandLife: &cCritical error : Failed to save lang.yml." + e.getMessage());

        }
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();
    }

    public static SkyblockIslandLife getInstance() {
        return instance;
    }

    public YamlConfiguration getLang() {
        return LANG;
    }

    public File getLangFile() {
        return LANG_FILE;
    }

    public SuperiorsSkyBlockHook getSuperiorsSkyBlockHook() {
        return superiorsSkyBlockHook;
    }
}

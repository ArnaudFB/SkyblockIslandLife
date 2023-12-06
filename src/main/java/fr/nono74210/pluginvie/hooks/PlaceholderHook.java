package fr.nono74210.pluginvie.hooks;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.database.DatabaseManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaceholderHook extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("PluginVie") != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }

        return super.register();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "islandVie";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nono74210";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        PluginVie pluginVie = PluginVie.getInstance();

        if (params.equalsIgnoreCase("islandVieLeft")) {
            UUID islanduuid = pluginVie.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(player.getUniqueId());
            return String.valueOf(DatabaseManager.getLivesByIslandUuid(islanduuid));
        }

        if (params.equalsIgnoreCase("islandVieMax")) {
            return String.valueOf(pluginVie.getConfig().getInt("lives.maxlives"));
        }

        return "";
    }
}

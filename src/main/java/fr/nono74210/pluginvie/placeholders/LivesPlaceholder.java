package fr.nono74210.pluginvie.placeholders;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.database.Database;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class LivesPlaceholder extends PlaceholderExpansion {

    private final PluginVie pluginVie;
    private final Database database;

    public LivesPlaceholder(PluginVie pluginVie, Database database) {
        this.pluginVie = pluginVie;
        this.database = database;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "island_vie";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nono74210";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return  true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if(params.equalsIgnoreCase("island_vie_left")) {
            UUID playeruuid = player.getUniqueId();
            UUID islanduuid = null;
            try {
                islanduuid = database.getIslandByPlayerUUID(playeruuid);
                int livesleft = database.getLivesLeftByUUID(islanduuid);
                return String.valueOf(livesleft);
            } catch (SQLException e) {
                return "";
            }

        }
        if(params.equalsIgnoreCase("island_vie_max")) {
            return String.valueOf(pluginVie.getConfig().getInt("lives.maxlives"));
        }
        return "";
    }
}

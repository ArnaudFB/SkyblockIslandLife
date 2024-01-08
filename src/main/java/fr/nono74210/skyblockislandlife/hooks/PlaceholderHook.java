package fr.nono74210.skyblockislandlife.hooks;

import fr.nono74210.skyblockislandlife.SkyblockIslandLife;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.utils.ResultT;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaceholderHook extends PlaceholderExpansion {

    private final SkyblockIslandLife plugin;

    public PlaceholderHook(SkyblockIslandLife plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("SkyblockIslandLife") != null;
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
        return "islandlife";
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
        SkyblockIslandLife skyblockXtreme = SkyblockIslandLife.getInstance();

        if (params.equalsIgnoreCase("left")) {
            ResultT<UUID> islanduuid = skyblockXtreme.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(player.getUniqueId());
            return String.valueOf(DatabaseManager.getLivesByIslandUuid(islanduuid.getResult()).getResult());
        }

        if (params.equalsIgnoreCase("max")) {
            return String.valueOf(skyblockXtreme.getConfig().getInt("Lives.MaxLives"));
        }

        return "";
    }
}

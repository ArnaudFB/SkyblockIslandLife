package fr.nono74210.skyblockislandlife.listeners;

import fr.nono74210.skyblockislandlife.SkyblockIslandLife;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.utils.ResultT;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        SkyblockIslandLife plugin = SkyblockIslandLife.getInstance();
        Player player = event.getEntity();
        UUID playerUuid = player.getUniqueId();

        ConfigurationSection configDeathCommands = plugin.getConfig().getConfigurationSection("CommandsOnDeath");
        if (configDeathCommands != null) {
            for (String line : configDeathCommands.getKeys(false)) {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
                }
        }

        ResultT<UUID> islanduuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(playerUuid);
        ResultT<Integer> resLivesleft = DatabaseManager.decrementLivesByIslandUuid(islanduuid.getResult());
        if (resLivesleft.inError()) {
            SkyblockIslandLife.log.sendMessage(resLivesleft.getErrorMessage());
        }


        if (resLivesleft.getResult() <= 0) {

            ConfigurationSection configCommandsNoMoreLives = plugin.getConfig().getConfigurationSection("CommandsNoMoreLives");
            if (configCommandsNoMoreLives != null) {
                for (String line : configCommandsNoMoreLives.getKeys(false)) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
                    }
            }
            DatabaseManager.setLivesByIslandUuid(islanduuid.getResult());

        }

    }

}

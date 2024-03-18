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

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        SkyblockIslandLife plugin = SkyblockIslandLife.getInstance();
        Player player = event.getEntity();
        UUID playerUuid = player.getUniqueId();

        ConfigurationSection configurationSectionDeath = plugin.getConfig().getConfigurationSection("CommandsOnDeath");
        if (configurationSectionDeath != null) {
            Map<String, Object> configDeathCommands = plugin.getConfig().getConfigurationSection("CommandsOnDeath").getValues(true);
            for (Object line : configDeathCommands.values()) {
                if (line instanceof String) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line.toString()).replaceAll("%player%", player.getName()));
                }
            }
        }

        ResultT<UUID> islanduuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(playerUuid);
        int penalty = plugin.getConfig().getInt("Lives.Penalty");
        ResultT<Integer> resLivesleft = DatabaseManager.decrementLivesByIslandUuid(islanduuid.getResult(), penalty);
        if (resLivesleft.inError()) {
            SkyblockIslandLife.log.sendMessage(resLivesleft.getErrorMessage());
            return ;
        }


        if (resLivesleft.getResult() <= 0) {

            ConfigurationSection configurationSectionNoMoreLives = plugin.getConfig().getConfigurationSection("CommandsNoMoreLives");
            if (configurationSectionNoMoreLives != null) {
                Map<String, Object> configNoMoreLivesCommands = plugin.getConfig().getConfigurationSection("CommandsNoMoreLives").getValues(true);
                for (Object line : configNoMoreLivesCommands.values()) {
                    if (line instanceof String) {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line.toString()).replaceAll("%player%", player.getName()));
                    }
                }
            }

            int startingAmount = plugin.getConfig().getInt("Lives.StartingAmount", 10);
            DatabaseManager.setLivesByIslandUuid(islanduuid.getResult(), startingAmount);
        }

    }

}

package fr.nono74210.skyblockxtreme.listeners;

import fr.nono74210.skyblockxtreme.SkyblockXtreme;
import fr.nono74210.skyblockxtreme.database.DatabaseManager;
import fr.nono74210.skyblockxtreme.utils.ResultT;
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
        SkyblockXtreme plugin = SkyblockXtreme.getInstance();
        Player player = event.getEntity();
        UUID playerUuid = player.getUniqueId();

        ConfigurationSection configDeathCommands = plugin.getConfig().getConfigurationSection("CommandsOnDeath");
        if (configDeathCommands != null) {
            for (String line : configDeathCommands.getKeys(false)) {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
            }
        }

        UUID islanduuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(playerUuid);
        ResultT<Integer> resLivesleft = DatabaseManager.decrementLivesByIslandUuid(islanduuid);
        if (resLivesleft.inError()) {
            SkyblockXtreme.log.sendMessage(resLivesleft.getErrorMessage());
        }


        if (resLivesleft.getResult() == 0) {

            player.sendMessage("Vous n'avez plus de vie sur votre île");
            //TODO: Inclure la logique du delete de l'ile (paste schématic via commande admin)

        }

    }

}

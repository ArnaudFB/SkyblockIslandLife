package fr.nono74210.pluginvie.listeners;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.database.DatabaseManager;
import fr.nono74210.pluginvie.utils.results.ResultT;
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
        PluginVie plugin = PluginVie.getInstance();
        Player player = event.getEntity();
        UUID playerUuid = player.getUniqueId();

        ConfigurationSection configDeathCommands = plugin.getConfig().getConfigurationSection("DeathCommands");
        if (configDeathCommands != null) {
            for (String line : configDeathCommands.getKeys(false)) {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
            }
        }

        UUID islanduuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(playerUuid);
        ResultT<Integer> resLivesleft = DatabaseManager.decrementLivesByIslandUuid(islanduuid);
        if (resLivesleft.inError()) {
            PluginVie.log.sendMessage(resLivesleft.getErrorMessage());
        }


        if (resLivesleft.getResult() == 0) {

            player.sendMessage("Vous n'avez plus de vie sur votre île");
            //TODO: Inclure la logique du delete de l'ile (commande ?)

        }

    }

}

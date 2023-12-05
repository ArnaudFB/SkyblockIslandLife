package fr.nono74210.pluginvie.listeners;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.database.Database;
import fr.nono74210.pluginvie.placeholders.LivesPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    private final PluginVie plugin;
    private final Database database;

    public PlayerDeathListener(Database database) {
        this.plugin = PluginVie.getInstance();
        this.database = database;
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) throws SQLException {

        UUID playeruuid = event.getEntity().getUniqueId();
        Player player = event.getEntity();

        for(String line : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("DeathCommands")).getKeys(false)) {

            String commandline = PlaceholderAPI.setPlaceholders(player, line);
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandline);

        }

        database.decrementLivesByUUID(playeruuid, 1);
        UUID islanduuid = database.getIslandByPlayerUUID(playeruuid);
        int livesleft = database.getLivesLeftByUUID(islanduuid);

        if(livesleft == 0) {

            player.sendMessage("Vous n'avez plus de vie sur votre île");
            //TODO: Inclure la logique du delete de l'ile (commande ?)

        }

    }

}

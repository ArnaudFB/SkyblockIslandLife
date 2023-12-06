package fr.nono74210.pluginvie.commands;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.database.DatabaseManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandVie implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, String label, String[] args) {
        PluginVie plugin = PluginVie.getInstance();

        if(!command.getName().equalsIgnoreCase("vie")) {
            return false;
        }

        if(args[0].equalsIgnoreCase("reload") && commandSender.hasPermission("vie.reload")) {
            plugin.reloadConfig();
            return true;
        }

        if(args[0].equalsIgnoreCase("add") && commandSender.hasPermission("vie.add")) {
            if(args.length > 3) {
                commandSender.sendMessage(plugin.getConfig().getString("TooManyArgsError", "Too many args"));
                return false;
            }
            Player target = Bukkit.getPlayerExact(args[1]);

            //todo: check NumberFormatException
            Integer amount = Integer.parseInt(args[2]);
            if(target == null) {
                commandSender.sendMessage(plugin.getConfig().getString("PlayerNotFoundError", "Player not found"));
                return false;
            }
            if(amount == null) {
                commandSender.sendMessage(plugin.getConfig().getString("NotAnIntError", "You must specify an integer"));
                return false;
            }

            UUID islandUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(target.getUniqueId());
            DatabaseManager.addLivesByIslandUuid(islandUuid, amount);
        }

        //todo: appel à la base pour calculer le nombre de vie restant

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            String message = plugin.getConfig().getString("DisplayLivesLeft", "Vous avez %island_vie_left% vie(s) restante(s)");
            String parsedmessage = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(parsedmessage);
        }

        return false;
    }
}

package fr.nono74210.skyblockxtreme.commands;

import fr.nono74210.skyblockxtreme.SkyblockXtreme;
import fr.nono74210.skyblockxtreme.database.DatabaseManager;
import fr.nono74210.skyblockxtreme.utils.ResultT;
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
        SkyblockXtreme plugin = SkyblockXtreme.getInstance();

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
            if(target == null) {
                commandSender.sendMessage(plugin.getConfig().getString("PlayerNotFoundError", "Player not found"));
                return false;
            }

            try {
                Integer amount = Integer.parseInt(args[2]);
                ResultT<UUID> resultUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(target.getUniqueId());
                if (resultUuid.isSuccess()){
                    DatabaseManager.addLivesByIslandUuid(resultUuid.getResult(), amount);
                }
            } catch (NumberFormatException e) {
                commandSender.sendMessage(plugin.getConfig().getString("NotAnIntError", "You must specify an integer"));
            }

        }

        //todo: appel à la base pour calculer le nombre de vie restant

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            String message = plugin.getConfig().getString("DisplayLivesLeft", "You have  %islandVieLeft% lives left");
            String parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(parsedMessage);
        }

        return false;
    }
}

package fr.nono74210.skyblockislandlife.commands;

import fr.nono74210.skyblockislandlife.SkyblockIslandLife;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.utils.ResultT;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandVie implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, String label, String[] args) {
        SkyblockIslandLife plugin = SkyblockIslandLife.getInstance();
        FileConfiguration languageConfig = SkyblockIslandLife.getInstance().getLanguageConfig();

        if(!command.getName().equalsIgnoreCase("vie")) {
            return false;
        }

        if (args.length == 0 && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            String message = languageConfig.getString("DisplayLivesLeft", "You have  %islandVieLeft% lives left");
            String parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(parsedMessage);
            return true;
        }

        if(args[0].equalsIgnoreCase("reload") && commandSender.hasPermission("vie.reload")) {
            plugin.reloadConfig();
            commandSender.sendMessage(languageConfig.getString("ReloadComplete"), "Reload complete");
            return true;
        }

        if(args[0].equalsIgnoreCase("add") && commandSender.hasPermission("vie.add")) {
            if(args.length > 3) {
                commandSender.sendMessage(languageConfig.getString("TooManyArgsError", "Too many args"));
                return false;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if(target == null) {
                commandSender.sendMessage(languageConfig.getString("PlayerNotFoundError", "Player not found"));
                return false;
            }

            try {
                Integer amount = Integer.parseInt(args[2]);
                ResultT<UUID> resultUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(target.getUniqueId());
                if (resultUuid.isSuccess()){
                    DatabaseManager.addLivesByIslandUuid(resultUuid.getResult(), amount);
                    return true;
                }
            } catch (NumberFormatException e) {
                commandSender.sendMessage(languageConfig.getString("NotAnIntError", "You must specify an integer"));
                return false;
            }

        }

        return false;
    }
}

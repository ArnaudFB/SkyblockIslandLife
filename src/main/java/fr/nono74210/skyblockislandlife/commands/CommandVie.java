package fr.nono74210.skyblockislandlife.commands;

import fr.nono74210.skyblockislandlife.SkyblockIslandLife;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.utils.ResultT;
import fr.nono74210.skyblockislandlife.utils.language.MessageUtils;
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
        FileConfiguration languageConfig = SkyblockIslandLife.getInstance().getLang();

        if (!command.getName().equalsIgnoreCase("vie")) {
            return false;
        }

        if (args.length == 0 && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            String message = languageConfig.getString("MiscMessages.DisplayLivesLeft", "§aYou have  %islandlife_left% lives left");
            String parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(MessageUtils.colorize(parsedMessage));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!commandSender.hasPermission("vie.reload")){
                commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("ErrorMessages.NoPermission", "§cYou don't have permissions to do that !")));
                return false;
            }
            plugin.reloadConfig();
            commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("MiscMessages.ReloadComplete", "§aReload complete")));
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (!commandSender.hasPermission("vie.add")) {
                commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("ErrorMessages.NoPermission", "§cYou don't have permissions to do that !")));
                return false;
            }
            if (args.length > 3) {
                commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("ErrorMessages.TooManyArgsError", "§cToo many args")));
                return false;
            }

            if (args.length < 3) {
                commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("ErrorMessages.NotEnoughArgsError", "§cNot enough args ! Usage is /vie add <player> <amount>")));
                return false;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("ErrorMessages.PlayerNotFoundError", "§cPlayer not found")));
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
                commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("ErrorMessages.NotAnIntError", "§cYou must specify an integer")));
                return false;
            }

        }

        return false;
    }
}

package fr.nono74210.skyblockislandlife.commands;

import fr.nono74210.skyblockislandlife.SkyblockIslandLife;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import fr.nono74210.skyblockislandlife.utils.ResultT;
import fr.nono74210.skyblockislandlife.utils.language.MessageUtils;
import static fr.nono74210.skyblockislandlife.utils.language.MessageUtils.colorize;

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
        boolean isDatabaseInError = DatabaseManager.isDatabaseInError();

        if (!command.getName().equalsIgnoreCase("vie")) {
            return false;
        }

        // Disable plugin's command if database in error
        if (isDatabaseInError) {
            return false;
        }

        // /vie
        if (args.length == 0 && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ResultT<UUID> islandPlayerUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(player.getUniqueId());
            if (islandPlayerUuid.inError()) {
                String defaultMsg = "§cYou don't have any island, create one with /is create !";
                String errorMessage = languageConfig.getString("ErrorMessages.PlayerHaveNoIslandError", defaultMsg);
                player.sendMessage(colorize(errorMessage));
                return false;
            }
            String defaultMsg = "§aYou have %islandlife_left% lives left";
            String message = languageConfig.getString("MiscMessages.DisplayLivesLeft", defaultMsg);
            String parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(colorize(parsedMessage));
            return true;
        }

        // /vie reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!commandSender.hasPermission("vie.reload")){
                String defaultMsg = "§cYou don't have permissions to do that !";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NoPermissionError", defaultMsg)));
                return false;
            }
            plugin.reloadConfig();
            String defaultMsg = "§aReload complete";
            commandSender.sendMessage(colorize(languageConfig.getString("MiscMessages.ReloadComplete", defaultMsg)));
            return true;
        }

        // /vie help
        if (args[0].equalsIgnoreCase("help")) {
            if (!commandSender.hasPermission("vie.help")) {
                String defaultMsg = "§cYou don't have permissions to do that !";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NoPermissionError", defaultMsg)));
                return false;
            }
            commandSender.sendMessage(MessageUtils.sendCenteredString(colorize(languageConfig.getString("PluginName", "§9§l[§3§lSky§b§lIslandLife§9§l]"))));
            commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("HelpMessages.VieCommand", "/vie : allows you to see your number of lives left")));
            commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("HelpMessages.ReloadCommand", "/vie reload : allows you to reload plugin's config")));
            commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("HelpMessages.AddCommand", "/vie add <player> <amount> : allows you to add a certain amount of lives to a player")));
            commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("HelpMessages.RemoveCommand", "/vie remove <player> <amount> : allows you to remove a certain amount of lives to a player")));
            commandSender.sendMessage(MessageUtils.colorize(languageConfig.getString("HelpMessages.SetCommand", "/vie set <player> <amount> : allows you to set a new amount of lives to a player")));
            return true;
        }

        // /vie add <player> <amount>
        if (args[0].equalsIgnoreCase("add")) {
            if (!commandSender.hasPermission("vie.add")) {
                String defaultMsg  ="§cYou don't have permissions to do that !";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NoPermissionError", defaultMsg)));
                return false;
            }
            if (args.length > 3) {
                String defaultMsg = "§cToo many args";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.TooManyArgsError", defaultMsg)));
                return false;
            }

            if (args.length < 3) {
                String defaultMsg = "§cNot enough args ! Usage is /vie add <player> <amount>";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NotEnoughArgsError", defaultMsg)));
                return false;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                String defaultMsg ="§cPlayer not found";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.PlayerNotFoundError", defaultMsg)));
                return false;
            }

            try {
                Integer amount = Integer.parseInt(args[2]);
                if (amount < 0) {
                    String defaultMsg = "§cYou must specify a positive integer !";
                    String message = languageConfig.getString("ErrorMessages.InvalidIntError", defaultMsg);
                    commandSender.sendMessage(colorize(message));
                    return false;
                }
                ResultT<UUID> resultUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(target.getUniqueId());
                if (resultUuid.isSuccess()){
                    DatabaseManager.addLivesByIslandUuid(resultUuid.getResult(), amount);
                    String defaultMessage = "§aYou added lives to the player %player%";
                    commandSender.sendMessage(colorize(languageConfig.getString("MiscMessages.LivesAddedSuccessfully", defaultMessage).replaceAll("%player%", target.getName())));
                    return true;
                }
            } catch (NumberFormatException e) {
                String defaultMsg = "§cYou must specify an integer";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NotAnIntError", defaultMsg)));
                return false;
            }

        }

        // /vie remove <player> <amount>
        if (args[0].equalsIgnoreCase("remove")) {
            if (!commandSender.hasPermission("vie.remove")) {
                String defaultMsg = "§cYou don't have permissions to do that !";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NoPermissionError", defaultMsg)));
                return false;
            }
            if (args.length > 3) {
                String defaultMsg = "§cToo many args";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.TooManyArgsError", defaultMsg)));
                return false;
            }

            if (args.length < 3) {
                String defaultMsg = "§cNot enough args ! Usage is /vie remove <player> <amount>";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NotEnoughArgsError", defaultMsg)));
                return false;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                String defaultMsg = "§cPlayer not found";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.PlayerNotFoundError", defaultMsg)));
                return false;
            }

            try {
                Integer amount = Integer.parseInt(args[2]);
                if (amount < 0) {
                    String defaultMsg = "§cYou must specify a positive integer !";
                    String message = languageConfig.getString("ErrorMessages.InvalidIntError", defaultMsg);
                    commandSender.sendMessage(colorize(message));
                    return false;
                }
                ResultT<UUID> resultUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(target.getUniqueId());
                if (resultUuid.isSuccess()){
                    DatabaseManager.decrementLivesByIslandUuid(resultUuid.getResult(), amount);
                    String defaultMsg = "§aYou removed lives to the player %player%";
                    commandSender.sendMessage(colorize(languageConfig.getString("MiscMessages.LivesRemovedSuccessfully", defaultMsg).replaceAll("%player%", target.getName())));
                    return true;
                }
            } catch (NumberFormatException e) {
                String defaultMsg = "§cYou must specify an integer";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NotAnIntError", defaultMsg)));
                return false;
            }

        }

        // /vie set <player> <amount>
        if (args[0].equalsIgnoreCase("set")) {
            if (!commandSender.hasPermission("vie.set")) {
                String defaultMsg = "§cYou don't have permissions to do that !";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NoPermissionError", defaultMsg)));
                return false;
            }
            if (args.length > 3) {
                String defaultMsg = "§cToo many args";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.TooManyArgsError", defaultMsg)));
                return false;
            }

            if (args.length < 3) {
                String defaultMsg = "§cNot enough args ! Usage is /vie set <player> <amount>";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NotEnoughArgsError", defaultMsg)));
                return false;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                String defaultMsg = "§cPlayer not found";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.PlayerNotFoundError", defaultMsg)));
                return false;
            }

            try {
                Integer amount = Integer.parseInt(args[2]);
                if (amount < 0) {
                    String defaultMsg = "§cYou must specify a positive integer !";
                    String message = languageConfig.getString("ErrorMessages.InvalidIntError", defaultMsg);
                    commandSender.sendMessage(colorize(message));
                    return false;
                }
                ResultT<UUID> resultUuid = plugin.getSuperiorsSkyBlockHook().getIslandByPlayerUUID(target.getUniqueId());
                if (resultUuid.isSuccess()){
                    DatabaseManager.setLivesByIslandUuid(resultUuid.getResult(), amount);
                    String defaultMsg = "§aYou set lives to the player %player%";
                    commandSender.sendMessage(colorize(languageConfig.getString("MiscMessages.LivesSetSuccessfully", defaultMsg).replaceAll("%player%", target.getName())));
                    return true;
                }
            } catch (NumberFormatException e) {
                String defaultMsg = "§cYou must specify an integer";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.NotAnIntError", defaultMsg)));
                return false;
            }

        }

        // /vie <player>
        if (args.length == 1) {
            Player targetedPlayer = Bukkit.getPlayerExact(args[0]);
            if (targetedPlayer == null) {
                String defaultMsg = "§cPlayer not found";
                commandSender.sendMessage(colorize(languageConfig.getString("ErrorMessages.PlayerNotFoundError", defaultMsg)));
                return false;
            }
            String defaultMsg = "§aThe player %player% has %islandlife_left% live(s) left";
            String message = languageConfig.getString("MiscMessages.DisplayOtherPlayerLivesLeft", defaultMsg);
            String parsedMsg = PlaceholderAPI.setPlaceholders(targetedPlayer, message).replaceAll("%player%", targetedPlayer.getName());
            commandSender.sendMessage(colorize(parsedMsg));
        }
        return false;
    }
}

package fr.nono74210.pluginvie.commands;

import fr.nono74210.pluginvie.PluginVie;
import fr.nono74210.pluginvie.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class CommandVie implements CommandExecutor {

    private final PluginVie plugin;

    private Database database;

    public CommandVie(PluginVie plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, String label, String[] args) {

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
            Integer amount = Integer.getInteger(args[2]);
            if(target == null) {
                commandSender.sendMessage(plugin.getConfig().getString("PlayerNotFoundError", "Player not found"));
                return false;
            }
            if(amount == null) {
                commandSender.sendMessage(plugin.getConfig().getString("NotAnIntError", "You must specify an integer"));
                return false;
            }
            UUID targetuuid = target.getUniqueId();
            try {
                database.decrementLivesByUUID(targetuuid, amount);
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(args == null) {
                //TODO: ajouter le parse de papi
                player.sendMessage(plugin.getConfig().getString("DisplayLivesLeft", ""));
            }

        }

        return false;
    }
}

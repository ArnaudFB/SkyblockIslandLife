package fr.nono74210.skyblockislandlife.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> arguments = new ArrayList<>(Arrays.asList("reload", "add", "remove", "set", "help"));
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(player.getName());
                }
            }
            for (String string : arguments) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(string);
                }
            }
            return result;
        } else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        result.add(player.getName());
                    }
                }
                return result;
            }
            if (args.length == 3) {
                for (int i = 1; i <= 100; i++) {
                    if (String.valueOf(i).startsWith(args[2])) {
                        result.add(String.valueOf(i));
                    }
                }
                return result;
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}

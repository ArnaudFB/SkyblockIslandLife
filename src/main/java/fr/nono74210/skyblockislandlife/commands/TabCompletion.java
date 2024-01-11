package fr.nono74210.skyblockislandlife.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {

    List<String> arguments = new ArrayList<>(Arrays.asList("add", "reload"));
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String string : arguments) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(string);
                }
                return result;
            }
        }

        if (args.length == 2) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    result.add(player.getName());
                }
                return result;
            }
        }
        return null;
    }
}

package fr.nono74210.skyblockislandlife.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {

    private static final List<String> COMMANDS = new ArrayList<String>(Arrays.asList("add", "reload"));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        final List<String> completions = new ArrayList<>();

        StringUtil.copyPartialMatches(args[0], COMMANDS, completions);

        return completions;

    }

}

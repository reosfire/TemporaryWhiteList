package ru.reosfire.twl.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import ru.reosfire.twl.common.commands.TwlCommand;
import ru.reosfire.twl.spigot.TemporaryWhiteList;

import java.util.List;

public class TwlCommandExecutor implements TabExecutor {
    private final TwlCommand rootCommand;

    public TwlCommandExecutor(TemporaryWhiteList plugin) {
        rootCommand = new TwlCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return rootCommand.onCommand(new SpigotTwlCommandSender(sender), args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return rootCommand.onTabComplete(new SpigotTwlCommandSender(sender), args);
    }
}
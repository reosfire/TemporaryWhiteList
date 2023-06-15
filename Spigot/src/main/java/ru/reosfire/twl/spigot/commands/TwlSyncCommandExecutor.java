package ru.reosfire.twl.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import ru.reosfire.twl.common.commands.TwlSyncCommand;
import ru.reosfire.twl.spigot.TemporaryWhiteList;

import java.util.List;

public class TwlSyncCommandExecutor implements TabExecutor {
    private final TwlSyncCommand rootCommand;

    public TwlSyncCommandExecutor(TemporaryWhiteList plugin) {
        rootCommand = new TwlSyncCommand(plugin);
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
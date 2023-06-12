package ru.reosfire.twl.spigot.commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.commands.subcommands.*;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    private final TwlCommandResultsConfig commandResults;
    public TwlCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);

        commandResults = pluginInstance.getMessages().CommandResults.Twl;

        addChildren(new AddCommand(pluginInstance));
        addChildren(new SetCommand(pluginInstance));
        addChildren(new RemoveCommand(pluginInstance));
        addChildren(new CheckCommand(pluginInstance));
        addChildren(new ListCommand(pluginInstance));
        addChildren(new ImportCommand(pluginInstance));
        addChildren(new ClearCommand(pluginInstance));
        addChildren(new EnableCommand(pluginInstance));
        addChildren(new DisableCommand(pluginInstance));
        addChildren(new ReloadCommand(pluginInstance));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}
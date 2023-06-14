package ru.reosfire.twl.spigot.commands;

import ru.reosfire.twl.common.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.commands.subcommands.AddCommand;
import ru.reosfire.twl.spigot.commands.subcommands.CheckCommand;
import ru.reosfire.twl.spigot.commands.subcommands.RemoveCommand;
import ru.reosfire.twl.spigot.commands.subcommands.SetCommand;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;

@CommandName("twl-sync")
public class TwlSyncCommand extends CommandNode
{
    private final TwlCommandResultsConfig commandResults;

    public TwlSyncCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);

        commandResults = pluginInstance.getMessages().CommandResults.Twl;

        addChildren(new AddCommand(pluginInstance, true));
        addChildren(new SetCommand(pluginInstance, true));
        addChildren(new CheckCommand(pluginInstance, true));
        addChildren(new RemoveCommand(pluginInstance, true));
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return false;
    }
}
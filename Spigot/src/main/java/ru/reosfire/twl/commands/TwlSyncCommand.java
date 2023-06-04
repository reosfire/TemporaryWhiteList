package ru.reosfire.twl.commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.TemporaryWhiteList;
import ru.reosfire.twl.commands.subcommands.AddCommand;
import ru.reosfire.twl.commands.subcommands.CheckCommand;
import ru.reosfire.twl.commands.subcommands.RemoveCommand;
import ru.reosfire.twl.commands.subcommands.SetCommand;
import ru.reosfire.twl.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.twl.lib.commands.CommandName;
import ru.reosfire.twl.lib.commands.CommandNode;

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
    protected boolean execute(CommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return false;
    }
}
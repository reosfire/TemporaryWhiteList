package ru.reosfire.temporarywhitelist.Commands.Sync;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.AddCommand;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.CheckCommand;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.RemoveCommand;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.SetCommand;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.TwlCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("twl-sync")
public class TwlSyncCommand extends CommandNode
{
    private final TwlCommandResultsConfig _commandResults;

    public TwlSyncCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);

        _commandResults = pluginInstance.getMessages().CommandResults.Twl;

        AddChildren(new AddCommand(pluginInstance, true));
        AddChildren(new SetCommand(pluginInstance, true));
        AddChildren(new CheckCommand(pluginInstance, true));
        AddChildren(new RemoveCommand(pluginInstance, true));
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        _commandResults.Usage.Send(sender);
        return false;
    }
}
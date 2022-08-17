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
package ru.reosfire.temporarywhitelist.commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.commands.subcommands.AddCommand;
import ru.reosfire.temporarywhitelist.commands.subcommands.CheckCommand;
import ru.reosfire.temporarywhitelist.commands.subcommands.RemoveCommand;
import ru.reosfire.temporarywhitelist.commands.subcommands.SetCommand;
import ru.reosfire.temporarywhitelist.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.temporarywhitelist.lib.commands.CommandName;
import ru.reosfire.temporarywhitelist.lib.commands.CommandNode;
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
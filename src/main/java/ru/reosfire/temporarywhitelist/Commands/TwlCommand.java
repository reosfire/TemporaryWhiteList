package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.*;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.TwlCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

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
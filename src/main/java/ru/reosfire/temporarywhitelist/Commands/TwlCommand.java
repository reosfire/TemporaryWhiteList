package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.*;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.TwlCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    private final TwlCommandResultsConfig _commandResults;
    public TwlCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);

        _commandResults = pluginInstance.getMessages().CommandResults.Twl;

        AddChildren(new AddCommand(pluginInstance));
        AddChildren(new SetCommand(pluginInstance));
        AddChildren(new RemoveCommand(pluginInstance));
        AddChildren(new CheckCommand(pluginInstance));
        AddChildren(new ListCommand(pluginInstance));
        AddChildren(new ImportCommand(pluginInstance));
        AddChildren(new EnableCommand(pluginInstance));
        AddChildren(new DisableCommand(pluginInstance));
        AddChildren(new ReloadCommand(pluginInstance));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        _commandResults.Usage.Send(sender);
        return true;
    }
}
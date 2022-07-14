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
    public TwlCommand(MessagesConfig messages, PlayerDatabase dataProvider, TemporaryWhiteList pluginInstance,
                      TimeConverter timeConverter, Config config)
    {
        super(messages.NoPermission);

        _commandResults = messages.CommandResults.Twl;

        AddChildren(new AddCommand(messages, dataProvider, timeConverter));
        AddChildren(new SetCommand(messages, dataProvider, timeConverter));
        AddChildren(new RemoveCommand(messages, dataProvider));
        AddChildren(new CheckCommand(messages, dataProvider, timeConverter));
        AddChildren(new ListCommand(messages, dataProvider, config.ListPageSize));
        AddChildren(new ImportCommand(messages, dataProvider, timeConverter));
        AddChildren(new EnableCommand(messages, pluginInstance));
        AddChildren(new DisableCommand(messages, pluginInstance));
        AddChildren(new ReloadCommand(messages, pluginInstance));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        _commandResults.Usage.Send(sender);
        return true;
    }
}
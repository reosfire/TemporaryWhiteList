package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.DisableCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("disable")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class DisableCommand extends CommandNode
{
    private final TemporaryWhiteList _pluginInstance;
    private final DisableCommandResultsConfig _commandResults;

    public DisableCommand(MessagesConfig messagesConfig, TemporaryWhiteList pluginInstance)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Disable;
        _pluginInstance = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        try
        {
            if (_pluginInstance.Disable()) _commandResults.Success.Send(sender);
            else _commandResults.NothingChanged.Send(sender);
        }
        catch (Exception e)
        {
            _commandResults.Error.Send(sender);
            e.printStackTrace();
        }
        return true;
    }
}
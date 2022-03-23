package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("disable")
@CommandPermission("TemporaryWhiteList.Administrate.Disable")
public class DisableCommand extends CommandNode
{
    private final TemporaryWhiteList _pluginInstance;

    public DisableCommand(MessagesConfig messagesConfig, TemporaryWhiteList pluginInstance)
    {
        super(messagesConfig.NoPermission);
        _pluginInstance = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        try
        {
            _pluginInstance.Disable();
            sender.sendMessage("disabled");
        }
        catch (Exception e)
        {
            sender.sendMessage("Error! Watch console");
            e.printStackTrace();
        }
        return true;
    }
}
package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("enable")
@CommandPermission("TemporaryWhiteList.Administrate.Enable")
public class EnableCommand extends CommandNode
{
    private final TemporaryWhiteList _pluginInstance;

    public EnableCommand(MessagesConfig messagesConfig, TemporaryWhiteList pluginInstance)
    {
        super(messagesConfig.NoPermission);
        _pluginInstance = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        try
        {
            _pluginInstance.Enable();
            sender.sendMessage("enabled");
        }
        catch (Exception e)
        {
            sender.sendMessage("Error! Watch console");
            e.printStackTrace();
        }
        return true;
    }
}
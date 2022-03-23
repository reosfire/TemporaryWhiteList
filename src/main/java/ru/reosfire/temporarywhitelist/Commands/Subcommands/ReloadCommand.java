package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("reload")
@CommandPermission("TemporaryWhiteList.Administrate.Reload")
public class ReloadCommand extends CommandNode
{
    private final TemporaryWhiteList _pluginInstance;

    public ReloadCommand(MessagesConfig messagesConfig, TemporaryWhiteList pluginInstance)
    {
        super(messagesConfig.NoPermission);
        _pluginInstance = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        _pluginInstance.Load();
        sender.sendMessage("reloaded");
        return true;
    }
}
package ru.reosfire.twl.spigot.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.ReloadCommandResultsConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;

@CommandName("reload")
@CommandPermission("TemporaryWhitelist.Administrate.Reload")
public class ReloadCommand extends CommandNode
{
    private final TemporaryWhiteList plugin;
    private final ReloadCommandResultsConfig commandResults;

    public ReloadCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Reload;
        plugin = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        try
        {
            plugin.load();
            commandResults.Success.Send(sender);
        }
        catch (Exception e)
        {
            commandResults.Error.Send(sender);
        }
        return true;
    }
}
package ru.reosfire.temporarywhitelist.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.configuration.localization.commandResults.ReloadCommandResultsConfig;
import ru.reosfire.temporarywhitelist.lib.commands.CommandName;
import ru.reosfire.temporarywhitelist.lib.commands.CommandNode;
import ru.reosfire.temporarywhitelist.lib.commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

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
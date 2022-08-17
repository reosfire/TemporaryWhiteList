package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ReloadCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
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
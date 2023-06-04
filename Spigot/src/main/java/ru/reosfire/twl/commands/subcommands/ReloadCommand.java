package ru.reosfire.twl.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.TemporaryWhiteList;
import ru.reosfire.twl.configuration.localization.commandResults.ReloadCommandResultsConfig;
import ru.reosfire.twl.lib.commands.CommandName;
import ru.reosfire.twl.lib.commands.CommandNode;
import ru.reosfire.twl.lib.commands.CommandPermission;

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
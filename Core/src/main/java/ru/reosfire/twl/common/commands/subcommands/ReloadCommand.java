package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.configuration.localization.commandResults.ReloadCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.CommandPermission;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

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
    public boolean execute(TwlCommandSender sender, String[] args)
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
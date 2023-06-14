package ru.reosfire.twl.spigot.commands.subcommands;

import ru.reosfire.twl.common.configuration.localization.commandResults.EnableCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;

@CommandName("enable")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class EnableCommand extends CommandNode
{
    private final TemporaryWhiteList plugin;
    private final EnableCommandResultsConfig commandResults;

    public EnableCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Enable;
        plugin = pluginInstance;
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        try
        {
            if (plugin.enable()) commandResults.Success.Send(sender);
            else commandResults.NothingChanged.Send(sender);
        }
        catch (Exception e)
        {
            commandResults.Error.Send(sender);
            e.printStackTrace();
        }
        return true;
    }
}
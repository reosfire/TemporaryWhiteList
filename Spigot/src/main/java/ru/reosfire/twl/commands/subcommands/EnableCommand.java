package ru.reosfire.twl.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.TemporaryWhiteList;
import ru.reosfire.twl.configuration.localization.commandResults.EnableCommandResultsConfig;
import ru.reosfire.twl.lib.commands.CommandName;
import ru.reosfire.twl.lib.commands.CommandNode;
import ru.reosfire.twl.lib.commands.CommandPermission;

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
    public boolean execute(CommandSender sender, String[] args)
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
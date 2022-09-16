package ru.reosfire.temporarywhitelist.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.configuration.localization.commandResults.EnableCommandResultsConfig;
import ru.reosfire.temporarywhitelist.lib.commands.CommandName;
import ru.reosfire.temporarywhitelist.lib.commands.CommandNode;
import ru.reosfire.temporarywhitelist.lib.commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

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
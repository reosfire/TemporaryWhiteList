package ru.reosfire.twl.spigot.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.DisableCommandResultsConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;

@CommandName("disable")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class DisableCommand extends CommandNode
{
    private final TemporaryWhiteList plugin;
    private final DisableCommandResultsConfig commandResults;

    public DisableCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Disable;
        plugin = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        try
        {
            if (plugin.disable()) commandResults.Success.Send(sender);
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
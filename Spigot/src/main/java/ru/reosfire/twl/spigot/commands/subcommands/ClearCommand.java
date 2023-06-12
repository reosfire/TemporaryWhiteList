package ru.reosfire.temporarywhitelist.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.configuration.localization.commandResults.ClearCommandResultsConfig;
import ru.reosfire.temporarywhitelist.lib.commands.CommandName;
import ru.reosfire.temporarywhitelist.lib.commands.CommandNode;
import ru.reosfire.temporarywhitelist.lib.commands.CommandPermission;

import java.util.concurrent.ThreadLocalRandom;

@CommandName("clear")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class ClearCommand extends CommandNode
{
    private int confirmationCode = -1;
    private final TemporaryWhiteList plugin;
    private final ClearCommandResultsConfig commandResults;

    public ClearCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Clear;
        plugin = pluginInstance;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (confirmationCode == -1) {
            confirmationCode = ThreadLocalRandom.current().nextInt(9000) + 1000;
            sender.sendMessage("To confirm clear use: /clear " + confirmationCode + "or any other number to cancel");
        } else {
            if (args.length == 0) {
                sender.sendMessage("To confirm clear use: /clear " + confirmationCode);
                return true;
            }

            int enteredCode;
            try
            {
                enteredCode = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                enteredCode = -1;
            }

            if (enteredCode != confirmationCode) {
                sender.sendMessage("cancelled");
            }
            else {
                plugin.getDatabase().clear().whenComplete((unused, exception) ->
                        sender.sendMessage("Success")
                );
                sender.sendMessage("cleared");
            }

            //TODO move messages to config

            confirmationCode = -1;
        }
        return true;
    }
}
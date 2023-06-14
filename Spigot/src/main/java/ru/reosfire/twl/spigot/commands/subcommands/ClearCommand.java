package ru.reosfire.twl.spigot.commands.subcommands;

import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;

import java.util.concurrent.ThreadLocalRandom;

@CommandName("clear")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class ClearCommand extends CommandNode
{
    private int confirmationCode = -1;
    private final TemporaryWhiteList plugin;
    //private final ClearCommandResultsConfig commandResults;

    public ClearCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        //commandResults = pluginInstance.getMessages().CommandResults.Clear;
        plugin = pluginInstance;
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        if (confirmationCode == -1) {
            confirmationCode = ThreadLocalRandom.current().nextInt(9000) + 1000;
            //sender.sendMessage("To confirm clear use: /clear " + confirmationCode + "or any other number to cancel");
        } else {
            if (args.length == 0) {
                //sender.sendMessage("To confirm clear use: /clear " + confirmationCode);
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
                //sender.sendMessage("cancelled");
            }
            else {
                plugin.getDatabase().clear().whenComplete((unused, exception) -> System.out.println("aaaa")
                        //sender.sendMessage("Success")
                );
                //sender.sendMessage("cleared");
            }

            //TODO move messages to config

            confirmationCode = -1;
        }
        return true;
    }
}
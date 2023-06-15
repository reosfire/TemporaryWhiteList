package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.CommandPermission;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

import java.util.concurrent.ThreadLocalRandom;

@CommandName("clear")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class ClearCommand extends CommandNode
{
    private int confirmationCode = -1;
    private final CommonTwlApi plugin;
    //private final ClearCommandResultsConfig commandResults;

    public ClearCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        //commandResults = pluginInstance.getMessages().CommandResults.Clear;
        plugin = commonApi;
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
package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.configuration.localization.commandResults.DisableCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.CommandPermission;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

@CommandName("disable")
@CommandPermission("TemporaryWhitelist.Administrate.EnableDisable")
public class DisableCommand extends CommandNode
{
    private final CommonTwlApi plugin;
    private final DisableCommandResultsConfig commandResults;

    public DisableCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Disable;
        plugin = commonApi;
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
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
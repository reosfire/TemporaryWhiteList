package ru.reosfire.twl.common.commands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.commands.subcommands.AddCommand;
import ru.reosfire.twl.common.commands.subcommands.CheckCommand;
import ru.reosfire.twl.common.commands.subcommands.RemoveCommand;
import ru.reosfire.twl.common.commands.subcommands.SetCommand;
import ru.reosfire.twl.common.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

@CommandName("twl-sync")
public class TwlSyncCommand extends CommandNode
{
    private final TwlCommandResultsConfig commandResults;

    public TwlSyncCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Twl;

        addChildren(new AddCommand(commonApi, true));
        addChildren(new SetCommand(commonApi, true));
        addChildren(new CheckCommand(commonApi, true));
        addChildren(new RemoveCommand(commonApi, true));
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return false;
    }
}
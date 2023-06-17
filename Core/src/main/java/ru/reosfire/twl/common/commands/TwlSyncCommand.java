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
    public final AddCommand Add;
    public final SetCommand Set;
    public final CheckCommand Check;
    public final RemoveCommand Remove;

    private final TwlCommandResultsConfig commandResults;

    public TwlSyncCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Twl;

        Add = new AddCommand(commonApi, true);
        Set = new SetCommand(commonApi, true);
        Check = new CheckCommand(commonApi, true);
        Remove = new RemoveCommand(commonApi, true);

        addChildren(Add, Set, Check, Remove);
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return false;
    }
}
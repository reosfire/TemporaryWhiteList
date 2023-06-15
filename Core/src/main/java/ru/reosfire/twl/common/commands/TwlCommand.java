package ru.reosfire.twl.common.commands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.commands.subcommands.*;
import ru.reosfire.twl.common.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    public final ImportCommand Import;

    private final TwlCommandResultsConfig commandResults;
    public TwlCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Twl;

        Import = new ImportCommand(commonApi);

        addChildren(new AddCommand(commonApi));
        addChildren(new SetCommand(commonApi));
        addChildren(new RemoveCommand(commonApi));
        addChildren(new CheckCommand(commonApi));
        addChildren(new ListCommand(commonApi));
        addChildren(Import);
        addChildren(new ClearCommand(commonApi));
        addChildren(new EnableCommand(commonApi));
        addChildren(new DisableCommand(commonApi));
        addChildren(new ReloadCommand(commonApi));
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}
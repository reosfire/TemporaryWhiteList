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
    public final AddCommand Add;
    public final SetCommand Set;
    public final RemoveCommand Remove;
    public final CheckCommand Check;
    public final ListCommand List;

    public final ImportCommand Import;
    public final ClearCommand Clear;
    public final EnableCommand Enable;
    public final DisableCommand Disable;
    public final ReloadCommand Reload;

    private final TwlCommandResultsConfig commandResults;
    public TwlCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Twl;

        Add = new AddCommand(commonApi);
        Set = new SetCommand(commonApi);
        Remove = new RemoveCommand(commonApi);
        Check = new CheckCommand(commonApi);
        List = new ListCommand(commonApi);
        Import = new ImportCommand(commonApi);
        Clear = new ClearCommand(commonApi);
        Enable = new EnableCommand(commonApi);
        Disable = new DisableCommand(commonApi);
        Reload = new ReloadCommand(commonApi);

        addChildren(Add, Set, Remove, Check, List, Import, Clear, Enable, Disable, Reload);
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}
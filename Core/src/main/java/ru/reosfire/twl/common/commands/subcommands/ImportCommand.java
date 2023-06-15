package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.commands.subcommands.importTypes.EasyWhitelistImportCommand;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.CommandPermission;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

@CommandName("import")
@CommandPermission("TemporaryWhitelist.Administrate.Import")
public class ImportCommand extends CommandNode
{
    private final ImportCommandResultConfig commandResults;

    public ImportCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Import;

        addChildren(new EasyWhitelistImportCommand(commonApi));
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}
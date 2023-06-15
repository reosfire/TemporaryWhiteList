package ru.reosfire.twl.spigot.commands.importTypes;

import ru.reosfire.twl.common.commands.subcommands.importTypes.BaseImportCommandNode;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.data.providers.SqlDataProvider;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;

@CommandName("self-sql")
public class SelfSqlImportCommand extends BaseImportCommandNode
{
    private final TemporaryWhiteList plugin;
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;

    public SelfSqlImportCommand(TemporaryWhiteList commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        plugin = commonApi;
        commandResults = commonApi.getMessages().CommandResults.Import;
        database = commonApi.getDatabase();
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(database.getProvider() instanceof SqlDataProvider, commandResults.ImportFromSelf, sender)) return true;
        if (sendMessageIf(args.length != 0, commandResults.SelfSqlUsage, sender)) return true;

        IDataExporter dataExporter = plugin.loadSqlData(plugin.getConfiguration());
        exportAsyncAndHandle(dataExporter, database, commandResults, sender);
        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
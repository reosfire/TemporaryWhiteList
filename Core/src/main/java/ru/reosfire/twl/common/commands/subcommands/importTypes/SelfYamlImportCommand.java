package ru.reosfire.twl.common.commands.subcommands.importTypes;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.data.providers.YamlDataProvider;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

import java.io.File;

@CommandName("self-yaml")
public class SelfYamlImportCommand extends BaseImportCommandNode
{
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;
    private final File dbFile;

    public SelfYamlImportCommand(CommonTwlApi commonApi, File dbFile)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Import;
        database = commonApi.getDatabase();
        this.dbFile = dbFile;
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(database.getProvider() instanceof YamlDataProvider, commandResults.ImportFromSelf, sender)) return true;
        if (sendMessageIf(args.length != 0, commandResults.SelfYamlUsage, sender)) return true;

        IDataExporter dataExporter = loadYamlData();
        exportAsyncAndHandle(dataExporter, database, commandResults, sender);
        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }

    public YamlDataProvider loadYamlData()
    {
        try
        {
            return new YamlDataProvider(dbFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading yaml database!");
        }
    }
}
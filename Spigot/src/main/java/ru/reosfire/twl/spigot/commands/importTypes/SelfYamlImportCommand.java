package ru.reosfire.twl.spigot.commands.importTypes;

import ru.reosfire.twl.common.commands.subcommands.importTypes.BaseImportCommandNode;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.data.providers.YamlDataProvider;

@CommandName("self-yaml")
public class SelfYamlImportCommand extends BaseImportCommandNode
{
    private final TemporaryWhiteList plugin;
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;

    public SelfYamlImportCommand(TemporaryWhiteList commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        plugin = commonApi;
        commandResults = commonApi.getMessages().CommandResults.Import;
        database = commonApi.getDatabase();
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(database.getProvider() instanceof YamlDataProvider, commandResults.ImportFromSelf, sender)) return true;
        if (sendMessageIf(args.length != 0, commandResults.SelfYamlUsage, sender)) return true;

        IDataExporter dataExporter = plugin.loadYamlData(plugin.getConfiguration());
        exportAsyncAndHandle(dataExporter, database, commandResults, sender);
        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
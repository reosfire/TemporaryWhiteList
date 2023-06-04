package ru.reosfire.twl.commands.subcommands.importTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.TemporaryWhiteList;
import ru.reosfire.twl.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.data.PlayerDatabase;
import ru.reosfire.twl.data.exporters.IDataExporter;
import ru.reosfire.twl.data.providers.YamlDataProvider;
import ru.reosfire.twl.lib.commands.CommandName;

@CommandName("self-yaml")
public class SelfYamlImportCommand extends BaseImportCommandNode
{
    private final TemporaryWhiteList plugin;
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;

    public SelfYamlImportCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        plugin = pluginInstance;
        commandResults = pluginInstance.getMessages().CommandResults.Import;
        database = pluginInstance.getDatabase();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (sendMessageIf(database.getProvider() instanceof YamlDataProvider, commandResults.ImportFromSelf, sender)) return true;
        if (sendMessageIf(args.length != 0, commandResults.SelfYamlUsage, sender)) return true;

        IDataExporter dataExporter = plugin.loadYamlData(plugin.getConfiguration());
        exportAsyncAndHandle(dataExporter, database, commandResults, sender);
        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
package ru.reosfire.twl.spigot.commands.subcommands.importTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.data.providers.SqlDataProvider;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandName;

@CommandName("self-sql")
public class SelfSqlImportCommand extends BaseImportCommandNode
{
    private final TemporaryWhiteList plugin;
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;

    public SelfSqlImportCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        plugin = pluginInstance;
        commandResults = pluginInstance.getMessages().CommandResults.Import;
        database = pluginInstance.getDatabase();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (sendMessageIf(database.getProvider() instanceof SqlDataProvider, commandResults.ImportFromSelf, sender)) return true;
        if (sendMessageIf(args.length != 0, commandResults.SelfSqlUsage, sender)) return true;

        IDataExporter dataExporter = plugin.loadSqlData(plugin.getConfiguration());
        exportAsyncAndHandle(dataExporter, database, commandResults, sender);
        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
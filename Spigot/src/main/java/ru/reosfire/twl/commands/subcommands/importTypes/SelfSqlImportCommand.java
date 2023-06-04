package ru.reosfire.twl.commands.subcommands.importTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.TemporaryWhiteList;
import ru.reosfire.twl.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.data.PlayerDatabase;
import ru.reosfire.twl.data.exporters.IDataExporter;
import ru.reosfire.twl.data.providers.SqlDataProvider;
import ru.reosfire.twl.lib.commands.CommandName;
import ru.reosfire.twl.lib.commands.CommandNode;

@CommandName("self-sql")
public class SelfSqlImportCommand extends CommandNode
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
        dataExporter.exportAsyncAndHandle(database, commandResults, sender);
        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
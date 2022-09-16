package ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Data.Providers.SqlDataProvider;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

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
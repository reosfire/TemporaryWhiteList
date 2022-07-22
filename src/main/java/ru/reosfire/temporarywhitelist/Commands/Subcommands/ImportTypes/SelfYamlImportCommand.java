package ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Data.YamlDataProvider;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("self-yaml")
public class SelfYamlImportCommand extends CommandNode
{
    private final TemporaryWhiteList _plugin;
    private final ImportCommandResultConfig _commandResults;
    private final PlayerDatabase _database;

    public SelfYamlImportCommand(TemporaryWhiteList plugin, PlayerDatabase database)
    {
        super(plugin.getMessages().NoPermission);
        _plugin = plugin;
        _commandResults = plugin.getMessages().CommandResults.Import;
        _database = database;
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (SendMessageIf(_database.getProvider() instanceof YamlDataProvider, _commandResults.ImportFromSelf, sender)) return true;
        if (SendMessageIf(args.length != 0, _commandResults.SelfYamlUsage, sender)) return true;

        IDataExporter dataExporter = _plugin.LoadYamlData(_plugin.getConfiguration());
        dataExporter.ExportAsyncAndHandle(_database, _commandResults, sender);
        _commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
package ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

@CommandName("self-yaml")
public class SelfYamlImportCommand extends CommandNode
{
    private final TemporaryWhiteList _plugin;
    private final ImportCommandResultConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;

    public SelfYamlImportCommand(TemporaryWhiteList plugin, PlayerDatabase database, TimeConverter timeConverter)
    {
        super(plugin.getMessages().NoPermission);
        _plugin = plugin;
        _commandResults = plugin.getMessages().CommandResults.Import;
        _database = database;
        _timeConverter = timeConverter;
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (args.length != 0)
        {
            _commandResults.MinecraftDefaultUsage.Send(sender);
            return true;
        }


        IDataExporter dataExporter = _plugin.LoadYamlData(_plugin.getConfiguration());
        dataExporter.ExportToAsync(_database).handle((res, ex) ->
        {
            if (ex == null) _commandResults.Success.Send(sender, res.getReplacements());
            else
            {
                _commandResults.Error.Send(sender);
                ex.printStackTrace();
            }
            return null;
        });
        return true;
    }
}
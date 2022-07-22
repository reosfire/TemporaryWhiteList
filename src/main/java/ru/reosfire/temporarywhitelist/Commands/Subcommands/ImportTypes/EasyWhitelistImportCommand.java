package ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.EasyWhitelist;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

import javax.management.ReflectionException;
import java.util.concurrent.atomic.AtomicReference;

@CommandName("easy-whitelist")
public class EasyWhitelistImportCommand extends CommandNode
{
    private final ImportCommandResultConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;

    public EasyWhitelistImportCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        _commandResults = pluginInstance.getMessages().CommandResults.Import;
        _database = pluginInstance.getDatabase();
        _timeConverter = pluginInstance.getTimeConverter();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (SendMessageIf(args.length != 2, _commandResults.EasyWhiteListUsage, sender)) return true;

        AtomicReference<Long> defaultTime = new AtomicReference<>();
        if (!TryParse(_timeConverter::ParseTime, args[0], defaultTime))
        {
            _commandResults.IncorrectTime.Send(sender);
            return true;
        }

        AtomicReference<Boolean> defaultPermanent = new AtomicReference<>();
        if (!TryParse(Boolean::parseBoolean, args[1], defaultPermanent))
        {
            _commandResults.IncorrectPermanent.Send(sender);
            return true;
        }

        try
        {
            IDataExporter dataExporter = new EasyWhitelist(defaultTime.get(), defaultPermanent.get());
            dataExporter.ExportAsyncAndHandle(_database, _commandResults, sender);
            _commandResults.SuccessfullyStarted.Send(sender);
        }
        catch (ReflectionException e)
        {
            _commandResults.EasyWhiteListPluginNotFound.Send(sender);
            e.printStackTrace();
        }
        return true;
    }
}
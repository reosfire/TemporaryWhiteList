package ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.Exporters.MinecraftDefaultWhitelist;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.util.concurrent.atomic.AtomicReference;

@CommandName("minecraft")
public class MinecraftDefaultImportCommand extends CommandNode
{
    private final ImportCommandResultConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;

    public MinecraftDefaultImportCommand(MessagesConfig messagesConfig, PlayerDatabase database, TimeConverter timeConverter)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Import;
        _database = database;
        _timeConverter = timeConverter;
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (SendMessageIf(args.length != 2, _commandResults.MinecraftDefaultUsage, sender)) return true;

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

        IDataExporter dataExporter = new MinecraftDefaultWhitelist(defaultTime.get(), defaultPermanent.get());
        dataExporter.ExportAsyncAndHandle(_database, _commandResults, sender);

        _commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
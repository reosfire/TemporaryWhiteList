package ru.reosfire.twl.commands.subcommands.importTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.TemporaryWhiteList;
import ru.reosfire.twl.TimeConverter;
import ru.reosfire.twl.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.data.PlayerDatabase;
import ru.reosfire.twl.data.exporters.IDataExporter;
import ru.reosfire.twl.data.exporters.MinecraftDefaultWhitelist;
import ru.reosfire.twl.lib.commands.CommandName;
import ru.reosfire.twl.lib.commands.CommandNode;

import java.util.concurrent.atomic.AtomicReference;

@CommandName("minecraft")
public class MinecraftDefaultImportCommand extends CommandNode
{
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;
    private final TimeConverter timeConverter;

    public MinecraftDefaultImportCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Import;
        database = pluginInstance.getDatabase();
        timeConverter = pluginInstance.getTimeConverter();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (sendMessageIf(args.length != 2, commandResults.MinecraftDefaultUsage, sender)) return true;

        AtomicReference<Long> defaultTime = new AtomicReference<>();
        if (!tryParse(timeConverter::parseTime, args[0], defaultTime))
        {
            commandResults.IncorrectTime.Send(sender);
            return true;
        }

        AtomicReference<Boolean> defaultPermanent = new AtomicReference<>();
        if (!tryParse(Boolean::parseBoolean, args[1], defaultPermanent))
        {
            commandResults.IncorrectPermanent.Send(sender);
            return true;
        }

        IDataExporter dataExporter = new MinecraftDefaultWhitelist(defaultTime.get(), defaultPermanent.get());
        dataExporter.exportAsyncAndHandle(database, commandResults, sender);

        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
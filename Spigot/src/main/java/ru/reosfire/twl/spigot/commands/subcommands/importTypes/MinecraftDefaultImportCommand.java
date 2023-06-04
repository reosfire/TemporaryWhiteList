package ru.reosfire.twl.spigot.commands.subcommands.importTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.TimeConverter;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.spigot.data.exporters.MinecraftDefaultWhitelist;
import ru.reosfire.twl.spigot.lib.commands.CommandName;

import java.util.concurrent.atomic.AtomicReference;

@CommandName("minecraft")
public class MinecraftDefaultImportCommand extends BaseImportCommandNode
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
        exportAsyncAndHandle(dataExporter, database, commandResults, sender);

        commandResults.SuccessfullyStarted.Send(sender);
        return true;
    }
}
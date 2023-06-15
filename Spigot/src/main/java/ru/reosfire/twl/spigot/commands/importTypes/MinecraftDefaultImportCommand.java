package ru.reosfire.twl.spigot.commands.importTypes;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.TimeConverter;
import ru.reosfire.twl.common.commands.subcommands.importTypes.BaseImportCommandNode;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.spigot.data.exporters.MinecraftDefaultWhitelist;

import java.util.concurrent.atomic.AtomicReference;

@CommandName("minecraft")
public class MinecraftDefaultImportCommand extends BaseImportCommandNode
{
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;
    private final TimeConverter timeConverter;

    public MinecraftDefaultImportCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Import;
        database = commonApi.getDatabase();
        timeConverter = commonApi.getTimeConverter();
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
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
package ru.reosfire.twl.common.commands.subcommands.importTypes;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.TimeConverter;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.data.exporters.EasyWhitelist;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

import javax.management.ReflectionException;
import java.util.concurrent.atomic.AtomicReference;

@CommandName("easy-whitelist")
public class EasyWhitelistImportCommand extends BaseImportCommandNode
{
    private final ImportCommandResultConfig commandResults;
    private final PlayerDatabase database;
    private final TimeConverter timeConverter;

    public EasyWhitelistImportCommand(CommonTwlApi commonApi)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        this.commandResults = commonApi.getMessages().CommandResults.Import;
        this.database = commonApi.getDatabase();
        this.timeConverter = commonApi.getTimeConverter();
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(args.length != 2, commandResults.EasyWhiteListUsage, sender)) return true;

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

        try
        {
            IDataExporter dataExporter = new EasyWhitelist(defaultTime.get(), defaultPermanent.get());
            exportAsyncAndHandle(dataExporter, database, commandResults, sender);
            commandResults.SuccessfullyStarted.Send(sender);
        }
        catch (ReflectionException e)
        {
            commandResults.EasyWhiteListPluginNotFound.Send(sender);
            e.printStackTrace();
        }
        return true;
    }
}
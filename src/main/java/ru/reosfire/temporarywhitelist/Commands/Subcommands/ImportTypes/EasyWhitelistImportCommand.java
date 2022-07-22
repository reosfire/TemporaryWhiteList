package ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.EasyWhitelist;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.Exporters.MinecraftDefaultWhitelist;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.TimeConverter;

import javax.management.ReflectionException;

@CommandName("easy-whitelist")
public class EasyWhitelistImportCommand extends CommandNode
{
    private final ImportCommandResultConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;

    public EasyWhitelistImportCommand(MessagesConfig messagesConfig, PlayerDatabase database, TimeConverter timeConverter)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Import;
        _database = database;
        _timeConverter = timeConverter;
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        if (args.length != 2)
        {
            _commandResults.MinecraftDefaultUsage.Send(sender);
            return true;
        }

        long defaultTime;
        try
        {
            defaultTime = _timeConverter.ParseTime(args[0]);
        }
        catch (Exception e)
        {
            _commandResults.IncorrectTime.Send(sender);
            return true;
        }

        boolean defaultPermanent;
        try
        {
            defaultPermanent = Boolean.parseBoolean(args[1]);
        }
        catch (Exception e)
        {
            _commandResults.IncorrectPermanent.Send(sender);
            return true;
        }

        try
        {
            IDataExporter dataExporter = new EasyWhitelist(defaultTime, defaultPermanent);
            dataExporter.ExportAsyncAndHandle(_database, _commandResults, sender);
        }
        catch (ReflectionException e)
        {

            e.printStackTrace();
        }
        return true;
    }
}
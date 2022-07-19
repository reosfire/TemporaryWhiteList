package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.AddCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;
import ru.reosfire.temporarywhitelist.Data.Exporters.MinecraftDefaultWhitelist;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TimeConverter;

@CommandName("import")
@CommandPermission("TemporaryWhitelist.Administrate.Import")
public class ImportCommand extends CommandNode
{
    private final AddCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;

    public ImportCommand(MessagesConfig messagesConfig, PlayerDatabase database, TimeConverter timeConverter)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Add;
        _database = database;
        _timeConverter = timeConverter;
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        IDataExporter dataExporter = new MinecraftDefaultWhitelist(0, true);

        for (PlayerData playerData : dataExporter.GetAll())
        {
            _database.Update(playerData);
        }

        return false;
    }
}
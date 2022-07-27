package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.RemoveCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.*;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

import java.util.ArrayList;
import java.util.stream.Collectors;

@CommandName("remove")
@CommandPermission("TemporaryWhitelist.Administrate.Remove")
@ExecuteAsync
public class RemoveCommand extends CommandNode
{
    private final RemoveCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final boolean _forceSync;

    public RemoveCommand(TemporaryWhiteList pluginInstance, boolean forceSync)
    {
        super(pluginInstance.getMessages().NoPermission);
        _commandResults = pluginInstance.getMessages().CommandResults.Remove;
        _database = pluginInstance.getDatabase();
        _forceSync = forceSync;
    }
    public RemoveCommand(TemporaryWhiteList pluginInstance)
    {
        this(pluginInstance, false);
    }


    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (SendMessageIf(args.length != 1, _commandResults.Usage, sender)) return true;

        Replacement playerReplacement = new Replacement("{player}", args[0]);

        if (_forceSync)
        {
            try
            {
                boolean changed = _database.Remove(args[0]).join();
                if (!changed) _commandResults.NothingChanged.Send(sender, playerReplacement);
                else _commandResults.Success.Send(sender, playerReplacement);
            }
            catch (Exception e)
            {
                _commandResults.Error.Send(sender, playerReplacement);
                e.printStackTrace();
            }
        }
        else
        {
            _database.Remove(args[0]).whenComplete((changed, exception) ->
            {
                if (!changed) _commandResults.NothingChanged.Send(sender, playerReplacement);
                else if (exception == null) _commandResults.Success.Send(sender, playerReplacement);
                else
                {
                    _commandResults.Error.Send(sender, playerReplacement);
                    exception.printStackTrace();
                }
            });
        }
        return true;
    }

    @Override
    public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if (args.length == 1)
            return _database.AllList().stream().map(e -> e.Name).filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
        return super.onTabComplete(sender, command, alias, args);
    }

    @Override
    public boolean isAsync()
    {
        if (_forceSync) return false;
        return super.isAsync();
    }
}
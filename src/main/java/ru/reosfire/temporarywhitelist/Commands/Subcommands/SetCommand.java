package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.SetCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.Lib.Commands.ExecuteAsync;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.util.ArrayList;
import java.util.Collections;

@CommandName("set")
@CommandPermission("TemporaryWhitelist.Administrate.Set")
@ExecuteAsync
public class SetCommand extends CommandNode
{
    private final SetCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;
    private final boolean _forceSync;

    public SetCommand(TemporaryWhiteList pluginInstance, boolean forceSync)
    {
        super(pluginInstance.getMessages().NoPermission);
        _commandResults = pluginInstance.getMessages().CommandResults.Set;
        _database = pluginInstance.getDatabase();
        _timeConverter = pluginInstance.getTimeConverter();
        _forceSync = forceSync;
    }
    public SetCommand(TemporaryWhiteList pluginInstance)
    {
        this(pluginInstance, false);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (SendMessageIf(args.length != 2, _commandResults.Usage, sender)) return true;

        Replacement playerReplacement = new Replacement("{player}", args[0]);
        Replacement timeReplacement = new Replacement("{time}", args[1]);

        if (args[1].equals("permanent"))
        {
            _database.SetPermanent(args[0]).whenComplete((changed, exception) ->
                    HandleCompletion(changed, exception, sender,playerReplacement, timeReplacement));
        }
        else
        {
            long time;
            try
            {
                time = _timeConverter.ParseTime(args[1]);
            }
            catch (Exception e)
            {
                _commandResults.IncorrectTime.Send(sender);
                return true;
            }

            if (_forceSync)
            {
                try
                {
                    Boolean changed = _database.Set(args[0], time).join();
                    if (!changed) _commandResults.NothingChanged.Send(sender, playerReplacement, timeReplacement);
                    else _commandResults.Success.Send(sender, playerReplacement, timeReplacement);
                }
                catch (Exception e)
                {
                    _commandResults.Error.Send(sender, playerReplacement, timeReplacement);
                    e.printStackTrace();
                }
            }
            else
            {
                _database.Set(args[0], time).whenComplete((changed, exception) ->
                        HandleCompletion(changed, exception, sender, playerReplacement, timeReplacement));
            }
        }
        return true;
    }

    private void HandleCompletion(boolean changed, Throwable exception, CommandSender sender, Replacement... replacements)
    {
        if (!changed)
            _commandResults.NothingChanged.Send(sender, replacements);
        else if (exception == null)
            _commandResults.Success.Send(sender, replacements);
        else
        {
            _commandResults.Error.Send(sender, replacements);
            exception.printStackTrace();
        }
    }

    @Override
    public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if (args.length == 1)
        {
            ArrayList<String> result = new ArrayList<>();

            for (PlayerData playerData : _database.AllList())
            {
                if (playerData.Name.startsWith(args[0])) result.add(playerData.Name);
            }

            return result;
        }
        else if (args.length == 2 && "permanent".startsWith(args[1])) return Collections.singletonList("permanent");
        return super.onTabComplete(sender, command, alias, args);
    }

    @Override
    public boolean isAsync()
    {
        if (_forceSync) return false;
        return super.isAsync();
    }
}
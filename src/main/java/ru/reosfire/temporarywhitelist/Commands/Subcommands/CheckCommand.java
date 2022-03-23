package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.CheckCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.Lib.Commands.ExecuteAsync;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.util.ArrayList;

@CommandName("check")
@CommandPermission("TemporaryWhitelist.CheckSelf")
@ExecuteAsync
public class CheckCommand extends CommandNode
{
    private final CheckCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;

    public CheckCommand(MessagesConfig messagesConfig, PlayerDatabase database, TimeConverter timeConverter)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Check;
        _database = database;
        _timeConverter = timeConverter;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            if (sender instanceof Player) SendInfo(sender, sender.getName());
            else _commandResults.ForPlayerOnly.Send(sender);
        }
        else if (args.length == 1)
        {
            if (!sender.hasPermission("TemporaryWhitelist.Administrate.CheckOther")) noPermissionAction(sender);
            else SendInfo(sender, args[0]);
        }
        else _commandResults.Usage.Send(sender);
        return true;
    }

    private void SendInfo(CommandSender to, String about)
    {
        PlayerData playerData = _database.getPlayerData(about);
        if (playerData == null)
        {
            _commandResults.InfoNotFound.Send(to);
            return;
        }

        Replacement[] replacements = new Replacement[]
                {
                        new Replacement("{player}", about),
                        new Replacement("{time_left}", _timeConverter.DurationToString(Math.max(playerData.TimeLeft(), 0))),
                        new Replacement("{started}", _timeConverter.DateTimeToString(playerData.StartTime)),
                        new Replacement("{will_end}", _timeConverter.DateTimeToString(playerData.EndTime())),
                        new Replacement("{permanent}", playerData.Permanent ?
                                _commandResults.PermanentTrue : _commandResults.PermanentFalse),
                };

        _commandResults.Format.Send(to, replacements);
    }

    @Override
    public java.util.List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (!sender.hasPermission("TemporaryWhitelist.Administrate.CheckOther"))
            return super.onTabComplete(sender, command, alias, args);
        if (args.length == 1)
        {
            ArrayList<String> result = new ArrayList<>();

            for (PlayerData playerData : _database.AllList())
            {
                if (playerData.Name.startsWith(args[0])) result.add(playerData.Name);
            }

            return result;
        }
        return super.onTabComplete(sender, command, alias, args);
    }
}
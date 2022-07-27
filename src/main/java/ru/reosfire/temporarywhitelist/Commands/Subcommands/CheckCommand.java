package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.CheckCommandResultsConfig;
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
import java.util.stream.Collectors;

@CommandName("check")
@CommandPermission("TemporaryWhitelist.CheckSelf")
@ExecuteAsync
public class CheckCommand extends CommandNode
{
    private final CheckCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;
    private final boolean _forceSync;

    public CheckCommand(TemporaryWhiteList pluginInstance, boolean forceSync)
    {
        super(pluginInstance.getMessages().NoPermission);
        _commandResults = pluginInstance.getMessages().CommandResults.Check;
        _database = pluginInstance.getDatabase();
        _timeConverter = pluginInstance.getTimeConverter();
        _forceSync = forceSync;
    }
    public CheckCommand(TemporaryWhiteList pluginInstance)
    {
        this(pluginInstance, false);
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
    public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if (!sender.hasPermission("TemporaryWhitelist.Administrate.CheckOther"))
            return super.onTabComplete(sender, command, alias, args);
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
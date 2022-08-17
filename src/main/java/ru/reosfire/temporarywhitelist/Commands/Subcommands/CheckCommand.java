package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.CheckCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.Lib.Commands.ExecuteAsync;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.util.List;
import java.util.stream.Collectors;

@CommandName("check")
@CommandPermission("TemporaryWhitelist.CheckSelf")
@ExecuteAsync
public class CheckCommand extends CommandNode
{
    private final CheckCommandResultsConfig commandResults;
    private final PlayerDatabase database;
    private final TimeConverter timeconverter;
    private final boolean forceSync;

    public CheckCommand(TemporaryWhiteList pluginInstance, boolean forceSync)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Check;
        database = pluginInstance.getDatabase();
        timeconverter = pluginInstance.getTimeConverter();
        this.forceSync = forceSync;
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
            if (sender instanceof Player) sendInfo(sender, sender.getName());
            else commandResults.ForPlayerOnly.Send(sender);
        }
        else if (args.length == 1)
        {
            if (!sender.hasPermission("TemporaryWhitelist.Administrate.CheckOther")) noPermissionAction(sender);
            else sendInfo(sender, args[0]);
        }
        else commandResults.Usage.Send(sender);
        return true;
    }

    private void sendInfo(CommandSender to, String about)
    {
        PlayerData playerData = database.getPlayerData(about);
        if (playerData == null)
        {
            commandResults.InfoNotFound.Send(to);
            return;
        }

        Replacement[] replacements = new Replacement[]
                {
                        new Replacement("{player}", about),
                        new Replacement("{time_left}", timeconverter.durationToString(Math.max(playerData.timeLeft(), 0))),
                        new Replacement("{started}", timeconverter.dateTimeToString(playerData.StartTime)),
                        new Replacement("{will_end}", timeconverter.dateTimeToString(playerData.endTime())),
                        new Replacement("{permanent}", playerData.Permanent ?
                                commandResults.PermanentTrue : commandResults.PermanentFalse),
                };

        commandResults.Format.Send(to, replacements);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if (!sender.hasPermission("TemporaryWhitelist.Administrate.CheckOther"))
            return super.onTabComplete(sender, command, alias, args);
        if (args.length == 1)
            return database.allList().stream().map(e -> e.Name).filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
        return super.onTabComplete(sender, command, alias, args);
    }

    @Override
    public boolean isAsync()
    {
        if (forceSync) return false;
        return super.isAsync();
    }
}
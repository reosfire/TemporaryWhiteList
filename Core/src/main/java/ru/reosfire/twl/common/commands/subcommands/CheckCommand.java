package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.TimeConverter;
import ru.reosfire.twl.common.configuration.localization.commandResults.CheckCommandResultsConfig;
import ru.reosfire.twl.common.data.PlayerData;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.commands.*;
import ru.reosfire.twl.common.lib.text.Replacement;

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

    public CheckCommand(CommonTwlApi commonApi, boolean forceSync)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Check;
        database = commonApi.getDatabase();
        timeconverter = commonApi.getTimeConverter();
        this.forceSync = forceSync;
    }
    public CheckCommand(CommonTwlApi pluginInstance)
    {
        this(pluginInstance, false);
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            if (sender.isPlayer()) sendInfo(sender, sender.getName());
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

    private void sendInfo(TwlCommandSender to, String about)
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
    public List<String> onTabComplete(TwlCommandSender sender, String[] args)
    {
        if (!sender.hasPermission("TemporaryWhitelist.Administrate.CheckOther"))
            return super.onTabComplete(sender, args);
        if (args.length == 1)
            return database.allList().stream().map(e -> e.Name).filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
        return super.onTabComplete(sender, args);
    }

    @Override
    public boolean isAsync()
    {
        if (forceSync) return false;
        return super.isAsync();
    }
}
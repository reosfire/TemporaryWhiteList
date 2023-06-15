package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.TimeConverter;
import ru.reosfire.twl.common.configuration.localization.commandResults.AddCommandResultsConfig;
import ru.reosfire.twl.common.data.PlayerData;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.commands.*;
import ru.reosfire.twl.common.lib.text.Replacement;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@CommandName("add")
@CommandPermission("TemporaryWhitelist.Administrate.Add")
@ExecuteAsync
public class AddCommand extends CommandNode
{
    private final AddCommandResultsConfig commandResults;
    private final PlayerDatabase database;
    private final TimeConverter timeConverter;
    private final boolean forceSync;

    public AddCommand(CommonTwlApi commonApi, boolean forceSync)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Add;
        database = commonApi.getDatabase();
        timeConverter = commonApi.getTimeConverter();
        this.forceSync = forceSync;
    }
    public AddCommand(CommonTwlApi pluginInstance)
    {
        this(pluginInstance, false);
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(args.length != 2, commandResults.Usage, sender)) return true;

        Replacement playerReplacement = new Replacement("{player}", args[0]);
        Replacement timeReplacement = new Replacement("{time}", args[1]);

        PlayerData playerData = database.getPlayerData(args[0]);
        if (sendMessageIf(playerData != null && playerData.Permanent, commandResults.AlreadyPermanent, sender, playerReplacement))
            return true;

        if (args[1].equals("permanent"))
        {
            database.setPermanent(args[0]).whenComplete((changed, exception) ->
                    handleCompletion(sender, exception, playerReplacement, timeReplacement));
        }
        else
        {
            AtomicReference<Long> time = new AtomicReference<>();
            if (!tryParse(timeConverter::parseTime, args[1], time))
            {
                commandResults.IncorrectTime.Send(sender);
                return true;
            }
            if (forceSync)
            {
                try
                {
                    database.add(args[0], time.get()).join();
                    commandResults.Success.Send(sender, playerReplacement, timeReplacement);
                }
                catch (Exception e)
                {
                    commandResults.Error.Send(sender, playerReplacement, timeReplacement);
                    e.printStackTrace();
                }
            }
            else
            {
                database.add(args[0], time.get()).whenComplete((result, exception) ->
                        handleCompletion(sender, exception, playerReplacement, timeReplacement));
            }
        }
        return true;
    }

    private void handleCompletion(TwlCommandSender sender, Throwable exception, Replacement... replacements)
    {
        if (exception == null)
            commandResults.Success.Send(sender, replacements);
        else
        {
            commandResults.Error.Send(sender, replacements);
            exception.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(TwlCommandSender sender, String[] args)
    {
        if (args.length == 1)
            return database.allList().stream().map(e -> e.Name).filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
        else if (args.length == 2 && "permanent".startsWith(args[1])) return Collections.singletonList("permanent");

        return super.onTabComplete(sender, args);
    }

    @Override
    public boolean isAsync()
    {
        if (forceSync) return false;
        return super.isAsync();
    }
}
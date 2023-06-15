package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.TimeConverter;
import ru.reosfire.twl.common.configuration.localization.commandResults.SetCommandResultsConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.commands.*;
import ru.reosfire.twl.common.lib.text.Replacement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CommandName("set")
@CommandPermission("TemporaryWhitelist.Administrate.Set")
@ExecuteAsync
public class SetCommand extends CommandNode
{
    private final SetCommandResultsConfig commandResults;
    private final PlayerDatabase database;
    private final TimeConverter timeConverter;
    private final boolean forceSync;

    public SetCommand(CommonTwlApi commonApi, boolean forceSync)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Set;
        database = commonApi.getDatabase();
        timeConverter = commonApi.getTimeConverter();
        this.forceSync = forceSync;
    }
    public SetCommand(CommonTwlApi pluginInstance)
    {
        this(pluginInstance, false);
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(args.length != 2, commandResults.Usage, sender)) return true;

        Replacement playerReplacement = new Replacement("{player}", args[0]);
        Replacement timeReplacement = new Replacement("{time}", args[1]);

        if (args[1].equals("permanent"))
        {
            database.setPermanent(args[0]).whenComplete((changed, exception) ->
                    handleCompletion(changed, exception, sender,playerReplacement, timeReplacement));
        }
        else
        {
            long time;
            try
            {
                time = timeConverter.parseTime(args[1]);
            }
            catch (Exception e)
            {
                commandResults.IncorrectTime.Send(sender);
                return true;
            }

            if (forceSync)
            {
                try
                {
                    Boolean changed = database.set(args[0], time).join();
                    if (!changed) commandResults.NothingChanged.Send(sender, playerReplacement, timeReplacement);
                    else commandResults.Success.Send(sender, playerReplacement, timeReplacement);
                }
                catch (Exception e)
                {
                    commandResults.Error.Send(sender, playerReplacement, timeReplacement);
                    e.printStackTrace();
                }
            }
            else
            {
                database.set(args[0], time).whenComplete((changed, exception) ->
                        handleCompletion(changed, exception, sender, playerReplacement, timeReplacement));
            }
        }
        return true;
    }

    private void handleCompletion(boolean changed, Throwable exception, TwlCommandSender sender, Replacement... replacements)
    {
        if (!changed)
            commandResults.NothingChanged.Send(sender, replacements);
        else if (exception == null)
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
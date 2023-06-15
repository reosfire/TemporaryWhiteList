package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.CommonTwlApi;
import ru.reosfire.twl.common.configuration.localization.commandResults.RemoveCommandResultsConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.commands.*;
import ru.reosfire.twl.common.lib.text.Replacement;

import java.util.List;
import java.util.stream.Collectors;

@CommandName("remove")
@CommandPermission("TemporaryWhitelist.Administrate.Remove")
@ExecuteAsync
public class RemoveCommand extends CommandNode
{
    private final RemoveCommandResultsConfig commandResults;
    private final PlayerDatabase database;
    private final boolean forceSync;

    public RemoveCommand(CommonTwlApi commonApi, boolean forceSync)
    {
        super(commonApi.getMessages().NoPermission, commonApi.getMessages().UnexpectedError);

        commandResults = commonApi.getMessages().CommandResults.Remove;
        database = commonApi.getDatabase();
        this.forceSync = forceSync;
    }
    public RemoveCommand(CommonTwlApi pluginInstance)
    {
        this(pluginInstance, false);
    }


    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(args.length != 1, commandResults.Usage, sender)) return true;

        Replacement playerReplacement = new Replacement("{player}", args[0]);

        if (forceSync)
        {
            try
            {
                boolean changed = database.remove(args[0]).join();
                if (!changed) commandResults.NothingChanged.Send(sender, playerReplacement);
                else commandResults.Success.Send(sender, playerReplacement);
            }
            catch (Exception e)
            {
                commandResults.Error.Send(sender, playerReplacement);
                e.printStackTrace();
            }
        }
        else
        {
            database.remove(args[0]).whenComplete((changed, exception) ->
            {
                if (!changed) commandResults.NothingChanged.Send(sender, playerReplacement);
                else if (exception == null) commandResults.Success.Send(sender, playerReplacement);
                else
                {
                    commandResults.Error.Send(sender, playerReplacement);
                    exception.printStackTrace();
                }
            });
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(TwlCommandSender sender, String[] args)
    {
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
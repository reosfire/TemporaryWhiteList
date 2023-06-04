package ru.reosfire.twl.spigot.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.TimeConverter;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.SetCommandResultsConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;
import ru.reosfire.twl.spigot.lib.commands.ExecuteAsync;

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

    public SetCommand(TemporaryWhiteList pluginInstance, boolean forceSync)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Set;
        database = pluginInstance.getDatabase();
        timeConverter = pluginInstance.getTimeConverter();
        this.forceSync = forceSync;
    }
    public SetCommand(TemporaryWhiteList pluginInstance)
    {
        this(pluginInstance, false);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
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

    private void handleCompletion(boolean changed, Throwable exception, CommandSender sender, Replacement... replacements)
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if (args.length == 1)
            return database.allList().stream().map(e -> e.Name).filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
        else if (args.length == 2 && "permanent".startsWith(args[1])) return Collections.singletonList("permanent");

        return super.onTabComplete(sender, command, alias, args);
    }

    @Override
    public boolean isAsync()
    {
        if (forceSync) return false;
        return super.isAsync();
    }
}
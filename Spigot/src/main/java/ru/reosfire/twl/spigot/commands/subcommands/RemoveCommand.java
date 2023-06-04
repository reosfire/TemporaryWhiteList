package ru.reosfire.twl.spigot.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.RemoveCommandResultsConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;
import ru.reosfire.twl.spigot.lib.commands.ExecuteAsync;

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

    public RemoveCommand(TemporaryWhiteList pluginInstance, boolean forceSync)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Remove;
        database = pluginInstance.getDatabase();
        this.forceSync = forceSync;
    }
    public RemoveCommand(TemporaryWhiteList pluginInstance)
    {
        this(pluginInstance, false);
    }


    @Override
    public boolean execute(CommandSender sender, String[] args)
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
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
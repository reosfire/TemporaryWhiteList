package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.RemoveCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.*;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;

import java.util.ArrayList;

@CommandName("remove")
@CommandPermission("TemporaryWhitelist.Administrate.Remove")
@ExecuteAsync
public class RemoveCommand extends CommandNode
{
    private final RemoveCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;

    public RemoveCommand(MessagesConfig messagesConfig, PlayerDatabase database)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.Remove;
        _database = database;
    }


    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (SendMessageIf(args.length != 1, _commandResults.Usage, sender)) return true;

        _database.Remove(args[0]).whenComplete((changed, exception) ->
        {
            Replacement playerReplacement = new Replacement("{player}", args[0]);
            if (!changed) _commandResults.NothingChanged.Send(sender, playerReplacement);
            else if (exception == null) _commandResults.Success.Send(sender, playerReplacement);
            else
            {
                _commandResults.Error.Send(sender, playerReplacement);
                exception.printStackTrace();
            }
        });
        return true;
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
        return super.onTabComplete(sender, command, alias, args);
    }
}
package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ListCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.Lib.Commands.ExecuteAsync;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;

import java.util.Collection;

@CommandName("list")
@CommandPermission("TemporaryWhitelist.Administrate.List")
@ExecuteAsync
public class ListCommand extends CommandNode
{
    private final ListCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final int _pageSize;

    public ListCommand(MessagesConfig messagesConfig, PlayerDatabase database, int pageSize)
    {
        super(messagesConfig.NoPermission);
        _commandResults = messagesConfig.CommandResults.List;
        _database = database;
        _pageSize = pageSize;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 1) _commandResults.Usage.Send(sender);
        int page = 1;
        if (args.length == 1)
        {
            try
            {
                page = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                _commandResults.IncorrectPage.Send(sender);
                return true;
            }
        }

        Collection<PlayerData> players = _database.AllList();
        int totalPages = CeilDivide(players.size(), _pageSize);

        if (page < 1 || page > totalPages)
        {
            _commandResults.IncorrectPage.Send(sender);
            return true;
        }

        _commandResults.Header.Send(sender);

        int sent = 0;
        int i = -1;
        for (PlayerData playerData : players)
        {
            i++;
            if (i < _pageSize * (page - 1)) continue;
            if (i >= _pageSize * page) break;
            _commandResults.PlayerFormat.Send(sender,
                    new Replacement("{player}", playerData.Name),
                    new Replacement("{number}", Integer.toString(i + 1)));
            sent++;
        }
        while (sent++ < _pageSize) sender.sendMessage("");

        int previousPage = Math.max(page - 1, 1);
        int nextPage = Math.min(page + 1, totalPages);

        _commandResults.PagesSwitch.Send(sender,
                new Replacement("{previous_page}", Integer.toString(previousPage)),
                new Replacement("{page}", Integer.toString(page)),
                new Replacement("{total_pages}", Integer.toString(totalPages)),
                new Replacement("{next_page}", Integer.toString(nextPage)));

        return true;
    }

    private int CeilDivide(int a, int b)
    {
        return (a + b - 1) / b;
    }
}
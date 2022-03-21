package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ListCommandResultsConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;

import java.util.List;

@CommandName("list")
@CommandPermission("TemporaryWhiteList.Administrate.List")
public class ListCommand extends CommandNode
{
    private final ListCommandResultsConfig _commandResults;
    private final PlayerDatabase _database;
    private final int _pageSize;

    public ListCommand(ListCommandResultsConfig commandResults, PlayerDatabase database, int pageSize)
    {
        _commandResults = commandResults;
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

        if (page < 1)
        {
            _commandResults.IncorrectPage.Send(sender);
            return true;
        }

        _commandResults.Header.Send(sender);


        List<PlayerData> players = _database.AllList();

        int sent = 0;
        for (int i = _pageSize * (page - 1); i < players.size() && i < _pageSize * page; i++)
        {
            PlayerData playerData = players.get(i);
            _commandResults.PlayerFormat.Send(sender,
                    new Replacement("{player}", playerData.Name),
                    new Replacement("{number}", Integer.toString(i + 1)));
            sent++;
        }
        while (sent++ < _pageSize) sender.sendMessage("");

        int previousPage = Math.max(1, page - 1);
        int nextPage = Math.min(page + 1, players.size() / _pageSize + (players.size() % _pageSize == 0 ? 0 : 1));

        _commandResults.PagesSwitch.Send(sender,
                new Replacement("{previous_page}", Integer.toString(previousPage)),
                new Replacement("{page}", Integer.toString(page)),
                new Replacement("{next_page}", Integer.toString(nextPage)));

        return true;
    }
}
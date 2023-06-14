package ru.reosfire.twl.spigot.commands.subcommands;

import ru.reosfire.twl.common.configuration.localization.commandResults.ListCommandResultsConfig;
import ru.reosfire.twl.common.data.PlayerData;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.lib.commands.CommandName;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;
import ru.reosfire.twl.spigot.lib.commands.CommandPermission;
import ru.reosfire.twl.spigot.lib.commands.ExecuteAsync;

import java.util.List;

@CommandName("list")
@CommandPermission("TemporaryWhitelist.Administrate.List")
@ExecuteAsync
public class ListCommand extends CommandNode
{
    private final ListCommandResultsConfig commandResults;
    private final PlayerDatabase database;
    private final int pageSize;

    public ListCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.List;
        database = pluginInstance.getDatabase();
        pageSize = pluginInstance.getConfiguration().ListPageSize;
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        if (sendMessageIf(args.length > 1, commandResults.Usage, sender)) return true;
        int page = 1;
        if (args.length == 1)
        {
            try
            {
                page = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                commandResults.IncorrectPage.Send(sender);
                return true;
            }
        }

        List<PlayerData> players = database.activeList();
        if (players.size() == 0)
        {
            commandResults.ListIsEmpty.Send(sender);
            return true;
        }
        int totalPages = ceilDivide(players.size(), pageSize);

        if (page < 1 || page > totalPages)
        {
            commandResults.IncorrectPage.Send(sender);
            return true;
        }

        commandResults.Header.Send(sender);

        int pageEnd = pageSize * page;
        int pageStart = pageEnd - pageSize;
        for (int i = pageStart; i < pageEnd; i++)
        {
            if (i < players.size())
            {
                commandResults.PlayerFormat.Send(sender,
                        new Replacement("{player}", players.get(i).Name),
                        new Replacement("{number}", Integer.toString(i + 1)));
            }
            //else sender.sendMessage("");
        }

        int previousPage = Math.max(page - 1, 1);
        int nextPage = Math.min(page + 1, totalPages);

        commandResults.PagesSwitch.Send(sender,
                new Replacement("{previous_page}", Integer.toString(previousPage)),
                new Replacement("{page}", Integer.toString(page)),
                new Replacement("{total_pages}", Integer.toString(totalPages)),
                new Replacement("{next_page}", Integer.toString(nextPage)));

        return true;
    }

    private int ceilDivide(int a, int b)
    {
        return (a + b - 1) / b;
    }
}
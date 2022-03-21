package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.*;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    private final TemporaryWhiteList _pluginInstance;

    public TwlCommand(MessagesConfig messages, PlayerDatabase dataProvider, TemporaryWhiteList pluginInstance,
                      TimeConverter timeConverter, Config config)
    {
        _pluginInstance = pluginInstance;

        AddChildren(new AddCommand(messages.CommandResults.Add, dataProvider, timeConverter));
        AddChildren(new SetCommand(messages.CommandResults.Set, dataProvider, timeConverter));
        AddChildren(new RemoveCommand(messages.CommandResults.Remove, dataProvider));
        AddChildren(new CheckCommand(messages.CommandResults.Check, dataProvider, timeConverter));
        AddChildren(new ListCommand(messages.CommandResults.List, dataProvider, config.ListPageSize));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        return true;
    }

    @CommandName("enable")
    @CommandPermission("TemporaryWhiteList.Administrate.Enable")
    public class Enable extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                _pluginInstance.Enable();
                sender.sendMessage("enabled");
            }
            catch (Exception e)
            {
                sender.sendMessage("Error! Watch console");
                e.printStackTrace();
            }
            return true;
        }
    }

    @CommandName("disable")
    @CommandPermission("TemporaryWhiteList.Administrate.Disable")
    public class Disable extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                _pluginInstance.Disable();
                sender.sendMessage("disabled");
            }
            catch (Exception e)
            {
                sender.sendMessage("Error! Watch console");
                e.printStackTrace();
            }
            return true;
        }
    }

    @CommandName("reload")
    @CommandPermission("TemporaryWhiteList.Administrate.Reload")
    public class Reload extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            _pluginInstance.Load();
            sender.sendMessage("reloaded");
            return true;
        }
    }
}
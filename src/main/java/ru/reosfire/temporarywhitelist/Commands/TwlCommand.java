package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    private final PlayerDatabase _database;
    private final MessagesConfig _messages;
    private final TemporaryWhiteList _pluginInstance;
    private final TimeConverter _timeConverter;

    public TwlCommand(MessagesConfig messages, PlayerDatabase dataProvider, TemporaryWhiteList pluginInstance, TimeConverter timeConverter)
    {
        _database = dataProvider;
        _messages = messages;
        _pluginInstance = pluginInstance;
        _timeConverter = timeConverter;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        return true;
    }

    @CommandName("add")
    @CommandPermission("TemporaryWhiteList.Add")
    public class Add extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            if(args.length == 1)
            {
                if (_database.CanJoin(args[0]))
                {
                    sender.sendMessage("Already whitelisted. Nothing will changed. Maybe you want /twl permanent set");
                    return true;
                }
                _database.Add(args[0]).whenComplete((result, exception) ->
                {
                    if (exception == null) sender.sendMessage(args[0] + " success added to white list");
                    else
                    {
                        sender.sendMessage("Error while adding " + args[0] + " to whitelist. Watch console");
                        exception.printStackTrace();
                    }
                });
            }
            else if(args.length == 2)
            {
                _database.Add(args[0], _timeConverter.ParseTime(args[1])).whenComplete((result, exception) ->
                {
                    if (exception == null) sender.sendMessage(args[0] + " success added to white list for " + args[1]);
                    else
                    {
                        sender.sendMessage("Error while adding " + args[0] + " to whitelist. Watch console");
                        exception.printStackTrace();
                    }
                });
            }
            return true;
        }
    }

    @CommandName("remove")
    @CommandPermission("TemporaryWhiteList.Remove")
    public class Remove extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            _database.Remove(args[0]).whenComplete((result, exception) ->
            {
                if (exception == null) sender.sendMessage(args[0] + " success removed from white list");
                else
                {
                    sender.sendMessage("Error while removing " + args[0] + " from whitelist. Watch console");
                    exception.printStackTrace();
                }
            });
            return true;
        }
    }

    @CommandName("permanent")
    @CommandPermission("TemporaryWhiteList.Permanent")
    public class Permanent extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            return true;
        }

        @CommandName("set")
        @CommandPermission("TemporaryWhiteList.Permanent.Set")
        public class Set extends CommandNode
        {
            @Override
            public boolean execute(CommandSender sender, String[] args)
            {
                _database.SetPermanent(args[0], true).whenComplete((result, exception) ->
                {
                    if (exception == null) sender.sendMessage(args[0] + " is whitelisted permanent now");
                    else
                    {
                        sender.sendMessage("Error while making " + args[0] + " whitelisted permanent");
                        exception.printStackTrace();
                    }
                });
                return true;
            }

        }

        @CommandName("reset")
        @CommandPermission("TemporaryWhiteList.Permanent.Reset")
        public class Reset extends CommandNode
        {
            @Override
            public boolean execute(CommandSender sender, String[] args)
            {
                _database.SetPermanent(args[0], false).whenComplete((result, exception) ->
                {
                    if (exception == null) sender.sendMessage(args[0] + " is not whitelisted permanent now");
                    else
                    {
                        sender.sendMessage("Error while making " + args[0] + " not whitelisted permanent");
                        exception.printStackTrace();
                    }
                });
                return true;
            }
        }
    }

    @CommandName("check")
    @CommandPermission("TemporaryWhiteList.Check")
    public class Check extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                if (args.length == 0)
                {
                    if (sender instanceof Player)
                    {
                        Player playerSender = (Player)sender;

                        Replacement replacement = new Replacement("{status}", _database.Check(sender.getName()));
                        sender.sendMessage(Text.Colorize(playerSender, _messages.CheckMessageFormat, replacement));
                    }
                    else
                    {
                        sender.sendMessage("For players only");
                    }
                }
                else if (args.length == 1)
                {
                    if (!sender.hasPermission("WMWhiteList.Check.Other") && !sender.isOp())
                    {
                        noPermissionAction(sender);
                    }
                    else sender.sendMessage(Text.SetColors(_database.Check(args[0])));
                }
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
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

    @CommandName("list")
    @CommandPermission("TemporaryWhiteList.Administrate.List")
    public class List extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                StringBuilder result = new StringBuilder();

                for (PlayerData playerData : _database.ActiveList())
                {
                    result.append(playerData.Name).append(", ");
                }

                result.replace(result.length() - 2, result.length() - 1, "");
                sender.sendMessage(result.toString());
            }
            catch (Exception e)
            {
                return false;
            }
            return true;
        }
    }

    @CommandName("count")
    @CommandPermission("TemporaryWhiteList.Administrate.Count")
    public class Count extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                sender.sendMessage(Integer.toString(_database.ActiveList().size()));
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    }
}
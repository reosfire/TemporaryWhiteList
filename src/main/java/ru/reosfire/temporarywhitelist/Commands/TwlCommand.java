package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.Command;
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

import java.util.ArrayList;
import java.util.Collections;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    private final PlayerDatabase _database;
    private final MessagesConfig _messages;
    private final TemporaryWhiteList _pluginInstance;
    private final TimeConverter _timeConverter;

    public TwlCommand(MessagesConfig messages, PlayerDatabase dataProvider, TemporaryWhiteList pluginInstance,
                      TimeConverter timeConverter)
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
            if (args.length != 2)
            {
                _messages.CommandResults.Add.Usage.Send(sender);
                return true;
            }

            Replacement playerReplacement = new Replacement("{player}", args[0]);
            Replacement timeReplacement = new Replacement("{time}", args[1]);

            PlayerData playerData = _database.getPlayerData(args[0]);
            if (playerData != null && playerData.Permanent)
            {
                _messages.CommandResults.Add.AlreadyPermanent.Send(sender, playerReplacement);
                return true;
            }

            if (args[1].equals("permanent"))
            {
                _database.SetPermanent(args[0]).whenComplete((changed, exception) ->
                {
                    if (exception == null)
                        _messages.CommandResults.Add.Success.Send(sender, playerReplacement, timeReplacement);
                    else
                    {
                        _messages.CommandResults.Add.Error.Send(sender, playerReplacement, timeReplacement);
                        exception.printStackTrace();
                    }
                });
            }
            else
            {
                long time;
                try
                {
                    time = _timeConverter.ParseTime(args[1]);
                }
                catch (Exception e)
                {
                    _messages.CommandResults.Add.IncorrectTime.Send(sender);
                    return true;
                }
                _database.Add(args[0], time).whenComplete((result, exception) ->
                {
                    if (exception == null)
                        _messages.CommandResults.Add.Success.Send(sender, playerReplacement, timeReplacement);
                    else
                    {
                        _messages.CommandResults.Add.Error.Send(sender, playerReplacement, timeReplacement);
                        exception.printStackTrace();
                    }
                });
            }
            return true;
        }

        @Override
        public java.util.List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
        {
            if (args.length == 2 && "permanent".startsWith(args[1])) return Collections.singletonList("permanent");

            return super.onTabComplete(sender, command, alias, args);
        }
    }

    @CommandName("set")
    @CommandPermission("TemporaryWhiteList.Set")
    public class Set extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            if (args.length != 2)
            {
                _messages.CommandResults.Set.Usage.Send(sender);
                return true;
            }

            Replacement playerReplacement = new Replacement("{player}", args[0]);
            Replacement timeReplacement = new Replacement("{time}", args[1]);

            if (args[1].equals("permanent"))
            {
                _database.SetPermanent(args[0]).whenComplete((changed, exception) ->
                {
                    if (!changed)
                        _messages.CommandResults.Set.NothingChanged.Send(sender, playerReplacement, timeReplacement);
                    if (exception == null)
                        _messages.CommandResults.Set.Success.Send(sender, playerReplacement, timeReplacement);
                    else
                    {
                        _messages.CommandResults.Set.Error.Send(sender, playerReplacement, timeReplacement);
                        exception.printStackTrace();
                    }
                });
            }
            else
            {
                long time;
                try
                {
                    time = _timeConverter.ParseTime(args[1]);
                }
                catch (Exception e)
                {
                    _messages.CommandResults.Set.IncorrectTime.Send(sender);
                    return true;
                }

                _database.Set(args[0], time).whenComplete((changed, exception) ->
                {
                    if (!changed)
                        _messages.CommandResults.Set.NothingChanged.Send(sender, playerReplacement, timeReplacement);
                    else if (exception == null)
                        _messages.CommandResults.Set.Success.Send(sender, playerReplacement, timeReplacement);
                    else
                    {
                        _messages.CommandResults.Set.Error.Send(sender, playerReplacement, timeReplacement);
                        exception.printStackTrace();
                    }
                });
            }
            return true;
        }

        @Override
        public java.util.List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
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
            else if (args.length == 2 && "permanent".startsWith(args[1])) return Collections.singletonList("permanent");
            return super.onTabComplete(sender, command, alias, args);
        }
    }

    @CommandName("remove")
    @CommandPermission("TemporaryWhiteList.Remove")
    public class Remove extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            if (args.length != 1)
            {
                _messages.CommandResults.Remove.Usage.Send(sender);
                return true;
            }

            _database.Remove(args[0]).whenComplete((changed, exception) ->
            {
                Replacement playerReplacement = new Replacement("{player}", args[0]);
                if (!changed) _messages.CommandResults.Remove.NothingChanged.Send(sender, playerReplacement);
                else if (exception == null) _messages.CommandResults.Remove.Success.Send(sender, playerReplacement);
                else
                {
                    _messages.CommandResults.Remove.Error.Send(sender, playerReplacement);
                    exception.printStackTrace();
                }
            });
            return true;
        }

        @Override
        public java.util.List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
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

    @CommandName("check")
    @CommandPermission("TemporaryWhiteList.Check")
    public class Check extends CommandNode
    {
        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            if (args.length == 0)
            {
                if (sender instanceof Player) SendInfo(sender, sender.getName());
                else sender.sendMessage("For players only");
            }
            else if (args.length == 1)
            {
                if (!sender.hasPermission("WMWhiteList.Check.Other")) noPermissionAction(sender);
                else SendInfo(sender, args[0]);
            }
            return true;
        }

        private void SendInfo(CommandSender to, String about)
        {
            PlayerData playerData = _database.getPlayerData(about);
            if (playerData == null)
            {
                to.sendMessage("Info about player not found");
                return;
            }

            Replacement[] replacements = new Replacement[]
                    {
                            new Replacement("{player}", about),
                            new Replacement("{time_left}", _timeConverter.DurationToString(Math.max(playerData.TimeLeft(), 0))),
                            new Replacement("{started}", _timeConverter.DateTimeToString(playerData.StartTime)),
                            new Replacement("{will_end}", _timeConverter.DateTimeToString(playerData.EndTime())),
                            new Replacement("{permanent}", playerData.Permanent ?
                                    _messages.CheckStatuses.PermanentTrue : _messages.CheckStatuses.PermanentFalse),
                    };

            _messages.CommandResults.Check.Format.Send(to, replacements);
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
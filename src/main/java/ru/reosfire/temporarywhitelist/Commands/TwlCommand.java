package ru.reosfire.temporarywhitelist.Commands;

import com.sun.xml.internal.bind.v2.TODO;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
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
    private final IDataProvider DataProvider;
    private final MessagesConfig Messages;
    private final TemporaryWhiteList PluginInstance;

    public TwlCommand(MessagesConfig messages, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        DataProvider = dataProvider;
        Messages = messages;
        PluginInstance = pluginInstance;
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
            try
            {
                if(args.length == 1)
                {
                    DataProvider.Add(args[0]);
                    sender.sendMessage(args[0] + " success added to white list");
                    return true;
                }
                else if(args.length == 2)
                {
                    DataProvider.Add(args[0], TimeConverter.ParseTime(args[1]));
                    sender.sendMessage(args[0] + " success added to white list for " + args[1]);
                    return true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
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
            try
            {
                DataProvider.Remove(args[0]);
                sender.sendMessage(args[0] + " success removed from white list");
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
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
                try
                {
                    DataProvider.SetPermanent(args[0], true);
                    sender.sendMessage(args[0] + "'s subscribe set permanent");
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
            }

        }

        @CommandName("reset")
        @CommandPermission("TemporaryWhiteList.Permanent.Reset")
        public class Reset extends CommandNode
        {
            @Override
            public boolean execute(CommandSender sender, String[] args)
            {
                try
                {
                    DataProvider.SetPermanent(args[0], false);
                    sender.sendMessage(args[0] + "'s subscribe set permanent");
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
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

                        Replacement replacement = new Replacement("{status}", DataProvider.Check(sender.getName()));
                        sender.sendMessage(Text.Colorize(playerSender, Messages.CheckMessageFormat, replacement));
                    }
                    else
                    {
                        //TODO for players only
                    }
                }
                else if (args.length == 1)
                {
                    if (!sender.hasPermission("WMWhiteList.Check.Other") && !sender.isOp())
                    {
                        noPermissionAction(sender);
                    }
                    else sender.sendMessage(DataProvider.Check(args[0]));
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
                PluginInstance.Enable();
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
                PluginInstance.Disable();
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
            PluginInstance.ReloadConfigurations();
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
                sender.sendMessage(String.join(", ", DataProvider.ActiveList()));
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
                sender.sendMessage(Integer.toString(DataProvider.ActiveList().size()));
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    }
}
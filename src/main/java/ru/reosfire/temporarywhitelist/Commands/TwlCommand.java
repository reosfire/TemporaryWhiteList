package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

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
    public String getName()
    {
        return "twl";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        return true;
    }

    public class Add extends CommandNode
    {
        @Override
        public String getName()
        {
            return "add";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Add";
        }

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
    public class Remove extends CommandNode
    {
        @Override
        public String getName()
        {
            return "remove";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Remove";
        }

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
    public class Permanent extends CommandNode
    {
        @Override
        public String getName()
        {
            return "permanent";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Permanent";
        }

        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            return true;
        }
        public class Set extends CommandNode
        {
            @Override
            public String getName()
            {
                return "set";
            }

            @Override
            public String getPermission()
            {
                return "TemporaryWhiteList.Permanent.Set";
            }

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
        public class Reset extends CommandNode
        {
            @Override
            public String getName()
            {
                return "reset";
            }

            @Override
            public String getPermission()
            {
                return "TemporaryWhiteList.Permanent.Reset";
            }

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
    public class Check extends CommandNode
    {
        @Override
        public String getName()
        {
            return "check";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Check";
        }

        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                if (args.length == 0)
                {
                    if (sender instanceof Player)
                    {
                        Player playerSender = (Player) sender;
                        playerSender.sendMessage(Text.Colorize(playerSender, Messages.CheckMessageFormat));
                    }
                }
                else if (args.length == 1)
                {
                    if (!sender.hasPermission("WMWhiteList.Check.Other") && !(sender instanceof ConsoleCommandSender))
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
    public class Enable extends CommandNode
    {
        @Override
        public String getName()
        {
            return "enable";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Administrate.Enable";
        }

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
    public class Disable extends CommandNode
    {

        @Override
        public String getName()
        {
            return "disable";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Administrate.Disable";
        }

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
    public class Reload extends CommandNode
    {

        @Override
        public String getName()
        {
            return "reload";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Administrate.Reload";
        }

        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            PluginInstance.ReloadConfigurations();
            sender.sendMessage("reloaded");
            return true;
        }
    }
    public class List extends CommandNode
    {
        @Override
        public String getName()
        {
            return "list";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Administrate.List";
        }

        @Override
        public boolean execute(CommandSender sender, String[] args)
        {
            try
            {
                java.util.List<String> activePlayers  = DataProvider.ActiveList();
                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < activePlayers.size(); i++)
                {
                    buffer.append(activePlayers.get(i)).append(", ");
                    if (i % 5 == 4)
                    {
                        sender.sendMessage(buffer.toString());
                        buffer = new StringBuilder();
                    }
                }
                if (!buffer.toString().equals(""))
                {
                    sender.sendMessage(buffer.toString());
                }
            }
            catch (Exception e)
            {
                return false;
            }
            return true;
        }
    }
    public class Count extends CommandNode
    {
        @Override
        public String getName()
        {
            return "count";
        }

        @Override
        public String getPermission()
        {
            return "TemporaryWhiteList.Administrate.Count";
        }

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
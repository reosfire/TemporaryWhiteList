package ru.reosfire.temporarywhitelist.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

public class TwlCommand extends CommandNode
{
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
                    TemporaryWhiteList.getDataProvider().Add(args[0]);
                    sender.sendMessage(args[0] + " success added to white list");
                    return true;
                }
                else if(args.length == 2)
                {
                    TemporaryWhiteList.getDataProvider().Add(args[0], TimeConverter.ParseTime(args[1]));
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
                TemporaryWhiteList.getDataProvider().Remove(args[0]);
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
                    TemporaryWhiteList.getDataProvider().SetPermanent(args[0], true);
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
                    TemporaryWhiteList.getDataProvider().SetPermanent(args[0], false);
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
                        playerSender.sendMessage(Text.Colorize(playerSender, TemporaryWhiteList.getMessages().CheckMessageFormat));
                    }
                }
                else if (args.length == 1)
                {
                    if (!sender.hasPermission("WMWhiteList.Check.Other") && !(sender instanceof ConsoleCommandSender))
                    {
                        noPermissionAction(sender);
                    }
                    else sender.sendMessage(TemporaryWhiteList.getDataProvider().Check(args[0]));
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
            TemporaryWhiteList.getSingleton().Enable();
            sender.sendMessage("enabled");
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
            TemporaryWhiteList.getSingleton().Disable();
            sender.sendMessage("disabled");
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
            TemporaryWhiteList.ReloadConfigurations();
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
                java.util.List<String> activePlayers  = TemporaryWhiteList.getDataProvider().ActiveList();
                String buffer = "";
                for (int i = 0; i < activePlayers.size(); i++)
                {
                    buffer += activePlayers.get(i) + ", ";
                    if (i % 5 == 4)
                    {
                        sender.sendMessage(buffer);
                        buffer = "";
                    }
                }
                if (!buffer.equals(""))
                {
                    sender.sendMessage(buffer);
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
                sender.sendMessage(Integer.toString(TemporaryWhiteList.getDataProvider().ActiveList().size()));
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    }
}
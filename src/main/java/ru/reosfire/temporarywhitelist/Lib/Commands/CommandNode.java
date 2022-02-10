package ru.reosfire.temporarywhitelist.Lib.Commands;

import org.bukkit.command.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandNode implements CommandExecutor, TabCompleter
{
    public static String noPermissionMessage = "You can't do that!";
    private List<CommandNode> Children = null;

    public CommandNode()
    {
        Class<? extends CommandNode> currentExtendedClass = this.getClass();
        Class<?>[] declaredClasses = currentExtendedClass.getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses)
        {
            try
            {
                Constructor<?> ctor = declaredClass.getDeclaredConstructor(this.getClass());
                Object innerInstance = ctor.newInstance(this);

                if (innerInstance instanceof CommandNode)
                {
                    CommandNode commandNode = (CommandNode) innerInstance;
                    AddChildren(commandNode);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public CommandNode(PluginCommand command)
    {
        this();
        Register(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        String requiredPermission = getPermission();
        if (!(sender instanceof ConsoleCommandSender) && requiredPermission != null && !sender.hasPermission(requiredPermission))
        {
            noPermissionAction(sender);
            return true;
        }
        boolean executorFound = false;
        boolean lastExecutionResult = false;
        if (Children != null && args.length > 0)
        {
            for (CommandNode child : Children)
            {
                if (child.getName().equals(args[0]))
                {
                    String[] newArgs = new String[args.length - 1];
                    for (int i = 1; i < args.length; i++)
                    {
                        newArgs[i - 1] = args[i];
                    }
                    command.setName(args[0]);
                    lastExecutionResult = child.onCommand(sender, command, args[0], newArgs);
                    executorFound = true;
                }
            }
        }
        if (!executorFound) return execute(sender, args);
        return lastExecutionResult;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        String requiredPermission = getPermission();
        if (!(sender instanceof ConsoleCommandSender) && requiredPermission != null && !sender.hasPermission(requiredPermission) && !sender.isOp())
        {
            return null;
        }
        if (Children == null) return completeTab(args);
        for (CommandNode child : Children)
        {
            if (child.getName().equals(args[0]))
            {
                String[] newArgs = new String[args.length - 1];
                for (int i = 1; i < args.length; i++)
                {
                    newArgs[i - 1] = args[i];
                }
                return child.onTabComplete(sender, command, alias, newArgs);
            }
        }
        if (args.length == 1)
        {
            List<String> result = new ArrayList<String>();
            for (CommandNode child : Children)
            {
                if (child.getName().startsWith(args[0]) && (child.getPermission() == null || sender.hasPermission(child.getPermission())))
                {
                    result.add(child.getName());
                }
            }
            return result;
        }
        return new ArrayList<>();
    }

    public final void AddChildren(CommandNode child)
    {
        if (Children == null) Children = new ArrayList<CommandNode>();
        Children.add(child);
    }

    protected abstract String getName();

    protected abstract boolean execute(CommandSender sender, String[] args);

    protected List<String> completeTab(String[] args)
    {
        return null;
    }

    protected String getPermission()
    {
        return null;
    }

    protected void noPermissionAction(CommandSender sender)
    {
        sender.sendMessage(noPermissionMessage);
    }

    public final void Register(PluginCommand command)
    {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
package ru.reosfire.temporarywhitelist.Lib.Commands;

import org.bukkit.command.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandNode implements CommandExecutor, TabCompleter
{
    public static String noPermissionMessage = "You can't do that!";
    private List<CommandNode> _children = null;

    public CommandNode()
    {
        Class<? extends CommandNode> currentExtendedClass = this.getClass();
        Class<?>[] declaredClasses = currentExtendedClass.getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses)
        {
            try
            {
                Constructor<?> constructor = declaredClass.getDeclaredConstructor(this.getClass());
                Object innerInstance = constructor.newInstance(this);

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
        if (_children != null && args.length > 0)
        {
            for (CommandNode child : _children)
            {
                if (child.getName().equals(args[0]))
                {
                    String[] newArgs = new String[args.length - 1];

                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);

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
            return new ArrayList<>();
        }
        if (_children == null) return completeTab(args);
        for (CommandNode child : _children)
        {
            if (child.getName().equals(args[0]))
            {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                return child.onTabComplete(sender, command, alias, newArgs);
            }
        }
        if (args.length == 1)
        {
            List<String> result = new ArrayList<>();
            for (CommandNode child : _children)
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
        if (_children == null) _children = new ArrayList<>();
        _children.add(child);
    }

    protected String getName()
    {
        CommandName annotation = this.getClass().getAnnotation(CommandName.class);
        if (annotation == null) return null;
        return annotation.value();
    }

    protected abstract boolean execute(CommandSender sender, String[] args);

    protected List<String> completeTab(String[] args)
    {
        return new ArrayList<>();
    }

    protected String getPermission()
    {
        CommandPermission annotation = this.getClass().getAnnotation(CommandPermission.class);
        if (annotation == null) return null;
        return annotation.value();
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
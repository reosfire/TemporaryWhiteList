package ru.reosfire.temporarywhitelist.lib.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.lib.text.Replacement;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class CommandNode implements CommandExecutor, TabCompleter
{
    private final String noPermissionMessage;
    private List<CommandNode> children = null;

    public CommandNode(String noPermission)
    {
        noPermissionMessage = noPermission;

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
                    addChildren(commandNode);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args)
    {
        String requiredPermission = getPermission();
        if (!(sender instanceof ConsoleCommandSender) && requiredPermission != null && !sender.hasPermission(requiredPermission))
        {
            noPermissionAction(sender);
            return true;
        }
        boolean executorFound = false;
        boolean lastExecutionResult = false;
        if (children != null && args.length > 0)
        {
            for (CommandNode child : children)
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
        if (!executorFound && (getArgsCount() < 0 || getArgsCount() == args.length))
            return execute(sender, args, isAsync());
        return lastExecutionResult;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        String requiredPermission = getPermission();
        if (!(sender instanceof ConsoleCommandSender) && requiredPermission != null && !sender.hasPermission(requiredPermission) && !sender.isOp())
        {
            return Collections.emptyList();
        }
        if (children == null) return completeTab(args);
        for (CommandNode child : children)
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
            for (CommandNode child : children)
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

    public final void addChildren(CommandNode child)
    {
        if (children == null) children = new ArrayList<>();
        children.add(child);
    }

    private boolean execute(CommandSender sender, String[] args, boolean async)
    {
        if (async)
        {
            CompletableFuture.runAsync(() -> execute(sender, args)).handle((res, ex) ->
            {
                if (ex != null)
                {
                    sender.sendMessage(ChatColor.RED + "Unhandled exception while executing async command. More info in console");
                    ex.printStackTrace();
                }
                return null;
            });
            return true;
        }
        return execute(sender, args);
    }
    protected abstract boolean execute(CommandSender sender, String[] args);

    protected List<String> completeTab(String[] args)
    {
        return Collections.emptyList();
    }

    protected String getName()
    {
        CommandName annotation = this.getClass().getAnnotation(CommandName.class);
        if (annotation == null) return null;
        return annotation.value();
    }
    protected String getPermission()
    {
        CommandPermission annotation = this.getClass().getAnnotation(CommandPermission.class);
        if (annotation == null) return null;
        return annotation.value();
    }
    protected boolean isAsync()
    {
        ExecuteAsync annotation = this.getClass().getAnnotation(ExecuteAsync.class);
        return annotation != null;
    }
    private int getArgsCount()
    {
        ArgsCount annotation = this.getClass().getAnnotation(ArgsCount.class);
        if (annotation == null) return -1;
        return annotation.value();
    }

    protected void noPermissionAction(CommandSender sender)
    {
        sender.sendMessage(noPermissionMessage);
    }

    public final void register(PluginCommand command)
    {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    protected final <T> boolean tryParse(Function<String, T> parser, String s, AtomicReference<T> container)
    {
        try
        {
            container.set(parser.apply(s));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    protected final boolean sendMessageIf(boolean send, MultilineMessage message, CommandSender sender, Replacement... replacements)
    {
        if (send) message.Send(sender, replacements);
        return send;
    }

    protected final List<String> getStartingWith(List<String> input, String start)
    {
        ArrayList<String> result = new ArrayList<>();

        for (String s : input)
        {
            if (s.startsWith(start)) result.add(start);
        }

        return result;
    }
}
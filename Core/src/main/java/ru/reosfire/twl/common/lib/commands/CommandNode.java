package ru.reosfire.twl.common.lib.commands;

import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class CommandNode
{
    private final MultilineMessage noPermissionMessage;
    private final MultilineMessage unexpectedErrorMessage;
    private List<CommandNode> children = null;

    public CommandNode(MultilineMessage noPermission, MultilineMessage unexpectedError)
    {
        noPermissionMessage = noPermission;
        unexpectedErrorMessage = unexpectedError;

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

    public boolean onCommand(TwlCommandSender sender, String[] args)
    {
        if (!sender.canUseCommand(this))
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

                    lastExecutionResult = child.onCommand(sender, newArgs);
                    executorFound = true;
                }
            }
        }
        if (!executorFound && (getArgsCount() < 0 || getArgsCount() == args.length))
            return execute(sender, args, isAsync());
        return lastExecutionResult;
    }

    public List<String> onTabComplete(TwlCommandSender sender, String[] args)
    {
        if (sender.canUseCommand(this))
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
                return child.onTabComplete(sender, newArgs);
            }
        }
        if (args.length == 1)
        {
            List<String> result = new ArrayList<>();
            for (CommandNode child : children)
            {
                if (child.getName().startsWith(args[0]) && sender.canUseCommand(child))
                {
                    result.add(child.getName());
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    public final void addChildren(CommandNode child)
    {
        if (children == null) children = new ArrayList<>();
        children.add(child);
    }

    private boolean execute(TwlCommandSender sender, String[] args, boolean async)
    {
        if (async)
        {
            CompletableFuture.runAsync(() -> execute(sender, args)).handle((res, ex) ->
            {
                if (ex != null)
                {
                    sender.sendMessage(unexpectedErrorMessage);
                    ex.printStackTrace();
                }
                return null;
            });
            return true;
        }
        return execute(sender, args);
    }
    protected abstract boolean execute(TwlCommandSender sender, String[] args);

    protected List<String> completeTab(String[] args)
    {
        return Collections.emptyList();
    }

    public String getName()
    {
        CommandName annotation = this.getClass().getAnnotation(CommandName.class);
        if (annotation == null) return null;
        return annotation.value();
    }
    public String getPermission()
    {
        CommandPermission annotation = this.getClass().getAnnotation(CommandPermission.class);
        if (annotation == null) return null;
        return annotation.value();
    }
    public boolean isAsync()
    {
        ExecuteAsync annotation = this.getClass().getAnnotation(ExecuteAsync.class);
        return annotation != null;
    }
    public int getArgsCount()
    {
        ArgsCount annotation = this.getClass().getAnnotation(ArgsCount.class);
        if (annotation == null) return -1;
        return annotation.value();
    }

    protected void noPermissionAction(TwlCommandSender sender)
    {
        sender.sendMessage(noPermissionMessage);
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

    protected final boolean sendMessageIf(boolean send, MultilineMessage message, TwlCommandSender sender, Replacement... replacements)
    {
        if (send) message.Send(sender, replacements);
        return send;
    }
}
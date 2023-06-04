package ru.reosfire.twl.lib.yaml.common.text;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.lib.text.Replacement;

import java.util.List;

public class MultilineMessage
{
    private final List<TextComponentConfig> Messages;

    public MultilineMessage(List<TextComponentConfig> messages)
    {
        Messages = messages;
    }

    public void Send(CommandSender sender, Replacement... replacements)
    {
        for (TextComponentConfig message : Messages)
        {
            message.Send(sender, replacements);
        }
    }
}
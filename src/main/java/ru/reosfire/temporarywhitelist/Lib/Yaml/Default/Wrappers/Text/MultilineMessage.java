package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;

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
package ru.reosfire.twl.common.lib.yaml.common.text;

import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.common.lib.text.Replacement;

import java.util.List;

public class MultilineMessage
{
    public final List<TextComponentConfig> Messages;

    public MultilineMessage(List<TextComponentConfig> messages)
    {
        Messages = messages;
    }

    public void Send(TwlCommandSender sender, Replacement... replacements) {
        sender.SendMessage(this, replacements);
    }
}
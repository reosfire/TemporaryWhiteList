package ru.reosfire.twl.common.lib.commands;

import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.twl.common.lib.yaml.common.text.TextComponentConfig;

public abstract class TwlCommandSender {
    public abstract void SendMessage(TextComponentConfig messageLine, Replacement... replacements);

    public void SendMessage(MultilineMessage multilineMessage, Replacement... replacements) {
        for (TextComponentConfig message : multilineMessage.Messages) {
            SendMessage(message, replacements);
        }
    }
}
package ru.reosfire.twl.common.lib.commands;

import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.twl.common.lib.yaml.common.text.TextComponentConfig;

public abstract class TwlCommandSender {
    public abstract void sendMessage(TextComponentConfig messageLine, Replacement... replacements);
    public abstract boolean canUseCommand(CommandNode command);
    public abstract boolean hasPermission(String permission);
    public abstract boolean isPlayer();
    public abstract String getName();

    public void sendMessage(MultilineMessage multilineMessage, Replacement... replacements) {
        for (TextComponentConfig message : multilineMessage.Messages) {
            sendMessage(message, replacements);
        }
    }
}
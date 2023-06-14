package ru.reosfire.twl.common.lib.commands;

import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public interface TwlCommandSender {
    void SendMessage(MultilineMessage multilineMessage, Replacement... replacements);
}
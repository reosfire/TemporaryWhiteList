package ru.reosfire.twl.spigot.commands;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.twl.common.lib.yaml.common.text.TextComponentConfig;

public class SpigotTwlCommandSender implements TwlCommandSender {
    private final CommandSender sender;

    public SpigotTwlCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void SendMessage(MultilineMessage multilineMessage, Replacement... replacements) {
        for (TextComponentConfig message : multilineMessage.Messages) {
            sender.sendMessage(message.toString());
        }
    }
}
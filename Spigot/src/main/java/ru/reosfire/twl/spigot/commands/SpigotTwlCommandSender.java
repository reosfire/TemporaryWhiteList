package ru.reosfire.twl.spigot.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.common.lib.text.IColorizer;
import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.common.text.ClickConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.HoverConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.TextComponentConfig;
import ru.reosfire.twl.spigot.lib.text.Text;

import java.util.Locale;

public class SpigotTwlCommandSender extends TwlCommandSender {
    private final CommandSender sender;

    public SpigotTwlCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(TextComponentConfig messageLine, Replacement... replacements) {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            player.spigot().sendMessage(Unwrap(messageLine, player, replacements));
        }
        else sender.sendMessage(messageLine.toString(replacements));
    }

    @Override
    public boolean canUseCommand(CommandNode command) {
        String requiredPermission = command.getPermission();
        return requiredPermission == null || sender.isOp() || (sender instanceof ConsoleCommandSender) || sender.hasPermission(requiredPermission);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    private TextComponent Unwrap(TextComponentConfig config, OfflinePlayer player, Replacement... replacements)
    {
        return Unwrap(config, s -> Text.colorize(player, s, replacements));
    }


    private TextComponent Unwrap(TextComponentConfig config, IColorizer colorizer)
    {
        TextComponent result;
        if (config.Content == null) result = new TextComponent(TextComponent.fromLegacyText(colorizer.colorize(config.TextContent)));
        else
        {
            TextComponent[] subComponents = new TextComponent[config.Content.size()];
            for (int i = 0; i < subComponents.length; i++)
                subComponents[i] = Unwrap(config.Content.get(i), colorizer);
            result = new TextComponent(subComponents);
        }

        if (config.ClickConfig != null) result.setClickEvent(Unwrap(config.ClickConfig, colorizer));
        if (config.HoverConfig != null) result.setHoverEvent(Unwrap(config.HoverConfig, colorizer));

        if (config.Color != null) result.setColor(getColor(config));

        result.setBold(config.Bold);
        result.setItalic(config.Italic);
        result.setUnderlined(config.Underlined);
        result.setStrikethrough(config.Strikethrough);

        return result;
    }

    private ChatColor getColor(TextComponentConfig config) {
        return ChatColor.valueOf(config.Color.toUpperCase(Locale.ROOT));
    }

    public ClickEvent Unwrap(ClickConfig config, IColorizer colorizer)
    {
        ClickEvent.Action action = ClickEvent.Action.valueOf(config.Action);
        return new ClickEvent(action, colorizer.colorize(config.Value));
    }

    public HoverEvent Unwrap(HoverConfig config, IColorizer colorizer)
    {
        HoverEvent.Action action = HoverEvent.Action.valueOf(config.Action);
        //noinspection deprecation because 1.12.2
        return new HoverEvent(action, new BaseComponent[] { new TextComponent(colorizer.colorize(config.Value)) });
    }
}
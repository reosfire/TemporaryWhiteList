package ru.reosfire.temporarywhitelist.Lib.Yaml.Default;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.util.List;

public class Announcement extends YamlConfig
{
    public final boolean MessageEnabled;
    public final List<String> Message;
    public final boolean SoundEnabled;
    private Sound Sound;
    private boolean TitleEnabled;
    public final String Title;
    private String Subtitle;
    private int TitleTime;
    public final boolean ActionBarEnabled;
    public final String ActionBarMessage;

    public Announcement(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Message = configurationSection.getStringList("Message");
        String oneLineMessage = configurationSection.getString("Message");
        if (oneLineMessage != null && Message.isEmpty()) Message.add(oneLineMessage);
        if (Message == null || Message.isEmpty()) { MessageEnabled = false; }
        else { MessageEnabled = true; }

        String sound = configurationSection.getString("Sound");
        if (sound == null || sound.isEmpty()) { SoundEnabled = false; }
        else
        {
            SoundEnabled = true;
            Sound = org.bukkit.Sound.valueOf(sound);
        }

        Title = configurationSection.getString("Title");
        if (Title != null && !Title.isEmpty())
        {
            Subtitle = configurationSection.getString("Subtitle", "");
            TitleTime = configurationSection.getInt("TitleTime", 20);
            TitleEnabled = true;
        }

        ActionBarMessage = configurationSection.getString("ActionBarMessage");
        if (ActionBarMessage == null || ActionBarMessage.isEmpty()) { ActionBarEnabled = false; }
        else { ActionBarEnabled = true; }
    }

    public void SendMessage(Player player, String message)
    {
        player.sendMessage(Text.Colorize(player, message));
    }

    public void SendMessage(Player player, Iterable<String> messages)
    {
        for (String message : messages)
        {
            SendMessage(player, message);
        }
    }

    public void ShowTitle(Player player, String title, String subtitle, int time)
    {
        title = Text.Colorize(player, title);
        subtitle = Text.Colorize(player, subtitle);
        player.sendTitle(title, subtitle, time / 4, time / 2, time / 4);
    }

    public void PlaySound(Player player, Sound sound)
    {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    public void SendActionBar(Player player, String message)
    {
        message = Text.Colorize(player, message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    public void Announce(Player player, Replacement... replacements)
    {
        if (MessageEnabled) SendMessage(player, Replacement.Set(Message, replacements));
        if (SoundEnabled) PlaySound(player, Sound);
        if (TitleEnabled)
        { ShowTitle(player, Replacement.Set(Title, replacements), Replacement.Set(Subtitle, replacements), TitleTime); }
        if (ActionBarEnabled) SendActionBar(player, Replacement.Set(ActionBarMessage, replacements));
    }

    public void Announce(Iterable<Player> players, Replacement... replacements)
    {
        for (Player player : players)
        {
            Announce(player, replacements);
        }
    }

    public void Announce(Iterable<Player> players, Player except, Replacement... replacements)
    {
        for (Player player : players)
        {
            if (player == except) continue;
            Announce(player, replacements);
        }
    }
}
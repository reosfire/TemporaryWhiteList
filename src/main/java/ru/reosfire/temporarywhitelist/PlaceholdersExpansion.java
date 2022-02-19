package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;

public class PlaceholdersExpansion extends PlaceholderExpansion
{
    private final MessagesConfig _messages;
    private final IDataProvider _dataProvider;
    private final TemporaryWhiteList _pluginInstance;

    public PlaceholdersExpansion(MessagesConfig messages, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        _messages = messages;
        _dataProvider = dataProvider;
        _pluginInstance = pluginInstance;
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public boolean canRegister()
    {
        return true;
    }

    @Override
    public @NotNull String getAuthor()
    {
        return _pluginInstance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "twl";
    }

    @Override
    public @NotNull String getVersion()
    {
        return _pluginInstance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier)
    {
        if (player == null) return "";
        if (identifier.equals("subscription_status"))
        {
            try
            {
                return _dataProvider.Check(player.getName());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "";
            }
        }
        if (identifier.equals("status"))
        {
            return _pluginInstance.isWhiteListEnabled() ? _messages.WhiteListEnabledStatus :
                    _messages.WhiteListDisabledStatus;
        }

        return "";
    }
}
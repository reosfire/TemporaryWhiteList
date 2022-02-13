package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;

public class PlaceholdersExpansion extends PlaceholderExpansion
{
    private final MessagesConfig Messages;
    private final IDataProvider DataProvider;
    private final TemporaryWhiteList PluginInstance;

    public PlaceholdersExpansion(MessagesConfig messages, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        Messages = messages;
        DataProvider = dataProvider;
        PluginInstance = pluginInstance;
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
        return PluginInstance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "WMWhiteList";
    }

    @Override
    public @NotNull String getVersion()
    {
        return PluginInstance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier)
    {
        if (player == null) return "";
        if (identifier.equals("subscription_status"))
        {
            try
            {
                return DataProvider.Check(player.getName());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "";
            }
        }
        if (identifier.equals("status"))
        {
            return PluginInstance.isWhiteListEnabled() ? Messages.WhiteListEnabledStatus :
                    Messages.WhiteListDisabledStatus;
        }

        return "";
    }
}
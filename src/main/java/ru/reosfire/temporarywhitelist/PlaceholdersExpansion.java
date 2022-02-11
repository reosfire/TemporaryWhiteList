package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;

public class PlaceholdersExpansion extends PlaceholderExpansion
{
    private final Config Configuration;
    private final IDataProvider DataProvider;
    private final TemporaryWhiteList PluginInstance;

    public PlaceholdersExpansion(Config configuration, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        Configuration = configuration;
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
    public String getAuthor()
    {
        return PluginInstance.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier()
    {
        return "WMWhiteList";
    }

    @Override
    public String getVersion()
    {
        return PluginInstance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        if (player == null || identifier == null) return "";
        if(identifier.equals("subscription_status"))
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
        if(identifier.equals("status"))
        {
            return Configuration.Enabled ?
                    Configuration.Messages.WhiteListEnabledStatus:
                    Configuration.Messages.WhiteListDisabledStatus;
        }

        return "";
    }
}
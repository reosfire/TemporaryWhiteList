package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholdersExpansion extends PlaceholderExpansion
{
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
        return TemporaryWhiteList.getSingleton().getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier()
    {
        return "WMWhiteList";
    }

    @Override
    public String getVersion()
    {
        return TemporaryWhiteList.getSingleton().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        if (player == null || identifier == null) return "";
        if(identifier.equals("subscription_status"))
        {
            try
            {
                return TemporaryWhiteList.getDataProvider().Check(player.getName());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "";
            }
        }
        if(identifier.equals("status"))
        {
            return TemporaryWhiteList.getConfiguration().Enabled ?
                    TemporaryWhiteList.getMessages().WhiteListEnabledStatus:
                    TemporaryWhiteList.getMessages().WhiteListDisabledStatus;
        }

        return "";
    }
}
package ru.reosfire.temporarywhitelist.Lib.Yaml;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Position extends YamlConfig
{
    public final double X;
    public final double Y;
    public final double Z;
    public final float Yaw;
    public final float Pitch;
    private boolean yawPitchSet;

    /**
     * @param configurationSection (X, Y, Z) : double (Yaw, Pitch) : double
     */
    public Position(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        X = getDouble("X");
        Y = getDouble("Y");
        Z = getDouble("Z");
        String yaw = getString("Yaw");
        String pitch  = getString("Pitch");
        if(!(yaw == null || pitch == null))
        {
            Yaw = Float.parseFloat(yaw);
            Pitch = Float.parseFloat(pitch);
            yawPitchSet = true;
        }
        else
        {
            Yaw = 0f;
            Pitch = 0f;
        }
    }

    public Location toLocation(World world)
    {
        return yawPitchSet ? new Location(world, X, Y, Z, Yaw, Pitch) : new Location(world, X, Y, Z);
    }
    public Vector toVector()
    {
        return new Vector(X, Y, Z);
    }
    public void Teleport(Player player)
    {
        Teleport(player, toLocation(player.getWorld()));
    }
    public static void Teleport(Player player, Location to)
    {
        boolean allowFlight = player.getAllowFlight();
        if(!allowFlight) player.setAllowFlight(true);
        player.teleport(to);
        if(!allowFlight) player.setAllowFlight(false);
    }
}
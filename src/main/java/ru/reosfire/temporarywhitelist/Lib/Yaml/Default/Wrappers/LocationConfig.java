package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class LocationConfig extends YamlConfig implements WrapperConfig<Location>
{
    public final World World;
    public final VectorConfig Vector;
    public final float Yaw;
    public final float Pitch;
    private boolean yawPitchSet;

    public LocationConfig(ConfigurationSection configurationSection, World world)
    {
        super(configurationSection);
        World = world;
        Vector = new VectorConfig(getSection());
        String yaw = getString("Yaw");
        String pitch = getString("Pitch");
        if (!(yaw == null || pitch == null))
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

    public LocationConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        World = Bukkit.getWorld(getString("World"));
        Vector = new VectorConfig(getSection());
        String yaw = getString("Yaw");
        String pitch = getString("Pitch");
        if (!(yaw == null || pitch == null))
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

    @Override
    public Location Unwrap()
    {
        return yawPitchSet ?
                new Location(World,
                        Vector.Unwrap().getX(),
                        Vector.Unwrap().getY(),
                        Vector.Unwrap().getZ(), Yaw, Pitch) :
                new Location(World,
                        Vector.Unwrap().getX(),
                        Vector.Unwrap().getY(),
                        Vector.Unwrap().getZ());
    }
    public void Teleport(Player player)
    {
        player.teleport(Unwrap());
    }
}
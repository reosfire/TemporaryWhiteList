package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class VectorConfig extends YamlConfig implements WrapperConfig<Vector>
{
    public final Double X;
    public final Double Y;
    public final Double Z;

    public VectorConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        X = getDouble("X");
        Y = getDouble("Y");
        Z = getDouble("Z");
    }

    @Override
    public Vector Unwrap()
    {
        return new Vector(X, Y, Z);
    }
    public Location Unwrap(World world)
    {
        return Unwrap().toLocation(world);
    }
    public void Teleport(Player player)
    {
        player.teleport(Unwrap(player.getWorld()));
    }
}
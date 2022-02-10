package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class MaterialDataConfig extends YamlConfig implements WrapperConfig<MaterialData>
{
    public final Material Material;
    public final byte Data;
    public MaterialDataConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Material = org.bukkit.Material.getMaterial(getString("Material"));
        Data = getByte("Data", (byte) 0);
    }

    @Override
    public MaterialData Unwrap()
    {
        try
        {
            if(Data > 0) return new MaterialData(Material, Data);
            else return new MaterialData(Material);
        }
        catch (Exception e)
        {
            return new MaterialData(Material);
        }
    }
}
package ru.reosfire.temporarywhitelist.Lib.Yaml.Default;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.ItemMeta;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class Enchantment extends YamlConfig
{
    public final org.bukkit.enchantments.Enchantment Type;
    public final int Level;
    public Enchantment(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Type = org.bukkit.enchantments.Enchantment.getByName(getString("Type"));
        Level = getInt("Level", 1);
    }
    public void setTo(ItemMeta meta, boolean force)
    {
        meta.addEnchant(Type, Level, force);
    }
}
package ru.reosfire.temporarywhitelist.Lib.Yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;

public class Item extends YamlConfig
{
    private final ItemStack Item;

    public ItemStack getItem()
    {
        return Item.clone();
    }
    /**
     * @param configurationSection Id: int, Data(0): byte, Count(1): int, Name: string, EnchantmentEnabled: bool,
     *                             Enchantment: string, EnchantmentLevel: int, Lore: stringList
     */
    public Item(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        int id = getInt("Id");
        int data = getInt("Data", 0);
        MaterialData joinMaterialData = new MaterialData(id, (byte)data);
        ItemStack item = joinMaterialData.toItemStack();
        item.setAmount(Math.max(getInt("Count", 1), 1));

        ItemMeta itemMeta = item.getItemMeta();

        String name = getString("Name", null);
        if(name != null) itemMeta.setDisplayName(Text.SetColors(name));

        if(getBoolean("EnchantmentEnabled"))
        {
            Enchantment type = Enchantment.getByName(getString("Enchantment"));
            int level = getInt("EnchantmentLevel");
            itemMeta.addEnchant(type,level,true);
        }

        itemMeta.setLore(getStringList("Lore"));

        item.setItemMeta(itemMeta);
        Item = item;
    }
}
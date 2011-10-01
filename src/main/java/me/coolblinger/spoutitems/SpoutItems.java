package me.coolblinger.spoutitems;

import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.ItemManager;

import java.util.logging.Logger;

public class SpoutItems extends JavaPlugin {
	Logger log = Logger.getLogger("Minecraft");
	ItemManager im;

	public void onDisable() {

	}

	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();
		PluginManager pm = getServer().getPluginManager();
		if (pm.getPlugin("Spout") == null) {
			log.severe("Spout was not found, SpoutItems will disable itself.");
			setEnabled(false);
			return;
		}
		im = SpoutManager.getItemManager();
		parseConfig();
		log.info("SpoutItems version " + pdf.getVersion() + " has been enabled!");
	}

	private void parseConfig() {
		Configuration config = getConfiguration();
		config.load();
		if (config.getAll().isEmpty()) {
			config.setProperty("353.name", "Weed");
			config.setProperty("353.texture", "http://dl.dropbox.com/u/677732/Minecraft/weed.png");
			config.save();
		}
		for (String item:config.getKeys()) {
			try {
				if (!item.contains(":")) {
					int id = Integer.parseInt(item);
					if (config.getProperty(item + ".name") != null) {
						im.setItemName(Material.getMaterial(id), config.getString(item + ".name"));
					}
					if (config.getProperty(item + ".texture") != null) {
						im.setItemTexture(Material.getMaterial(id), this, config.getString(item + ".texture"));
						SpoutManager.getFileManager().addToCache(this, config.getString(item + ".texture"));
					}
				} else {
					String[] split = item.split(":");
					int id = Integer.parseInt(split[0]);
					short data = Short.parseShort(split[1]);
					if (config.getProperty(item + ".name") != null) {
						im.setItemName(Material.getMaterial(id), data, config.getString(item + ".name"));
					}
					if (config.getProperty(item + ".texture") != null) {
						im.setItemTexture(Material.getMaterial(id), data, this, config.getString(item + ".texture"));
						SpoutManager.getFileManager().addToCache(this, config.getString(item + ".texture"));
					}
				}
			} catch (NumberFormatException e) {
				log.warning("[SpoutItems] '" + item + "' is not a valid item ID.");
			}
		}
	}
}

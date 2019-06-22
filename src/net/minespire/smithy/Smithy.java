package net.minespire.smithy;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Smithy extends JavaPlugin {
	
	public static Smithy plugin;
	private Upgrade upgrade;
    private File customConfigFile;
    private FileConfiguration customConfig;
    
	public Smithy() {
		plugin = this;
	}
	@Override
	public void onEnable() {
		plugin.getLogger().info("Smithy enabled");
        createCustomConfig();
		Tool.getToolNames();
		this.getCommand("smithy").setTabCompleter(new CommandCompleter());
		this.getCommand("smithy").setExecutor(new CommandHandler());
		upgrade = new Upgrade();
		getServer().getPluginManager().registerEvents(upgrade, this);

	}
	
	@Override
	public void onDisable() {
		
	}
	
    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
         }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

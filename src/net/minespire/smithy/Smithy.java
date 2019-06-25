package net.minespire.smithy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.CraftedPlugins.cyberkm.Database;
import com.CraftedPlugins.cyberkm.SQLite;

public class Smithy extends JavaPlugin {
	
	public static Smithy plugin;
	private Upgrade upgrade;
    private File customConfigFile;
    private FileConfiguration customConfig;
    private Map<String, Tool> toolSet;
    
    private Database db;
    
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
		toolSet = new HashMap<>(100);
		
        this.db = new SQLite(this);
        this.db.load();
	}
	
	@Override
	public void onDisable() {
		for(Map.Entry<String, Tool> entry : toolSet.entrySet()) {
			plugin.getDatabase().saveTool(entry.getValue().getID(), entry.getValue().getBlocksBroken(), entry.getValue().getEnergy());
		}
			
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
    
    public Tool getTool(String id) {
    	return toolSet.get(id);
    }
    
    public boolean toolExists(String id) {
    	return toolSet.containsKey(id);
    }
    
    public String getToolID() {
    	return UUID.randomUUID().toString();
    }
    
    public void saveToolToMap(Tool tool) {
    	toolSet.put(tool.getID(), tool);
    }
    
    



    public Database getDatabase() {
        return this.db;
    }
}

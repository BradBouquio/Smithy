package net.minespire.smithy;

import com.CraftedPlugins.cyberkm.Database;
import com.CraftedPlugins.cyberkm.SQLite;
import net.minespire.smithy.gui.GuiManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Smithy extends JavaPlugin {
	
	public static Smithy plugin;
	private GuiManager upgrade;
    private File customConfigFile;
    private FileConfiguration customConfig;

    
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
		upgrade = new GuiManager();
		getServer().getPluginManager().registerEvents(upgrade, this);
		
        this.db = new SQLite(this);
        this.db.load();
	}
	
	@Override
	public void onDisable() {
		for(Map.Entry<String, Tool> entry : Tool.toolSet.entrySet()) {
			plugin.getDatabase().saveTool(entry.getValue());
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

    public Database getDatabase() {
        return this.db;
    }
}

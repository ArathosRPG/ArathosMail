package com.github.derwisch.paperMail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperMail extends JavaPlugin {
	  
	public static String NEW_MAIL_GUI_TITLE = ChatColor.BLACK + "Neuen Brief verfassen:" + ChatColor.RESET;
	public static String INBOX_GUI_TITLE = ChatColor.BLACK + "Postfach:" + ChatColor.RESET;
	public static String NEW_MAIL_ITEM_LORE = ChatColor.GRAY + "Nimm dies zur Hand, um einen neuen Brief zu verfassen." + ChatColor.RESET;
	public static String MAIL_ITEM_INGREDIENTS = "PAPER,FEATHER,INK_SACK";
	
	public static PaperMail instance;
	public static Server server;
	public static Logger logger;
	public static Economy economy = null;
	
	private PaperMailListener listener;
	private FileConfiguration configuration;
	
    @Override
    public void onEnable() {
    	instance = this;
    	server = this.getServer();
    	logger = this.getLogger();
    	
    	saveDefaultConfig();
    	configuration = this.getConfig();
    	Settings.LoadConfiguration(configuration);
    	
    	NEW_MAIL_GUI_TITLE = Settings.NewMailGuiTitle;
    	INBOX_GUI_TITLE = Settings.InboxGuiTitle;
    	NEW_MAIL_ITEM_LORE = Settings.CreateMailItemLore;
    	MAIL_ITEM_INGREDIENTS = Settings.MailItemIngredients;
    	
    	//Load Economy
    	if (setupEconomy().booleanValue())
    		System.out.println(this + " linked into " + economy.getName() + ", via Vault");
        if ((setupEconomy() == false) && (Settings.EnableMailCosts == true)) {
        	System.out.println(this + ": Vault economy not found, switching to Default Economy!");
        }
    	
    	PaperMailCommandExecutor commandExecutor = new PaperMailCommandExecutor(this); 
    	getCommand("papermail").setExecutor(commandExecutor);
    	
    	listener = new PaperMailListener();
        this.getServer().getPluginManager().registerEvents(listener, this);
        
        initializeRecipes();
        initializeInboxes();
        
    	logger.info("Enabled PaperMail");
    }
    
	@Override
    public void onDisable() {
		try {
			Inbox.SaveAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Settings.SaveConfiguration(configuration);
		this.saveConfig();
    	getLogger().info("Disabled PaperMail");
    }
    
    @SuppressWarnings("deprecation")
	private void initializeRecipes() {
		ItemStack letterPaper = new ItemStack(Settings.MailItemID);
		ItemMeta letterPaperMeta = letterPaper.getItemMeta();
		ArrayList<String> letterPaperLore = new ArrayList<String>();
		letterPaperMeta.setDisplayName(ChatColor.WHITE + Settings.MailItemName + ChatColor.RESET);
		letterPaperLore.add(NEW_MAIL_ITEM_LORE);
		letterPaperMeta.setLore(letterPaperLore);
    	letterPaper.setItemMeta(letterPaperMeta);
    	letterPaper.setDurability((short)Settings.MailItemDV);
		
		ShapelessRecipe letterPaperRecipe = new ShapelessRecipe(letterPaper);
		
		int x = 0;
		String[] ingredient = MAIL_ITEM_INGREDIENTS.split(",");
		
		if(ingredient.length > 0 && ingredient.length < 10) {
			while(x < ingredient.length) {
				letterPaperRecipe.addIngredient(Material.getMaterial(ingredient[x].toUpperCase()));
				x++;
			} 
		}else {
			System.out.println("Invalid length of ingredients: " + ingredient.length);
			letterPaperRecipe.addIngredient(Material.PAPER);
			letterPaperRecipe.addIngredient(Material.INK_SACK);
			letterPaperRecipe.addIngredient(Material.FEATHER);
		}
		
		
		this.getServer().addRecipe(letterPaperRecipe);
    }

    private void initializeInboxes() {
		for (Player player : getServer().getOnlinePlayers()) {
			if (player == null) {
				continue;
			}
			try {
				Inbox.AddInbox(player.getName());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		for (OfflinePlayer offPlayer : getServer().getOfflinePlayers()) {
			
			Player player = offPlayer.getPlayer();
			
			if (player == null) {
				continue;
			}
			try {
				Inbox.AddInbox(player.getName());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
    
    public static boolean isGoldIngot() {
        return economy == null;
     }

     
   @SuppressWarnings("rawtypes")
public Boolean setupEconomy()
     {
       Plugin vault = getServer().getPluginManager().getPlugin("Vault");
      if (vault == null) {
        return Boolean.valueOf(false);
       }
     RegisteredServiceProvider economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
     if (economyProvider != null) {
        economy = (Economy)economyProvider.getProvider();
       }

        return Boolean.valueOf(economy != null);
     }
}

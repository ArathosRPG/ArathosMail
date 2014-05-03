package com.github.derwisch.paperMail;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.Configuration;

public class Settings {

	// Config values
	public static boolean EnableTextMail = true;
	public static boolean EnableItemMail = true;
	public static boolean EnableEnderchest = true;
	public static boolean EnableMailCosts = false;
	public static boolean PerItemCosts = false;
	public static boolean EnableSendMoney = false;

	public static int MailWindowRows = 4;
	public static int DefaultBoxRows = 5;
	
	public static int EnderChestRequirementID = 368;
	public static int EnderChestRequirementDV = 0;
	
	public static int CurrencyItemID = 266;
	public static double Price = 0;
	public static double ItemCost = 0;
	public static int MailItemID = 339;
	public static int MailItemDV = 1;
	public static int BankNoteNum = 339;
	public static int Increments = 1;
	public static String MailItemName = "Briefpapier";
	public static String NewMailGuiTitle = "Neuen Brief verfassen:";
	public static String InboxGuiTitle = "Postfach:";
	public static String CreateMailItemLore = "Nimm dies zur Hand, um einen neuen Brief zu verfassen.";
	public static String MailItemIngredients = "PAPER,FEATHER,INK_SACK";
	public static String SignText = "[Briefkasten]";
	
	public static List<String> InboxPlayers = new ArrayList<String>();
	
    public static void LoadConfiguration(Configuration config) {
        try {
        	DefaultBoxRows = Math.max(3, config.getInt("general.DefaultBoxRows"));
        	MailWindowRows = Math.max(3, config.getInt("general.MailWindowRows"));
        	
        	EnableTextMail = config.getBoolean("general.EnableTextMail");
        	EnableItemMail = config.getBoolean("general.EnableItemMail");
        	EnableEnderchest = config.getBoolean("general.EnableEnderchest");
        	EnableMailCosts = config.getBoolean("general.EnableMailCosts");
        	PerItemCosts = config.getBoolean("general.EnablePerItemCosts");
        	EnableSendMoney = config.getBoolean("general.EnableSendMoney");
        	
        	Price = config.getDouble("general.CostToText");
        	ItemCost = config.getDouble("general.SendItemCost");

        	CurrencyItemID = config.getInt("general.CurrencyItemID");
        	MailItemID = config.getInt("general.MailItemID");
        	MailItemDV = config.getInt("general.MailItemDV");
        	BankNoteNum = config.getInt("general.BankNoteID");
        	MailItemName = config.getString("general.MailItemName");
        	
        	//new configuration options
        	NewMailGuiTitle = config.getString("general.NewMailGuiTitle");
        	InboxGuiTitle = config.getString("general.InboxGuiTitle");
        	CreateMailItemLore = config.getString("general.CreateMailItemLore");
        	MailItemIngredients = config.getString("general.MailItemIngredients");
        	SignText = config.getString("general.SignText");
        	
        	InboxPlayers = config.getStringList("inboxPlayers");
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
    
    public static void SaveConfiguration(Configuration config) {
    	config.set("general.DefaultBoxRows", DefaultBoxRows);
    	config.set("general.MailWindowRows", MailWindowRows);
    	
    	config.set("general.EnableTextMail", EnableTextMail);
    	config.set("general.EnableItemMail", EnableItemMail);
    	config.set("general.EnableEnderchest", EnableEnderchest);
    	config.set("general.EnableMailCosts", EnableMailCosts);
    	config.set("general.EnablePerItemCosts", PerItemCosts);
    	config.set("general.EnableSendMoney", EnableSendMoney);
    	
    	config.set("general.CostToText", Price);
    	config.set("general.SendItemCost", ItemCost);
    	
    	config.set("general.CurrencyItemID", CurrencyItemID);
    	config.set("general.MailItemID", MailItemID);
    	config.set("general.MailItemDV", MailItemDV);
    	config.set("general.BankNoteID", BankNoteNum);
    	config.set("general.MailItemName", MailItemName);
    	
    	//new configuration options
    	config.set("general.NewMailGuiTitle", NewMailGuiTitle);
    	config.set("general.InboxGuiTitle", InboxGuiTitle);
    	config.set("general.CreateMailItemLore", CreateMailItemLore);
    	config.set("general.MailItemIngredients", MailItemIngredients);
    	config.set("general.SignText", SignText);
    	    	
    	config.set("inboxPlayers", InboxPlayers);
    }
    
}

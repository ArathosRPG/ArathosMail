package com.github.derwisch.paperMail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
//import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PaperMailGUI {

	public static final String RECIPIENT_TITLE = ChatColor.RED + "Empfänger" + ChatColor.RESET;
	public static final String SEND_BUTTON_ON_TITLE = ChatColor.WHITE + "Abschicken" + ChatColor.RESET;
	public static final String CANCEL_BUTTON_TITLE = ChatColor.WHITE + "Abbrechen" + ChatColor.RESET;
	public static final String ENDERCHEST_BUTTON_TITLE = ChatColor.WHITE + "Enderkiste öffnen" + ChatColor.RESET;
	public static final String MONEY_SEND_BUTTON_TITLE = ChatColor.WHITE + "Geld senden" + ChatColor.RESET;
	public static final String BANK_NOTE_DISPLAY = ChatColor.GREEN + "Scheck";
	
	private static ArrayList<PaperMailGUI> itemMailGUIs = new ArrayList<PaperMailGUI>();
	private static Map<String, PaperMailGUI> openGUIs = new HashMap<String, PaperMailGUI>();
	
	public Inventory Inventory;
	public Player Player;
	public static boolean cancel = false;

	private ItemStack recipientMessage; 
	private ItemStack sendButtonEnabled;
	private ItemStack cancelButton; 
	private ItemStack enderChestButton;
	private ItemStack sendMoneyButton;
	private boolean paperSent;
	
	public SendingGUIClickResult Result = SendingGUIClickResult.CANCEL;
	
	public static void RemoveGUI(String playerName) {
		openGUIs.put(playerName, null);
		openGUIs.remove(playerName);
	}
	
	public static PaperMailGUI GetOpenGUI(String playerName) {
		return openGUIs.get(playerName);
	}
	
	public PaperMailGUI(Player player) {
		this.paperSent = false;
		Player = player;
		Inventory = Bukkit.createInventory(player, Settings.MailWindowRows * 9, PaperMail.NEW_MAIL_GUI_TITLE);
		initializeButtons();
    	itemMailGUIs.add(this);
	}

	public PaperMailGUI(Player player, boolean paperSent) {
		this.paperSent = paperSent;
		Player = player;
		Inventory = Bukkit.createInventory(player, Settings.MailWindowRows * 9, PaperMail.NEW_MAIL_GUI_TITLE);
		initializeButtons();
    	itemMailGUIs.add(this);
	}
	
	private void initializeButtons() {
		Inventory.setMaxStackSize(127);
		recipientMessage = new ItemStack(Material.PAPER);
		sendButtonEnabled = new ItemStack(Material.WOOL);
		cancelButton = new ItemStack(Material.WOOL);
		enderChestButton = new ItemStack(Material.ENDER_CHEST);
		sendMoneyButton = new ItemStack(Material.GOLD_INGOT);

    	sendButtonEnabled.setDurability((short)5);
    	cancelButton.setDurability((short)14);

    	ItemMeta recipientMessageMeta = recipientMessage.getItemMeta();
    	ItemMeta sendButtonEnabledMeta = sendButtonEnabled.getItemMeta();
    	ItemMeta cancelButtonMeta = cancelButton.getItemMeta();
    	ItemMeta enderChestButtonMeta = enderChestButton.getItemMeta();
    	ItemMeta sendMoneyButtonMeta = sendMoneyButton.getItemMeta();
    	
    	ArrayList<String> recipientMessageLore = new ArrayList<String>();
    	ArrayList<String> sendButtonDisabledLore = new ArrayList<String>();
    	ArrayList<String> enderChestButtonLore = new ArrayList<String>();
    	ArrayList<String> sendMoneyButtonLore = new ArrayList<String>();

    	recipientMessageLore.add(ChatColor.GRAY + "Lege hier ein beschriebenes" + ChatColor.RESET);
    	recipientMessageLore.add(ChatColor.GRAY + "Buch ab, dessen Titel den Namen" + ChatColor.RESET);
    	recipientMessageLore.add(ChatColor.GRAY + "eines Spielers hat." + ChatColor.RESET);

    	sendButtonDisabledLore.add(ChatColor.GRAY + "Lege eine Empfänger" + ChatColor.RESET);
    	sendButtonDisabledLore.add(ChatColor.GRAY + "fest, um den Brief abzuschicken." + ChatColor.RESET);

    	enderChestButtonLore.add(ChatColor.GRAY + "Gibt dir Zugriff auf" + ChatColor.RESET);
    	enderChestButtonLore.add(ChatColor.GRAY + "deine Enderkiste." + ChatColor.RESET);
    	enderChestButtonLore.add(ChatColor.GRAY + "Du kehrst in dieses Fenster" + ChatColor.RESET);
    	enderChestButtonLore.add(ChatColor.GRAY + "nach dem Schließen zurueck." + ChatColor.RESET);
    	
    	sendMoneyButtonLore.add(ChatColor.GREEN + "Linksklick" + ChatColor.GRAY + " auf diesen Button" + ChatColor.RESET);
    	sendMoneyButtonLore.add(ChatColor.GRAY + "erhoeht die Menge an Geld" + ChatColor.RESET);
    	sendMoneyButtonLore.add(ChatColor.GRAY + "das du versendest" + ChatColor.RESET);
    	sendMoneyButtonLore.add(ChatColor.GRAY + "jeweils um " + Settings.Increments + "." + ChatColor.RESET);
    	sendMoneyButtonLore.add(ChatColor.BLUE + "Rechtsklick" + ChatColor.GRAY + " auf diesen Button" + ChatColor.RESET);
    	sendMoneyButtonLore.add(ChatColor.GRAY + "verringert die Menge an Geld " + Settings.Increments + "." + ChatColor.RESET);
//    	sendMoneyButtonLore.add(ChatColor.GRAY + "Minimum send amount of 2. Max" + ChatColor.RESET);
//    	sendMoneyButtonLore.add(ChatColor.GRAY + "send amount of 64." + ChatColor.RESET);
    	
    	recipientMessageMeta.setDisplayName(RECIPIENT_TITLE);
    	recipientMessageMeta.setLore(recipientMessageLore);

    	sendButtonEnabledMeta.setDisplayName(SEND_BUTTON_ON_TITLE);

    	cancelButtonMeta.setDisplayName(CANCEL_BUTTON_TITLE);
    	
    	enderChestButtonMeta.setDisplayName(ENDERCHEST_BUTTON_TITLE);
    	enderChestButtonMeta.setLore(enderChestButtonLore);
    	
    	sendMoneyButtonMeta.setDisplayName(MONEY_SEND_BUTTON_TITLE);
    	sendMoneyButtonMeta.setLore(sendMoneyButtonLore);

    	recipientMessage.setItemMeta(recipientMessageMeta);
    	sendButtonEnabled.setItemMeta(sendButtonEnabledMeta);
    	cancelButton.setItemMeta(cancelButtonMeta);
    	enderChestButton.setItemMeta(enderChestButtonMeta);
    	sendMoneyButton.setItemMeta(sendMoneyButtonMeta);

    	Inventory.setItem(0, recipientMessage);
    	if (Settings.EnableEnderchest) {
    		Inventory.setItem(8, enderChestButton);
    	}
    	Inventory.setItem(((Settings.MailWindowRows - 1) * 9) - 1, sendButtonEnabled);
    	Inventory.setItem((Settings.MailWindowRows * 9) - 1, cancelButton);
    	if ((Settings.EnableSendMoney == true) && (PaperMail.economy != null)){
    		if(Settings.MailWindowRows > 3){
    		Inventory.setItem(((Settings.MailWindowRows - 2) * 9) -1, sendMoneyButton);
    		}else{
    			Inventory.setItem(7, sendMoneyButton);
    		}
    	}
	}
	
	public void Show() {
		if (Settings.EnableItemMail) {
			Player.openInventory(Inventory);
			openGUIs.put(Player.getName(), this);
		}
	}
	
	public void SetClosed() {
		RemoveGUI(Player.getName());
		itemMailGUIs.remove(this);
	}
		
	public void close() {
		Player.closeInventory();
	}
	
	public void SendContents() throws IOException, InvalidConfigurationException {
		Player player = this.Player;
		ArrayList<ItemStack> sendingContents = new ArrayList<ItemStack>();
		String playerName = "";
		int numItems = 0;
		double itemCost = Settings.ItemCost;
		int amount = 0;
		ItemStack CraftStack;
		for (int i = 0; i < Inventory.getSize(); i++) {
			
			CraftStack = Inventory.getItem(i);
			if (CraftStack == null)
				continue;
			
			ItemMeta itemMeta = CraftStack.getItemMeta();
			if (itemMeta.getDisplayName() != SEND_BUTTON_ON_TITLE && 
				itemMeta.getDisplayName() != CANCEL_BUTTON_TITLE && 
				itemMeta.getDisplayName() != ENDERCHEST_BUTTON_TITLE &&
				itemMeta.getDisplayName() != RECIPIENT_TITLE &&
				CraftStack.getType() != Material.WRITTEN_BOOK &&
				itemMeta.getDisplayName() != MONEY_SEND_BUTTON_TITLE) {
				sendingContents.add(CraftStack);
				//count the total number of items to be sent
				numItems = numItems + CraftStack.getAmount();
			}
			//get the name from the Written book of the recipient
			if (CraftStack.getType() == Material.WRITTEN_BOOK && playerName == "") {
				BookMeta bookMeta = (BookMeta)itemMeta;
				Player p = Bukkit.getPlayer(bookMeta.getTitle());
				if (p != null) {
					playerName = p.getName();
				    } else {
				    OfflinePlayer op = Bukkit.getOfflinePlayer(bookMeta.getTitle());
				    if (op != null) {
				    	playerName = op.getName();
				        } else {
				        	playerName = bookMeta.getTitle();
				        	player.sendMessage(ChatColor.DARK_RED + "Der Spieler "  + playerName + " existiert nicht oder hat keinen Briefkasten. " + ChatColor.RESET);
				        }
				    }
			}
			//If Sending Money is enabled, count the amount the player wants to send and convert it to Bank Note later.
			if((itemMeta.getDisplayName() == MONEY_SEND_BUTTON_TITLE) && (Settings.EnableSendMoney == true)){
				if (CraftStack.getAmount() > 1){
				amount = CraftStack.getAmount();
				}
			}
		}
		//Calculate the money for each item sent if PerItemCosts is enabled
		if ((Settings.EnableMailCosts == true) && (Settings.PerItemCosts == true) && (Settings.ItemCost != 0) && (!this.Player.hasPermission(Permissions.COSTS_EXEMPT))){
				itemCost = numItems * itemCost;		
		}
		if(((Settings.EnableMailCosts == true && (numItems != 0) && (!this.Player.hasPermission(Permissions.COSTS_EXEMPT))) && (Settings.EnableSendMoney == true && amount > 1)) || (((Settings.EnableMailCosts == true) && (Settings.ItemCost != 0) && (!this.Player.hasPermission(Permissions.COSTS_EXEMPT))) && (Settings.EnableSendMoney == true || amount > 1))){
			//if itemcosts and sending money is enabled
			if((Settings.EnableMailCosts == true && numItems != 0) && (Settings.EnableSendMoney == true && amount > 1)){
				double totalcost = itemCost + amount;
				if(PaperMailEconomy.hasMoney(totalcost, player)){
					PaperMailEconomy.takeMoney(itemCost, player);
					if(amount > 1){
					CraftStack = PaperMailEconomy.getBankNote(amount, player);
					sendingContents.add(CraftStack);
					}
				}
			//if only itemCosts is enabled
			}else if(PaperMailEconomy.hasMoney(itemCost, player) && (Settings.EnableMailCosts == true) && (numItems != 0) && ((Settings.EnableSendMoney == false) || (amount < 2))){
				PaperMailEconomy.takeMoney(itemCost, player);
			}
		}
		//If there are no items to be sent, yet player is still sending money.
		if((Settings.EnableSendMoney == true) && ((this.Player.hasPermission(Permissions.COSTS_EXEMPT)) || (Settings.EnableMailCosts == false) || (itemCost == 0) || (numItems == 0))){
			if(amount > 1){
				CraftStack = PaperMailEconomy.getBankNote(amount, player);
				sendingContents.add(CraftStack);
				}
		}
		
		//add the items to the recipients inbox.
			Inbox inbox = Inbox.GetInbox(playerName);
			inbox.AddItems(sendingContents, Player);
		
		if (paperSent) {
			ItemStack itemInHand = Player.getInventory().getItemInHand();
			itemInHand.setAmount(itemInHand.getAmount() - 1);
			Player.setItemInHand(itemInHand);
		}
	}
	
	public static PaperMailGUI GetGUIfromPlayer(Player player) {
		for (PaperMailGUI gui : itemMailGUIs) {
			if (gui.Player == player)
				return gui;
		}
		return null;
	}
}

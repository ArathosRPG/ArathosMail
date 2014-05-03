package com.github.derwisch.paperMail;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PaperMailCommandExecutor implements CommandExecutor {
    
	private PaperMail plugin;
	private double Cost = Settings.Price;
 
	public PaperMailCommandExecutor(PaperMail plugin) {
		this.plugin = plugin;
		this.plugin.getLogger().info("ItemMailCommandExecutor initialized");
	}
 
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (cmd.getName().equalsIgnoreCase("papermail")){
			if (!(sender instanceof Player) && args.length > 0) {
				sender.sendMessage("Aktuelle Version: " + PaperMail.instance.getDescription().getVersion());
				return true;
			} else {
				if (args.length == 0) {
					sender.sendMessage("Aktuelle Version: " + PaperMail.instance.getDescription().getVersion());
					return true;
				}
				
				Player player = (Player) sender;
				
				
				if (!args[0].toLowerCase().equals("sendtext") && !args[0].toLowerCase().equals("createbox")) {
					player.sendMessage(ChatColor.DARK_RED + "invalid argument \"" + args[0] + "\"" + ChatColor.RESET);
					return true;
				}

				if (Settings.EnableTextMail && args[0].toLowerCase().equals("sendtext") && player.hasPermission(Permissions.SEND_TEXT_PERM)) {
					if (args.length < 3) {
						if (args.length < 2) {
							player.sendMessage(ChatColor.DARK_RED + "Es fehlen einige Angaben!" + ChatColor.RESET);
							return true;
						}
						player.sendMessage(ChatColor.DARK_RED + "Der Text fehlt!" + ChatColor.RESET);
						return true;
					}
					//if player isn't cost exempt and costs is enabled and price is set, try to send textmail
					if((Settings.EnableMailCosts == true) && (Settings.Price != 0) && (!player.hasPermission(Permissions.COSTS_EXEMPT))){
						//check if player has the correct amount of currency
						if(PaperMailEconomy.hasMoney(Settings.Price, player) == true){
							sendText(player, args);
							PaperMailEconomy.takeMoney(Cost, player);
							return true;
						//if player doesn't have enough money don't send textmail
						}else{
	                    	player.sendMessage(ChatColor.RED + "Du hast nicht genügend Geld, um den Brief abzuschicken!");
	                    	return true;
						}
                    }
					//if player is cost exempt or price is zero or mailcosts is off send textmail
					if((Settings.EnableMailCosts == false) || (player.hasPermission(Permissions.COSTS_EXEMPT) && (Settings.EnableMailCosts == true)) || ((Settings.EnableMailCosts == true) && (Settings.Price == 0) && (!player.hasPermission(Permissions.COSTS_EXEMPT)))){
						sendText(player, args);
						return true;
					}
				}
				
				//create inbox chest
				if (args[0].toLowerCase().equals("createbox")) {
					Inbox inbox = null;
					
					if (args.length == 1 && (player.hasPermission(Permissions.CREATE_CHEST_SELF_PERM)  || player.hasPermission(Permissions.CREATE_CHEST_ALL_PERM))) {
						try {
							inbox = Inbox.GetInbox(player.getName());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InvalidConfigurationException e) {
							e.printStackTrace();
						}
					} else if (args.length == 2 && player.hasPermission(Permissions.CREATE_CHEST_ALL_PERM)) {
						try {
							inbox = Inbox.GetInbox(args[1]);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InvalidConfigurationException e) {
							e.printStackTrace();
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Zu viele Argumente!" + ChatColor.RESET);
						return true;
					}
					
					Block block = player.getTargetBlock(null, 10);
					
					if (block != null && block.getType() == Material.CHEST) {

						
						Chest chest = (Chest)block.getState();
						
						inbox.SetChest(chest);
						
						player.sendMessage(ChatColor.DARK_GREEN + "Briefkasten erstellt!" + ChatColor.RESET);
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Du musst eine Kiste anvisieren!" + ChatColor.RESET);
						return true;
					}
					
				}
			}
			return true;
		}
		return false;
	}
	
	//Send the textmail
	public void sendText(Player player, String[] args){
		
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = itemStack.getItemMeta();
	
		itemMeta.setDisplayName(ChatColor.WHITE + "Brief von " + player.getName() + ChatColor.RESET);
		ArrayList<String> lines = new ArrayList<String>();
	
		int count = 0;
		String currentLine = "";
	
		for (int i = 2; i < args.length; i++) {
			currentLine += args[i] + " ";
			count += args[i].length() + 1;
			if (++count >= 20) {
				count = 0;
				lines.add(ChatColor.GRAY + currentLine + ChatColor.RESET);
				currentLine = "";
			}
	}
	
	if (currentLine != "") {
		lines.add(ChatColor.GRAY + currentLine + ChatColor.RESET);	
	}
	
	itemMeta.setLore(lines);
	itemStack.setItemMeta(itemMeta);
	String playerName = args[1];
	Player p = Bukkit.getPlayer(playerName);
	if (p != null) {
		playerName = p.getName();
	    } else {
	    OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
	    if (op != null) {
	    	playerName = op.getName();
	    	player.sendMessage(ChatColor.GREEN + "Player " + playerName + " ist offline. Der Brief wurde an seinen Briefkasten geschickt!" + ChatColor.RESET);
	        } else {
	        	playerName = args[1];
	        	player.sendMessage(ChatColor.DARK_RED + "Player "  + playerName + " existiert nicht oder hat noch keinen Briefkasten." + ChatColor.RESET);
	        }
	    }
	try {
		Inbox.GetInbox(playerName).AddItem(itemStack, player);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	player.sendMessage(ChatColor.DARK_GREEN + "Textnachricht an "  + playerName + " gesendet!" + ChatColor.RESET);
	}
}

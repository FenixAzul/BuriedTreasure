package net.fenixazul.bt;

import java.util.HashMap;
//import java.util.logging.Level; For Future use
import java.util.logging.Logger;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 * Buried Treasure Alpha Code
 * 
 * @author FenixAzul, with the Help[HUGE] of Humsas & StrikeForceZero <3.
 */
public class BTPlayerListener extends PlayerListener {
	private final BuriedTreasure plugin;
	public static final Logger log = Logger.getLogger("Minecraft");

	public BTPlayerListener(BuriedTreasure instance) {
		plugin = instance;
	}

	/*
	 * @Override public void onPlayerQuit(PlayerEvent event) { //do the code for
	 * change player position to the one before it joined arena
	 * 
	 * }
	 */

	public void onPlayerInteract(PlayerInteractEvent event) {
		
		//Wand action listener stuff
		if (plugin.isBearingWand(event.getPlayer())) {
			if (event.getPlayer().getItemInHand().getTypeId() == BuriedTreasure.wandID
					&& plugin.canAccess(event.getPlayer(), false,
							"bt.admin.wand")) {
				Block clickedblock = event.getClickedBlock();
				// Set the one to set based on action
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

					getSelection(event.getPlayer()).loc1 = clickedblock
							.getLocation();
					event.getPlayer().sendMessage(ChatColor.GOLD +
							"Position 1 Selected: " + clickedblock.getX() + " "
									+ clickedblock.getY() + " "
									+ clickedblock.getZ());
				}

				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					getSelection(event.getPlayer()).loc2 = clickedblock
							.getLocation();
					event.getPlayer().sendMessage(ChatColor.GOLD +
							"Position 2 Selected: " + clickedblock.getX() + " "
									+ clickedblock.getY() + " "
									+ clickedblock.getZ());
				}

			} else
				event.getPlayer().sendMessage(
						ChatColor.RED
								+ "Use the Wand to Set the Locations or use "
								+ ChatColor.DARK_GREEN + "/bt wand"
								+ ChatColor.RED + ", to exit");
			// bt wand <arena name>
		}
		//POWERUPs Codez
		if(event.getAction() == Action.LEFT_CLICK_BLOCK){
			if(event.getClickedBlock().getType().equals(Material.CHEST)){
				try{
				Chest chest = (Chest) event.getClickedBlock().getState();
				
				if(chest.getInventory() !=null) { //chest.getInventory().getSize() >= 0 && 
					
						// The chest is not empty.
						ItemStack[] inventory = chest.getInventory().getContents();
						
					
						for(ItemStack i : inventory) {
							
							
				//		event.getPlayer().sendMessage("This chest contains " + i.getAmount() + " of the material" + i.getType());
						if(i !=null)
						{
							if(i.getTypeId() > 0){
								ItemStack item = new ItemStack(i.getType().getId(), 1);
								plugin.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " got a "+ item.getType().toString()  +" POWER-UP");
								event.getPlayer().getInventory().clear();	
						
								event.getPlayer().setItemInHand(item);
								chest.getInventory().clear();
								event.getClickedBlock().setType(Material.AIR);
							}
						}
						
						}
						
						
					}
				}catch (NullPointerException e) {
					// TODO: handle exception
					plugin.getServer().broadcastMessage("null pointer:"+ e);
				}
			
				
			}
			
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(event.getClickedBlock().getType().equals(Material.CHEST)){
				event.setCancelled(true);
				
			}
			
			
			
		}

	}

	public static HashMap<String, CuboidSelection> selections = new HashMap<String, CuboidSelection>();

	public static CuboidSelection getSelection(String playername) {
		CuboidSelection sel = selections.get(playername);

		if (sel == null) {
			sel = new CuboidSelection(null,null);
			selections.put(playername, sel);  
		}

		return sel;
	}

	public static CuboidSelection getSelection(Player player) {
		return getSelection(player.getName());
	}

	public static class CuboidSelection {
		public Location loc1;
		public Location loc2;
		public CuboidSelection(Location pos1, Location pos2) {
			this.loc1 = loc1;
			this.loc2 = loc2;
		}
	}
}

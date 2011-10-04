package net.fenixazul.bt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import com.nijiko.permissions.PermissionHandler;

import net.fenixazul.bt.BTPlayerListener.CuboidSelection;
import net.fenixazul.bt.BTArena;

public class BuriedTreasure extends JavaPlugin {
	private static final Logger log = Logger.getLogger("BuriedTreasure");
	public PermissionHandler Permissions;
	private final BTPlayerListener playerListener = new BTPlayerListener(this);
	private final BTBlockListener blockListener = new BTBlockListener(this);
	private boolean UsePermissions = false;
	public PluginDescriptionFile info = null;
	private List<Player> wandBearers = new ArrayList<Player>();
	public static int wandID;
	public StandardConfig arenacfg = new StandardConfig();
	public 	CuboidSelection sel ;
	private List<BTArena> arenas = new ArrayList<BTArena>();
	private BTArena activeArena;

	
	@Override
	public void onDisable() {
		System.out.println("Buried Treasure Disabled!");
		
	}

	@Override
	public void onEnable() {
	
		arenacfg.loadArenasToMEM(arenas);
		//wandID = arenacfg.getConfig().getInt("wand", 269);
		wandID = arenacfg.getWandId();
		
		registerEvents();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");
	}
	

	
	private void registerEvents() {
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener,
				Priority.Normal, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("bt")) {
			// if no arguments
			String subc = "";
			try {
				subc = args[0];
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				player.sendMessage(ChatColor.WHITE + "Type " + ChatColor.GOLD
						+ "/bt help " + ChatColor.WHITE
						+ "to see commands and their usage");
				return true;
			}

			subc = subc.toLowerCase();
			String arg = null;
			try {
				arg = args[1];
			} catch (Exception e) {
				arg = null;
			}

			if (subc.equals("help")) {
				player.sendMessage(ChatColor.GREEN + "Commands:");
				if (canAccess(player, false, "bt.play", "bt.admin"))
					player.sendMessage(ChatColor.GOLD + "/bt list"
							+ ChatColor.WHITE + "- List all the avaible arenas");
				if (canAccess(player, true, "bt.admin"))
					player.sendMessage(ChatColor.GOLD + "/bt wand  "
							+ ChatColor.WHITE
							+ "- gives you a wand for cuboid selection.");
				if (canAccess(player, true, "bt.admin"))
					player.sendMessage(ChatColor.GOLD
							+ "/bt createarena <arenaname>" + ChatColor.WHITE
							+ "- Creates an arena");
				if (canAccess(player, true, "bt.admin"))
					player.sendMessage(ChatColor.GOLD
							+ "/bt deletearena <arenaname>" + ChatColor.WHITE
							+ "- Deletes an arena");
				if (canAccess(player, true, "bt.admin"))
					player.sendMessage(ChatColor.GOLD + "/bt setlobbyspawn"
							+ ChatColor.WHITE
							+ "- set the lobby spawn point for the arena");
				if (canAccess(player, true, "bt.admin"))
					player.sendMessage(ChatColor.GOLD
							+ "/bt setspawn <s[number]>" + ChatColor.WHITE
							+ "- Defines spawns for the players in the arena");
				if (canAccess(player, true, "bt.admin"))
					player.sendMessage(ChatColor.GOLD
							+ "/bt active <arenaname>"
							+ ChatColor.WHITE
							+ "- Sets an arena active for resetting spawns and configs");
				if (canAccess(player, false, "bt.play", "bt.admin"))
					player.sendMessage(ChatColor.GOLD + "/bt join <arena name>"
							+ ChatColor.WHITE
							+ "- join a Buried Treasure Arena");
				if (canAccess(player, false, "bt.play", "bt.admin"))
					player.sendMessage(ChatColor.GOLD + "/bt leave"
							+ ChatColor.WHITE
							+ "- Command for leaving an arena");

				return true;
			}
			if (!canAccess(player, false, "bt.play", "bt.admin")) {
				player.sendMessage(ChatColor.RED
						+ "You are not allowed to do that.");
				return true;
			}
			if (subc.equals("list")) {

				List<String> arenasList = arenacfg.arenaNames();
				StringBuilder arenas = new StringBuilder("BT Arenas:");
				// for(List<String> l : arenasList) {
				for (String s : arenasList) {
					arenas.append(' ').append(s);// += " "+s;
				}
				// }
				if (arenasList.size() == 0)
					player.sendMessage("No BT Arenas Defined");
				else
					player.sendMessage(String.valueOf(arenas));
			}
			if (subc.equals("join")) {
				// if()
				if (hasEmptyInventory(player)) {

					player.getLocation();
					if (arg == null) {
						player.sendMessage(ChatColor.WHITE + "Usage: "
								+ ChatColor.RED + "/bt join <arena name> ");
						return true;
					}

				} else {
					player.sendMessage("You must clear your inventory to join a BuriedTreasure arena.");
				}

			}
			if (subc.equals("leave")) {
				// for leaving the running game or lobby
			};
			if (canAccess(player, true, "bt.admin")) {

				if (subc.equals("reload")) {
					arenacfg = new StandardConfig();
					player.sendMessage("Buried Treasure Reloaded");
				}

				if (subc.equals("save")) {
					arenacfg.saveArenasFromMEM(arenas);
					player.sendMessage("Arenas Saved");
				}
				if (subc.equals("wand")) {
					if (!wandBearers.contains(player)) {
						wandBearers.add(player);
						player.getInventory().addItem(
								(new ItemStack(wandID, 1)));
						player.sendMessage("Left click a block with the wand to set the first corner of the cuboid. Right-click a block to choose the second corner. ");
					} else {
						wandBearers.remove(player);
						player.getInventory()
								.remove((new ItemStack(wandID, 1)));
						player.sendMessage("Wand Deactivated");
					}

					// playa.getInventory().setHelmet(new ItemStack(90,1)); set
					// player helmet
				}

				if (subc.equals("createarena")) {
					if (checkCuboid(player)) {

						BTArena newArena = new BTArena(subc, sel.loc1, sel.loc2, player.getWorld().getName());
						// example
						arenas.add(newArena);
						// usage /bt createarena <arenaname>
					}

				}

				if (subc.equals("deletearena")) {

					if (arg.equals("")) {
						player.sendMessage(ChatColor.RED + "No arena selected!");
						return true;
					}
					BTArena arena = getArena(arg);
					if (arena == null) { //arenacfg.getString("arenas." + arg) == null) {
						player.sendMessage(ChatColor.RED + "This arena "
								+ ChatColor.GOLD + arg + ChatColor.RED
								+ " does not exist!");
						return true;
					} else {
						// usage /bt deletearena <arenaname>
						//arenacfg.removeProperty("arenas." + arg);
						//arenacfg.save();
						arenas.remove(arena);
						arenacfg.deleteArena(arg);
						arenacfg.saveArenasFromMEM(arenas);
						player.sendMessage(ChatColor.GOLD + arg + " "
								+ ChatColor.WHITE + "arena was removed.");
						return true;
					}

				}
				if (subc.equals("setlobbyspawn")) {
					//TODO
					 if(playerHaveActive(player) == true)
					 activeArena = getPlayerActiveArena(player);
					 
					 arenas.get(arenas.indexOf(activeArena.getArenaName())).setLobbyspawn(player.getLocation());
				}

				if (subc.equals("setspawn")) {
					// usage /bt setspawn <s[number]>
					// it also override a spawn with the same name.

				}

				if (subc.equals("active")) {
					if (arg.equals("")) {
						player.sendMessage(ChatColor.RED + "No arena selected!");
					} else if (!arenaexsist(arg)) {
						player.sendMessage(ChatColor.RED + "This arena "
								+ ChatColor.GOLD + arg + ChatColor.RED
								+ " does not exist!");
						return true;
					} else {
						player.sendMessage(ChatColor.GOLD + arg + " "
								+ ChatColor.WHITE + "arena was selected.");
						// /bt active <arenaname>
						// bt setprize 267
						activeArena = getArena(arg);
					}
				}
				if (subc.equals("setname")) {
					if (activeArena != null) {
						if (arg.equals("")) {
							player.sendMessage(ChatColor.RED
									+ "No arena specified!");
						} else if (arenaexsist(arg)) {
							player.sendMessage(ChatColor.RED + "This arena "
									+ ChatColor.GOLD + arg + ChatColor.RED
									+ " does not exist!");
							return true;
						} else {
							player.sendMessage(ChatColor.GOLD
									+ activeArena.getArenaName() + " "
									+ ChatColor.WHITE + "was named to " + arg);
							activeArena.setArenaName(arg);
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED + "No arena selected!");
						return true;
					}
				}

			} else
				player.sendMessage(ChatColor.RED
						+ "You are not allowed to do that.");

		}
		
		return true;
	}
	
	
	private BTArena getPlayerActiveArena(Player player) {
		return activeArena;
		//TODO
	}

	private boolean playerHaveActive(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean arenaexsist(String name){
		for(BTArena arena : arenas)
		{
			if(arena.getArenaName().equals(name))
				return true;
		}
		return false;
	}
	
	public BTArena getArena(String name)	{
		for(BTArena arena : arenas)	{
			if(arena.getArenaName().equals(name))
				return arena;
		}
		return null;
		
	}

	public boolean canAccess(Player player, boolean OpCommand,
			String... permissions) {
		if (UsePermissions) {
			for (String perm : permissions) {
				if (Permissions.has(player, perm))
					return true;
			}
		} else {
			if (!OpCommand)
				return true;
			else
				return player.isOp();
		}

		return false;
	}
	
	private boolean checkCuboid(Player player) {
	sel = BTPlayerListener.getSelection(player);
		
		//Bukkit.getServer().broadcastMessage(""+Math.abs(sel.loc1.getY() - sel.loc2.getY()));
		if (sel.loc1 == null && sel.loc2 == null) {
			player.sendMessage("Selection empty");
			return false;
		} else if (sel.loc2 != null && sel.loc1 == null) {
			player.sendMessage("Position 1 Missing");
			return false;
		} else if (sel.loc1 != null && sel.loc2 == null) {
			player.sendMessage("Position 2 Missing");
			return false;
		} else if (Math.abs(sel.loc1.getY() - sel.loc2.getY()) == 1) {
			return true;
		} else {
			player.sendMessage("Cuboid Height Must be 2");
			return false;
		}
	}
	public static boolean hasEmptyInventory(Player p) {
		ItemStack[] inventory = p.getInventory().getContents();
		ItemStack[] armor = p.getInventory().getArmorContents();

		// For inventory, check for null
		for (ItemStack stack : inventory)
			if (stack != null)
				return false;

		// For armor, check for id 0, or AIR
		for (ItemStack stack : armor)
			if (stack.getTypeId() != 0)
				return false;

		return true;
	}
	public boolean isBearingWand(Player player) {
		if (wandBearers.contains(player))
			return true;
		else
			return false;
	}
	
	
	public static void log(Level info2, String string) {
			log.log(info2,string);
		
	}
}

package net.fenixazul.bt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.wolvereness.config.Config;

public class StandardConfig extends Config {
	
	public StandardConfig() {
		super("config.yml");
	}
	@Override
	public String getName() {
		return "BuriedTreasure";
	}

	@Override
	public void defaults() {
		//getConfig().getString("node", "default");
		getWandId();
	}
	public int getWandId() {
		return getConfig().getInt("wand",269);
			
	}
	public void loadArenasToMEM(List<BTArena> arenas){
		BuriedTreasure.log(Level.INFO, "[BT] Loading Arenas...");
		List<String> arenaNames = getConfig().getKeys("arenas");
			for(String arena : arenaNames){
				BuriedTreasure.log(Level.INFO, "[BT] Loading: "+arena);
				World world = Bukkit.getServer().getWorld(getConfig().getString("arenas."+arena+".world"));
				double x1 = getConfig().getInt("arenas."+arena+".p1.x",0);
				double y1 = getConfig().getInt("arenas."+arena+".p1.y",0);
				double z1 = getConfig().getInt("arenas."+arena+".p1.z",0);
				double x2 = getConfig().getInt("arenas."+arena+".p2.x",0);
				double y2 = getConfig().getInt("arenas."+arena+".p2.y",0);
				double z2 = getConfig().getInt("arenas."+arena+".p2.z",0);
				
				BTArena newArena = new BTArena(arena, new Location(world, x1, y1, z1), new Location(world, x2, y2, z2), getConfig().getString("arenas."+arena+".world"));
				
				
				double lx = getConfig().getInt("arenas."+arena+".lobbyspawn.x",0);
				double ly = getConfig().getInt("arenas."+arena+".lobbyspawn.y",0);
				double lz = getConfig().getInt("arenas."+arena+".lobbyspawn.z",0);
				
				//VERIFY
				List<Location> spawns = new ArrayList<Location>();

				int size = getConfig().getKeys("arenas." + arena + ".gamespawns").size();
				System.out.println(size + " Size of Gamespawns of " + arena);
				for(int i = 1; i <= size; i++){
				 
					
				int x= getConfig().getInt("arenas."+ arena +".gamespawns.s"+ i + ".x", 0);
				int y= getConfig().getInt("arenas."+ arena +".gamespawns.s"+ i + ".y", 0);
				int z= getConfig().getInt("arenas."+ arena +".gamespawns.s"+ i + ".z", 0);
				spawns.add(new Location(world, x, y, z));
				
				}
				
				
				newArena.setGameSpawns(spawns);
				
				String[] winprize = (getConfig().getString("arenas."+ arena + ".config.winprize")).split(",");
				List<Integer> powerups = getConfig().getIntList("arenas."+ arena + ".config.powerups", null);//CHECK
				Boolean pvp = getConfig().getBoolean("arenas."+ arena + ".config.pvp" , false);
				
				
				newArena.setLobbyspawn(new Location(world, lx, ly, lz));
				
				newArena.setwinPrize(new ItemStack(Integer.parseInt(winprize[0]), Integer.parseInt(winprize[1])));
				newArena.setPowerUps(powerups);
				newArena.setpvp(pvp);
				if(!arenas.contains(newArena))
					arenas.add(newArena);
				BuriedTreasure.log(Level.INFO, "[BT] Loaded: "+arena);
				
			}
		
		BuriedTreasure.log(Level.INFO, "[BT] Loaded "+arenas.size()+" Arenas");
	}
	public void saveArenasFromMEM(List<BTArena> arenas){
		BuriedTreasure.log(Level.INFO, "[BT] Saving Arenas...");
		getConfig().removeProperty("arenas");
		
		for(BTArena arena : arenas) {
				BuriedTreasure.log(Level.INFO, "[BT] Saving: "+arena.getArenaName());
			
				String arenaName = arena.getArenaName();
			//getConfig().setProperty("arenas", arenaName );
				
					
				getConfig().setProperty("arenas."+arenaName+".world",arena.getWorldName());
				double x1 = arena.getPos1().getX();
				double y1 = arena.getPos1().getY();
				double z1 = arena.getPos1().getZ();
				double x2 = arena.getPos2().getX();
				double y2 = arena.getPos2().getY();
				double z2 = arena.getPos2().getZ();

				
				getConfig().setProperty("arenas."+ arenaName+".lobbyspawn", arena.getLobbyspawn());
				
				getConfig().setProperty("arenas."+arenaName+".p2.x",x2);
				getConfig().setProperty("arenas."+arenaName+".p2.y",y2);
				getConfig().setProperty("arenas."+arenaName+".p2.z",z2);	
				getConfig().setProperty("arenas."+arenaName+".p1.x",x1);
				getConfig().setProperty("arenas."+arenaName+".p1.y",y1);
				getConfig().setProperty("arenas."+arenaName+".p1.z",z1);
				
				getConfig().setProperty("arenas."+ arenaName +".lobbyspawn.x", arena.getLobbyspawn().getX());
				getConfig().setProperty("arenas."+ arenaName +".lobbyspawn.y", arena.getLobbyspawn().getY());
				getConfig().setProperty("arenas."+ arenaName +".lobbyspawn.z", arena.getLobbyspawn().getZ());
				
				List<Location> gameSpawns = arena.getGameSpawns();
				int count = 1;
				for(Location spawn : gameSpawns){
				 
				getConfig().setProperty("arenas."+ arenaName +".gamespawns.s"+ count + ".x", spawn.getX());
				getConfig().setProperty("arenas."+ arenaName +".gamespawns.s"+ count + ".y", spawn.getY());
				getConfig().setProperty("arenas."+ arenaName +".gamespawns.s"+ count + ".z", spawn.getZ());
				count++;
				}
				
				getConfig().setProperty("arenas."+ arenaName + ".config.winprize", arena.getwinPrize().getTypeId() + "," +  arena.getwinPrize().getAmount());
				getConfig().setProperty("arenas."+ arenaName + ".config.powerups", arena.getPowerUps());
				getConfig().setProperty("arenas."+ arenaName + ".config.pvp" , arena.getpvp());
				
					
				BuriedTreasure.log(Level.INFO, arenaName);
				BuriedTreasure.log(Level.INFO, "[BT] Saved: "+arenaName);
				
			}
		getConfig().save();
		BuriedTreasure.log(Level.INFO, "[BT] Saved "+arenas.size()+" Arenas");
	}
	public void deleteArena(String ArenaName){
		getConfig().removeProperty("arenas."+ ArenaName);
		
	}
	public List<String> arenaNames() {
		return getConfig().getKeys("arenas");
	}
	public Location getlocation(String node,World world){
		double x = getConfig().getInt(node, 0);
		double y = getConfig().getInt(node, 0);
		double z = getConfig().getInt(node, 0);
		return new Location(world,x,y,z); 
	}

}

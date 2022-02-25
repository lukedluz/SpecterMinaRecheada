package PombaMinaRecheada;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import PombaMinaRecheada.Comandos.MinaRecheada;
import PombaMinaRecheada.Eventos.DataBase;
import PombaMinaRecheada.Eventos.Evento;

public class Main extends JavaPlugin {
	
	public static boolean estado;
	public static ArrayList<String> minarecheada = new ArrayList<>();
	public static Main m;
	
	public void onEnable() {
		estado = false;
		m = this;
		
		DataBase.create();
		DataBase.SaveConfig();
		
		Bukkit.getPluginManager().registerEvents(new MinaRecheada(), this);
		Bukkit.getPluginManager().registerEvents(new Evento(), this);
		getCommand("minarecheada").setExecutor(new MinaRecheada());
	}
}

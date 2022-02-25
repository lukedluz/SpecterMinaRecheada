package PombaMinaRecheada.Eventos;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PombaMinaRecheada.Main;

public class DataBase {

	static File f;
	public static FileConfiguration fc;
	static DataBase m;

	public static void create() {
		f = new File(Main.m.getDataFolder() + "/databases/database.db");
		fc = YamlConfiguration.loadConfiguration(f);
	}

	public static void SaveConfig() {
		try {
			fc.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataBase config() {
		return m;
	}
	

}

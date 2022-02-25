package PombaMinaRecheada.Eventos;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import PombaMinaRecheada.Main;

public class Evento implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Main.minarecheada.contains(p.getName())) {
			Main.minarecheada.remove(p.getName());
		}
	}
	
	@EventHandler
	public void PlayerPreProcess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (Main.minarecheada.contains(p.getName())) {
			if (e.getMessage().toLowerCase().startsWith("/spawn")) {
				Main.minarecheada.remove(p.getName());
			}
		}
	}
}
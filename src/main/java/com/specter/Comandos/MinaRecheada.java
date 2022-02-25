package PombaMinaRecheada.Comandos;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import PombaMinaRecheada.Main;
import PombaMinaRecheada.Eventos.DataBase;
import PombaPicareta.API.PicaretaAPI;
import PombaPicareta.MySQL.PicaretaMySQL;
import PombaPicareta.MySQL.PontosMySQL;
import net.lightshard.prisonmines.PrisonMines;
import net.lightshard.prisonmines.mine.reset.ResetType;

public class MinaRecheada implements Listener, CommandExecutor {
	
	public static boolean isEmpty(Player p) {
		boolean isEmpty = true;
		for (ItemStack item : p.getInventory().getContents()) {
		    if(item != null) {
		        isEmpty = false;
		        break;
		    }
		}
		if(isEmpty) {
		   return true;
		} else {
		    return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandSender p = sender;

		if (command.getName().equalsIgnoreCase("minarecheada")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player p2 = (Player) sender;
					if (Main.estado == true) {
						if (!isEmpty(p2)) {
							p2.sendMessage("§cVocê deve estar com o inventario vazio para entrar nesse evento");
							return true;
						}
						World world = Bukkit.getWorld(DataBase.fc.getString("Entrada.World"));
						Double x = DataBase.fc.getDouble("Entrada.X");
						Double y = DataBase.fc.getDouble("Entrada.Y");
						Double z = DataBase.fc.getDouble("Entrada.Z");
						p2.teleport(new Location(world, x, y, z));
						Main.minarecheada.add(p.getName());
						Main.m.getServer().getScheduler().runTaskAsynchronously(Main.m, new Runnable() {
							public void run() {
								if (PontosMySQL.Have(p.getName()) == false) {
									PontosMySQL.CreatePontos(p.getName(), 0.0);
								}
								if (PicaretaMySQL.Have(p.getName()) == false) {
									PicaretaMySQL.CreatePicareta(p.getName(), 0);
								}
							}
						});
						PicaretaAPI.GivePicareta(p2);
					} else {
						p.sendMessage("§cO evento Mina Recheada não está aberto no momento!");
					}
				}
			} else if (args.length == 1) {
				if (p.hasPermission("pomba.baseperdida")) {
					if (args[0].equalsIgnoreCase("help")) {
						p.sendMessage("");
						p.sendMessage("§a/minarecheada iniciar - §7Inicia o evento Mina Recheada");
						p.sendMessage("§a/minarecheada finalizar - §7Finaliza o evento Mina Recheada");
						p.sendMessage("§a/minarecheada set <entrada/saida> - §7seta as posições do evento");
					} else if (args[0].equalsIgnoreCase("iniciar")) {
						if (Main.estado == true) {
							p.sendMessage("§cEsse evento já está ocorrendo");
							return true;
						}
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§6§lEvento Mina Recheada");
						Bukkit.broadcastMessage("§aDigite §c/minarecheada §apara participar");
						Bukkit.broadcastMessage("§aO evento sera finalizado em 10 minutos");
						Bukkit.broadcastMessage("");
						PrisonMines.getAPI().getByName("MinaRecheada").reset();
						PrisonMines.getAPI().getByName("MinaRecheada").getTimedReset().setEnabled(true);
						PrisonMines.getAPI().getByName("MinaRecheada").setResetType(ResetType.GRADUAL);
						PrisonMines.getAPI().getByName("MinaRecheada").setResetMessagesEnabled(false);
						PrisonMines.getAPI().getByName("MinaRecheada").getTimedReset().setResetInterval(5 * 60);
						Main.estado = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Main.m, new Runnable() {
							public void run() {
								if (Main.estado == true) {
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§6§lEvento Mina Recheada");
									Bukkit.broadcastMessage("§aO evento mina recheada foi finalizado");
									Bukkit.broadcastMessage("");
									Main.estado = false;
									PrisonMines.getAPI().getByName("MinaRecheada").getTimedReset().setEnabled(false);
									Iterator<Block> it = PrisonMines.getAPI().getByName("MinaRecheada").getRegion()
											.iterator();
									while (it.hasNext()) {
										Block block = (Block) it.next();
										block.setType(Material.AIR);
									}
									for (Player t : Bukkit.getOnlinePlayers()) {
										if (Main.minarecheada.contains(t.getName())) {
											World world = Bukkit.getWorld(DataBase.fc.getString("Saida.World"));
											Double x = DataBase.fc.getDouble("Saida.X");
											Double y = DataBase.fc.getDouble("Saida.Y");
											Double z = DataBase.fc.getDouble("Saida.Z");
											t.teleport(new Location(world, x, y, z));
											PicaretaAPI.RemovePicareta(t);
										}
									}
								}
							}
						}, 10 * 1200);
					} else if (args[0].equalsIgnoreCase("finalizar")) {
						if (Main.estado == false) {
							p.sendMessage("§cEsse evento não esta ocorrendo");
							return true;
						}
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§6§lEvento Mina Recheada");
						Bukkit.broadcastMessage("§aO evento mina recheada foi finalizado");
						Bukkit.broadcastMessage("");
						Main.estado = false;
						PrisonMines.getAPI().getByName("MinaRecheada").getTimedReset().setEnabled(false);
						Iterator<Block> it = PrisonMines.getAPI().getByName("MinaRecheada").getRegion().iterator();
						while (it.hasNext()) {
							Block block = (Block) it.next();
							block.setType(Material.AIR);
						}
						for (Player t : Bukkit.getOnlinePlayers()) {
							if (Main.minarecheada.contains(t.getName())) {
								World world = Bukkit.getWorld(DataBase.fc.getString("Saida.World"));
								Double x = DataBase.fc.getDouble("Saida.X");
								Double y = DataBase.fc.getDouble("Saida.Y");
								Double z = DataBase.fc.getDouble("Saida.Z");
								t.teleport(new Location(world, x, y, z));
								PicaretaAPI.RemovePicareta(t);
							}
						}
					}
				} else {
					p.sendMessage("Você não tem permissão para isso");
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("set")) {
					if (sender instanceof Player) {
						Player p2 = (Player) sender;
						if (args[1].equalsIgnoreCase("entrada")) {
							DataBase.fc.set("Entrada.World", p2.getLocation().getWorld().getName());
							DataBase.fc.set("Entrada.X", p2.getLocation().getX());
							DataBase.fc.set("Entrada.Y", p2.getLocation().getY());
							DataBase.fc.set("Entrada.Z", p2.getLocation().getZ());
							DataBase.SaveConfig();
							p2.sendMessage("§aEntrada setada com sucesso");
						} else if (args[1].equalsIgnoreCase("saida")) {
							DataBase.fc.set("Saida.World", p2.getLocation().getWorld().getName());
							DataBase.fc.set("Saida.X", p2.getLocation().getX());
							DataBase.fc.set("Saida.Y", p2.getLocation().getY());
							DataBase.fc.set("Saida.Z", p2.getLocation().getZ());
							DataBase.SaveConfig();
							p2.sendMessage("§aSaida setada com sucesso");
						}
					}
				}
			}
		}
		return false;
	}

}

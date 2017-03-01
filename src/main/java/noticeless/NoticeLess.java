package noticeless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;
import commands.NoticeCommand;
import database.DataBase;
import tasks.UpdateNoticeTask;

public class NoticeLess extends PluginBase implements Listener {
	public static HashMap<Player, Long> players = new HashMap();
	static TaskHandler task;
	static NoticeLess instance;

	@Override
	public void onEnable() {
		new DataBase(this);
		this.getServer().getPluginManager().registerEvents(this, this);
		task = this.getServer().getScheduler().scheduleDelayedRepeatingTask(new UpdateNoticeTask(this), 10 * 20,
				DataBase.getInstance().repeat * 20);
		this.getServer().getCommandMap().register("공지관리", new NoticeCommand(this));
		instance = this;
	}

	@Override
	public void onDisable() {
		DataBase.getInstance().save(this);
	}

	public static NoticeLess getInstance() {
		return instance;
	}

	public void setPeriod(int i) {
		task.setPeriod(i * 20);
	}

	public static String message(String message) {
		return "§a§l[알림] §r§7" + message;
	}

	public static String alert(String message) {
		return "§c§l[알림] §r§7" + message;
	}

	public static String command(String message) {
		return "§l§6[알림]§r§7 " + message;
	}

	public static String success(String message) {
		return "§l§b[안내]§r§7 " + message;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		players.put(event.getPlayer(), event.getPlayer().createBossBar("", 100));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		players.remove(event.getPlayer());
	}

	public void update() {
		String message = DataBase.getInstance().next();
		this.getServer().broadcastMessage(message);
		players.forEach((player, id) -> {
			player.updateBossBar(message, 100, id);
		});
	}
}

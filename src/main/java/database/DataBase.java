package database;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.nukkit.utils.Config;
import noticeless.NoticeLess;

public class DataBase {
	static DataBase instance;
	public int index = 0;
	public int size;
	public LinkedList<String> notice = new LinkedList<>();
	public String prefix = "";
	public int repeat = 120;

	public DataBase(NoticeLess main) {
		main.getDataFolder().mkdirs();
		Config config = new Config(new File(main.getDataFolder(), "notice.json"), Config.JSON);
		List<String> notice = config.getStringList("list");
		prefix = config.getString("prefix", "§l§b[ 공지 ]§f ");
		repeat = (int) config.getDouble("repeat",120.0);
		for (String str : notice) {
			addNotice(str);
		}
		size = notice.size();
		instance = this;
	}

	public static DataBase getInstance() {
		return instance;
	}

	public void addNotice(String message) {
		notice.add(message);
		size++;

	}

	public void delNotice(int index) {
		notice.remove(index);
		size--;
	}

	public String next() {
		return prefix + notice.get((index++ % size));
	}

	public LinkedList<String> getList() {
		return notice;
	}

	public void save(NoticeLess main) {
		Config config = new Config(new File(main.getDataFolder(), "notice.json"), Config.JSON);
		config.set("list", notice);
		config.set("prefix", prefix);
		config.set("repeat", repeat);
		config.save();
	}

}

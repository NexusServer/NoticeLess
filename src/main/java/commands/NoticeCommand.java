package commands;

import java.util.HashMap;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import database.DataBase;
import noticeless.NoticeLess;

public class NoticeCommand extends Command {
	NoticeLess plugin;

	public NoticeCommand(NoticeLess plugin) {
		super("공지관리", "서버공지를 관리합니다", "/공지 <추가|제거|목록|반복주기>");
		this.commandData.permission = "op";
		this.setPermission("op");
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("추가", new CommandParameter[] { new CommandParameter("추가", false), new CommandParameter("메세지") });
				put("제거", new CommandParameter[] { new CommandParameter("제거", false),
						new CommandParameter("번호", CommandParameter.ARG_TYPE_INT, false) });
				put("목록", new CommandParameter[] { new CommandParameter("목록", false) });
				put("반복주기", new CommandParameter[] { new CommandParameter("반복주기"),
						new CommandParameter("초", CommandParameter.ARG_TYPE_INT, false) });
				put("접두사", new CommandParameter[] { new CommandParameter("접두사", false), new CommandParameter("접두사") });
			}
		});
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		switch (args[0]) {
		case "추가":
			String mes = String.join(" ", args).substring(args[0].length());
			DataBase.getInstance().addNotice(mes);
			sender.sendMessage(NoticeLess.success(mes + "  §f메세지가 공지에 정상적으로 추가되었습니다"));
			return true;
		case "제거":
			int index = Integer.parseInt(args[1]);
			if (DataBase.getInstance().size <= index) {
				sender.sendMessage(NoticeLess.alert("해당 번호의 공지는 아직 만들어지지 않았습니다"));
				return true;
			}
			sender.sendMessage(
					NoticeLess.command(DataBase.getInstance().getList().get(index) + "§f 공지가 정상적으로 삭제되었습니다"));
			DataBase.getInstance().delNotice(index);
			return true;
		case "목록":
			for (int i = 0; i < DataBase.getInstance().size; i++) {
				sender.sendMessage(NoticeLess.command(i + DataBase.getInstance().getList().get(i)));
			}
			sender.sendMessage(NoticeLess.success("출력완료============================="));
			return true;

		case "반복주기":
			int repeat = Integer.parseInt(args[1]);
			NoticeLess.getInstance().setPeriod(repeat);
			DataBase.getInstance().repeat = repeat;
			sender.sendMessage(NoticeLess.success("성공적으로 반복주기가 변경되었습니다"));
			return true;
		case "접두사":
			String prefix = String.join(" ", args).substring(args[0].length());
			DataBase.getInstance().prefix = prefix;
			sender.sendMessage(NoticeLess.success("접두사가 정상적으로 변경되었습니다"));
			return true;
		}
		return false;
	}
}

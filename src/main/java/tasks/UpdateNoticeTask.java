package tasks;

import cn.nukkit.scheduler.PluginTask;
import noticeless.NoticeLess;

public class UpdateNoticeTask extends PluginTask<NoticeLess> {

	public UpdateNoticeTask(NoticeLess owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onRun(int currentTick) {
		NoticeLess.getInstance().update();

	}
}

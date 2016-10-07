package Task;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.util.List;
/*
 * 이름 : MCJINWOO with angelless
 * 소속 : Organic *Nexus*
 * 
 * 무단 수정 및 도용은 금지합니다.
 * 상업적 이익을 얻는용도로 사용을 금지합니다.
 * 
 * 제작일 : 2016/10/03 11:11
 */

public class noticelist extends PluginBase implements Listener {
   public int number = 0;
   @SuppressWarnings("unused")
   private Config noticelist;

   @Override
   public void onLoad() {
      this.getLogger().info("서버공지 플러그인~! 로딩중");
   }

   @Override
   public void onEnable() {
      this.getLogger().info("서버공지 플러그인~! 로딩완료 (활성화중)");
      this.getLogger().notice("서버 공지 라능!!");
      this.getServer().getPluginManager().registerEvents(this, this);
      
      this.getDataFolder().mkdirs();
      this.registerCommand("공지","※ /공지 명령어 ※ 명령어를 보여줍니다","/공지 추가|목록|삭제|명령어","abc.command.*");
      
      this.noticelist = new Config(this.getDataFolder() + "/noticelist.json", Config.JSON); this.getServer().getScheduler().scheduleDelayedRepeatingTask(new Tick(this), 200, 400, false);
   }
   
   @Override
   public void onDisable(){
      this.noticelist.save();
   }

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	  try{
      if (command.getName().equals("공지")) {
         if (sender.isOp()) {
            switch (args[0]) {
            case "추가":
                this.addNotice(String.join(" ", args).replace("추가 ",""));
                return true;
            case "삭제":
            try{
                if(this.delNotice(Integer.parseInt(args[1]))){
                }else{
                sender.sendMessage(TextFormat.colorize("&c"+args[1]+"번호의 공지가 없습니다"));
                return true;
                }
                }catch(Exception e){
                sender.sendMessage(TextFormat.colorize("&c"+args[1]+"&4는 숫자가 아닙니다"));
                return true;
                }
               case "목록":
                for (int i = 0; i<= this.noticelist.getStringList("Notice").size(); i++) {
                   sender.sendMessage(i + "  :  "+this.noticelist.getStringList("Notice").get(i));
                }
                return true;
               case "명령어":
              	 sender.sendMessage(
              	         " --------------------\n"
              	        +"|       공지 명령어              |\n"
              	        +"| /공지 명령어    (명령어 목록)  |\n"
              	        +"| /공지 추가    (&색코드 내용)  |\n"
              	        +"| /공지 삭제    (&색코드 내용)  |\n"
              	        +"| /공지 목록    (&색코드 목록)  |\n"
              	        +" --------------------\n");    	
            }
         }
            return true;
         }
      }catch (Exception e){
     	 return true;
      }
      return true;
      
   }
public void addNotice(String str){
List<String> newNotice = this.noticelist.getStringList("Notice");
newNotice.add(str);
this.noticelist.set("Notice",newNotice);
this.noticelist.save();
}
public boolean delNotice(int index){
List<String> newNotice = this.noticelist.getStringList("Notice");
if(newNotice.get(index)!=null){
newNotice.remove(index);
this.noticelist.set("Notice",newNotice);
this.noticelist.save();
return true;
}else{
return false;}
}
   public void registerCommand(String name, String descript, String usage, String permission) {
      SimpleCommandMap commandMap = getServer().getCommandMap();
      PluginCommand<noticelist> command = new PluginCommand<noticelist>(name, this);
      command.setDescription(descript);
      command.setUsage(usage);
      command.setPermission(permission);
      commandMap.register(name, command);
   }

   public class Tick extends PluginTask<noticelist> {
      public Tick(noticelist plugin) {
         super(plugin);
      }
      @Override
      public void onRun(int currentTick) {
          for (Player player : Server.getInstance().getOnlinePlayers().values()) {          
             player.sendMessage(TextFormat.colorize("&e[Organic]"+noticelist.getStringList("Notice")
               .get(owner.number % noticelist.getStringList("Notice").size())));//채팅창 출력
             player.sendPopup(TextFormat.colorize("&e[Organic] "+noticelist.getStringList("Notice")  //팝업 출력
                 .get(owner.number % noticelist.getStringList("Notice").size())));

          }
          owner.number++;
       }
    }
 }

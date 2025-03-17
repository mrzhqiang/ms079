//importPackage(Packages.client);
var status = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        if (status == 0) {
            var txt = "";
            txt = "你好，我是每日跑环任务第二环NPC，跑环的次数越多奖励越丰富。\r\n\r\n";

            if (cm.getPS() == 1){// cm.getPS()  的意思是 读取跑商值如果等于0 就得出他没有开始跑商 就运行他进行第一环跑商!

                txt += "#L1##b收集300个树枝#v4000003#交给我！#l";
                cm.sendSimple(txt);
			}else if (cm.getPS() == 2){
                txt += "恭喜你完成第二环任务，请去林中之城联系那里的猪接受第三环任务！";
                cm.sendOk(txt);
                cm.dispose();
            }else{
                txt += "还没有完成上一环任务或者已经完成了请第二天再来。";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (cm.getPS() == 1 && cm.haveItem(4000003,300)){
                cm.gainPS(1);//cm.gainPS(1);  的意思是 你完成跑商第一环的时候给予你 跑商值+1这样你就无法在重复做第一环了。只有凌晨12点刷新才行！
		
                cm.gainItem(4000003, -300);
				cm.gainDY(2000);
                cm.sendOk("跑商第二环完成!");
                cm.dispose();
            }else{
                cm.sendOk("收集300个树枝#v4000003#交给我!");
                cm.dispose();
            }
        }
    }
}

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
            txt = "你好，我是每日跑环任务第五环NPC，跑环的次数越多奖励越丰富。\r\n\r\n";

            if (cm.getPS() == 4){// cm.getPS()  的意思是 读取跑商值如果等于0 就得出他没有开始跑商 就运行他进行第一环跑商!

                txt += "#L1##b收集5个黄金#v4011006#交给我！#l";
                cm.sendSimple(txt);
			}else if (cm.getPS() == 5){
                txt += "恭喜你完成第五环任务，请去童话村联系小卓接受第六环任务！";
                cm.sendOk(txt);
                cm.dispose();
            }else{
                txt += "还没有完成上一环任务或者已经完成了请第二天再来。";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (cm.getPS() == 4 && cm.haveItem(4011006,5)){
                cm.gainPS(1);//cm.gainPS(1);  的意思是 你完成跑商第一环的时候给予你 跑商值+1这样你就无法在重复做第一环了。只有凌晨12点刷新才行！
		
                cm.gainItem(4011006, -5);
				cm.gainMeso(2000000);
                cm.sendOk("跑商第五环完成!");
                cm.dispose();
            }else{
                cm.sendOk("收集5个黄金#v4011006#交给我!");
                cm.dispose();
            }
        }
    }
}

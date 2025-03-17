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
            txt = "我是每日跑商第3环NPC哦！\r\n\r\n";

            if (cm.getPS() == 2){// cm.getPS()  的意思是 读取跑商值如果等于1 就得出他跑商已经完成了第一环 就运行他进行第二环跑商!

                txt += "#L1##b收集100个树枝#v4000003#交给我！！#l";
                cm.sendSimple(txt);
            }else{
                txt += "你已经完成过了.\r\n请第二天再来！";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (cm.haveItem(4000003,100)){
                cm.gainPS(1);//cm.gainPS(1);  的意思是 你完成跑商第一环的时候给予你 跑商值+1这样你就无法在重复做第二环了。只有凌晨12点刷新才行！
		
                cm.gainItem(4000003, -100);
//cm.gainExp(+500000);
cm.gainMeso(+30000);
cm.gainNX(+300);
//cm.喇叭(2, "[" + cm.getPlayer().getName() + "]成功完成第三轮跑商任务，获得金币50万，经验20万，获得点券奖励300点，请再接再厉！！！");
                cm.sendOk("跑商第3环完成!恭喜获得点卷300点\r\n\r\n进行下一环！");
                cm.dispose();
            }else{
                cm.sendOk("收集100个树枝#v4000003#交给我!");
                cm.dispose();
            }
        }
    }
}

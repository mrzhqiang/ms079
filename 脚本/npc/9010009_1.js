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
            txt = "我是每日跑商任务NPC！第一环.\r\n\r\n";

            if (cm.getPS() == 0){// cm.getPS()  的意思是 读取跑商值如果等于0 就得出他没有开始跑商 就运行他进行第一环跑商!
                txt += "#L1##b收集50个红蜗牛壳#v4000016#交给我！#l";
                cm.sendSimple(txt);
            }else{
                txt += "你已经完成过了然后你去找诺特勒斯号码头，进行下一环.!\r\n请第二天再来完成第一环！";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (cm.haveItem(4000016,50)){
                cm.gainPS(1);//cm.gainPS(1);  的意思是 你完成跑商第一环的时候给予你 跑商值+1这样你就无法在重复做第一环了。只有凌晨12点刷新才行！
		
                cm.gainItem(4000016, -50);
            //    cm.gainMeso(+200000);//读取变量
           //     cm.gainExp(+100000);
                cm.gainNX(+100);
            //    cm.喇叭(2, "[" + cm.getPlayer().getName() + "]成功完成第一轮跑商任务，获得金币20万，经验10万，获得点券奖励100点，请再接再厉！！！");
                cm.sendOk("跑商第一环完成!获得点卷100点\r\n\r\n然后你去找诺特勒斯号码头，进行下一环.");
                cm.dispose();
            }else{
                cm.sendOk("收集50个红蜗牛壳#v4000016#交给我!");
                cm.dispose();
            }
        }
    }
}

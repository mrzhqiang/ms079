//importPackage(Packages.client);
var status = 0;
var item = java.lang.Math.floor(Math.random() * 100 + 50);
var drop = java.lang.Math.floor(Math.random() * 8 + 3);
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
			if(cm.getPlayer().getLevel() < 70){
                txt += "请70级以后再来找我接任务！";
                cm.sendOk(txt);
                cm.dispose();
			}else if (cm.getSJRW() == 0){
				txt = "你好，这里是每日赏金任务，赏金任务一共有3次，每一次难度不同，奖励的金币会越来越多哟！\r\n\r\n";
                txt += "#L1##b悬赏200个猴子娃娃#v4000026#交给我！#l";
                cm.sendSimple(txt);
			}else if (cm.getSJRW() == 1){
				txt = "你好，这里是第二次赏金任务，赏金任务一共有3次，每一次难度不同，奖励的金币会越来越多哟！\r\n\r\n";
                txt += "#L2##b悬赏200个海狮皮革#v4000155#交给我！#l";
                cm.sendSimple(txt);
			}else if (cm.getSJRW() == 2){
				txt = "你好，这里是第三次赏金任务，赏金任务一共有3次，每一次难度不同，奖励的金币会越来越多哟！\r\n\r\n";
                txt += "#L3##b悬赏1个喷火龙的尾巴#v4000235#交给我！#l \r\n#b悬赏1个格瑞芬多角#v4000243#交给我！#l\r\n#b悬赏50个铜人心#v4000407#交给我！#l";
                cm.sendSimple(txt);
            }else{
                txt += "你已经完成过，或者时间未到。!\r\n请第二天再来！";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (cm.haveItem(4000026,200)){
			if(item >= 50){
				if(cm.canHold(4011000 , drop)){
					cm.gainItem(4011000 , drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 50 && item <= 55){
				if(cm.canHold(4011001 , drop)){
					cm.gainItem(4011001 , drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 55 && item <= 60){
				if(cm.canHold(4011002 , drop)){
					cm.gainItem(4011002 , drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 60 && item <= 65){
				if(cm.canHold(4011003 , drop)){
					cm.gainItem(4011003, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 65 && item <= 67){
				if(cm.canHold(4011004, drop)){
					cm.gainItem(4011004, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 67 && item <= 69){
				if(cm.canHold(4011005, drop)){
					cm.gainItem(4011005, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 69 && item <= 71){
				if(cm.canHold(4011006, drop)){
					cm.gainItem(4011006, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 71 && item <= 73){
				if(cm.canHold(4011008, drop)){
					cm.gainItem(4011008, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 73 && item <= 75){
				if(cm.canHold(4021000, drop)){
					cm.gainItem(4021000, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 75 && item <= 77){
				if(cm.canHold(4021001, drop)){
					cm.gainItem(4021001, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 77 && item <= 79){
				if(cm.canHold(4021002, drop)){
					cm.gainItem(4021002, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 79 && item <= 81){
				if(cm.canHold(4021003, drop)){
					cm.gainItem(4021003, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 81 && item <= 83){
				if(cm.canHold(4021004, drop)){
					cm.gainItem(4021004, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 83 && item <= 85){
				if(cm.canHold(4021005, drop)){
					cm.gainItem(4021005, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 85 && item <= 87){
				if(cm.canHold(4021006, drop)){
					cm.gainItem(4021006, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 87 && item <= 89){
				if(cm.canHold(4021007, drop)){
					cm.gainItem(4021007, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 89){
				if(cm.canHold(4021008, drop)){
					cm.gainItem(4021008, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}
                cm.gainSJRW(1);//
				cm.gainMeso(1000000);
                cm.gainItem(4000026, -200);
                cm.sendOk("恭喜你完成第一次赏金任务，请接第二次任务。");
                cm.dispose();
            }else{
                cm.sendOk("悬赏200个猴子娃娃#v4000026#交给我!");
                cm.dispose();
            }

        } else if (selection == 2) {
            if (cm.haveItem(4000155,200)){
			if(item >= 50){
				if(cm.canHold(4011000 , drop)){
					cm.gainItem(4011000 , drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 50 && item <= 55){
				if(cm.canHold(4011001 , drop)){
					cm.gainItem(4011001 , drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 55 && item <= 60){
				if(cm.canHold(4011002 , drop)){
					cm.gainItem(4011002 , drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 60 && item <= 65){
				if(cm.canHold(4011003 , drop)){
					cm.gainItem(4011003, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 65 && item <= 67){
				if(cm.canHold(4011004, drop)){
					cm.gainItem(4011004, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 67 && item <= 69){
				if(cm.canHold(4011005, drop)){
					cm.gainItem(4011005, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 69 && item <= 71){
				if(cm.canHold(4011006, drop)){
					cm.gainItem(4011006, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 71 && item <= 73){
				if(cm.canHold(4011008, drop)){
					cm.gainItem(4011008, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 73 && item <= 75){
				if(cm.canHold(4021000, drop)){
					cm.gainItem(4021000, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 75 && item <= 77){
				if(cm.canHold(4021001, drop)){
					cm.gainItem(4021001, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 77 && item <= 79){
				if(cm.canHold(4021002, drop)){
					cm.gainItem(4021002, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 79 && item <= 81){
				if(cm.canHold(4021003, drop)){
					cm.gainItem(4021003, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 81 && item <= 83){
				if(cm.canHold(4021004, drop)){
					cm.gainItem(4021004, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 83 && item <= 85){
				if(cm.canHold(4021005, drop)){
					cm.gainItem(4021005, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 85 && item <= 87){
				if(cm.canHold(4021006, drop)){
					cm.gainItem(4021006, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 87 && item <= 89){
				if(cm.canHold(4021007, drop)){
					cm.gainItem(4021007, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 89){
				if(cm.canHold(4021008, drop)){
					cm.gainItem(4021008, drop);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}
                cm.gainSJRW(1);//cm.gainPS(1);  的意思是 你完成跑商第一环的时候给予你 跑商值+1这样你就无法在重复做第二环了。只有凌晨12点刷新才行！
				cm.gainMeso(2000000);
                cm.gainItem(4000155, -200);
                cm.sendOk("恭喜你完成第二次赏金任务，请接第三次任务。");
                cm.dispose();
            }else{
                cm.sendOk("悬赏200个海狮皮革#v4000155#交给我!");
                cm.dispose();
            }

        } else if (selection == 3) {
            if (cm.haveItem(4000235,1) && cm.haveItem(4000243,1) && cm.haveItem(4000407,50)){
				if(item > 50 && item <= 55){
				cm.gainNX(3000);
			}else if(item > 55 && item <= 60){
				if(cm.canHold(2049100, 2)){
					cm.gainItem(2049100, 2);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 60 && item <= 65){
				if(cm.canHold(2340000, 2)){
					cm.gainItem(2340000, 2);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 65 && item <= 67){
				if(cm.canHold(1032205)){
					cm.gainItem(1032205);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 67 && item <= 69){
				if(cm.canHold(1032206)){
					cm.gainItem(1032206);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 69 && item <= 71){
				if(cm.canHold(1032207)){
					cm.gainItem(1032207);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 71 && item <= 73){
				if(cm.canHold(1102040)){
					cm.gainItem(1102040);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 73 && item <= 75){
				if(cm.canHold(1102041)){
					cm.gainItem(1102041);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 75 && item <= 77){
				if(cm.canHold(1102042)){
					cm.gainItem(1102042);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 77 && item <= 79){
				if(cm.canHold(1102043)){
					cm.gainItem(1102043);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 79 && item <= 81){
				if(cm.canHold(1122001)){
					cm.gainItem(1122001);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 81 && item <= 83){
				if(cm.canHold(1122002)){
					cm.gainItem(1122002);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 83 && item <= 85){
				if(cm.canHold(1122003)){
					cm.gainItem(1122003);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 85 && item <= 87){
				if(cm.canHold(1122004)){
					cm.gainItem(1122004);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 87 && item <= 89){
				if(cm.canHold(1122005)){
					cm.gainItem(1122005);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 89 && item <= 91){
				if(cm.canHold(1122006)){
					cm.gainItem(1122006);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 91 && item <= 93){
				if(cm.canHold(1012170)){
					cm.gainItem(1012170);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 93 && item <= 95){
				if(cm.canHold(1302063)){
					cm.gainItem(1302063);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item > 95 && item <= 97){
				if(cm.canHold(1302106)){
					cm.gainItem(1302106);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}else if(item >= 98){
				if(cm.canHold(1402014)){
					cm.gainItem(1402014);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			}
                cm.gainSJRW(1);//cm.gainPS(1);  的意思是 你完成跑商第一环的时候给予你 跑商值+1这样你就无法在重复做第二环了。只有凌晨12点刷新才行！
				cm.gainMeso(3500000);
                cm.gainItem(4000407, -50);
                cm.gainItem(4000235, -1);
                cm.gainItem(4000243, -1);
                cm.sendOk("恭喜你完成全部赏金任务，请明天再来。");
				cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"]完成每日赏金任务，大家一起膜拜吧。");
                cm.dispose();
            }else{
                cm.sendOk("#b悬赏1个喷火龙的尾巴#v4000235#交给我！#l \r\n#b悬赏1个格瑞芬多角#v4000243#交给我！#l\r\n#b悬赏50个铜人心#v4000407#交给我！#l");
                cm.dispose();
            }
        }
    }
}

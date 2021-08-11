//Mina_MS
var status = 0;
var typeName = new Array("【单】","【双】","【小】","【中】","【大】","【一点】","【二点】","【三点】","【四点】","【五点】","【六点】");
var selectTouType=new Array(2,2,3,3,3,6,6,6,6,6,6);
var selectTouNum=new Array(1,5,1,3,5,1,2,3,4,5,6);
var selectTou=-1;
var nx=500;
var race;
var num;
function start() {
status = -1;
action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == -1) {
cm.dispose();
} else {
if (mode == 1)
status++;
else
status--;
		if (status == -1) {
			cm.dispose();
		 } 
		else if (status == 0) {
		 var where ="冒险岛赌博系统\r\n假如你中奖了,要扣掉5%的佣金.您的点卷数量为:#r"+cm.getChar().getNX()+"\r\n选择你要下注的选项。\r\n";
		 if(cm.getChar().isGM()){
		 where+="#r管理员提示:#k吃进点卷:#r"+cm.seeAlltouzhu()+"#k 赔出点卷:#r"+cm.seeAllpeichu()+"#k(仅GM可见.)\r\n";
		 }
		// where+="#r2倍奖励#k\r\n#L0##b【单】#k#l#L1##b【双】#k#l\r\n\r\n";
		 where+="#r3倍奖励#k\r\n\r\n#L2##b【小】#k#l#L3##b【中】#k#l#L4##b【大】#k#l\r\n\r\n";
		 where+="#r6倍奖励#k\r\n#L5##b【一】#k#l#L6##b【二】#k#l#L7##b【三】#k#l#L8##b【四】#k#l#L9##b【五】#k#l#L10##b【六】#k#l\r\n\r\n\r\n";
		 where+="本期开奖前投注统计,每5分钟开奖刷新统计:\r\n";
		 where+="#b2倍投注数当前:#k"+cm.seeTouzhuByType(2)+"人投注\r\n";
		 where+="#b3倍投注数:#k"+cm.seeTouzhuByType(3)+"\r\n";
		 where+="#b6倍投注数:#k"+cm.seeTouzhuByType(6)+"\r\n\r\n";

		 where+="#r本系统仅在频道1有效,每一段时间开奖一次,请不要随意更换频道或者下线.否则造成获奖点卷丢失,将不给予补偿。#k"
		 cm.sendSimple(where);
		 } 
		else if (status == 1) {
			if(cm.getChar().getClient().getChannel()!=1){
			cm.sendOk("该系统仅在频道1开放。如果在其他频道奖不会获得奖励。");
				cm.dispose();
			}
				else
			if(cm.getChar().getTouzhuNum()>0){
				cm.sendOk("本次开奖前您已经投过注了。您的投注金额:"+cm.getChar().getTouzhuNX());
				cm.dispose();
			}else{
		selectTou=selection;
		race=selectTouType[selectTou];
		num=selectTouNum[selectTou];
		var prompt="你选择的投注类型为:"+typeName[selectTou]+"倍率为:"+selectTouType[selectTou]+"\r\n最高投注10000点，最低投注500点。\r\n您的点卷数量为:"+cm.getChar().getNX()+"\r\n请输入你要投注的点卷数目。";
		cm.sendGetNumber(prompt,500,500,10000);
		}
		} 
		else if (status == 2) {
			status=4;
			nx=selection;
			cm.sendYesNo("您确定要投注 "+nx+" 点吗？倍数:"+race+" 号码:"+num);
		} 
		else if (status == 3) {
        cm.sendOk("看样子你还很犹豫，那就想好了再来吧？");
        cm.dispose();
		} 
		else if (status == 4) {
			cm.sendOk("谢谢");
        cm.dispose();
		} 
		else if (status == 5) {
			if(nx>cm.getChar().getNX()){
			cm.sendOk("您的点卷不足 "+nx+" 点");
			cm.dispose();
			}else
		if(cm.touzhu(race, nx, num)){
				cm.sendOk("投注完毕,每分钟开奖。请不要离开喔。");
				cm.dispose();
			}else{
		cm.sendOk("投注出现错误。");
        cm.dispose();
		}
		} 
		else if (status == 6) {
		cm.sendOk("6");
        cm.dispose();
		} 
		else if (status == 7) {
        cm.dispose();
		} 
}
}  

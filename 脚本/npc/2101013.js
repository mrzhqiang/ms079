/* Author: aaroncsn(MapleSea Like)
	NPC Name: 		Karcasa
	Map(s): 		The Burning Sands: Tents of the Entertainers(260010600)
	Description: 		Warps to Victoria Island
*/
var towns = new Array(100000000,101000000,102000000,103000000,104000000);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
	if (status == 0 && mode == 0) {
		cm.sendNext("是的…你害怕速度或高度？你不能相信我的飞行技能？相信我，我已经解决了所有的问题!");
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if(status == 0){
		cm.sendAcceptDecline("我不知道你是怎么发现这个的，但是你来的是正确的地方！对于那些徘徊在尼哈尔沙漠和越来想家，我提供一个航班直达金银岛，不停！不要担心这艘飞船--它只是一次或两次坠落！你不觉得幽闭在小船长途飞行吗？你觉得怎么样？你愿意接受这一直接航班的报价吗?");
	} else if(status == 1){
		cm.sendAcceptDecline("请记住两点。一，这条线实际上是为海外航运，所以 #r我不能保证你会确切的城镇土地#k. 因为我把你放在这个特别的飞行，这会有点贵。服务费是 #e#b10,000 金币#n#k. 有一个航班，那就要起飞了。你有兴趣?");
	} else if(status == 2){
		cm.sendNext("好了，准备起飞~");
	} else if(status == 3){
		if(cm.getMeso() >= 10000){
			cm.gainMeso(-10000);
			cm.warp(towns[Math.floor(Math.random() * towns.length)]);
		} else{
			cm.sendNextPrev("嘿，你没有现金? 我告诉过你，你需要 #b10,000#k 金币 为了得到这个.");
			cm.dispose();
			}
		}
	}
}
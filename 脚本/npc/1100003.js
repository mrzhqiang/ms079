/* Dawnveil
    To Rien
	Puro
    Made by Daenerys
*/
function start() {
    cm.sendYesNo("你想要去维多利亚港必须支付#b 500 金币#k 到那边约一分钟.");
}

function action(mode, type, selection) {
    if (mode == 0) {
        cm.sendOk("等你考虑好再来找我吧!");
        cm.dispose();
    } else {
        if(cm.getPlayer().getMeso() >= 500) {
            cm.gainMeso(-500);
            cm.warpBack(200090051,101000400,80);
        } else {
            cm.sendSimple("你的钱好像不够");
        }
        cm.dispose();
    }
}

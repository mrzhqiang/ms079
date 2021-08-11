var status = 0;
var skin = Array(0, 1, 2, 3, 4);
var price;

function start() {
    cm.sendSimple("欢迎来到101大道护发中心!，我是#p9330054# 您好~幸会 是否有想要染的皮肤呢?? 就像我的健康皮肤??  如果你有 #b#t5153008##k, 就可以随意染的想到的皮肤~~~\r\n\#L2#我已经有一个#t5153008#!#l");
}

function action(mode, type, selection) {
    if (mode < 1)
        cm.dispose();
    else {
        status++;
        if (status == 1)
            cm.sendStyle("选一个想要的风格.", skin);
        else {
            if (cm.haveItem(5153008)){
                cm.gainItem(5153008, -1);
                cm.setSkin(selection);
                cm.sendOk("享受!");
            } else
                cm.sendOk("您貌似没有#b#t5153008##k..");
            cm.dispose();
        }
    }
}

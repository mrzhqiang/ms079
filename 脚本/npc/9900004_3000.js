/* global cm */

function start() {
    cm.sendSimple("您想要挑战闹钟吗。想好了，请告诉我。\r\n每人每天可以进入10次哦~！\r\n#k#L0#挑战闹钟 \r\n#L1#不去了，我害怕#k \r\n")
}


function action(mode, type, selection) {
    cm.dispose();

    switch (selection) {
        case 0:
            if (cm.getLevel() < 100) {
                cm.sendOk("挑战闹钟的最低等级为100级，再去练练吧！");
            } else if (cm.getBossLog('naozhong') > 10) {
                cm.sendOk("抱歉你每天只能参加10次");
                cm.dispose();
            } else {
                cm.setBossLog('naozhong');
                cm.warp(220080001, 0);
		cm.deleteboss();
                cm.getPlayer().bossmap(cm.getPlayer().getId(), 220080001, 1);
                cm.serverNotice("『挑战闹钟』：【" + cm.getChar().getName() + "】悍不畏死的去挑战闹钟BOSS去了");
                cm.dispose();
            }
            break;
        case 1:
            cm.sendOk("等你考虑好再来找我吧");
            cm.dispose();

    }
}

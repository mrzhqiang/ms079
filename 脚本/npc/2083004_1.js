/* global cm */

function start() {
    cm.sendSimple("您想要挑战普通暗黑龙王吗。想好了，请告诉我。\r\n每人每天可以进入10次哦~！\r\n#k#L0#挑战暗黑龙王(普通) \r\n#L1#不去了，我害怕#k \r\n")
}


function action(mode, type, selection) {
    cm.dispose();

    switch (selection) {
        case 0:
            if (cm.getLevel() < 120) {
                cm.sendOk("挑战普通暗黑龙王的最低等级为120级，再去练练吧！");
            } else if (cm.getBossLog('ptheilong') > 5) {
                cm.sendOk("抱歉你只能参加5次");
                cm.dispose();
            } else {
                cm.setBossLog('ptheilong');
                cm.warp(240060200, 0);
		cm.deleteboss();
                cm.getPlayer().bossmap(cm.getPlayer().getId(), 240060200, 3);
                cm.serverNotice("『挑战黑龙』：【" + cm.getChar().getName() + "】悍不畏死的去挑战黑龙王BOSS(普通难度)去了");
                cm.dispose();
            }
            break;
        case 1:
            cm.sendOk("等你考虑好再来找我吧");
            cm.dispose();

    }
}

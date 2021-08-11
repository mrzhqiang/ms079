var status = 0;
var mhair = Array(30270, 30240, 30020, 30000, 30132, 30192, 30032, 30112, 30162);
var fhair = Array(31150, 31250, 31310, 31050, 31050, 31030, 31070, 31091, 31001);
var hairnew = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.sendNext("好吧，如果准备好随时来找我谈谈。");
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {
        if (cm.getQuestStatus(8860) == 2 && !cm.haveItem(4031528)) {
            cm.sendNext("请确认是否做好了准备。");
            cm.dispose();
        } else {
            cm.sendYesNo("准备要剪一个漂亮的发型了？？");
        }
    }
    if (status == 1) {
        hairnew = Array();
        if (cm.getChar().getGender() == 0) {
            for (var i = 0; i < mhair.length; i++) {
                hairnew.push(mhair[i]);
            }

        }
        if (cm.getChar().getGender() == 1) {
            for (var i = 0; i < fhair.length; i++) {
                hairnew.push(fhair[i]);
            }
        }
        cm.sendNext("那来吧～！");
    }
    if (status == 2) {
        if (cm.haveItem(4031528)) {
            cm.gainItem(4031528, -1);
            cm.setHair(hairnew[Math.floor(Math.random() * hairnew.length)]);
            cm.sendBackNext("享受！");
            cm.dispose();
        } else {
            cm.sendNext("嗯...你确定有我们的免费优惠券？");
            cm.dispose();
        }
    }
}

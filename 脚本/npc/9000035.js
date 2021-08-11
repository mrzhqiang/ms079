/* global cm */

function start() {
//这里是脚本开始。
//4000463 - 国庆纪念币 - 是国庆纪念币
//970030020 - 隐藏地图 - 工作人员本部
    //判断玩家背包是否存在国庆币，并且玩家所在地图在- 隐藏地图 - 工作人员本部
    if (cm.haveItem(4000463) && cm.getChar().getMapId() == 970030020) {
        cm.sendSimple("#b" + cm.getChar().getName() + "\r\n\r\n暂未开启27宫副本 \r\n\r\n#L1#我要离开这里#l");
    } else {
        cm.sendOk("找我什么事，想要启动我的力量吗，你需要足够的条件");
        cm.dispose();
    }
}
function action(mode, type, selection) {
    cm.dispose();
    if (selection == 0) {
        if (cm.getBossLog("每日试炼") == 0) {//
            cm.warp(970030100, 0);//传送进入第一关
            cm.setBossLog('每日试炼');
            cm.dispose();
        } else {
            cm.sendOk("你每天只能挑战一次27关试炼任务！");
            cm.dispose();
        }
    } else if (selection == 1) {
        cm.warp(910000000, 0);//传送到自由市场
        cm.dispose();
    }
}
/*
 *  Scarf Snowman - Happy Ville NPC
 */


function start() {
    cm.sendYesNo("这里的风景这么美 你真的要回去吗?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(209000000);
    } else {
	cm.sendNext("你需要更多的时间装饰树, 阿? 如果你想要离开这个地方，随时都可以来跟我说话?");
    }
    cm.dispose();
}
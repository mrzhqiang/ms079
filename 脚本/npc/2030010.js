/* Amon
 * Last Mission : Zakum's Altar (280030000)
 */

function start() {
    cm.sendYesNo("如果你现在离开，你将不得不重新开始。你确定要离开这里到外面去吗？");
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (cm.getPlayer().getClient().getChannel() != 3) {
            cm.warp(211042300);
        } else {
            cm.warp(211042301);
        }
		cm.getPlayer().setbosslog(0);//BOSS重返
    }
    cm.dispose();
}
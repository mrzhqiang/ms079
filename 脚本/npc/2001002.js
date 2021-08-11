/*
 *  Metal Bucket Snowman - Happy Ville NPC
 */

function start() {
    cm.sendSimple("安安 我是 #p2001002# 你要去哪个小房间? PS可结伴同行 \n\r #b#L0#月光森林6#l \n\r #L1#月光森林7#l \n\r #L2#月光森林8#l \n\r #L3#月光森林9#l \n\r #L4#月光森林10#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(209000006 + selection, 0);
    }
    cm.dispose();
}

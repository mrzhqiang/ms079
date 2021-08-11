/*
 *  Strawhat Snowman - Happy Ville NPC
 */

function start() {
    cm.sendSimple("安安 我是 #p2001003# 你要去哪个小房间? PS可结伴同行 \n\r #b#L0#月光森林11#l \n\r #L1#月光森林12#l \n\r #L2#月光森林13#l \n\r #L3#月光森林14#l \n\r #L4#月光森林15#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(209000011 + selection, 0);
    }
    cm.dispose();
}

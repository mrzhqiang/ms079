/* Ali
 * 
 * Adobis's Mission I: The Room of Tragedy (280090000)
 * 
 * Zakum Quest NPC Exit
*/

function start() {
    if (cm.haveItem(4031061)) {
        cm.sendNext("你很好的完成了第一关的任务！ 好吧……。 我会把你送到 #b#p2030008##k 那里。 不过在那之前！！ 你不能把这里特殊的东西留到外面去。我将会在你的背包中拿走这些东西。那么，就这样吧！回头见！");
    } else {
        cm.sendNext("你在中途退出了任务。好吧……。我会送你出去。但是在那之前！！你不能把这里特殊的东西带到外面去。我讲会在你的背包中拿走这些东西。那么，就这样吧！回头见。");
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        cm.removeAll(4001015);
        cm.removeAll(4001016);
        cm.removeAll(4001018);
        cm.warp(211042300, 0);
    }
    cm.dispose();
}
/*
	名字: 		杰伊克
	地图: 		婚礼村小镇
	描述: 		结婚戒指交换
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (cm.getPlayer().getMarriageId() <= 0) {
            cm.sendOk("你好像还没结婚呢，婚都没结就想要结婚戒指？你还是先找个心爱的人，结完婚再来吧~");
            cm.dispose();
        } else {
            cm.sendSimple("你好啊~ 我闻到了一股甜蜜蜜的新婚味道哦~ 哎哟，怎么还戴着订婚戒指啊？结了婚就要换漂亮的结婚戒指才行嘛！你愿意的话，我可以给你们换，怎么样？\r\n\r\n#L0# 把订婚戒指换成结婚戒指。#l");
        }
    } else if (status == 1) {
        cm.sendNext("结婚戒指也可以装备的，一定要试试看哦~");
        cm.dispose();
    }
}
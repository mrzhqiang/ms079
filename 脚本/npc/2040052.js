/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Wiz the Librarian - Helios Tower <Library>(222020000)
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Information
---------------------------------------------------------------------------------------------------
**/

var status = 0;
var questid = new Array(3615,3616,3617,3618,3630,3633,3639);
var questitem = new Array(4031235,4031236,4031237,4031238,4031270,4031280,4031298);
var i;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	var counter = 0;
	var books = "";

	for (var i = 0; i < questid.length; i++) {
	    if (cm.getQuestStatus(questid[i]) == 2) {
		counter++;
		books += "\r\n#v"+questitem[i]+"# #b#t"+questitem[i]+"##k";
	    }
	}
	if(counter == 0) {
	    cm.sendOk("#b#h ##k 还没有还一本故事书.");
	    cm.safeDispose();
	} else {
	    cm.sendNext("我看看 #b#h ##k 总共还了 of #b"+counter+"#k 书 下列是还得书本列表:"+books);
	}
    } else if (status == 1) {
	cm.sendNextPrev("该书库沉淀下来，现在归功于你 #b#h ##k 钜大的帮助，如果故事书被混合再一次被混和起来的话，我会希望你在帮忙一次。");
    } else if (status == 2) {
	cm.dispose();
    }
}
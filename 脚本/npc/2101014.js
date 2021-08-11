/**
 * @author: Eric
 * @npc: Cesar
 * @func: Ariant PQ
*/

var status = 0;
var sel;
var empty = [false, false, false];
var closed = false;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection){
    (mode == 1 ? status++ : status--);
    if (status == 0) {
		cm.sendSimple("#e<阿里安特：竞技场>#n\r\n欢迎来到竞技场里可以对抗其他玩家和展示你的能力.#b\r\n#L0#请求进入 [到竞技场房间].\r\n#L1#关于对 [到竞技场房间]\r\n#L2#[到竞技场房间] 评价标准\r\n#L3#检查今天的剩余挑战计数.\r\n#L4#兑换竞技场奖励.");
	} else if (status == 1) {
		if (selection == 0) {
			if (closed || (cm.getPlayer().getLevel() < 50 && !cm.getPlayer().isGM())) {
				cm.sendOk(closed ? "阿里安特竞技场是获得一个好的事情吧。请稍后再回来." : "50级~200级. 对不起，您可能不参加.");
				cm.dispose();
				return;
			}
			var text = "你想要什么?#b";
			for(var i = 0; i < 3; i += 1)
				if (cm.getPlayerCount(980010100 + (i * 100)) > 0)
					if (cm.getPlayerCount(980010101 + (i * 100)) > 0)
						continue;
					else
						text += "\r\n#L" + i + "# Battle Arena " + (i + 1) + " (" + cm.getPlayerCount(980010100 + (i * 100)) + "/" + cm.getPlayer().getAriantSlotsRoom(i) + " Users. Leader: " + cm.getPlayer().getAriantRoomLeaderName(i) + ")#l";
				else {
					empty[i] = true;
					text += "\r\n#L" + i + "# Battle Arena " + (i + 1) + " (Empty)#l";
					if (cm.getPlayer().getAriantRoomLeaderName(i) != "")
						cm.getPlayer().removeAriantRoom(i);
				}
			cm.sendSimple(text);
		} else if (selection == 1) {
			cm.sendNext("阿里安特竞技场是一个激烈的战场，真正的战士将被清理！即使你是个胆小鬼，你也不要把你的眼睛放在上面！一个探险家能使阿列达喜爱的宝石最会被选为最好的宝物！很简单，是吧？\r\n - #e等级#n : 50或以上#r(推荐等级 : 50 - 80 )#k\r\n - #e时间限制#n : 8 分钟\r\n - #e队员#n : 2-6\r\n - #e物品#n :\r\n#i1113048:# 冠军戒指");
			cm.dispose();
		} else if (selection == 2) {
			status = 9;
			cm.sendNext("你想知道怎么 #r例外的冠军#k 得到 #b分数#k? 多么雄心勃勃！好，我会向你解释.");
		} else if (selection == 3) {
			var ariant = cm.getQuestRecord(150139);
			var data = ariant.getCustomData();
			if (data == null) {
				ariant.setCustomData("10");
				data = "10";
			}
			cm.sendNext("#r#h ##k, 你可以参加竞技场 #b" + parseInt(data) + "#k 时间（今天）.");
			cm.dispose();
		} else if (selection == 4) {
			status = 4;
			cm.sendNext("你有什么本事在大竞技场！如果你的竞技场分数高于 150, 你将得到 #i1113048:# #b冠军戒指#k.\r\n这是真正的斗士的象征.");
		}
	} else if (status == 2) {
		var sel = selection;
		if(cm.getPlayer().getAriantRoomLeaderName(sel) != "" && empty[sel])
            empty[sel] = false;
        else if(cm.getPlayer().getAriantRoomLeaderName(sel) != "") {
			cm.warp(980010100 + (sel * 100));
            cm.dispose();
            return;
        }
        if (!empty[sel]) {
            cm.sendNext("另一个战士创造了竞技场第一。我建议你要么建立一个新的，要么加入战斗竞技场已经建立.");
            cm.dispose();
            return;
        }
		cm.getPlayer().setApprentice(sel);
        cm.sendGetNumber("有多少与会者可以参加这场比赛? (2~6 ppl)", 0, 2, 6);
	} else if (status == 3) {
		var sel = cm.getPlayer().getApprentice(); // how 2 final in javascript.. const doesn't work for shit
		if (cm.getPlayer().getAriantRoomLeaderName(sel) != "" && empty[sel])
			empty[sel] = false;
        if (!empty[sel]) {
            cm.sendNext("另一个战士创造了竞技场第一。我建议你要么建立一个新的，要么加入战斗竞技场已经建立.");
            cm.dispose();
            return;
        }
        cm.getPlayer().setAriantRoomLeader(sel, cm.getPlayer().getName());
        cm.getPlayer().setAriantSlotRoom(sel, selection);
        cm.warp(980010100 + (sel * 100));
		cm.getPlayer().setApprentice(0);
        cm.dispose();
	} else if (status == 5) {
		cm.sendNextPrev("问题是，你的竞技场分数只有 #b0#k. 你必须得分高于 #b150#k 得到 #b冠军戒指#k. 足够高的分数来证明你有资格获得这一.");
	} else if (status == 6) { // todo: code champion rings :c
		cm.dispose();
	} else if (status == 10) {
		cm.sendNextPrev("让我告诉你最简单的规则。最伟大的冠军 #b灵魂宝石#k将被选为最佳的冠军。当然，如果你赢得了一场比赛的话，你会得到更高的赞美 #b无数冠军#k.\r\n\r\n(#b当比赛结束时，你的排名将会被你所拥有的灵魂宝石所决定。此外，如果更多的参与者继续，您将获得更多的奖励.)#k");
	} else if (status == 11) {
		cm.sendNextPrev("即使你不够坚强，也不要担心。如果你能做 #b至少 15#k 灵魂的宝石，没有人敢否认的事实，你是一个伟大的战士.\r\n\r\n(如果你做 #b至少 15 灵魂宝石, 你将获得平均报酬.)#k");
	} else if (status == 12) {
		cm.sendNextPrev("如果你赚的多 15?当然，我们会对这种特殊的冠军奖励更多的奖励！这并不意味着你会得到 #r无限量的奖励#k, 虽然。如果你做 #b30#k 宝石，你会得到的 #r最佳报酬#k.\r\n\r\n(使 #b30 灵魂宝石获得最高质量奖励.)#k");
	} else if (status == 13) {
		cm.sendNextPrev("如果你不做至少15颗宝石，那就意味着你不会得到任何奖励? 不，那不可能是这样！我们美丽的女王阿列达吩咐我们给予一定的奖励 #b冠军谁甚至失败了至少 15#k 宝石。在这种情况下，你会得到 #r较少的奖励#k. 有什么抱怨吗？如果你不喜欢它，训练你的技能和执行一个竞技场比赛中！\r\n\r\n(如果你做 #b少于15 灵魂宝石，你将获得低质量的奖励.)#k");
	} else if (status == 14) {
		cm.sendNextPrev("当然，一个臭名昭著的失败者不值得被对待，以及其他人。即使 #b6 灵魂宝石#k 都太多了 #r你要做的#k, 那么，这仅仅意味着你没有达到标准。不管怎样，你几乎不受 #r任何奖励#k 为比赛中的比赛。所以，试着得到至少6个或更多的宝石.\r\n\r\n(如果你做 #b5 或更少的灵魂宝石，你将获得几乎任何奖励.)#k");
	} else if (status == 15) {
		cm.sendNextPrev("最后, #r懦夫#k 和不能完成任务的失败者 #b时间限制#k 将获得一些奖励的基础上 #r过去的时间#k.\r\n\r\n(#b如果竞技场就在它的中间停了下来，奖励将根据经过的时间了.)#k");
		cm.dispose();
	}
}

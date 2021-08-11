/*
	任务: 通往未来之门
	描述: 与阿夕亚的相遇
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    } else if (mode == 0) {
        if (status == 41) {
            qm.sendNext("了解了吗，这是攸关逆奥之城命运的问题。我身为不受#b阿卡夏的咒语#k束缚的人民，会用生命试着阻止国家的灭亡。");
            qm.dispose();
            return;
        }
        status--;
    } else {
        status++;
    }
    switch (status) {
    case 0:
        if (qm.getQuestCustomData() != null) { // if (qm.getQuestCustomData().equals("readHistory")) {
            qm.sendSimple("枫之谷世界的居民，接受考验的结果如何？#b \n\r #L0#获得了时间旅行者沙漏！#l \n\r #L1#找到了逆奥之流。（商城道具）#l");
            status = 99;
        } else {
            qm.sendNext("我是#p9120025#，来自#m802000101#为逆奥之城感到惋惜的人。");
        }
        break;
    case 1:
        qm.sendNextPrev("（#p9120025#眯起眼睛）");
        break;
    case 2:
        qm.sendNextPrev("你真的长大了。\r\n<格里特你说的是正确的…>");
        break;
    case 3:
        qm.sendNextPrev("我来到枫之谷的时候，当时你还小，现在已成长为一位堂堂正正的战士了。对一直以来守护着你的我而言…没有比这个更令人高兴的事了…\r\n（#p9120025#的眼眶泛红）");
        break;
    case 4:
        qm.sendNextPrev("…\r\n你不断的磨练必杀技术，以坚强意志造就出不屈不挠的精神，又是力量的求道者，也曾经和枫之谷世界黑暗军队交战过，这样的你一定可以…");
        break;
    case 5:
        qm.sendNextPrev("─ 对不起，我刚才说话的口气太没礼貌了。");
        break;
    case 6:
        qm.sendNextPrev("我的使命是鉴别有能力的人，或推测出可能拥有能力的人。");
        break;
    case 7:
        qm.sendNextPrev("不仅如此，导引逆奥之城的救世主，使得逆奥之城能够免于崩坏的危机，更是我的天命。");
        break;
    case 8:
        qm.sendNextPrev("这个地方叫做#m802000101#，是逆奥之城最远且最边界的地方。");
        break;
    case 9:
        qm.sendNextPrev("我要告诉你逆奥之城的真相。");
        break;
    case 10:
        qm.sendNextPrev("…");
        break;
    case 11:
        qm.sendNextPrev("一百年后，逆奥之城会消失。");
        break;
    case 12:
        qm.sendNextPrev("会完完全全地从这个世界上消失。我已预见，逆奥之城将被突然从时空裂缝出现的#b时空扭曲#k吞没。");
        break;
    case 13:
        qm.sendNextPrev("古代的逆奥之城曾经十分盛行高度的魔法文明。我就是生在那个时代，自小学习魔法技能的逆奥之城魔法师。");
        break;
    case 14:
        qm.sendNextPrev("当时的逆奥之城，与玛加提亚的交流持续加深。虽然在现今（你所身处的时代），科学的力量不断抬头，但魔法研究在过去才是主流。");
        break;
    case 15:
        qm.sendNextPrev("而玛加提亚可谓魔法研究的先驱。\n我与当地的魔法师相遇，在研究进入尾声之时，我得到了…#b长生不老的力量#k。");
        break;
    case 16:
        qm.sendNextPrev("这是还没有人练成过，最困难的魔法。为了逆奥之城，当时的我尽力拼了。\n但过没多久，永生不死就成了禁忌、异端，且不被人所接受。过去的同伴、自己的国家和整个世界开始与我为敌，当时无处可逃的我，决定要找到世界的终点，因此在逆奥之城的内陆隐居下来了。");
        break;
    case 17:
        qm.sendNextPrev("但末日的到来比我想像中还要快。");
        break;
    case 18:
        qm.sendNextPrev("当逆奥之城被巨大的#b时空扭曲#k吞没的瞬间，我使出时空跳跃的魔法，漂流在消失后的世界里，然后发现了一件事。");
        break;
    case 19:
        qm.sendNextPrev("我发现了逆奥之城的真相。");
        break;
    case 20:
        qm.sendNextPrev("逆奥之城的历史全都纪录在一本书里。");
        break;
    case 21:
        qm.sendNextPrev("而将此书所记载的内容具现化，就是逆奥之城的历史。");
        break;
    case 22:
        qm.sendNextPrev("原来将逆奥之城的人们所有的行动都用超次元技术记录的#b阿卡夏 - 编年史#k也实际存在…。");
        break;
    case 23:
        qm.sendNextPrev("我的人生就是被这本书弄得乱七八糟的吗。\n我察觉到我的愤怒了。");
        break;
    case 24:
        qm.sendNextPrev("在动荡的世界里，我将#b阿卡夏 - 编年史#k拿在手里读着。");
        break;
    case 25:
        qm.sendNextPrev("我注意到一件事。");
        break;
    case 26:
        qm.sendNextPrev("#b阿卡夏 - 编年史#k里没有记载关于我的内容。");
        break;
    case 27:
        qm.sendNextPrev("我是不存在于过去的。");
        break;
    case 28:
        qm.sendNextPrev("不知道是不是因为与逆奥之城以外的人密切接触，或是因此变得长生不老后，才从#b阿卡夏 - 编年史#k的诅咒#k解脱的。");
        break;
    case 29:
        qm.sendNextPrev("同时我也得知#b阿卡夏- 编年史#k的内容属于流动式，且可加以改编。");
        break;
    case 30:
        qm.sendNextPrev("如果是这样，我想要回到过去，重新改写历史，将原本会出现在历史未来的#b时空扭曲#k，封印在时空的边界，让逆奥之城免于灭亡的命运。");
        break;
    case 31:
        qm.sendNextPrev("不过虽说是为了逆奥之城。但改写历史实在是狂妄的行为。\n若历史能改写，那么人在未来的存在也将遭到剥夺。");
        break;
    case 32:
        qm.sendNextPrev("所以我的结论是，只改写毁灭前的逆奥之城历史。");
        break;
    case 33:
        qm.sendNextPrev("改写历史并非一次就可以完成的工作。历史本身具有自我修正的能力，即使我做了修改，也将回到本来的面貌。");
        break;
    case 34:
        qm.sendNextPrev("我以#m802000101#为根据地，至今仍持续监控接近毁灭前的逆奥之城。");
        break;
    case 35:
        qm.sendNextPrev("接着之后…");
        break;
    case 36:
        qm.sendNextPrev("将会出现一波我前所未见的历史修正浪潮。");
        break;
    case 37:
        qm.sendNextPrev("我知道这是攸关逆奥之城的大事，原本应该让逆奥之城人民自己解决的。");
        break;
    case 38:
        qm.sendNextPrev("但逆奥之城人民因为#b阿卡夏的咒语#k而无法改变历史。");
        break;
    case 39:
        qm.sendNextPrev("如果说这一切都是为了逆奥之城的未来，是太狂妄了些。");
        break;
    case 40:
        qm.sendNextPrev("（#p9120025#恭敬地请求）");
        break;
    case 41:
        qm.sendYesNo("你没有受到#b阿卡夏的咒语#k束缚，我希望能借助你的力量。");
        break;
    case 42:
        //qm.forceStartQuest();
        qm.setQuestCustomData("readHistory");
        qm.sendNextPrev("谢谢你，来自冒险世界的勇者（#p9120025#安息了）");
        break;
    case 43:
        qm.sendNextPrev("相信拥有强大力量的你，即使在未来的时代也能够克服战斗。");
        break;
    case 44:
        qm.sendNextPrev("只是…让我见识你真正的力量吧。");
        break;
    case 45:
        qm.sendNextPrev("我从几千年前开始就一直在寻找勇者。\n因为，在逆奥之城就要毁灭的世界里，不仅有高度发展的科学文明，连敌人都拥有难以想像的强大力量。");
        break;
    case 46:
        qm.sendNextPrev("为了寻找真正的强者，我一直在进行一项计划。");
        break;
    case 47:
        qm.sendNextPrev("逆奥之城因为目前的危机而发生了时空扭曲，从超空间开了个裂缝，与这个世界发生了关联。");
        break;
    case 48:
        qm.sendNextPrev("这个神秘的出入口，恰好连接到了日本，并且与其历史产生了联系。导致了日本的过去和现在，也出现过许多奇怪的现象。");
        break;
    case 49:
        qm.sendNextPrev("而正是因为如此，你可以通过出现在日本的水晶，来到这神秘的卡姆那。");
        break;
    case 50:
        qm.sendNextPrev("在卡姆那，可以获得前往逆奥之城的过去与未来的不可思议的力量。");
        break;
    case 51:
        qm.sendNextPrev("逆奥之城的历史中，枫叶古城是一个特殊时期。");
        break;
    case 52:
        qm.sendNextPrev("它像极了日本的战国，可能是因为两者在平行世界的共鸣。忍者、武士、妖怪，似乎不时地穿行在这2个空间。");
        break;
    case 53:
        qm.sendNextPrev("这段久远的故事，也包含了太多值得体验和挑战的内容。但是目前你不能回，当务之急是要对逆奥之城未来的历史进行拯救！");
        break;
    case 54:
        qm.sendNextPrev("噢，也许你不是很明白。所谓未来的历史，对现在的你来说，是未来，对我，它可能已经成为历史……一个悲剧的历史……");
        break;
    case 55:
        qm.sendNextPrev("因为它在未来面临消失……我希望你能帮我改变这一切，我无法离开，所以希望你帮我拯救逆奥之城的未来！");
        break;
    case 56:
        qm.sendNextPrev("你的能力很强，但请让我在最后见证你真正的力量。");
        break;
    case 57:
        qm.sendNextPrev("要穿梭时空，需要时间的力量。你必须打倒时间神殿的怪物，并将其证明#b#t04000340# 300个#b、#b#t04000342# 1个、#b#t04000343# 1个#b带来。");
        qm.dispose();
        break;
    case 100:
        if (selection == 0) {
            if (qm.haveItem(4000343, 1) && qm.haveItem(4000340, 300) && qm.haveItem(4000342, 1)) {
                status = 119;
                qm.sendNextPrev("辉煌。。");
            } else {
                qm.sendNext("这些时间旅行者的沙漏数量，还不足以证明你的能力，无法穿越时空。你必须要能获得更多的证明。");
                qm.dispose();
            }
        } else {
            if (qm.haveItem(5252002, 1)) {
                status = 129;
                qm.sendNextPrev("辉煌。。");
            } else {
                status = 109;
                qm.sendNext("要是没有可以穿梭时空的能量和证明，是无法前往未来的。因为时空跳转需要特殊的能量，而且未来力量超强的敌人，没有实力实在很危险。");
            }
        }
        break;
    case 110:
        qm.sendNextPrev("打倒时间神殿的怪物后，再拿证明过来。");
        break;
    case 111:
        qm.sendPrev("听说在商城可以买得到特殊的证明…给你更加自由的选择和机会。窜改历史的波涛已演变到那个地步了。不能犹豫。照你的意思。");
        qm.dispose();
        break;
    case 120:
        qm.sendNextPrev("现在我要接收你的道具了。");
        break;
    case 124:
        qm.gainItem(4000343, -1);
        qm.gainItem(4000340, -300);
        qm.gainItem(4000342, -1);
        qm.forceStartQuest();
        qm.dispose();
        break;
    case 130:
        qm.sendNextPrev("现在我要接收你的道具了。.");
        break;
    case 131:
        qm.gainItem(5252002, -1);
        qm.forceStartQuest();
        qm.dispose();
        break;
    default:
        qm.dispose();
        break;
    }
}

function end(mode, type, selection) {
}
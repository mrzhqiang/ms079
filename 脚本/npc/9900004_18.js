
//importPackage(java.lang);

var status = 0;
var slot = Array();
var stats = Array("七星注力");
var selected;
var statsSel;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
            cm.sendSimple("亲爱的#r#h ##k您好,这里是七星注力（攻击力-魔法力）\r\n\r\n武器含有攻击力或者魔法力或者2种都存在都会随机注力\r\n\r\n注力一次需要消耗1个#r#v4032323##z4032323#\r\n\r\n属性将随机注入#r根据原装备的攻击力或者魔法力的#k基础上 上下浮动30点。\r\n\r\n比如：#r原装备攻击力跟魔法力都是50.那么就会随机浮动，最高浮动30点\r\n\r\n也就是说最高可能注入到80，最低可能降低到20.原基础上浮动30点~\r\n\r\n#L2#七星注力（攻击力-魔法力）-需要：#r#v4032323##z4032323##l");
        } else if (selection == 2) {
            var avail = "";
            for (var i = -1; i > -199; i--) {
                if (cm.getInventory( - 1).getItem(i) != null) {
                    avail += "#L" + Math.abs(i) + "##t" + cm.getInventory( - 1).getItem(i).getItemId() + "##v"+cm.getInventory( - 1).getItem(i).getItemId()+"##l\r\n";
                }
                slot.push(i);
            }
            cm.sendSimple("请选择您需要七星注力的装备:\r\n#b" + avail);
    } else if (status == 2) {
        selected = selection - 1;
        var text = "";
        for (var i = 0; i < stats.length; i++) {
            text += "#L" + i + "#" + stats[i] + "#l\r\n";
        }
        cm.sendSimple("你选择了需要七星注力的装备 #b#t" + cm.getInventory( - 1).getItem(slot[selected]).getItemId() + "##k 装备\r\n#b" + text);
    } else if (status == 3) {
        statsSel = selection;
        cm.playerMessage("当前选择 " + selection+"  "+slot[selected]);
        if (selection == 20) {
            cm.sendGetText("你确定是这个装备吗 #b#t" + cm.getInventory( - 1).getItem(slot[selected]).getItemId() + "##k's " + stats[statsSel] + " to?");
	} else {
       cm.sendYesNo("请确认需要七星注力的装备！");
        }
    } else if (status == 4) {
	   if(cm.haveItem(4032323,1)){
	//cm.getPlayer().modifyCSPoints(1,-10000)
	cm.gainItem(4032323, -1);
        cm.equipqh(slot[selected], true); 
        cm.sendOk("装备七星注力成功！");
	   }else{
        cm.sendOk("#v4032323#不足1个无法升级！");
	   }
        cm.dispose();
    } else {
        cm.dispose();
    }
}

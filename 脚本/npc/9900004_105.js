
//任务一NPC名称
var NPC名称1 = "吉夫"
//任务二NPC名称
var NPC名称2 = "npc2"
//任务三NPC名称
var NPC名称3 = "npc3"
//任务四NPC名称
var NPC名称4 = "npc4"
//任务五NPC名称
var NPC名称5 = "npc5"

//var 爱心 = "#fEffect/CharacterEff/1022223/4/0#";

var 爱心 = "#fUI/GuildMark.img/Mark/Etc/00009001/14#";
var 红色箭头 = "#fUI/UIWindow/Quest/icon6/7#";
var 蓝色角点 = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2#";

var 可以开始 = "#fUI/UIWindow/Quest/Tab/enabled/0#";
var 正在进行中 = "#fUI/UIWindow/Quest/Tab/enabled/1#";
var 完成 = "#fUI/UIWindow/Quest/Tab/enabled/2#";
var 换行符 = "\r\n"
var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1 || mode == 0) {
		cm.dispose();
		return;
	} else {
		if (mode == 1)
			status++; 
		else
			status--;
		if (status == 0) {
                    var txt = "                     "+蓝色角点+" #r#e每日跑商 "+蓝色角点+换行符;
                    txt += "#r#e跑商介绍：#k#n #b跑商任务在五个城镇中找到跑商NPC接受任务，每个城镇的跑商任务一天可以做一次，不要错过哦，快去接任务吧。 "+换行符;
                    txt += "#r#e跑商奖励：#k#n #b大量经验,金币,点卷, 随机物品一个,有几率获得制作稀有装备材料"+换行符;
                    
                    txt += ""+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+爱心+换行符;
                    
                if (cm.getPlayer().getBossLog('sjwc1') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt1') == 0){//sj = 收集状态   判断任务状态
                        txt += 红色箭头+"#b[明珠港 "+NPC名称1+" 跑商任务]"+可以开始+换行符;
                    }else{
                        txt += 红色箭头+"#b[明珠港 "+NPC名称1+" 跑商任务]"+正在进行中+换行符;
                    }
                }else{
                    txt += 红色箭头+"#b[明珠港 "+NPC名称1+" 跑商任务]" + 完成 + 换行符;
                }
            
            
            //收集任务二
            
                if (cm.getPlayer().getBossLog('sjwc2') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt2') == 0){//sj = 收集状态   判断任务状态
                        txt += 红色箭头+"#b[射手村 "+NPC名称2+" 跑商任务]"+可以开始+换行符;
                    }else{
                        txt += 红色箭头+"#b[射手村 "+NPC名称2+" 跑商任务]"+正在进行中+换行符;
                    }
                }else{
                    txt += 红色箭头+"#b[射手村 "+NPC名称2+" 跑商任务]" + 完成 + 换行符;
                }
            
            
            //收集任务三
           
                if (cm.getPlayer().getBossLog('sjwc3') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt3') == 0){//sj = 收集状态   判断任务状态
                        txt += 红色箭头+"[魔法密林 "+NPC名称3+" 跑商任务]"+可以开始+换行符;
                    }else{
                        txt += 红色箭头+"#b[魔法密林 "+NPC名称3+" 跑商任务]"+正在进行中+换行符;
                    }
                }else{
                    txt += 红色箭头+"#b[魔法密林 "+NPC名称3+" 跑商任务]" + 完成 + 换行符;
                }
            
            
            //收集任务四
            
                if (cm.getPlayer().getBossLog('sjwc4') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt4') == 0){//sj = 收集状态   判断任务状态
                        txt += 红色箭头+"[勇士部落 "+NPC名称4+" 跑商任务]"+可以开始+换行符;
                    }else{
                        txt += 红色箭头+"#b[勇士部落 "+NPC名称4+" 跑商任务]"+正在进行中+换行符;
                    }
                }else{
                    txt += 红色箭头+"#b[勇士部落 "+NPC名称4+" 跑商任务]" + 完成 + 换行符;
                }
            
            
            //收集任务五

                if (cm.getPlayer().getBossLog('sjwc5') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt5') == 0){//sj = 收集状态   判断任务状态
                        txt += 红色箭头+"[废弃都市 "+NPC名称5+" 跑商任务]"+可以开始+换行符;
                    }else{
                        txt += 红色箭头+"#b[废弃都市 "+NPC名称5+" 跑商任务]"+正在进行中+换行符;
                    }
                }else{
                    txt += 红色箭头+"#b[废弃都市 "+NPC名称5+" 跑商任务]" + 完成 + 换行符;
                }

            //显示出来
                    cm.sendOk(txt); 
                    cm.dispose();
		}
    }
}

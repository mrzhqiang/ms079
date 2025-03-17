//importPackage(Packages.client);
var random = java.lang.Math.floor(Math.random() * 4 + 1);
var randoma = java.lang.Math.floor(Math.random() * 3 + 1);
var sgsj = java.lang.Math.floor(Math.random() * 5 + 2);
var sgsja = java.lang.Math.floor(Math.random() * 5 + 2);
var item = java.lang.Math.floor(Math.random() * 100 + 1);

var status = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        if (status == 0) {
            var txt = "";
            var txta = "";
            var txtas = "";
			if (cm.getFBRW() == 0 && cm.getFBRWA() == 0 &&　cm.getPlayer().getmrfbrw() == 0 && cm.getPlayer().getmrfbrwa() == 0){
				txt = "#e#r你好，完成每日副本后增加完成次数（中途掉线不算）。\r\n\r\n";
                txt += "#L1##b领取每日副本任务！#l";
                cm.sendSimple(txt);
			}else if (cm.getPlayer().getmrfbrw() > 0 && cm.getPlayer().getmrfbrwa() > 0 && cm.getFBRW() < cm.getPlayer().getmrfbrws() || cm.getFBRWA() < cm.getPlayer().getmrfbrwas()){
				
				txt = "#e#r你好，完成每日副本后增加完成次数（中途掉线不算）。\r\n\r\n";
				if(cm.getPlayer().getmrfbrw() == 1){
					txtas = "月妙副本废弃副本";
				}else if(cm.getPlayer().getmrfbrw() == 2){
					txtas = "玩具副本";
				}else if(cm.getPlayer().getmrfbrw() == 3){
					txtas = "天空副本";
				}else if(cm.getPlayer().getmrfbrw() == 4){
					txtas = "废弃副本";
				}
				if(cm.getPlayer().getmrfbrwa() == 1){
					txta = "毒雾副本";
				}else if(cm.getPlayer().getmrfbrwa() == 2){
					txta = "男女副本";
				}else if(cm.getPlayer().getmrfbrwa() == 3){
					txta = "海盗副本";
				}
                txt += "#b你当前任务完成进度：#l\r\n\r\n";
                txt += "#r"+txtas+"：任务需求 "+cm.getPlayer().getmrfbrws()+"/"+cm.getFBRW()+" 目前已经完成#l\r\n";
                txt += "#r"+txta+"：任务需求 "+cm.getPlayer().getmrfbrwas()+"/"+cm.getFBRWA()+" 目前已经完成#l";
                cm.sendOk(txt);
                cm.dispose();
			}else if (cm.getFBRW() >= cm.getPlayer().getmrfbrws() && cm.getFBRWA() >= cm.getPlayer().getmrfbrwas() && cm.getPlayer().getmrfbrws() > 0 &&  cm.getPlayer().getmrfbrwas() > 0){
				
				txt = "#e#r你好，完成每日副本后增加完成次数（中途掉线不算）。\r\n\r\n";
                txt += "#L2##b恭喜你完成每日副本任务[点我领取奖励]！#l";
                cm.sendSimple(txt);
            }else{
                txt += "你已经完成过了!\r\n请第二天再来！";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (random <= 1){
                
				cm.getPlayer().setmrfbrw(1);	
				cm.getPlayer().setmrfbrws(sgsj);	
            }else if(random == 2){
                
				cm.getPlayer().setmrfbrw(2);
				cm.getPlayer().setmrfbrws(sgsj);		
            }else if(random == 3){
                
				cm.getPlayer().setmrfbrw(3);
				cm.getPlayer().setmrfbrws(sgsj);		
            }else if(random == 4){	
				cm.getPlayer().setmrfbrw(4);
				cm.getPlayer().setmrfbrws(sgsj);	
            }
			
            if (randoma <= 1){
                
				cm.getPlayer().setmrfbrwa(1);	
				cm.getPlayer().setmrfbrwas(sgsja);	
            }else if(randoma == 2){
                
				cm.getPlayer().setmrfbrwa(2);	
				cm.getPlayer().setmrfbrwas(sgsja);	
            }else if(randoma == 3){
                
				cm.getPlayer().setmrfbrwa(3);	
				cm.getPlayer().setmrfbrwas(sgsja);		
            }
                cm.sendOk("成功领取每日副本任务。\r\n#r请重新打开我查看进度。#l");
                cm.dispose();
        } else if (selection == 2) {
			if(item > 50){
					cm.gainItem(4000463, 5);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
				cm.getPlayer().setmrfbrw(0);	
				cm.getPlayer().setmrfbrws(0);
				cm.getPlayer().setmrfbrwa(0);	
				cm.getPlayer().setmrfbrwas(0);	
				cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"]完成每日副本任务，大家一起膜拜吧。");
                cm.sendOk("恭喜你完成每日副本任务，请明天再来!");
                cm.dispose();
        }
    }
}

//importPackage(Packages.client);
var random = java.lang.Math.floor(Math.random() * 7 + 1);
var randoma = java.lang.Math.floor(Math.random() * 8 + 1);
var sgsj = java.lang.Math.floor(Math.random() * 2 + 1);
var sgsja = java.lang.Math.floor(Math.random() * 2 + 1);
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
			if(cm.getPlayer().getLevel() < 10){
                txt += "请10级以后再来找我接任务！";
                cm.sendOk(txt);
                cm.dispose();
			}else if (cm.getSBOSSRW() == 0 && cm.getSBOSSRWA() == 0 &&　cm.getPlayer().getmrsbossrw() == 0 && cm.getPlayer().getmrsbossrwa() == 0){
				txt = "#e#r你好，这里是每日BOSS任务，在野外升级杀BOSS的同时也能完成任务。这种待遇很不错吧。\r\n\r\n";
                txt += "#L1##b领取每日击杀BOSS任务！#l";
                cm.sendSimple(txt);
			}else if (cm.getPlayer().getmrsbossrw() > 0 && cm.getPlayer().getmrsbossrwa() > 0 && cm.getSBOSSRW() < cm.getPlayer().getmrsbossrws() || cm.getSBOSSRWA() < cm.getPlayer().getmrsbossrwas()){
				txt = "你好，这里是每日BOSS任务，在野外升级杀BOSS的同时也能完成任务。这种待遇很不错吧。\r\n\r\n";
				if(cm.getPlayer().getmrsbossrw() == 2220000){
					txtas = "红蜗牛王";
				}else if(cm.getPlayer().getmrsbossrw() == 3220000){
					txtas = "树妖王";
				}else if(cm.getPlayer().getmrsbossrw() == 6300005){
					txtas = "僵尸蘑菇王";
				}else if(cm.getPlayer().getmrsbossrw() == 5220002){
					txtas = "浮士德";
				}else if(cm.getPlayer().getmrsbossrw() == 5220003){
					txtas = "提莫";
				}else if(cm.getPlayer().getmrsbossrw() == 6130101){
					txtas = "蘑菇王";
				}else if(cm.getPlayer().getmrsbossrw() == 6220000){
					txtas = "多尔";
				}
				if(cm.getPlayer().getmrsbossrwa() == 6220001){
					txta = "朱诺";
				}else if(cm.getPlayer().getmrsbossrwa() == 7220000){
					txta = "肯德熊";
				}else if(cm.getPlayer().getmrsbossrwa() == 7220001){
					txta = "九尾狐";
				}else if(cm.getPlayer().getmrsbossrwa() == 7220002){
					txta = "妖怪禅师";
				}else if(cm.getPlayer().getmrsbossrwa() == 8150000){
					txta = "蝙蝠魔";
				}else if(cm.getPlayer().getmrsbossrwa() == 9600009){
					txta = "大王蜈蚣";
				}else if(cm.getPlayer().getmrsbossrwa() == 8180000){
					txta = "火焰龙";
				}else if(cm.getPlayer().getmrsbossrwa() == 8180001){
					txta = "天鹰";
				}
                txt += "#b你当前任务完成进度：#l\r\n\r\n";
                txt += "#r"+txtas+"：任务需求 "+cm.getPlayer().getmrsbossrws()+"/"+cm.getSBOSSRW()+" 目前已经击杀#l\r\n";
                txt += "#r"+txta+"：任务需求 "+cm.getPlayer().getmrsbossrwas()+"/"+cm.getSBOSSRWA()+" 目前已经击杀#l";
                cm.sendOk(txt);
                cm.dispose();
			}else if (cm.getSBOSSRW() >= cm.getPlayer().getmrsbossrws() && cm.getSBOSSRWA() >= cm.getPlayer().getmrsbossrwas() && cm.getPlayer().getmrsbossrws() > 0 &&  cm.getPlayer().getmrsbossrwas() > 0){
				txt = "你好，这里是每日BOSS任务，在野外升级杀BOSS的同时也能完成任务。这种待遇很不错吧。\r\n\r\n";
                txt += "#L2##b恭喜你完成每日杀boss任务[点我领取奖励]！#l";
                cm.sendSimple(txt);
            }else{
                txt += "你已经完成过了!\r\n请第二天再来！";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (random <= 1){
                
				cm.getPlayer().setmrsbossrw(2220000);	
				cm.getPlayer().setmrsbossrws(sgsj);	
            }else if(random == 2){
                
				cm.getPlayer().setmrsbossrw(3220000);
				cm.getPlayer().setmrsbossrws(sgsj);		
            }else if(random == 3){
                
				cm.getPlayer().setmrsbossrw(6300005);
				cm.getPlayer().setmrsbossrws(sgsj);		
            }else if(random == 4){
                
				cm.getPlayer().setmrsbossrw(5220002);	
				cm.getPlayer().setmrsbossrws(sgsj);	
            }else if(random == 5){
                
				cm.getPlayer().setmrsbossrw(5220003);	
				cm.getPlayer().setmrsbossrws(sgsj);	
            }else if(random == 6){
                
				cm.getPlayer().setmrsbossrw(6130101);	
				cm.getPlayer().setmrsbossrws(sgsj);	
            }else if(random >= 7){
                
				cm.getPlayer().setmrsbossrw(6220000);	
				cm.getPlayer().setmrsbossrws(sgsj);	
            }
			
            if (randoma <= 1){
                
				cm.getPlayer().setmrsbossrwa(6220001);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma == 2){
                
				cm.getPlayer().setmrsbossrwa(7220000);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma == 3){
                
				cm.getPlayer().setmrsbossrwa(7220001);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma == 4){
                
				cm.getPlayer().setmrsbossrwa(7220002);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma == 5){
                
				cm.getPlayer().setmrsbossrwa(8150000);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma == 6){
                
				cm.getPlayer().setmrsbossrwa(9600009);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma == 7){
                
				cm.getPlayer().setmrsbossrwa(8180000);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }else if(randoma >= 8){
                
				cm.getPlayer().setmrsbossrwa(8180001);	
				cm.getPlayer().setmrsbossrwas(sgsja);	
            }
                cm.sendOk("成功领取每日杀BOSS任务。\r\n#r请重新打开我查看进度以及要杀的BOSS怪物。#l");
                cm.dispose();
        } else if (selection == 2) {
			if(item > 50){
					cm.gainItem(4000463, 5);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
				cm.getPlayer().setmrsbossrw(0);	
				cm.getPlayer().setmrsbossrws(0);
				cm.getPlayer().setmrsbossrwa(0);	
				cm.getPlayer().setmrsbossrwas(0);	
				cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"]完成每日击杀BOSS任务，大家一起膜拜吧。");
                cm.sendOk("恭喜你完成每日杀BOSS任务，请明天再来!");
                cm.dispose();
        }
    }
}

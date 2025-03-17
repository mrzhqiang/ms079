//importPackage(Packages.client);
var random = java.lang.Math.floor(Math.random() * 7 + 1);
var randoma = java.lang.Math.floor(Math.random() * 8 + 1);
var sgsj = java.lang.Math.floor(Math.random() * 300 + 100);
var sgsja = java.lang.Math.floor(Math.random() * 300 + 100);
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
                txt += "#e请10级以后再来找我接任务！";
                cm.sendOk(txt);
                cm.dispose();
			}else if (cm.getSGRW() == 0 && cm.getSGRWA() == 0 &&　cm.getPlayer().getmrsgrw() == 0 && cm.getPlayer().getmrsgrwa() == 0){
				txt = "#e#r你好，这里是每日杀怪任务，在野外升级杀怪的同时也能完成任务，这种待遇很不错吧。\r\n\r\n";
                txt += "#L1##b领取每日杀怪任务！#l";
                cm.sendSimple(txt);
			}else if (cm.getPlayer().getmrsgrw() > 0 && cm.getPlayer().getmrsgrwa() > 0 && cm.getSGRW() < cm.getPlayer().getmrsgrws() || cm.getSGRWA() < cm.getPlayer().getmrsgrwas()){
				txt = "你好，这里是每日杀怪任务，在野外升级杀怪的同时也能完成任务。这种待遇很不错吧。\r\n\r\n";
				if(cm.getPlayer().getmrsgrw() == 1130100){
					txtas = "斧木妖";
				}else if(cm.getPlayer().getmrsgrw() == 210100){
					txtas = "绿水灵";
				}else if(cm.getPlayer().getmrsgrw() == 3230308){
					txtas = "空军雀";
				}else if(cm.getPlayer().getmrsgrw() == 4230100){
					txtas = "冰独眼兽";
				}else if(cm.getPlayer().getmrsgrw() == 8150100){
					txtas = "鲨鱼";
				}else if(cm.getPlayer().getmrsgrw() == 5130103){
					txtas = "黑鳄鱼";
				}else if(cm.getPlayer().getmrsgrw() == 5200002){
					txtas = "火石球";
				}
				if(cm.getPlayer().getmrsgrwa() == 6230300){
					txta = "红小丑";
				}else if(cm.getPlayer().getmrsgrwa() == 7130101){
					txta = "长枪牛魔王";
				}else if(cm.getPlayer().getmrsgrwa() == 7130601){
					txta = "邪恶侏儒怪";
				}else if(cm.getPlayer().getmrsgrwa() == 5130102){
					txta = "黑石头人";
				}else if(cm.getPlayer().getmrsgrwa() == 8140103){
					txta = "寒冰半人马";
				}else if(cm.getPlayer().getmrsgrwa() == 8140600){
					txta = "骨骸鱼";
				}else if(cm.getPlayer().getmrsgrwa() == 8150300){
					txta = "红飞龙";
				}else if(cm.getPlayer().getmrsgrwa() == 8190005){
					txta = "泥人妖";
				}
                txt += "#b你当前任务完成进度：#l\r\n\r\n";
                txt += "#r"+txtas+"：任务需求 "+cm.getPlayer().getmrsgrws()+"/"+cm.getSGRW()+" 目前已经击杀#l\r\n";
                txt += "#r"+txta+"：任务需求 "+cm.getPlayer().getmrsgrwas()+"/"+cm.getSGRWA()+" 目前已经击杀#l";
                cm.sendOk(txt);
                cm.dispose();
			}else if (cm.getSGRW() >= cm.getPlayer().getmrsgrws() && cm.getSGRWA() >= cm.getPlayer().getmrsgrwas() && cm.getPlayer().getmrsgrws() > 0 &&  cm.getPlayer().getmrsgrwas() > 0){
				txt = "你好，这里是每日杀怪任务，在野外升级杀怪的同时也能完成任务。这种待遇很不错吧。\r\n\r\n";
                txt += "#L2##b恭喜你完成每日杀怪任务[点我领取奖励]！#l";
                cm.sendSimple(txt);
            }else{
                txt += "你已经完成过了!\r\n请第二天再来！";
                cm.sendOk(txt);
                cm.dispose();
            }

        } else if (selection == 1) {
            if (random <= 1){
                
				cm.getPlayer().setmrsgrw(1130100);	
				cm.getPlayer().setmrsgrws(sgsj);	
            }else if(random == 2){
                
				cm.getPlayer().setmrsgrw(210100);
				cm.getPlayer().setmrsgrws(sgsj);		
            }else if(random == 3){
                
				cm.getPlayer().setmrsgrw(3230308);
				cm.getPlayer().setmrsgrws(sgsj);		
            }else if(random == 4){
                
				cm.getPlayer().setmrsgrw(4230100);	
				cm.getPlayer().setmrsgrws(sgsj);	
            }else if(random == 5){
                
				cm.getPlayer().setmrsgrw(8150100);	
				cm.getPlayer().setmrsgrws(sgsj);	
            }else if(random == 6){
                
				cm.getPlayer().setmrsgrw(5130103);	
				cm.getPlayer().setmrsgrws(sgsj);	
            }else if(random >= 7){
                
				cm.getPlayer().setmrsgrw(5200002);	
				cm.getPlayer().setmrsgrws(sgsj);	
            }
			
            if (randoma <= 1){
                
				cm.getPlayer().setmrsgrwa(6230300);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma == 2){
                
				cm.getPlayer().setmrsgrwa(7130101);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma == 3){
                
				cm.getPlayer().setmrsgrwa(7130601);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma == 4){
                
				cm.getPlayer().setmrsgrwa(5130102);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma == 5){
                
				cm.getPlayer().setmrsgrwa(8140103);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma == 6){
                
				cm.getPlayer().setmrsgrwa(8140600);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma == 7){
                
				cm.getPlayer().setmrsgrwa(8150300);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }else if(randoma >= 8){
                
				cm.getPlayer().setmrsgrwa(8190005);	
				cm.getPlayer().setmrsgrwas(sgsja);	
            }
                cm.sendOk("成功领取每日杀怪任务。\r\n#r请重新打开我查看进度以及要杀的怪物。#l");
                cm.dispose();
        } else if (selection == 2) {
			if(item > 50){
					cm.gainItem(4000463, 5);
				}else{
					cm.sendOk("背包空位不足!");
					cm.dispose();
				}
			
				cm.getPlayer().setmrsgrw(0);	
				cm.getPlayer().setmrsgrws(0);
				cm.getPlayer().setmrsgrwa(0);	
				cm.getPlayer().setmrsgrwas(0);	
				cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"]完成每日杀怪任务，大家一起膜拜吧。");
                cm.sendOk("恭喜你完成每日杀怪任务，请明天再来!");
                cm.dispose();
        }
    }
}

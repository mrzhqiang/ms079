//importPackage(Packages.tools.packet);

var Petlist = new Array(); //用于存储已装或者说已放出来的宠物对象。为 MaplePet类数据。
var petname;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.sendOk("好的。下次再见！");
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.sendOk("好的。下次再见！");
			cm.dispose();
			return;
		}
		if (mode == 1) {
			status++;
		} else {
			status--;
		}
		
		if (status == 0) {		
			cm.sendNext("\r\n#e您好！我是宠物改名NPC\r\n改名的时候,需要把要改名的宠物放出来\r\n尽量不要使用复杂符号,以免卡号\r\n每次需要#v5170000#\r\n#r#k");			
		} else if (status == 1){
		//	cm.getPlayer().dropMessage("0");
			for(i = 0; i < 3; i++){
				if(cm.getChar().getPet(i) != null){
					Petlist.push(cm.getChar().getPet(i)); //循环检查角色装备栏三个宠物位置，如果有装备宠物，就把该宠物对象压入 Petlist对象
				}				
			}
			//cm.getPlayer().dropMessage("1");
			if(Petlist.length > 0){
			//	cm.getPlayer().dropMessage("2");
				if(Petlist.length > 1){  //判断Petlist的长度。也就是说放出来的宠物的数量
				//cm.getPlayer().dropMessage("3");
					cm.sendOk("只有放出一只宠物时才能正确被识别!");
					cm.dispose();
				}else{
				//	cm.getPlayer().dropMessage("4");
					var petid = Petlist[0].getPetItemId();					
					var text = "您将要为 #v" + petid + "# 改名字。 "
					text += "现在名字是：#r" +  Petlist[0].getName() + "#k\r\n\r\n";				
					text += "您确定要更改它的名字吗？";
					cm.sendYesNo(text);										
				}

			}else{
				cm.sendOk("请把要改名的宠物放出来!");
				cm.dispose();
			}			
		}else if (status == 2){			
			cm.sendGetText("请输入新的宠物名字：\r\n#r注意：为了避免出错,请尽量不要使用复杂的符号、火星文等\r\n ");
		}else if (status == 3){
			petname = cm.getText().trim();
			if(petname.getBytes("GBK").length > 12 || petname.getBytes("GBK").length < 2){			
				cm.sendOk("您输入的宠物名字必须是2-10个字符之间,一个汉字算两个字符!");
				cm.dispose();
			}else{			
				if(checktext(petname) == false){
					cm.sendOk("对不起,您的宠物不能使用这个名字!");
					cm.dispose();
				}else{
					cm.sendYesNo("此次宠物改名需要收取宠物更名卡\r\n确定要将#r " + Petlist[0].getName() + " #k改成#r " + petname + " #k吗?");
				}							
			}
		}else if (status == 4){	
			if(cm.haveItem(5170000, 1)) {
				cm.gainItem(5170000, -1);			
			    Petlist[0].setName(petname);			
                cm.getC().getPlayer().getMap().broadcastMessage(MTSCSPacket.changePetName(cm.getPlayer(), petname, 0));
				Petlist[0].saveToDb();
				cm.sendOk("#r宠物改名成功!\r\n需要换线才能正常显示");
			}else{
				cm.sendOk("对不起,您没有宠物更名卡,我不能帮您的宠物改名字。");
			}
			cm.dispose();
		}
	}
}	

function checktext(text){    //字符串过虑功能，自动删除关键字宠物
    var gl = new Array("傻B","GM","gm","管理","你妈","操","SB","笑笑","可可","杨伟","伟航","逗比","SB","阳痿","冒险岛","冒险岛");
    for(i=0; i<gl.length; i++){
	if(text.indexOf(gl[i]) >= 0){		
		return false;
		break;    
	}
    }    
    return true;    
}  




var status = 0; 
function start() { 
    status = -1; 
    action(1, 0, 0); 
} 
function action(mode, type, selection) { 
    if (mode == -1) { 
        cm.dispose(); 
    } else { 
        if (mode == 1) 
            status++; 
        else 
            status--; 
        if (status == 0) { 
            cm.sendNext("#b你想去挑战#k#rPB#k#k#b，每次挑战需要六个人组队才能进去哦!"); 
        } else if (status == 1) { 
            if(cm.getLevel() >= 120 ){  //判断人物等级 
                if (cm.getParty() == null) { // 没有开队伍 
                    cm.sendOk("#e你好像还没有一个队伍,我是不能送你进去的."); 
                    cm.dispose();
                    return ; 
                    }
                     if ( cm.getBossLog('PB') >= 5) { 
                    cm.sendOk("#e你今天已经挑战五次了，请明天再来."); 
                    cm.dispose();
                    return ; 
                    } 
            if (!cm.isLeader()) { // 不是队长 
                cm.sendSimple("如果你想挑战一下PB, 让你的组队队长来找我！"); 
                cm.dispose(); 
                    }else { 
            var party = cm.getParty().getMembers(); 
            var next = true; 
                if (party.size() != 1){
                    next = false; 
                    cm.sendOk("#r本次任务只能6人组队参加哦."); 
                    cm.dispose();
                    return ; 
                    } 
                if (next) { 
            var em = cm.getEventManager("pbquest"); 
                if (em == null) { 
                    cm.sendOk("error！"); 
                } else {  
                cm.setBossLog('PB');
                em.startInstance(cm.getParty(),cm.getChar().getMap()); 
               party = cm.getChar().getEventInstance().getPlayers(); 
              
                } 
            cm.dispose(); 
                    } 
                } 
            }else{ 
                cm.sendOk("#e#r对不起,你的等级太低."); 
                cm.dispose(); 
            } 
        } 
    } 
}    
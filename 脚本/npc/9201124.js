var status; 

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == 1) { 
        status++; 
    }else{ 
        status--; 
    } 
    if (status == 0) { 
    if (cm.getPlayer().getJob() == 0) { 
        cm.sendNext("Welcome, Beginning Explorer! In Maple Story,you can\r\nchoose a #rjob#k when you reach #rLv 10#k (Lv 8 for Magicians).\r\n\r\nIn other words, you'll be choosing your own future path!\r\nWhen you get a job,you get to use various skills and magic nwhice will make your experience in Maple Story more enjoyable.So,work hard to carve your own destiny"); 
    } else { 
        cm.sendOk("It looks like you've already made a job advancement!\r\nTransportation can only be used by beginners"); 
        cm.dispose(); 
    } 
    } else if (status == 1) { 
        cm.sendNextPrev("My role is to help you become a #rBowman.#k\r\n\r\nBowmen specialize in Long-Ranged Attacks from the back of the battle lines, since they are deft but have limited Strength. Bowmen become stronger as they level up, employing various attack skills that make them particularly effective with long-ranged Attacks. They aare also very capable hunters who can take advantage of the topography."); 
    } else if (status == 2) { 
        cm.sendNextPrev("Weapons used include the #bBow#k and #bCrossbow#k\r\n\r\nRequired Level: #rOver Lv 10#k\r\nLocation: #rBowman Instrustional School#k in #bHenesys#k\r\nJob Instructor: #rAthena Pierce#k"); 
    } else if (status == 3) { 
        cm.sendSimple("Would you like to become a #rBowman?#k\r\n#b#L0#Yes#l\r\n#L1#No#l#k"); 
    } else if (status == 4) { 
      if (selection == 0) { 
        cm.sendSimple("In order to make the job advancement, you must visit #rAthena Pierce#k at the #rBowman Instrustional School#k in #bHenesys#k.Would you like to be trasported there now?-The transportation service cannot be used once you make the job advancement-\r\n\r\n#b#L0#Yes#l\r\n#L1#No#l#k"); 
    } else if (selection == 1) { 
        cm.sendNext("Please talk to me again if you have any questions."); 
        cm.dispose(); 
    } 
    } else if (status == 5) { 
      if (selection == 0) { 
        cm.sendNext("Alright.I will now take you to the #rBowman Instrustional School#k in #bHenesys.#k"); 
    } else if (selection == 1) { 
        cm.sendNext("Please talk to me again if you have any questions."); 
        cm.dispose(); 
    } 
  } else if (status == 6) { 
        cm.warp(100000201, 11); 
	cm.dispose();
  } 
}  
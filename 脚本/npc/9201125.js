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
        cm.sendNext("Welcome, Beginning Explorer! In Maple Story,you can\r\nchoose a #rjob#k when you reach #rLv 8#k.\r\n\r\nIn other words, you'll be choosing your own future path!\r\nWhen you get a job,you get to use various skills and magic nwhice will make your experience in Maple Story more enjoyable.So,work hard to carve your own destiny"); 
    } else { 
        cm.sendOk("It looks like you've already made a job advancement!\r\nTransportation can only be used by beginners"); 
        cm.dispose(); 
    } 
    } else if (status == 1) { 
        cm.sendNextPrev("My role is to help you become a #rMagician.#k\r\n\r\nPursuing ancient knowledge is their lifelong task, therefore high intelligence is required to become a Magician. While their strength and defense is low compared to other job classes, Magicians use elemental magic skills that create wondrous displays and secondary magic skills that can be useful while hunting in a party. Elemental magic skills can be learned with the 2nd job advancement, which can cause great damage to enemies with opposiing elemental natures."); 
    } else if (status == 2) { 
        cm.sendNextPrev("Weapons used include the #bWands#k and #bStaffs#k\r\n\r\nRequired Level: #rOver Lv 8#k\r\nLocation: #rMagic Library#k in #bEllinia#k\r\nJob Instructor: #rGrendel the Really Old#k"); 
    } else if (status == 3) { 
        cm.sendSimple("Would you like to become a #rMagician?#k\r\n#b#L0#Yes#l\r\n#L1#No#l#k"); 
    } else if (status == 4) { 
      if (selection == 0) { 
        cm.sendSimple("In order to make the job advancement, you must visit #rGrendel the Really Old#k at the #rMagic Library#k in #bEllinia#k.Would you like to be trasported there now?-The transportation service cannot be used once you make the job advancement-\r\n\r\n#b#L0#Yes#l\r\n#L1#No#l#k"); 
    } else if (selection == 1) { 
        cm.sendNext("Please talk to me again if you have any questions."); 
        cm.dispose(); 
    } 
    } else if (status == 5) { 
      if (selection == 0) { 
        cm.sendNext("Alright.I will now take you to the #rMagician#k in #bEllinia.#k"); 
    } else if (selection == 1) { 
        cm.sendNext("Please talk to me again if you have any questions."); 
        cm.dispose(); 
    } 
  } else if (status == 6) { 
        cm.warp(101000003, 10); 
	cm.dispose();
  } 
}  
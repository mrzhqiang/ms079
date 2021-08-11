/* Author: Xterminator
	NPC Name: 		Robin
	Map(s): 		Maple Road : Snail Hunting Ground I (40000)
	Description: 		Beginner Helper
*/
var status;
var sel;

function start() {
    status = -1;
    sel = -1;
    cm.sendSimple("我可以告诉你些冒险者的技巧唷!!\r\n#L0##b要怎么移动？#l\r\n#L1#我要如何击退怪物？#l\r\n#L2#我要怎么捡起物品？#l\r\n#L3#当我死掉会发生什么事情？#l\r\n#L4#我何时能选择职业？#l\r\n#L5#告诉我有关这个岛屿！#l\r\n#L6#我要怎么做才能成为战士？#l\r\n#L7#我要怎么做才能成为弓箭手？#l\r\n#L8#我要怎么做才能成为魔法师？#l\r\n#L9#我要怎么做才能成为盗贼？#l\r\n#L10#怎么提升能力值？(S)#l\r\n#L11#我要怎么确认我捡起来的物品呢？#l\r\n#L12#我要怎么装备物品？#l\r\n#L13#我要怎么确认我身上已经装备的物品？#l\r\n#L14#什么是技能？(K)#l\r\n#L15#我要怎么前往维多利亚岛？#l\r\n#L16#金币是什么？#l#k");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if(mode == 0 && type != 4)
            status -= 2;
        else{
            cm.dispose();
            return;
        }
    }
    if (status == 0) {
        if(sel == -1)
            sel = selection;
        if (sel == 0)
            cm.sendNext("好，我来教你如何移动。 使用 #方向左键#k 就能在平台上移动了，按下 #bAlt#k 可以进行跳跃。 有些鞋子能提升你的速度以及跳跃力。");
        else if (sel == 1)
            cm.sendNext("好，击退怪物很简单，每个怪物有自己的血条，你可以使用武器将他们杀死。当然，如果怪物等级越高，你越难击退它们。");
        else if (sel == 2)
            cm.sendNext("接下来告诉你如何剪取物品，当你击退怪物时，会有机会掉落宝物以及金币，当地上有物品时，按下#bZ#k 或是 数字键盘上的 #b0 来捡取物品。");
        else if (sel == 3)
            cm.sendNext("你好奇地找出当你死会发生什么吗？ 当你的HP归零时，你会变成幽灵。 而地上会出现一块墓碑，而你无法移动，但是你还是可以聊天。");
        else if (sel == 4)
            cm.sendNext("什么时候你可以选择你的职业？哈哈哈，别紧张，我的朋友啊～每个职业都有等级的限制。通常在8等和10等之间会进行。");
        else if (sel == 5)
            cm.sendNext("你想要知道这个岛屿吗？ 这里是枫之岛，这座岛屿浮在天空上。由于浮在天空上，强大的怪物们无法靠近。这里非常和平，非常适合新手。");
        else if (sel == 6)
            cm.sendNext("你想成为#b战士#k？ 摁...那我建议你到维多利亚港，寻找一个叫做#r勇士之村#k的战士村庄以及去找寻#bDances with Balrog#k。 他会教你如何成为一个战士。 喔对了，有件很重要的事，你必须达到等级10才能成为战士！");
        else if (sel == 7)
            cm.sendNext("You want to become a #bBowman#k? You'll need to go to Victoria Island to make the job advancement. Head over to a bowman-town called #rHenesys#k and talk to the beautiful #bAthena Pierce#k and learn the in's and out's of being a bowman. Ohh, and one VERY important thing: You'll need to be at least level 10 in order to become a bowman!!");
        else if (sel == 8)
            cm.sendNext("You want to become a #bMagician#k? For you to do that, you'll have to head over to Victoria Island. Head over to a magician-town called #rEllinia#k, and at the very top lies the Magic Library. Inside, you'll meet the head of all wizards, #bGrendel the Really Old#k, who'll teach you everything about becoming a wizard.");
        else if (sel == 9)
            cm.sendNext("You want to become a #bThief#k? In order to become one, you'll have to head over to Victoria Island. Head over to a thief-town called #rKerning City#k, and on the shadier side of town, you'll see a thief's hideaway. There, you'll meet #bDark Lord#k who'll teach you everything about being a thief. Ohh, and one VERY important thing: You'll need to be at least level 10 in order to become a thief!!");
        else if (sel == 10)
            cm.sendNext("You want to know how to raise your character's ability stats? First press #bS#k to check out the ability window. Every time you level up, you'll be awarded 5 ability points aka AP's. Assign those AP's to the ability of your choice. It's that simple.");
        else if (sel == 11)
            cm.sendNext("You want to know how to check out the items you've picked up, huh? When you defeat a monster, it'll drop an item on the ground, and you may press #bZ#k to pick up the item. That item will then be stored in your item inventory, and you can take a look at it by simply pressing #bI#k.");
        else if (sel == 12)
            cm.sendNext("You want to know how to wear the items, right? Press #bI#k to check out your item inventory. Place your mouse cursor on top of an item and double-click on it to put it on your character. If you find yourself unable to wear the item, chances are your character does not meet the level & stat requirements. You can also put on the item by opening the equipment inventory (#bE#k) and dragging the item into it. To take off an item, double-click on the item at the equipment inventory.");
        else if (sel == 13)
            cm.sendNext("You want to check on the equipped items, right? Press #bE#k to open the equipment inventory, where you'll see exactly what you are wearing right at the moment. To take off an item, double-click on the item. The item will then be sent to the item inventory.");
        else if (sel == 14)
            cm.sendNext("The special 'abilities' you get after acquiring a job are called skills. You'll acquire skills that are specifically for that job. You're not at that stage yet, so you don't have any skills yet, but just remember that to check on your skills, press #bK#k to open the skill book. It'll help you down the road.");
        else if (sel == 15)
            cm.sendNext("How do you get to Victoria Island? On the east of this island there's a harbor called Southperry. There, you'll find a ship that flies in the air. In front of the ship stands the captain. Ask him about it.");
        else if (sel == 16)
            cm.sendNext("It's the currency used in MapleStory. You may purchase items through mesos. To earn them, you may either defeat the monsters, sell items at the store, or complete quests...");
    } else if (status == 1) {
        if (sel == 0)
            cm.sendNextPrev("In order to attack the monsters, you'll need to be equipped with a weapon. When equipped, press #bCtrl#k to use the weapon. With the right timing, you'll be able to easily take down the monsters.");
        else if (sel == 1)
            cm.sendNextPrev("Once you make the job advancement, you'll acquire different kinds of skills, and you can assign them to HotKeys for easier access. If it's an attacking skill, you don't need to press Ctrl to attack, just press the button assigned as a HotKey.");
        else if (sel == 2)
            cm.sendNextPrev("Remember, though, that if your item inventory is full, you won't be able to acquire more. So if you have an item you don't need, sell it so you can make something out of it. The inventory may expand once you make the job advancement.");
        else if (sel == 3)  
            cm.sendNextPrev("There isn't much to lose when you die if you are just a beginner. Once you have a job, however, it's a different story. You'll lose a portion of your EXP when you die, so make sure you avoid danger and death at all cost.");
        else if (sel == 4) 
            cm.sendNextPrev("Level isn't the only thing that determines the advancement, though. You also need to boost up the levels of a particular ability based on the occupation. For example, to be a warrior, your STR has to be over 35, and so forth, you know what I'm saying? Make sure you boost up the abilities that has direct implications to your job.");
        else if (sel == 5)
            cm.sendNextPrev("But, if you want to be a powerful player, better not think about staying here for too long. You won't be able to get a job anyway. Underneath this island lies an enormous island called Victoria Island. That place is so much bigger than here, it's not even funny.");
        else if (sel == 8)
            cm.sendNextPrev("Oh by the way, unlike other jobs, to become a magician you only need to be at level 8. What comes with making the job advancement early also comes with the fact that it takes a lot to become a true powerful mage. Think long and carefully before choosing your path.");
        else if (sel == 10)
            cm.sendNextPrev("Place your mouse cursor on top of all abilities for a brief explanation. For example, STR for warriors, DEX for bowman, INT for magician, and LUK for thief. That itself isn't everything you need to know, so you'll need to think long and hard on how to emphasize your character's strengths through assigning the points.");
        else if (sel == 15)
            cm.sendNextPrev("Oh yeah! One last piece of information before I go. If you are not sure where you are, always press #bW#k. The world map will pop up with the locator showing where you stand. You won't have to worry about getting lost with that.");
        else
            start();
    }else
        start();
}

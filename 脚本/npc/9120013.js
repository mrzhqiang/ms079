/* Boss Kitty
	Zipangu : Showa Town (801000000)
	
	Quiz for quest 8012 (Sakura, the Kitty, and the Orange Marble)
*/

var status = -1;
var questions = new Array("下面物品不是狸猫所掉出的物品?","古代神社中，写有『香菇』的地方有几处？","古代神社的贩卖物品里，何者是提升攻击力的？?","下列物品中，那个物品是存在的东西？?","那个物品不存在??","在昭和镇蔬菜店老板叫什么名字?","这些物品的那个存在?","昭和村卖鱼的铺子外面写着哪几个字?","哪种道具的说明有错误？?","何者不是古代神社的元泰卖的拉面？?","昭和电影院门前的NPC 是谁？?");;
var answers  = new Array(new Array("狸猫柴火","独角狮的硬角","红色的砖"),new Array("6","5","4"),new Array("章鱼烧","福建面","面粉"),new Array("乌鸦屎","黄色雨伞","骆驼蛋"),new Array("冻冻鱼","寒冰破魔枪","苍蝇拍"),new Array("萨米","卡米","由美"),new Array("云狐的牙齿","花束","狐狸的尾巴"),new Array("商荣繁盛","全场一折","欢迎光临"),new Array("竹矛-战士唯一的武器","橡皮榔头-单手剑","龙背刃-双手剑"),new Array("蛋炒面","日本炒面","蘑菇特制拉面"),new Array("武大郎","樱桃小丸子","绘里香"));;
var correctAnswer = new Array(1,1,0,1,2,2,2,0,0,2,2);
var questionNum;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	if (cm.getQuestStatus(8012) == 1 && !cm.haveItem(4031064)){ //quest in progress
	    cm.sendYesNo("你得到他们了吗？你准备回答我所有的问题吗?");
	} else { //quest not started or already completed
	    cm.sendOk("喵喵喵~!");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	var hasChicken = cm.haveItem(2020001, 300);

	if (!hasChicken) {
	    cm.sendOk("什么？不!我需要300个炸鸡。如果你想要的话，你要交更多的朋友，但我至少需要300个.并不是所有的人都能像你一样伟大...");
	    cm.safeDispose();
	} else {
	    cm.gainItem(2020001, -300)
	    cm.sendNext("干得好!现在开始答题!我这里有一些食物!帮助自己. 好吧，现在是时候让我问你一些问题了。我相信你会意识到这一点，但记住，如果你错了，那就结束了。这一切或什么都没有!");
	}
    } else if (status == 7) { //2-6 are the questions
	if (selection != correctAnswer.pop()){
	    cm.sendNext("嗯，反正所有人类犯错误! 如果你想再来回答一次，那就给我带300个炸鸡.")
	    cm.safeDispose();
	}
	else {
	    cm.sendNext("喵~，你回答了所有的问题。我可能不喜欢人类，但我不喜欢破坏一个承诺，所以，正如所承诺的，这里的橙色大理石.")
	}
    } else if (status == 8) { //gain marble
	cm.gainItem(4031064, 1);
	cm.sendOk("我们的生意结束了，非常感谢你！你现在可以走了!");
	cm.safeDispose();
    } else if (status >= 2 && status <= 6 && mode == 1) {//questions
	var cont = true;
	if (status > 2) {
	    if (selection != correctAnswer.pop()){
		cm.sendNext("嗯，反正所有人类犯错误！如果你想再来回答一次，那就给我带300个炸鸡.")
		cm.safeDispose();
		cont = false;
	    }
	}
			
	if (cont) {
	    questionNum = Math.floor(Math.random() * questions.length);
	    if (questionNum != (questions.length - 1)){
		var temp;
		temp = questions[questionNum];
		questions[questionNum] = questions[questions.length - 1];
		questions[questions.length - 1] = temp;
		temp = answers[questionNum];
		answers[questionNum] = answers[questions.length - 1];
		answers[questions.length - 1] = temp;
		temp = correctAnswer[questionNum];
		correctAnswer[questionNum] = correctAnswer[questions.length - 1];
		correctAnswer[questions.length - 1] = temp;
	    }
				
	    var question = questions.pop();
	    var answer = answers.pop();
	    var prompt = "问题." + (status - 1) + ": " + question;
				
	    for (var i = 0; i < answer.length; i++) {
		prompt += "\r\n#b#L" + i + "#" + answer[i] + "#l#k"
	    }
				
	    cm.sendSimple(prompt);
	}
    }
}
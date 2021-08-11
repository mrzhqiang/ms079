/* 	Sixx
 Singa REG/VIP Eye Color Changer
 */
var status = -1;
var beauty = 0;
var colors = Array();

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {
        cm.sendSimple("Hi, there! I'm Sixx, in charge of Da Yan Jing Lens Shop here at CBD! With #b#t5152039##k or #b#t5152040##k, you can let us take care of the rest and have the kind of beautiful look you've always craved! Remember, the first thing everyone notices about you are the eyes, and we can help you find the cosmetic lens that most fits you! Now, what would you like to use?\r\n#L1#Cosmetic Lenses: #i5152039##t5152039##l\r\n#L2#Cosmetic Lenses: #i5152040##t5152040##l");
    } else if (selection == 1) {
        beauty = 1;
        if (cm.getPlayerStat("GENDER") == 0) {
            var current = cm.getPlayerStat("FACE") % 100 + 20000;
        } else {
            var current = cm.getPlayerStat("FACE") % 100 + 21000;
        }
        colors = Array();
        colors = Array(current, current + 100, current + 200, current + 300, current + 400, current + 500, current + 600, current + 700);
        cm.sendYesNo("If you use the regular coupon, you'll be awarded a random pair of cosmetic lenses. Are you going to use a #b#t5152039##k and really make the change to your eyes?");
    } else if (selection == 2) {
        beauty = 2;
        if (cm.getPlayerStat("GENDER") == 0) {
            var current = cm.getPlayerStat("FACE") % 100 + 20000;
        } else {
            var current = cm.getPlayerStat("FACE") % 100 + 21000;
        }
        colors = Array();
        colors = Array(current, current + 100, current + 200, current + 300, current + 400, current + 500, current + 600, current + 700);
        cm.sendStyle("With our specialized machine, you can see yourself after the treatment in advance. What kind of lens would you like to wear? Choose the style of your liking.", colors);
    }
    else if (status == 2) {
        cm.dispose();
        if (beauty == 1) {
            if (cm.setAvatar(5152039, colors[Math.floor(Math.random() * colors.length)] == 1)) {
                cm.sendOk("Enjoy your new and improved cosmetic lenses!");
            } else {
                cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you..");
            }
        }
        if (beauty == 2) {
            if (cm.setAvatar(5152040, colors[selection] == 1)) {
                cm.sendOk("Enjoy your new and improved cosmetic lenses!");
            } else {
                cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you..");
            }
        }
    }
}
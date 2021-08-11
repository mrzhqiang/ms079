var compchoice; 
var playerchoice; 
var Frock = "#fUI/UIWindow.img/RpsGame/Frock#"; 
var Fpaper = "#fUI/UIWindow.img/RpsGame/Fpaper#"; 
var Fscissor = "#fUI/UIWindow.img/RpsGame/Fscissor#"; 
var rock = "#fUI/UIWindow.img/RpsGame/rock#"; 
var paper = "#fUI/UIWindow.img/RpsGame/paper#"; 
var scissor = "#fUI/UIWindow.img/RpsGame/scissor#"; 
var win = "#fUI/UIWindow.img/RpsGame/win#"; 
var lose = "#fUI/UIWindow.img/RpsGame/lose#"; 
var draw = "#fUI/UIWindow.img/RpsGame/draw#"; 
var spacing = "                                   "; 
var beta = "#fUI/UIWindow.img/BetaEdition/BetaEdition#\r\n"; 
var status = -1;
var winmatch = false; 
var losematch = false 
var drawmatch = false; 

function start() { 
    cm.sendNext(beta + "I am the Master of Rock, Paper, Scissors..."); 
} 

function action(mode, type, selection) {
    if (mode != 1) {
        if (status == 1)
            cm.sendOk("Why of course, you're tot chicken to face me in Rock, Paper, Scissors!"); 
        cm.dispose();
        return;
    } else
        status++;
    if (status == 0) { 
            cm.askAcceptDecline("Would you like to challenge me to a game of Rock, Paper Scissors?"); 
    } else if (status == 1) { 
            cm.sendSimple("Choose one...\r\n" 
            + "#L0##fUI/UIWindow.img/RpsGame/Frock##l" 
            + "#L1##fUI/UIWindow.img/RpsGame/Fpaper##l" 
            + "#L2##fUI/UIWindow.img/RpsGame/Fscissor##l" 
            ); 
    } else if (status == 2) { 
        if (selection == 0) { 
            playerchoice = "rock"; 
        } else if (selection == 1) { 
            playerchoice = "paper"; 
        } else if (selection == 2) { 
            playerchoice = "scissor"; 
        } 
        var random = Math.floor(Math.random()*4); 
        if (random <= 1) { 
            compchoice = "rock"; 
        } else if (random <= 2) { 
            compchoice = "paper"; 
        } else if (random <= 4) { 
            compchoice = "scissor"; 
        } 
        cm.sendNext("And the results are..."); 
    } else if (status == 3) { 
        if (playerchoice == "rock" && compchoice == "rock") { 
            cm.sendOk(Frock + spacing + rock + draw); 
            drawmatch = true; 
        } else if (playerchoice == "rock" && compchoice == "paper") { 
            cm.sendOk(Frock + spacing + paper + lose); 
            losematch = true; 
        } else if (playerchoice == "rock" && compchoice == "scissor") { 
            cm.sendOk(Frock + spacing + scissor + win); 
            winmatch = true; 
        } else if (playerchoice == "paper" && compchoice == "rock") { 
            cm.sendOk(Fpaper + spacing + rock + win); 
            winmatch = true; 
        } else if (playerchoice == "paper" && compchoice == "paper") { 
            cm.sendOk(Fpaper + spacing + paper + draw); 
            drawmatch = true; 
        } else if (playerchoice == "paper" && compchoice == "scissor") { 
            cm.sendOk(Fpaper + spacing + scissor + lose); 
            losematch = true; 
        } else if (playerchoice == "scissor" && compchoice == "rock") { 
            cm.sendOk(Fscissor + spacing + rock + lose); 
            losematch = true; 
        } else if (playerchoice == "scissor" && compchoice == "paper") { 
            cm.sendOk(Fscissor + spacing + paper + win); 
            winmatch = true; 
        } else if (playerchoice == "scissor" && compchoice == "scissor") { 
            cm.sendOk(Fscissor + spacing + scissor + draw); 
            drawmatch = true; 
        } else { 
            cm.sendOk("Error"); 
        } 
    } else if (status == 4) { 
        if (losematch == true) 
            cm.gainMeso(-5000);
        if (winmatch == true)
            cm.gainMeso(5000);
        cm.dispose();
    }
}
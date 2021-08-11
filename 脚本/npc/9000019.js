function start() { 
	cm.sendOk("#e#bRock, Paper, Scissors Notice#k#n\r\nThe participation fee for this Rock, Paper, Scissors game is #r1,000 mesos#k.\r\nWinning games will allow you to obtain various Winning Streak Certificates awarded for each consecutive win, but failure to complete the challenge will result in not receiving the Certificate.\r\nThe received Certificate can be traded with the NPC's Paul, Jean, Martin, and Tony.");
} 

function action(mode, type, selection) {
	if (mode != -1) {
		cm.sendRPS();
	}
	cm.dispose();
}
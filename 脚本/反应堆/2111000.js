/* @Author Lerk
 * 
 * 2111000.js: Zakum Party Quest Chest - summons 3 "Mimics"
*/

function act(){
	rm.playerMessage(5, "Oh noes! Monsters in the chest!");
	rm.spawnMonster(9300004,3);
}
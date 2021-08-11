/*
Papulatus Reactor: Performs the Papulatus commands
*/

function act(){
    rm.mapMessage(5, "藉由<時空裂縫的碎片>填補了時空的裂縫。");
    rm.changeMusic("Bgm09/TimeAttack");
    rm.spawnMonster(8500000, -410, -400);
    rm.getMap(220080000).setReactorState();
}
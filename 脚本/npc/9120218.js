var status = -1;

function action(mode, type, selection) {
    if (cm.getMap(806140000).getAllCharactersThreadsafe().size() == 0) {
        cm.warp(806140000);
    } else {
	cm.warp(260010003);
    }
    cm.forceCompleteQuest(50696);
    cm.forceStartQuest(50698);
    cm.dispose();
}
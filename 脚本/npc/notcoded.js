function action(mode, type, selection) {
	cm.sendOk("嗨~ 我是没工作的NPC.\r\n我的代码是 #r" + npcid.toString() + "#k." + "您看到这个就表示这个 NPC 没有工作\r\n如果这是个很重要的 NPC 请联系 GM\r\n谢谢您 !");
	cm.safeDispose();

}
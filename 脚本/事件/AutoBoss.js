var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
    while (nextTime <= java.lang.System.currentTimeMillis()) {
        nextTime += 1000 * 6 * 1;
    }
    setupTask = em.scheduleAtTimestamp("start", nextTime);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function start() {
    scheduleNew();
	var random = java.lang.Math.floor(Math.random() * 3 + 1);//随机频道1-3频道
var mapid = java.lang.Math.floor(Math.random() * 4 + 1);//随机地图一共4个地图 明珠港 射手 废弃 勇士
var BOSS = java.lang.Math.floor(Math.random() * 4 + 1);//随机设置BOSS	1-4个选项随机
var 活动开启时间 = 21;//19 = 19点
var 活动限制时间 = 10;//此选项无效。也别修改！
var 怪物血量;
var bossid;
	if(BOSS <= 1){
		 bossid = 100100;
		怪物血量 = 1000;
	}else if(BOSS == 2){
		 bossid = 100101;
		怪物血量 = 1000;
	}else if(BOSS == 3){
		 bossid = 100120;
		怪物血量 = 1000;
	}else if(BOSS >= 4){
		 bossid = 100121;
		怪物血量 = 1000;
	}
	if(mapid <= 1){
    em.getChannelServer().AutoBoss(random,100000000,活动开启时间,活动限制时间,bossid,0,0,怪物血量);
	}else if(mapid == 2){
    em.getChannelServer().AutoBoss(random,102000000,活动开启时间,活动限制时间,bossid,0,0,怪物血量);
	}else if(mapid == 3){
    em.getChannelServer().AutoBoss(random,103000000,活动开启时间,活动限制时间,bossid,0,0,怪物血量);
	}else if(mapid >= 4){
    em.getChannelServer().AutoBoss(random,104000000,活动开启时间,活动限制时间,bossid,0,0,怪物血量);
	}
}
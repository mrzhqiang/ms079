var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
    em.setProperty("state", "false");
    em.setProperty("endEvent", "true");
    em.setProperty("check", "0");
    em.setProperty("maxCheck", "9999999");
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
	while (nextTime <= java.lang.System.currentTimeMillis()) {
			nextTime += 1000 * 60 * 1; //设置多久开启 1分钟 这写1就是1分钟么 还是什么
			//nextTime += 1000 * 60 * 60 * 6; //设置多久开启 12小时
	}
    setupTask = em.scheduleAtTimestamp("startEvent", nextTime);
}

function startEvent() {
    em.setProperty("state", "true");
    em.setProperty("endEvent", "false");
    em.setProperty("check", 0);
    em.setProperty("maxCheck", "" + getMaxCheck(Math.floor(Math.random() * 2)));
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
    while (nextTime <= java.lang.System.currentTimeMillis()) {
        nextTime += 1000 * 60 * 30; //设置多久结束
    }
    setupTask = em.scheduleAtTimestamp("finishEvent", nextTime);
      // em.serverNotice("12333333333333333333333333333333333333");  
}

function finishEvent() {
    if (em.getProperty("endEvent").equals("false")) {
      // em.serverNotice("[抢楼活动]：[抢楼活动] 活动已经结束。每晚8点开启。本次活动未开出所有奖励，请大家再接再厉。");
    } else {
       //em.serverNotice("[抢楼活动]：[抢楼活动] 本次活动所有奖励已经发放，下次活动将在每晚8点开启，希望大家积极参加。");


    }
    scheduleNew();
}

function cancelSchedule() {
    if (setupTask != null) {
        setupTask.cancel(true);
    }
}

function getMaxCheck(type) {
    switch (type) {
    case 0:
        return 6666;
    case 1:
        return 8888;
    case 2:
        return 9999;
    }
    return 9999;
}

function rand(lbound, ubound) {
    return Math.floor(Math.random() * (ubound - lbound)) + lbound;
}
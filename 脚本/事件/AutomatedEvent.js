var setupTask;
var nextTime;

function init() {
    	var cal = java.util.Calendar.getInstance();
    	cal.set(java.util.Calendar.HOUR, 4);
    	cal.set(java.util.Calendar.MINUTE, 0);
    	cal.set(java.util.Calendar.SECOND, 0);
    	nextTime = cal.getTimeInMillis();
        while (nextTime <= java.lang.System.currentTimeMillis()) {
	    nextTime += 1000 * 60 * 240; // 4小時
        }
    	scheduleNew();
}

function scheduleNew() {
    setupTask = em.scheduleAtTimestamp("setup", nextTime);
    em.setWorldEvent();
}

function cancelSchedule() {
	if (setupTask != null) {
		setupTask.cancel(true);
	}
}

function setup() {
    //em.scheduleRandomEvent();
    setupTask = em.schedule("scheduleNew", 120000); // 2分
    nextTime += 1000 * 60 * 240; // 4小時
}
/*
 结构撰写By 宗达
 */

/* global cm */

var status = -1;
var Editing = false // true = 维修中， false = 开放。
var msg = "";


function start() {
    if (status = -1) {
       msg = "欢迎来玩 屁屁谷Ver:113\r\n"+
			"快来加入我们 http:ppms.tw \r\n"+
			"热门时段人数都是破千的喔\r\n";
        cm.sendNext(msg);
        cm.dispose();
        return;
    }
  
}



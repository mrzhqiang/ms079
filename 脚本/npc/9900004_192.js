var status = 0;
var types = new Array("装备栏", "消耗栏", "设置栏", "其他栏", "特殊栏");
var selectedMap = -1;
function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 3 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else {
            cm.dispose();
            return;
        }
        if (status == 0) {
            var a = "你好我是垃圾回收员,我可以帮你清理无法丢出的物品,请问您要清空：\r\n#b"
            for (var i = 0; i < types.length; i++) {
                a += "\r\n#L" + i + "#" + types[ i ] + "";
            }
            cm.sendSimple(a);
        } else if (status == 1) {
            cm.deleteItem(selection + 1);
            cm.sendOk("清理成功!");
            cm.dispose();
        }


    }
} 
var status = -1;

function end(mode, type, selection) {
    if (mode == 0) {
	status--;
    } else {
	status++;
    }
    if (status == 0) {
	qm.forceCompleteQuest(3514);
	qm.dispose();
    }
}
/*  <imgdir name="02022337">
    <imgdir name="info">
      <canvas name="icon" width="26" height="32">
        <vector name="origin" x="-4" y="32" />
      </canvas>
      <canvas name="iconRaw" width="15" height="31">
        <vector name="origin" x="-8" y="32" />
      </canvas>
      <int name="tradeBlock" value="1" />
      <int name="notSale" value="1" />
      <int name="price" value="1" />
    </imgdir>
    <imgdir name="spec">
      <int name="hpR" value="-100" />
      <int name="mpR" value="-100" />
    </imgdir>*/
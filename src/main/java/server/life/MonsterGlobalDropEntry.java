package server.life;

import com.github.mrzhqiang.maplestory.domain.DDropDataGlobal;

public class MonsterGlobalDropEntry {

    public final DDropDataGlobal global;
    public final boolean onlySelf;

    public MonsterGlobalDropEntry(DDropDataGlobal global) {
        this(global, false);
    }

    public MonsterGlobalDropEntry(DDropDataGlobal global, boolean onlySelf) {
        this.global = global;
        this.onlySelf = onlySelf;
    }
}

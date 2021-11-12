package client.inventory;

import com.github.mrzhqiang.maplestory.domain.query.QVInventoryItemAggregate;
import com.github.mrzhqiang.maplestory.domain.query.QVPetAggregate;
import com.github.mrzhqiang.maplestory.domain.query.QVRingAggregate;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MapleInventoryIdentifier implements Serializable {

    private static final long serialVersionUID = 21830921831301L;
    private AtomicInteger runningUID;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock(), writeLock = rwl.writeLock();
    private static MapleInventoryIdentifier instance = new MapleInventoryIdentifier();

    public MapleInventoryIdentifier() {
        this.runningUID = new AtomicInteger(0);
        getNextUniqueId();
    }

    public static int getInstance() {
        return instance.getNextUniqueId();
    }

    public int getNextUniqueId() {
        if (grabRunningUID() <= 0) {
            setRunningUID(initUID());
        }
        incrementRunningUID();
        return grabRunningUID();
    }

    public int grabRunningUID() {
        readLock.lock();
        try {
            return runningUID.get(); //maybe we should really synchronize/do more here..
        } finally {
            readLock.unlock();
        }
    }

    public void incrementRunningUID() {
        setRunningUID(grabRunningUID() + 1);
    }

    public void setRunningUID(int rUID) {
        if (rUID < grabRunningUID()) { //dont go backwards.
            return;
        }
        writeLock.lock();
        try {
            runningUID.set(rUID); //maybe we should really synchronize/do more here..
        } finally {
            writeLock.unlock();
        }
    }

    public int initUID() {
        int ret = 0;
        if (grabRunningUID() > 0) {
            return grabRunningUID();
        }
        int[] ids = new int[4];
        ids[0] = new QVInventoryItemAggregate()
                .select(QVInventoryItemAggregate.alias().uniqueid)
                .findOneOrEmpty()
                .map(it -> it.uniqueid + 1)
                .orElse(0);
        ids[1] = new QVPetAggregate()
                .select(QVPetAggregate.alias().petid)
                .findOneOrEmpty()
                .map(it -> it.petid + 1)
                .orElse(0);
        ids[2] = new QVRingAggregate()
                .select(QVRingAggregate.alias().ringid)
                .findOneOrEmpty()
                .map(it -> it.ringid + 1)
                .orElse(0);
        ids[3] = new QVRingAggregate()
                .select(QVRingAggregate.alias().partnerringid)
                .findOneOrEmpty()
                .map(it -> it.partnerringid + 1)
                .orElse(0);
        for (int i = 0; i < 4; i++) {
            if (ids[i] > ret) {
                ret = ids[i];
            }
        }
        return ret;
    }
}

package server;

import client.inventory.IItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MerchItemPackage {

    private LocalDateTime sentTime;
    private int mesos = 0, packageid;
    private List<IItem> items = new ArrayList<>();

    public void setItems(List<IItem> items) {
        this.items = items;
    }

    public List<IItem> getItems() {
        return items;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public int getMesos() {
        return mesos;
    }

    public void setMesos(int set) {
        mesos = set;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }
}

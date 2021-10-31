package server.life;

import server.maps.AbstractAnimatedMapleMapObject;

public abstract class AbstractLoadedMapleLife extends AbstractAnimatedMapleMapObject {

    private final int id;
    private int f;
    private boolean hide;
    private int fh, originFh;
    private int cy;
    private int rx0;
    private int rx1;
    private String ctype;
    private int mtime;

    public AbstractLoadedMapleLife(int id) {
        this.id = id;
    }

    public AbstractLoadedMapleLife(AbstractLoadedMapleLife life) {
        this(life.getId());
        this.f = life.f;
        this.hide = life.hide;
        this.fh = life.fh;
        this.originFh = life.fh;
        this.cy = life.cy;
        this.rx0 = life.rx0;
        this.rx1 = life.rx1;
        this.ctype = life.ctype;
        this.mtime = life.mtime;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public boolean isHidden() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public int originFh() {
        return originFh;
    }

    public int getFh() {
        return fh;
    }

    public void setFh(int fh) {
        this.fh = fh;
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public int getRx0() {
        return rx0;
    }

    public void setRx0(int rx0) {
        this.rx0 = rx0;
    }

    public int getRx1() {
        return rx1;
    }

    public void setRx1(int rx1) {
        this.rx1 = rx1;
    }

    public int getId() {
        return id;
    }

    public int getMTime() {
        return mtime;
    }

    public void setMTime(int mtime) {
        this.mtime = mtime;
    }

    public String getCType() {
        return ctype;
    }

    public void setCType(String type) {
        this.ctype = type;
    }
}

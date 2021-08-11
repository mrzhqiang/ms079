package KinMS.db;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import java.util.Collection;
import server.maps.MapleMapFactory;

public abstract interface CherryMSLottery {

    public abstract void addChar(MapleCharacter paramMapleCharacter);

    public abstract void doLottery();

    public abstract void drawalottery();

    public abstract long getAllpeichu();

    public abstract long getAlltouzhu();

    public abstract ChannelServer getChannelServer();

    public abstract Collection<MapleCharacter> getCharacters();

    public abstract MapleMapFactory getMapleMapFactory();

    public abstract int getTouNumbyType(int paramInt);

    public abstract int getZjNum();

    public abstract void setAllpeichu(long paramLong);

    public abstract void setAlltouzhu(long paramLong);

    public abstract void setCharacters(Collection<MapleCharacter> paramCollection);

    public abstract void setZjNum(int paramInt);

    public abstract void warp(int paramInt, MapleCharacter paramMapleCharacter);
}

package server.life;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import com.github.mrzhqiang.maplestory.domain.DPlayerNPC;
import com.github.mrzhqiang.maplestory.domain.DPlayerNPCEquip;
import com.github.mrzhqiang.maplestory.domain.Gender;
import com.github.mrzhqiang.maplestory.domain.query.QDPlayerNPC;
import com.github.mrzhqiang.maplestory.domain.query.QDPlayerNPCEquip;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import handling.channel.ChannelServer;
import handling.world.World;
import tools.MaplePacketCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class PlayerNPC extends MapleNPC {

    public final DPlayerNPC npc;
    private final Map<Integer, Integer> equips = new HashMap<>();
    private final int[] pets = new int[3];

    public PlayerNPC(DPlayerNPC npc) {
        this(npc, null);
    }

    public PlayerNPC(DPlayerNPC npc, MapleCharacter chr) {
        super(npc.getScriptId(), npc.getName());
        this.npc = npc;
        setCoords(npc.getX(), npc.getY(), npc.getDir(), npc.getFoothold());

        if (chr != null) {
            update(chr);
        } else {
            String[] pet = npc.getPets().split(",");

            for (int i = 0; i < 3; i++) {
                if (pet[i] != null) {
                    pets[i] = Integer.parseInt(pet[i]);
                } else {
                    pets[i] = 0;
                }
            }

            new QDPlayerNPCEquip().npcId.eq(getId()).findEach(it ->
                    equips.put(it.getEquipPos(), it.getEquipId()));
        }
    }

    public void setCoords(int x, int y, int f, int fh) {
        setPosition(Vector.of(x, y));
        setCy(y);
        setRx0(x - 50);
        setRx1(x + 50);
        setF(f);
        setFh(fh);
    }

    public static void loadAll() {
        List<PlayerNPC> toAdd = new QDPlayerNPC().findStream().map(PlayerNPC::new).collect(Collectors.toList());

        for (PlayerNPC npc : toAdd) {
            npc.addToServer();
        }
    }

    public static void updateByCharId(MapleCharacter chr) {
        if (World.Find.findChannel(chr.getId()) > 0) { //if character is in cserv
            for (PlayerNPC npc : ChannelServer.getInstance(World.Find.findChannel(chr.getId())).getAllPlayerNPC()) {
                npc.update(chr);
            }
        }
    }

    public void addToServer() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.addPlayerNPC(this);
        }
    }

    public void removeFromServer() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.removePlayerNPC(this);
        }
    }

    public void update(MapleCharacter chr) {
        if (chr == null || npc.getCharId() != chr.getId()) {
            return; //不能使用名称，因为它实际上可能已经改变了..
        }
        setName(chr.getName());
        setHair(chr.getHair());
        setFace(chr.getFace());
        setSkin((chr.getSkinColor()));
        setGender(chr.getGender());
        setPets(chr.getPets());

        equips.clear();
        for (IItem item : chr.getInventory(MapleInventoryType.EQUIPPED).list()) {
            if (item.getPosition() < -128) {
                continue;
            }
            equips.put(item.getPosition(), item.getItemId());
        }
        saveToDB();
    }

    public void destroy() {
        destroy(false); //just sql
    }

    public void destroy(boolean remove) {
        new QDPlayerNPC().scriptId.eq(getId()).delete();
        new QDPlayerNPCEquip().npcId.eq(getId()).delete();

        if (remove) {
            removeFromServer();
        }
    }

    public void saveToDB() {
        if (getNPCFromWZ() == null) {
            destroy(true);
            return;
        }
        destroy();

        npc.save();

        for (Entry<Integer, Integer> equip : equips.entrySet()) {
            DPlayerNPCEquip npcEquip = new DPlayerNPCEquip();
            npcEquip.setNpcId(getId());
            npcEquip.setCharId(getCharId());
            npcEquip.setEquipId(equip.getValue());
            npcEquip.setEquipPos(equip.getKey());
            npcEquip.save();
        }
    }

    public Map<Integer, Integer> getEquips() {
        return equips;
    }

    public int getSkin() {
        return npc.getSkin();
    }

    public Gender getGender() {
        return npc.getGender();
    }

    public int getFace() {
        return npc.getFace();
    }

    public int getHair() {
        return npc.getHair();
    }

    public int getCharId() {
        return npc.getCharId();
    }

    public int getMapId() {
        return npc.getMap();
    }

    public void setSkin(int s) {
        this.npc.setSkin(s);
    }

    public void setFace(int f) {
        this.npc.setFace(f);
    }

    public void setHair(int h) {
        this.npc.setHair(h);
    }

    public void setGender(Gender g) {
        this.npc.setGender(g);
    }

    public int getPet(int i) {
        return Math.max(pets[i], 0);
    }

    public void setPets(List<MaplePet> p) {
        for (int i = 0; i < 3; i++) {
            if (p != null && p.size() > i && p.get(i) != null) {
                this.pets[i] = p.get(i).getPetItemId();
            } else {
                this.pets[i] = 0;
            }
        }
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.getSession().write(MaplePacketCreator.spawnNPC(this, true));
        client.getSession().write(MaplePacketCreator.spawnPlayerNPC(this));
        client.getSession().write(MaplePacketCreator.spawnNPCRequestController(this, true));
    }

    public MapleNPC getNPCFromWZ() {
        MapleNPC npc = MapleLifeFactory.getNPC(getId());
        if (npc != null) {
            npc.setName(getName());
        }
        return npc;
    }
}

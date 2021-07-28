/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.world.guild;

public class MapleGuildSummary implements java.io.Serializable {

    public static final long serialVersionUID = 3565477792085301248L;
    private String name;
    private short logoBG;
    private byte logoBGColor;
    private short logo;
    private byte logoColor;
    private int allianceid;

    public MapleGuildSummary(MapleGuild g) {
        name = g.getName();
        logoBG = (short) g.getLogoBG();
        logoBGColor = (byte) g.getLogoBGColor();
        logo = (short) g.getLogo();
        logoColor = (byte) g.getLogoColor();
        allianceid = g.getAllianceId();
    }

    public String getName() {
        return name;
    }

    public short getLogoBG() {
        return logoBG;
    }

    public byte getLogoBGColor() {
        return logoBGColor;
    }

    public short getLogo() {
        return logo;
    }

    public byte getLogoColor() {
        return logoColor;
    }

    public int getAllianceId() {
        return allianceid;
    }
}

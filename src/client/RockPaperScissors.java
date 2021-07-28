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
package client;

import server.MapleInventoryManipulator;
import server.Randomizer;
import tools.MaplePacketCreator;

public class RockPaperScissors {

    private int round = 0;
    private boolean ableAnswer = true;
    private boolean win = false;

    public RockPaperScissors(final MapleClient c, final byte mode) {
        c.getSession().write(MaplePacketCreator.getRPSMode((byte) (0x09 + mode), -1, -1, -1));
        if (mode == 0) {
            c.getPlayer().gainMeso(-1000, true, true, true);
        }
    }

    public final boolean answer(final MapleClient c, final int answer) {
        if (ableAnswer && !win && answer >= 0 && answer <= 2) {
            final int response = Randomizer.nextInt(3);
            if (response == answer) {
                c.getSession().write(MaplePacketCreator.getRPSMode((byte) 0x0B, -1, (byte) response, (byte) round));
                //dont do anything. they can still answer once a draw
            } else if ((answer == 0 && response == 2) || (answer == 1 && response == 0) || (answer == 2 && response == 1)) { //they win
                c.getSession().write(MaplePacketCreator.getRPSMode((byte) 0x0B, -1, (byte) response, (byte) (round + 1)));
                ableAnswer = false;
                win = true;
            } else { //they lose
                c.getSession().write(MaplePacketCreator.getRPSMode((byte) 0x0B, -1, (byte) response, (byte) -1));
                ableAnswer = false;
            }
            return true;
        }
        reward(c);
        return false;
    }

    public final boolean timeOut(final MapleClient c) {
        if (ableAnswer && !win) {
            ableAnswer = false;
            c.getSession().write(MaplePacketCreator.getRPSMode((byte) 0x0A, -1, -1, -1));
            return true;
        }
        reward(c);
        return false;
    }

    public final boolean nextRound(final MapleClient c) {
        if (win) {
            round++;
            if (round < 10) {
                win = false;
                ableAnswer = true;
                c.getSession().write(MaplePacketCreator.getRPSMode((byte) 0x0C, -1, -1, -1));
                return true;
            }
        }
        reward(c);
        return false;
    }

    public final void reward(final MapleClient c) {
        if (win) {
            MapleInventoryManipulator.addById(c, 4031332 + round, (short) 1, "", null, 0, (byte) 0);
        } else if (round == 0) {
            c.getPlayer().gainMeso(500, true, true, true);
        }
        c.getPlayer().setRPS(null);
    }

    public final void dispose(final MapleClient c) {
        reward(c);
        c.getSession().write(MaplePacketCreator.getRPSMode((byte) 0x0D, -1, -1, -1));
    }
}

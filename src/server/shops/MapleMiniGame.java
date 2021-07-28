/*
 This file is part of the ZeroFusion MapleStory Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

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
package server.shops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleQuestStatus;
import constants.GameConstants;
import server.quest.MapleQuest;
import tools.packet.PlayerShopPacket;

public class MapleMiniGame extends AbstractPlayerStore {

    private final static int slots = 2; //change?!
    private boolean[] exitAfter;
    private boolean[] ready;
    private int[] points;
    private int GameType = 0;
    private int[][] piece = new int[15][15];
    private List<Integer> matchcards = new ArrayList<Integer>();
    int loser = 0;
    int turn = 1;
    int piecetype = 0;
    int firstslot = 0;
    int tie = -1;
    int REDO = -1;

    public MapleMiniGame(MapleCharacter owner, int itemId, String description, String pass, int GameType) {
        super(owner, itemId, description, pass, slots - 1); //?
        this.GameType = GameType;
        this.points = new int[slots];
        this.exitAfter = new boolean[slots];
        this.ready = new boolean[slots];
        reset();
    }

    public void reset() {
        for (int i = 0; i < slots; i++) {
            points[i] = 0;
            exitAfter[i] = false;
            ready[i] = false;
        }
    }

    public void setFirstSlot(int type) {
        firstslot = type;
    }

    public int getFirstSlot() {
        return firstslot;
    }

    public void setPoints(int slot) {
        points[slot]++;
        checkWin();
    }

    public int getPoints() {
        int ret = 0;
        for (int i = 0; i < slots; i++) {
            ret += points[i];
        }
        return ret;
    }

    public void checkWin() {
        if (getPoints() >= getMatchesToWin() && !isOpen()) {
            int x = 0;
            int highest = 0;
            boolean tie = false;
            boolean REDO = false;
            for (int i = 0; i < slots; i++) {
                if (points[i] > highest) {
                    x = i;
                    highest = points[i];
                    tie = false;
                    REDO = false;
                } else if (points[i] == highest) {
                    tie = true;
                    REDO = true;
                }
                points[i] = 0;
            }
            this.broadcastToVisitors(PlayerShopPacket.getMiniGameResult(this, tie ? 1 : 2, x));
            this.setOpen(true);
            update();
            checkExitAfterGame();
        }
    }

    public int getOwnerPoints(int slot) {
        return points[slot];
    }

    public void setPieceType(int type) {
        piecetype = type;
    }

    public int getPieceType() {
        return piecetype;
    }

    public void setGameType() {
        if (GameType == 2) { //omok = 1
            matchcards.clear();
            for (int i = 0; i < getMatchesToWin(); i++) {
                matchcards.add(i);
                matchcards.add(i);
            }
        }
    }

    public void shuffleList() {
        if (GameType == 2) {
            Collections.shuffle(matchcards);
        } else {
            piece = new int[15][15];
        }
    }

    public int getCardId(int slot) {
        return matchcards.get(slot - 1);
    }

    public int getMatchesToWin() {
        return (getPieceType() == 0 ? 6 : (getPieceType() == 1 ? 10 : 15));
    }

    public void setLoser(int type) {
        loser = type;
    }

    public int getLoser() {
        return loser;
    }

    public void send(MapleClient c) {
        if (getMCOwner() == null) {
            closeShop(false, false);
            return;
        }
        c.getSession().write(PlayerShopPacket.getMiniGame(c, this));
    }

    public void setReady(int slot) {
        ready[slot] = !ready[slot];
    }

    public boolean isReady(int slot) {
        return ready[slot];
    }

    public void setPiece(int move1, int move2, int type, MapleCharacter chr) {
        if (piece[move1][move2] == 0 && !isOpen()) {
            piece[move1][move2] = type;
            this.broadcastToVisitors(PlayerShopPacket.getMiniGameMoveOmok(move1, move2, type));
            boolean found = false;
            for (int y = 0; y < 15; y++) {
                for (int x = 0; x < 15; x++) {
                    if (!found && searchCombo(x, y, type)) {
                        this.broadcastToVisitors(PlayerShopPacket.getMiniGameResult(this, 2, getVisitorSlot(chr)));
                        this.setOpen(true);
                        update();
                        checkExitAfterGame();
                        found = true;
                    }
                }
            }
            nextLoser();
        }
    }

    public void nextLoser() { //lol
        loser++;
        if (loser > slots - 1) {
            loser = 0;
        }
    }

    public void exit(MapleCharacter player) {
        if (player == null) {
            return;
        }
        player.setPlayerShop(null);
        if (isOwner(player)) {
            update();
            removeAllVisitors(3, 1);
        } else {
            removeVisitor(player);
        }
    }

    public boolean isExitAfter(MapleCharacter player) {
        if (getVisitorSlot(player) > -1) {
            return this.exitAfter[getVisitorSlot(player)];
        }
        return false;
    }

    public void setExitAfter(MapleCharacter player) {
        if (getVisitorSlot(player) > -1) {
            this.exitAfter[getVisitorSlot(player)] = !this.exitAfter[getVisitorSlot(player)];
        }
    }

    public void checkExitAfterGame() {
        for (int i = 0; i < slots; i++) {
            if (exitAfter[i]) {
                exitAfter[i] = false;
                exit(i == 0 ? getMCOwner() : chrs[i - 1].get());
            }
        }
    }

    public boolean searchCombo(int x, int y, int type) {
        boolean ret = false;
        if (!ret && x < 11) {
            ret = true;
            for (int i = 0; i < 5; i++) {
                if (piece[x + i][y] != type) {
                    ret = false;
                    break;
                }
            }
        }
        if (!ret && y < 11) {
            ret = true;
            for (int i = 0; i < 5; i++) {
                if (piece[x][y + i] != type) {
                    ret = false;
                    break;
                }
            }
        }
        if (!ret && x < 11 && y < 11) {
            ret = true;
            for (int i = 0; i < 5; i++) {
                if (piece[x + i][y + i] != type) {
                    ret = false;
                    break;
                }
            }
        }
        if (!ret && x > 3 && y < 11) {
            ret = true;
            for (int i = 0; i < 5; i++) {
                if (piece[x - i][y + i] != type) {
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }

    public int getScore(MapleCharacter chr) {
        //TODO: Fix formula
        int score = 2000;
        int wins = getWins(chr);
        int ties = getTies(chr);
        int losses = getLosses(chr);
        if (wins + ties + losses > 0) {
            score += wins * 2;
            score += ties;
            score -= losses * 2;
        }
        return score;
    }

    @Override
    public byte getShopType() {
        return GameType == 1 ? IMaplePlayerShop.OMOK : IMaplePlayerShop.MATCH_CARD;
    }

    //questids:
    //omok - win = 122200
    //matchcard - win = 122210
    //TODO: record points
    public int getWins(MapleCharacter chr) {
        return Integer.parseInt(getData(chr).split(",")[2]);
    }

    public int getTies(MapleCharacter chr) {
        return Integer.parseInt(getData(chr).split(",")[1]);
    }

    public int getLosses(MapleCharacter chr) {
        return Integer.parseInt(getData(chr).split(",")[0]);
    }

    public void setPoints(int i, int type) { //lose = 0, tie = 1, win = 2
        MapleCharacter z;
        if (i == 0) {
            z = getMCOwner();
        } else {
            z = getVisitor(i - 1);
        }
        if (z != null) {
            String[] data = getData(z).split(",");
            data[type] = String.valueOf(Integer.parseInt(data[type]) + 1);
            StringBuilder newData = new StringBuilder();
            for (int s = 0; s < data.length; s++) {
                newData.append(data[s]);
                newData.append(",");
            }
            String newDat = newData.toString();
            z.getQuestNAdd(MapleQuest.getInstance(GameType == 1 ? GameConstants.OMOK_SCORE : GameConstants.MATCH_SCORE)).setCustomData(newDat.substring(0, newDat.length() - 1));
        }
    }

    public String getData(MapleCharacter chr) {
        MapleQuest quest = MapleQuest.getInstance(GameType == 1 ? GameConstants.OMOK_SCORE : GameConstants.MATCH_SCORE);
        MapleQuestStatus record;
        if (chr.getQuestNoAdd(quest) == null) {
            record = chr.getQuestNAdd(quest);
            record.setCustomData("0,0,0");
        } else {
            record = chr.getQuestNoAdd(quest);
            if (record.getCustomData() == null || record.getCustomData().length() < 5 || record.getCustomData().indexOf(",") == -1) {
                record.setCustomData("0,0,0"); //refresh
            }
        }
        return record.getCustomData();
    }

    public int getRequestedTie() {
        return tie;
    }

    public void setRequestedTie(int t) {
        this.tie = t;
    }
    
    public int getRequestedREDO() {
        return REDO;
    }

    public void setRequestedREDO(int t) {
        this.REDO = t;
    }    

    public int getTurn() {
        return turn;
    }

    public void setTurn(int t) {
        this.turn = t;
    }

    @Override
    public void closeShop(boolean s, boolean z) {
        removeAllVisitors(3, 1);
        if (getMCOwner() != null) {
            getMCOwner().setPlayerShop(null);
        }
        update();
        getMap().removeMapObject(this);
    }

    @Override
    public void buy(MapleClient c, int z, short i) {
    }
}

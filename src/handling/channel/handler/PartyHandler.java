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
package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import server.maps.Event_DojoAgent;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.data.input.SeekableLittleEndianAccessor;

public class PartyHandler {

    public static final void DenyPartyRequest(final SeekableLittleEndianAccessor slea, final MapleClient c) {
        final int action = slea.readByte();
        final int partyid = slea.readInt();

        if (c.getPlayer().getParty() == null) {
            MapleParty party = World.Party.getParty(partyid);
            if (party != null) {
                if (action == 0x1B) { //accept
                    if (party.getMembers().size() < 6) {
                        World.Party.updateParty(partyid, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
                        c.getPlayer().receivePartyMemberHP();
                        c.getPlayer().updatePartyMemberHP();
                    } else {
                        c.getSession().write(MaplePacketCreator.partyStatusMessage(17));
                    }
                } else if (action != 0x16) {
                    final MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterById(party.getLeader().getId());
                    if (cfrom != null) {
                        cfrom.getClient().getSession().write(MaplePacketCreator.partyStatusMessage(23, c.getPlayer().getName()));
                    }
                }
            } else {
                c.getPlayer().dropMessage(5, "要参加的队伍不存在。");
            }
        } else {
            c.getPlayer().dropMessage(5, "您已经有一个组队，无法加入其他组队!");
        }

    }

    public static final void PartyOperatopn(final SeekableLittleEndianAccessor slea, final MapleClient c) {
        final int operation = slea.readByte();
        MapleParty party = c.getPlayer().getParty();
        MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());
        switch (operation) {
            case 1: // 创建队伍
                if (party == null) {//如果申请创建组队的玩家队伍是空的，那么执行如下
                    party = World.Party.createParty(partyplayer);
                    c.getPlayer().setParty(party);//创建一个队伍分配一个随机ID
                    c.getSession().write(MaplePacketCreator.partyCreated(party.getId()));//发送封包通知客户端，创建队伍信息获得一个随机的队伍ID
                } else//如果申请创建组队的玩家队伍不是空的。执行如下
                {
                    if (partyplayer.equals(party.getLeader()) && party.getMembers().size() == 1) { //如果是组队长。并且队伍人数是等于1人的绝对数 ===
                        c.getSession().write(MaplePacketCreator.partyCreated(party.getId()));//发送封包通知客户端，创建队伍信息获得一个随机的队伍ID
                    } else {
                        c.getPlayer().dropMessage(5, "你已经有一个队伍了，无法再次创建！");
                    }
                }
                break;
            case 2: //退出队伍
                if (party != null) { //想退出队伍，先判断其是不是有队伍
                    if (partyplayer.equals(party.getLeader())) {// 如果离开的玩家是队长就解散队伍
                        if (GameConstants.is武陵道场(c.getPlayer().getMapId())) {//如果是在武陵道场的地图
                            Event_DojoAgent.道场任务失败(c.getPlayer());//那么队长解散队伍的话，结束此次道场挑战，全员传送出去。
                        }
                        if (c.getPlayer().getPyramidSubway() != null) {//如果是在- 隐藏地图 - 奈特的金字塔副本
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());//那么队长解散队伍的话，结束此次道场挑战，全员传送出去。
                        }
                        if (c.getPlayer().getEventInstance() != null) {//副本中有点问题没修好.解散队伍有时候不会传送出去
                            c.getPlayer().getEventInstance().disbandParty();//判断是否是副本状态下。是的话那么队长解散队伍的话，结束此次副本，全员传送出去。
                        }
                        World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);//向客户端发送更新队伍信息，通知解散队伍
                    } else {
                        if (GameConstants.is武陵道场(c.getPlayer().getMapId())) {//如果是在武陵道场的地图
                            Event_DojoAgent.道场任务失败(c.getPlayer());//那么队员退组的话，结束此次道场挑战，全员传送出去。
                        }
                        if (c.getPlayer().getPyramidSubway() != null) {//如果是在- 隐藏地图 - 奈特的金字塔副本
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());//那么队员退组的话，结束此次道场挑战，全员传送出去。
                        }
                        if (c.getPlayer().getEventInstance() != null) {
                            c.getPlayer().getEventInstance().leftParty(c.getPlayer());
                        }
                        World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);//向客户端发送更新队伍信息，通知某个队员退出队伍
                    }
                    c.getPlayer().setParty(null);//发送一个空组队的状态。//必须加一条这，否则队员退组，在副本中不会解散队伍。
                }
                break;
            case 3: //接受邀请
                final int 队伍编号 = slea.readInt();//每个队伍都有一个独立的编号进行存储，这个编号是整数型随机的编号
                if (party == null) {//如果被邀请的玩家，没有队伍
                    party = World.Party.getParty(队伍编号);//获取队伍编号
                    if (party != null) {//如果邀请别人的队伍解除了
                        if (party.getMembers().size() < 6) {//如果队伍小于6人
                            c.getPlayer().setParty(party);//执行给予队伍
                            World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);//更新队伍信息，玩家加入队伍
                            c.getPlayer().receivePartyMemberHP();//收到队伍里面队员的血条
                            c.getPlayer().updatePartyMemberHP();//更新队伍里面队员的HP
                        } else {
                            c.getPlayer().dropMessage(5, "队伍成员已满");//队伍已经不小于6人了。 队长收到提示
                        }
                    } else {
                        c.getPlayer().dropMessage(5, "要加入的队伍不存在");//玩家收到邀请信息后，如果邀请人退出了队伍，就是邀请人的队伍不存在了。那么被邀请者收到信息。
                    }
                } else {
                    c.getPlayer().dropMessage(5, "您已经有一个组队，无法加入其他组队!");//如果被邀请的玩家存在队伍，那么收到信息
                }
                break;
            case 4: // 邀请
                // TODO存储等待邀请和检查
                final String 玩家名字 = slea.readMapleAsciiString();
                final MapleCharacter 邀请 = c.getChannelServer().getPlayerStorage().getCharacterByName(玩家名字);
                if (邀请 != null) {
                    if (邀请.getParty() != null) {//如果被邀请的玩家组队不等于空，就是有组队了
                        c.getPlayer().dropMessage(5, "'" + 玩家名字 + "'已经加入其他组。");//不知道封包是多少，直接用dropMessage
                    } else if (party.getMembers().size() < 6) {//如果队伍人数小于6人
                        c.getPlayer().dropMessage(5, "向" + 邀请.getName() + "发送了组队邀请。");
                        //c.getSession().write(MaplePacketCreator.partyStatusMessage(0x1F, 邀请.getName()));//发送邀请信息 //错误1.邀请他人，自己这边会提示未知错误
                        邀请.getClient().getSession().write(MaplePacketCreator.partyInvite(c.getPlayer()));//发送组队邀请的封包
                    } else {
                        c.getPlayer().dropMessage(5, "组队成员已满");
                        //c.getSession().write(MaplePacketCreator.partyStatusMessage(16));//16这个封包好像不对，直接用dropMessage
                    }
                } else {
                    c.getPlayer().dropMessage(5, "找不到'" + 玩家名字 + "");
                }
                break;
            case 5: // 驱逐成员
                int 被驱逐的玩家ID = slea.readInt();
                if (partyplayer.equals(party.getLeader())) {//如果是组队长
                    final MaplePartyCharacter 驱逐队员 = party.getMemberById(被驱逐的玩家ID);//被驱逐的队员，变量=获取队伍里面玩家的ID,，每个队伍都有一个ID,每个玩家加入后都会存在一个ID
                    if (驱逐队员 != null) {//被驱逐的队员是存在此队伍里的
                        World.Party.updateParty(party.getId(), PartyOperation.EXPEL, 驱逐队员);
                        if (c.getPlayer().getEventInstance() != null) {
                            if (驱逐队员.isOnline()) {
                                c.getPlayer().getEventInstance().disbandParty();
                            }
                        }
                        if (c.getPlayer().getPyramidSubway() != null && 驱逐队员.isOnline()) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    }
                }
                break;
            case 6: //改变队长
                int 编号ID = slea.readInt();
                MaplePartyCharacter 新的队长 = party.getMemberById(编号ID);
                if (partyplayer.equals(party.getLeader()) && 新的队长 != null) {//如果申请改变队长的人，是这个队伍的队长，并且，新的将被任命的队长是存在在这个队伍里的
                    party.setLeader(新的队长);//给予其队长
                    World.Party.updateParty(party.getId(), PartyOperation.SILENT_UPDATE, 新的队长);//通知客户端更新队伍新队长的信息
                }
                break;
            default:
                if (c.getPlayer().isAdmin()) {
                    System.out.println("未知的队伍操作 : 0x" + StringUtil.getLeftPaddedStr(Integer.toHexString(operation).toUpperCase(), '0', 2) + " " + slea);
                }
                break;
        }
    }
}

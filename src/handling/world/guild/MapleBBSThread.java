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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MapleBBSThread implements java.io.Serializable {

    public static final long serialVersionUID = 3565477792085301248L;
    public String name, text;
    public long timestamp;
    public int localthreadID, guildID, ownerID, icon;
    public Map<Integer, MapleBBSReply> replies = new HashMap<Integer, MapleBBSReply>();

    public MapleBBSThread(final int localthreadID, final String name, final String text, final long timestamp,
            final int guildID, final int ownerID, final int icon) {
        this.localthreadID = localthreadID;
        this.name = name;
        this.text = text;
        this.timestamp = timestamp;
        this.guildID = guildID;
        this.ownerID = ownerID;
        this.icon = icon;
    }

    public final int getReplyCount() {
        return replies.size();
    }

    public final boolean isNotice() {
        return localthreadID == 0;
    }

    public static class MapleBBSReply implements java.io.Serializable {

        public int replyid, ownerID;
        public long timestamp;
        public String content;

        public MapleBBSReply(final int replyid, final int ownerID, final String content, final long timestamp) {
            this.ownerID = ownerID;
            this.replyid = replyid;
            this.content = content;
            this.timestamp = timestamp;
        }
    }

    public static class ThreadComparator implements Comparator<MapleBBSThread>, java.io.Serializable {

        @Override
        public int compare(MapleBBSThread o1, MapleBBSThread o2) {
            if (o1.localthreadID < o2.localthreadID) {
                return 1;
            } else if (o1.localthreadID == o2.localthreadID) {
                return 0;
            } else {
                return -1; //opposite here as oldest is last, newest is first
            }
        }
    }
}

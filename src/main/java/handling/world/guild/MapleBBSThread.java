package handling.world.guild;

import com.github.mrzhqiang.maplestory.domain.DBbsReply;
import com.github.mrzhqiang.maplestory.domain.DBbsThread;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class MapleBBSThread {

    public final Map<Integer, MapleBBSReply> replies = new HashMap<>();

    public final DBbsThread entity;

    public String text;
    public int ownerID;

    public MapleBBSThread(DBbsThread entity) {
        this.entity = entity;
        for (DBbsReply data : entity.replys) {
            replies.put(data.id, new MapleBBSReply(data));
        }
    }

    public MapleBBSThread(int localthreadID, String name, String text, long timestamp, int guildID, int ownerID, int icon) {
        this.entity = new DBbsThread();
        this.entity.localthreadid = localthreadID;
        this.entity.name = name;
        this.entity.timestamp = timestamp;
        this.entity.guild = new QDGuild().id.eq(guildID).findOne();
        this.entity.icon = icon;
        this.text = text;
        this.ownerID = ownerID;
    }

    public int getReplyCount() {
        return replies.size();
    }

    public boolean isNotice() {
        return entity.localthreadid == 0;
    }

    public static class MapleBBSReply {

        public final DBbsReply reply;

        public MapleBBSReply(DBbsReply reply) {
            this.reply = reply;
        }

        public MapleBBSReply(int replyid, int ownerID, String content, long timestamp) {
            this.reply = new DBbsReply();
            this.reply.id = replyid;
            this.reply.postercid = ownerID;
            this.reply.content = content;
            this.reply.timestamp = timestamp;
        }
    }

    public static class ThreadComparator implements Comparator<MapleBBSThread> {

        @Override
        public int compare(MapleBBSThread o1, MapleBBSThread o2) {
            if (o1.entity.localthreadid < o2.entity.localthreadid) {
                return 1;
            } else if (o1.entity.localthreadid.equals(o2.entity.localthreadid)) {
                return 0;
            } else {
                return -1; //opposite here as oldest is last, newest is first
            }
        }
    }
}

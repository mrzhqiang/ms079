package handling.world.guild;

import com.github.mrzhqiang.maplestory.domain.DBbsReply;
import com.github.mrzhqiang.maplestory.domain.DBbsThread;
import com.github.mrzhqiang.maplestory.domain.query.QDBbsReply;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MapleBBSThread implements java.io.Serializable {

    public static final long serialVersionUID = 3565477792085301248L;
    public String name, text;
    public long timestamp;
    public int localthreadID, guildID, ownerID, icon;
    public Map<Integer, MapleBBSReply> replies = new HashMap<>();

    public final DBbsThread thread;

    public MapleBBSThread(DBbsThread thread) {
        this.thread = thread;
        new QDBbsReply().threadid.eq(thread.id).findEach(data -> replies.put(data.id, new MapleBBSReply(data)));
    }

    public MapleBBSThread(final int localthreadID, final String name, final String text, final long timestamp,
                          final int guildID, final int ownerID, final int icon) {
        this.thread = null;
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

        public final DBbsReply reply;

        public MapleBBSReply(DBbsReply reply) {
            this.reply = reply;
        }

        public MapleBBSReply(final int replyid, final int ownerID, final String content, final long timestamp) {
            this.reply = new DBbsReply();
            this.reply.id = replyid;
            this.reply.postercid = ownerID;
            this.reply.content = content;
            this.reply.timestamp = timestamp;
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

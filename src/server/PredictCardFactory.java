package server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class PredictCardFactory {

    private static final PredictCardFactory instance = new PredictCardFactory();
    protected MapleDataProvider etcData;
    protected Map<Integer, PredictCard> predictCard;
    protected Map<Integer, PredictCardComment> predictCardComment;

    public PredictCardFactory() {
        this.etcData = MapleDataProviderFactory.getDataProvider(new File("wz/Etc.wz"));
        this.predictCard = new HashMap();
        this.predictCardComment = new HashMap();
    }

    public static PredictCardFactory getInstance() {
        return instance;
    }

    public void initialize() {
        if ((!this.predictCard.isEmpty()) || (!this.predictCardComment.isEmpty())) {
            return;
        }
        MapleData infoData = this.etcData.getData("PredictCard.img");

        for (MapleData cardDat : infoData) {
            if (cardDat.getName().equals("comment")) {
                continue;
            }
            PredictCard card = new PredictCard();
            card.name = MapleDataTool.getString("name", cardDat, "");
            card.comment = MapleDataTool.getString("comment", cardDat, "");
            this.predictCard.put(Integer.valueOf(Integer.parseInt(cardDat.getName())), card);
        }

        MapleData commentData = infoData.getChildByPath("comment");
        for (MapleData commentDat : commentData) {
            PredictCardComment comment = new PredictCardComment();
            comment.worldmsg0 = MapleDataTool.getString("0", commentDat, "");
            comment.worldmsg1 = MapleDataTool.getString("1", commentDat, "");
            comment.score = MapleDataTool.getIntConvert("score", commentDat, 0);
            comment.effectType = MapleDataTool.getIntConvert("effectType", commentDat, 0);
            this.predictCardComment.put(Integer.valueOf(Integer.parseInt(commentDat.getName())), comment);
          //  System.out.println("Type :" + comment.effectType + " score : " + comment.score + " MSG: " + comment.worldmsg0 + " || " + comment.worldmsg1);
        }
    }

    public PredictCard getPredictCard(int id) {
        if (!this.predictCard.containsKey(Integer.valueOf(id))) {
            return null;
        }
        return (PredictCard) this.predictCard.get(Integer.valueOf(id));
    }

    public PredictCardComment getPredictCardComment(int id) {
        // id-=1;
        if (!this.predictCardComment.containsKey(Integer.valueOf(id))) {
            return null;
        }
        return (PredictCardComment) this.predictCardComment.get(Integer.valueOf(id));
    }

    public PredictCardComment RandomCardComment() {
        return getPredictCardComment(Randomizer.nextInt(this.predictCardComment.size()));
    }

    public int getCardCommentSize() {
        return this.predictCardComment.size();
    }

    public static class PredictCard {

        public String name;
        public String comment;
    }

    public static class PredictCardComment {

        public int score;
        public int effectType;
        public String worldmsg0;
        public String worldmsg1;
    }
}

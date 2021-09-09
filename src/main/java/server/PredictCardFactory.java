package server;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;

import java.util.HashMap;
import java.util.Map;

public class PredictCardFactory {

    private static final PredictCardFactory instance = new PredictCardFactory();
    protected Map<Integer, PredictCard> predictCard;
    protected Map<Integer, PredictCardComment> predictCardComment;

    public PredictCardFactory() {
        this.predictCard = new HashMap<>();
        this.predictCardComment = new HashMap<>();
    }

    public static PredictCardFactory getInstance() {
        return instance;
    }

    public void initialize() {
        if ((!this.predictCard.isEmpty()) || (!this.predictCardComment.isEmpty())) {
            return;
        }

        WzData.ETC.directory().findFile("PredictCard.img")
                .map(WzFile::content)
                .map(content -> {
                    content.findByName("comment").ifPresent(element -> {
                        PredictCardComment comment = new PredictCardComment();
                        comment.worldmsg0 = Elements.findString(element, "0");
                        comment.worldmsg1 = Elements.findString(element, "1");
                        comment.score = Elements.findInt(element, "score");
                        comment.effectType = Elements.findInt(element, "effectType");
                        predictCardComment.put(Numbers.ofInt(element.name()), comment);
                    });
                    return content;
                })
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.filter(element -> !element.name().equals("comment"))
                        .forEach(element -> {
                            PredictCard card = new PredictCard();
                            card.name = Elements.findString(element, "name");
                            card.comment = Elements.findString(element, "comment");
                            predictCard.put(Numbers.ofInt(element.name()), card);
                        }));
    }

    public PredictCard getPredictCard(int id) {
        if (!this.predictCard.containsKey(id)) {
            return null;
        }
        return this.predictCard.get(id);
    }

    public PredictCardComment getPredictCardComment(int id) {
        // id-=1;
        if (!this.predictCardComment.containsKey(id)) {
            return null;
        }
        return this.predictCardComment.get(id);
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

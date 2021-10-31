package client;

import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DMonsterBook;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDMonsterBook;
import constants.GameConstants;
import server.MapleItemInformationProvider;
import tools.data.output.MaplePacketLittleEndianWriter;
import tools.packet.MonsterBookPacket;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MonsterBook implements Serializable {

    private static final long serialVersionUID = 7179541993413738569L;

    private boolean changed = false;
    private int SpecialCard = 0, NormalCard = 0, BookLevel = 1;
    private Map<Integer, Integer> cards;

    public MonsterBook(Map<Integer, Integer> cards) {
        this.cards = cards;

        for (Entry<Integer, Integer> card : cards.entrySet()) {
            if (GameConstants.isSpecialCard(card.getKey())) {

                SpecialCard += card.getValue();
            } else {
                NormalCard += card.getValue();
            }
        }
        calculateLevel();
    }

    public Map<Integer, Integer> getCards() {
        return cards;
    }

    public final int getTotalCards() {
        return SpecialCard + NormalCard;
    }

    public final int getLevelByCard(final int cardid) {
        return cards.get(cardid) == null ? 0 : cards.get(cardid);
    }

    public static MonsterBook loadCards(final int charid) {
        List<DMonsterBook> books = new QDMonsterBook().cardid.eq(charid).orderBy().cardid.asc().findList();
        Map<Integer, Integer> cards = new LinkedHashMap<>();
        for (DMonsterBook book : books) {
            cards.put(book.cardid, book.level);
        }
        return new MonsterBook(cards);
    }

    public void saveCards(int charid) {
        if (!changed || cards.size() == 0) {
            return;
        }

        new QDMonsterBook().cardid.eq(charid).delete();

        DCharacter one = new QDCharacter().id.eq(charid).findOne();

        for (Entry<Integer, Integer> all : cards.entrySet()) {
            DMonsterBook book = new DMonsterBook();
            book.character = one;
            book.cardid = all.getKey();
            book.level = all.getValue();
            book.save();
        }
    }

    private void calculateLevel() {
        int Size = NormalCard + SpecialCard;
        BookLevel = 8;

        for (int i = 0; i < 8; i++) {
            if (Size <= GameConstants.getBookLevel(i)) {
                BookLevel = (i + 1);
                break;
            }
        }
    }

    public final void addCardPacket(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeShort(cards.size());

        for (Entry<Integer, Integer> all : cards.entrySet()) {
            mplew.writeShort(GameConstants.getCardShortId(all.getKey())); // Id
            mplew.write(all.getValue()); // Level
        }
    }

    public final void addCharInfoPacket(final int bookcover, final MaplePacketLittleEndianWriter mplew) {
        mplew.writeInt(BookLevel);
        mplew.writeInt(NormalCard);
        mplew.writeInt(SpecialCard);
        mplew.writeInt(NormalCard + SpecialCard);
        mplew.writeInt(MapleItemInformationProvider.getInstance().getCardMobId(bookcover));
    }

    public final void updateCard(final MapleClient c, final int cardid) {
        c.getSession().write(MonsterBookPacket.changeCover(cardid));
    }

    public final void addCard(final MapleClient c, final int cardid) {
        changed = true;
        // c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MonsterBookPacket.showForeginCardEffect(c.getPlayer().getId()), false);

        if (cards.containsKey(cardid)) {
            final int levels = cards.get(cardid);
            if (levels >= 5) {
                c.getSession().write(MonsterBookPacket.addCard(true, cardid, levels));
            } else {
                if (GameConstants.isSpecialCard(cardid)) {
                    SpecialCard += 1;
                } else {
                    NormalCard += 1;
                }
                c.getSession().write(MonsterBookPacket.addCard(false, cardid, levels));
                c.getSession().write(MonsterBookPacket.showGainCard(cardid));
                // c.getSession().write(MaplePacketCreator.showSpecialEffect(13));
                cards.put(cardid, levels + 1);
                calculateLevel();
            }
            return;
        }
        if (GameConstants.isSpecialCard(cardid)) {
            SpecialCard += 1;
        } else {
            NormalCard += 1;
        }
        // New card
        cards.put(cardid, 1);
        c.getSession().write(MonsterBookPacket.addCard(false, cardid, 1));
        c.getSession().write(MonsterBookPacket.showGainCard(cardid));
        //   c.getSession().write(MaplePacketCreator.showSpecialEffect(13));
        calculateLevel();
    }

    public int getSeen() {
        return this.cards.size();
    }
}

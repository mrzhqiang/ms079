package server.events;

import com.github.mrzhqiang.maplestory.domain.DWzOxData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzOxData;
import server.Randomizer;
import tools.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class MapleOxQuizFactory {

    private boolean initialized = false;
    private final Map<Pair<Integer, Integer>, MapleOxQuizEntry> questionCache = new HashMap<>();
    private static final MapleOxQuizFactory instance = new MapleOxQuizFactory();

    public static MapleOxQuizFactory getInstance() {
        return instance;
    }

    public boolean hasInitialized() {
        return initialized;
    }

    public Entry<Pair<Integer, Integer>, MapleOxQuizEntry> grabRandomQuestion() {
        int size = questionCache.size();
        while (true) {
            for (Entry<Pair<Integer, Integer>, MapleOxQuizEntry> oxquiz : questionCache.entrySet()) {
                if (Randomizer.nextInt(size) == 0) {
                    return oxquiz;
                }
            }
        }
    }

    public void initialize() {
        if (initialized) {
            return;
        }

        new QDWzOxData().findEach(it ->
                questionCache.put(new Pair<>(it.getPkQuestion().getQuestionSet(), it.getPkQuestion().getQuestionId()), get(it)));
        initialized = true;
    }

    public static MapleOxQuizEntry getOxEntry(int questionSet, int questionId) {
        return getInstance().getOxQuizEntry(new Pair<>(questionSet, questionId));
    }

    public static MapleOxQuizEntry getOxEntry(Pair<Integer, Integer> pair) {
        return getInstance().getOxQuizEntry(pair);
    }

    public MapleOxQuizEntry getOxQuizEntry(Pair<Integer, Integer> pair) {
        MapleOxQuizEntry mooe = questionCache.get(pair);
        if (mooe == null) {
            if (initialized) {
                return null;
            }

            DWzOxData one = new QDWzOxData()
                    .pkQuestion.questionSet.eq(pair.left)
                    .pkQuestion.questionId.eq(pair.right)
                    .findOne();
            if (one != null) {
                mooe = get(one);
                questionCache.put(pair, mooe);
            }
        }
        return mooe;
    }

    private MapleOxQuizEntry get(DWzOxData data) {
        return new MapleOxQuizEntry(data.getQuestion(), data.getDisplay(), getAnswerByText(data.getAnswer()), data.getPkQuestion().getQuestionSet(), data.getPkQuestion().getQuestionId());
    }

    private int getAnswerByText(String text) {
        if (text.equalsIgnoreCase("x")) {
            return 0;
        } else if (text.equalsIgnoreCase("o")) {
            return 1;
        } else {
            return -1;
        }
    }

    public static class MapleOxQuizEntry {

        private String question, answerText;
        private int answer, questionset, questionid;

        public MapleOxQuizEntry(String question, String answerText, int answer, int questionset, int questionid) {
            this.question = question;
            this.answerText = answerText;
            this.answer = answer;
            this.questionset = questionset;
            this.questionid = questionid;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswerText() {
            return answerText;
        }

        public int getAnswer() {
            return answer;
        }

        public int getQuestionSet() {
            return questionset;
        }

        public int getQuestionId() {
            return questionid;
        }
    }
}

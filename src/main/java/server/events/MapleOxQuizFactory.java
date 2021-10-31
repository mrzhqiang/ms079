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
package server.events;

import com.github.mrzhqiang.maplestory.domain.DWzOxData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzOxData;
import server.Randomizer;
import tools.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapleOxQuizFactory {

    private boolean initialized = false;
    private Map<Pair<Integer, Integer>, MapleOxQuizEntry> questionCache;
    private static MapleOxQuizFactory instance = new MapleOxQuizFactory();

    public MapleOxQuizFactory() {
        questionCache = new HashMap<Pair<Integer, Integer>, MapleOxQuizEntry>();
    }

    public static MapleOxQuizFactory getInstance() {
        return instance;
    }

    public boolean hasInitialized() {
        return initialized;
    }

    public Entry<Pair<Integer, Integer>, MapleOxQuizEntry> grabRandomQuestion() {
        final int size = questionCache.size();
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
                questionCache.put(new Pair<>(it.pkQuestion.questionset, it.pkQuestion.questionid), get(it)));
        initialized = true;
    }

    public static MapleOxQuizEntry getOxEntry(int questionSet, int questionId) {
        return getInstance().getOxQuizEntry(new Pair<Integer, Integer>(questionSet, questionId));
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
                    .pkQuestion.questionset.eq(pair.left)
                    .pkQuestion.questionid.eq(pair.right)
                    .findOne();
            if (one != null) {
                mooe = get(one);
                questionCache.put(pair, mooe);
            }
        }
        return mooe;
    }

    private MapleOxQuizEntry get(DWzOxData data) {
        return new MapleOxQuizEntry(data.question, data.display, getAnswerByText(data.answer), data.pkQuestion.questionset, data.pkQuestion.questionid);
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

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

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import java.util.Map.Entry;
import server.Randomizer;
import tools.Pair;

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
        //System.out.println("加载 OX Quiz  :::");
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM wz_oxdata");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                questionCache.put(new Pair<Integer, Integer>(rs.getInt("questionset"), rs.getInt("questionid")), get(rs));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("Done\r");
        initialized = true;
    }

    public MapleOxQuizEntry getFromSQL(String sql) {
        MapleOxQuizEntry ret = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ret = get(rs);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
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
            mooe = getFromSQL("SELECT * FROM wz_oxdata WHERE questionset = " + pair.getLeft() + " AND questionid = " + pair.getRight());
            questionCache.put(pair, mooe);
        }
        return mooe;
    }

    private MapleOxQuizEntry get(ResultSet rs) throws SQLException {
        return new MapleOxQuizEntry(rs.getString("question"), rs.getString("display"), getAnswerByText(rs.getString("answer")), rs.getInt("questionset"), rs.getInt("questionid"));
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

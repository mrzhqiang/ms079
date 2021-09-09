package tools.wztosql;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import database.DatabaseConnection;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Itzik
 */
public class DumpOxQuizData {

    private final Connection con = DatabaseConnection.getConnection();
    static CharsetEncoder asciiEncoder = Charset.forName("GBK").newEncoder();

    public static void main(String[] args) {
        try {
            //String output = args[0];
            //File outputDir = new File(output);
            //File cashTxt = new File("ox.sql");
            //outputDir.mkdir();
            //cashTxt.createNewFile();
            System.out.println("OXQuiz.img Loading ...");
            //try (PrintWriter writer = new PrintWriter(new FileOutputStream(cashTxt))) {
            //    writer.println("INSERT INTO `wz_oxdata` (`questionset`, `questionid`, `question`, `display`, `answer`) VALUES");
            DumpOxQuizData dump = new DumpOxQuizData();
            dump.dumpOxData();
            //    writer.flush();
            //}
            System.out.println("Ox quiz data is complete");
        } catch (SQLException ex) {
            Logger.getLogger(DumpOxQuizData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dumpOxData() throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM `wz_oxdata`");
        ps.execute();
        ps.close();
        WzData.ETC.directory().findFile("OXQuiz.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(child1 -> {
                    String name1 = child1.name();
                    child1.childrenStream().forEach(child2 -> {
                        String name2 = child2.name();
                        String qs = Elements.findString(child2, "q");
                        String ds = Elements.findString(child2, "d");
                        String as;
                        int a = Elements.findInt(child2, "a");
                        if (a == 0) {
                            as = "x";
                        } else {
                            as = "o";
                        }
                        if (!asciiEncoder.canEncode(name1)
                                || !asciiEncoder.canEncode(name2)
                                || !asciiEncoder.canEncode(qs)
                                || !asciiEncoder.canEncode(ds)
                                || !asciiEncoder.canEncode(as)) {
                            return;
                        }
                        PreparedStatement statement;
                        try {
                            statement = con.prepareStatement("INSERT INTO `wz_oxdata`"
                                    + " (`questionset`, `questionid`, `question`, `display`, `answer`)"
                                    + " VALUES (?, ?, ?, ?, ?)");
                            statement.setString(1, name1);
                            statement.setString(2, name2);
                            statement.setString(3, qs);
                            statement.setString(4, ds);
                            statement.setString(5, as);
                            statement.execute();
                            statement.close();
                        } catch (SQLException ignore) {
                        }
                    });
                }));
    }
}

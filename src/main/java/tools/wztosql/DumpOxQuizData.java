package tools.wztosql;

import com.github.mrzhqiang.maplestory.domain.DWzOxData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzOxData;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Itzik
 */
public class DumpOxQuizData {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DumpOxQuizData.class);

    static CharsetEncoder asciiEncoder = Charset.forName("GBK").newEncoder();

    public static void main(String[] args) {
        try {
            //String output = args[0];
            //File outputDir = new File(output);
            //File cashTxt = new File("ox.sql");
            //outputDir.mkdir();
            //cashTxt.createNewFile();
            LOGGER.debug("OXQuiz.img Loading ...");
            //try (PrintWriter writer = new PrintWriter(new FileOutputStream(cashTxt))) {
            //    writer.println("INSERT INTO `wz_oxdata` (`questionset`, `questionid`, `question`, `display`, `answer`) VALUES");
            DumpOxQuizData dump = new DumpOxQuizData();
            dump.dumpOxData();
            //    writer.flush();
            //}
            LOGGER.debug("Ox quiz data is complete");
        } catch (SQLException ex) {
            Logger.getLogger(DumpOxQuizData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dumpOxData() throws SQLException {
        new QDWzOxData().delete();
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
                        DWzOxData oxData = new DWzOxData();
                        oxData.getPkQuestion().setQuestionSet(Integer.parseInt(name1));
                        oxData.getPkQuestion().setQuestionId(Integer.parseInt(name2));
                        oxData.setQuestion(qs);
                        oxData.setDisplay(ds);
                        oxData.setAnswer(as);
                        oxData.save();
                    });
                }));
    }
}

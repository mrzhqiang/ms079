package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class GetInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetInfo.class);

    public static void main(String[] args) {
        Config();
        getConfig();
        GetInfo.all();
    }

    /*
      windows和Linux，mac下結果不一樣
     */
    public static void getIpconfig() {
        Map<String, String> map = System.getenv();
        LOGGER.debug(String.valueOf(map));

        /*windows*/
        LOGGER.debug(map.get("USERNAME"));//獲取用戶名
        LOGGER.debug(map.get("COMPUTERNAME"));//獲取計算機名
        LOGGER.debug(map.get("USERDOMAIN"));//獲取計算機域名

        /*mac*/
        LOGGER.debug(map.get("USER"));
    }

    //其它的一些東西,會有用到的時候的
    public static void all() {
        Properties 設定檔 = System.getProperties();
        LOGGER.debug("Java的運行環境版本：" + 設定檔.getProperty("java.version"));
        LOGGER.debug("Java的運行環境供應商：" + 設定檔.getProperty("java.vendor"));
        LOGGER.debug("Java供應商的URL：" + 設定檔.getProperty("java.vendor.url"));
        LOGGER.debug("Java的安裝路徑：" + 設定檔.getProperty("java.home"));
        LOGGER.debug("Java的虛擬機規範版本：" + 設定檔.getProperty("java.vm.specification.version"));
        LOGGER.debug("Java的虛擬機規範供應商：" + 設定檔.getProperty("java.vm.specification.vendor"));
        LOGGER.debug("Java的虛擬機規範名稱：" + 設定檔.getProperty("java.vm.specification.name"));
        LOGGER.debug("Java的虛擬機實現版本：" + 設定檔.getProperty("java.vm.version"));
        LOGGER.debug("Java的虛擬機實現供應商：" + 設定檔.getProperty("java.vm.vendor"));
        LOGGER.debug("Java的虛擬機實現名稱：" + 設定檔.getProperty("java.vm.name"));
        LOGGER.debug("Java運行時環境規範版本：" + 設定檔.getProperty("java.specification.version"));
        //       LOGGER.debug("Java運行時環境規範供應商："+設定檔.getProperty("java.specification.vender"));
        LOGGER.debug("Java運行時環境規範名稱：" + 設定檔.getProperty("java.specification.name"));
        LOGGER.debug("Java的類格式版本號：" + 設定檔.getProperty("java.class.version"));
        LOGGER.debug("Java的類路徑：" + 設定檔.getProperty("java.class.path"));
        LOGGER.debug("加載庫時搜索的路徑列表：" + 設定檔.getProperty("java.library.path"));
        LOGGER.debug("默認的臨時文件路徑：" + 設定檔.getProperty("java.io.tmpdir"));
        LOGGER.debug("一個或多個擴展目錄的路徑：" + 設定檔.getProperty("java.ext.dirs"));
        //     LOGGER.debug("操作系統的名稱："+設定檔.getProperty("os.name"));
        LOGGER.debug("操作系統的構架：" + 設定檔.getProperty("os.arch"));
        LOGGER.debug("操作系統的版本：" + 設定檔.getProperty("os.version"));
        LOGGER.debug("文件分隔符：" + 設定檔.getProperty("file.separator"));
        //在 unix 系統中是＂／＂
        LOGGER.debug("路徑分隔符：" + 設定檔.getProperty("path.separator"));
        //在 unix 系統中是＂:＂
        LOGGER.debug("行分隔符：" + 設定檔.getProperty("line.separator"));
        //在 unix 系統中是＂/n＂
        LOGGER.debug("用戶的賬戶名稱：" + 設定檔.getProperty("user.name"));
        LOGGER.debug("用戶的主目錄：" + 設定檔.getProperty("user.home"));
        LOGGER.debug("用戶的當前工作目錄：" + 設定檔.getProperty("user.dir"));
    }

    public static void Config() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString(); //獲取本機ip
            String hostName = addr.getHostName().toString(); //獲取本機計算機名稱
            LOGGER.debug("本機IP：" + ip + "\n本機名稱:" + hostName);
            Properties 設定檔 = System.getProperties();
            LOGGER.debug("操作系統的名稱：" + 設定檔.getProperty("os.name"));
            LOGGER.debug("操作系統的版本：" + 設定檔.getProperty("os.version"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到計算機的ip地址和mac地址<br />
     * IP：127.0.0.1<br />
     * MAC：FE-80-00-00-00-00-00-00-00-00-00-00-00-00-00-01
     */
    public static void getConfig() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();
            byte[] mac = ni.getHardwareAddress();
            if (mac == null) {
                mac = ni.getInetAddresses().nextElement().getAddress();
            }

            String sIP = address.getHostAddress();
            String sMAC = "";
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ? "-" : "").toString();
            }
            LOGGER.debug("IP：" + sIP);
            LOGGER.debug("MAC：" + sMAC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
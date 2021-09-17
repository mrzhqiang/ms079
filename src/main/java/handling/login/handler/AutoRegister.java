package handling.login.handler;

import client.LoginCrypto;
import constants.ServerConstants;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutoRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoRegister.class);

    private static final int ACCOUNTS_PER_MAC = 1;
    public static boolean autoRegister = ServerConstants.getAutoReg();
    public static boolean success = false, mac = true;

    public static boolean getAccountExists(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            LOGGER.debug("执行 sql 出现问题", ex);
        }
        return accountExists;
    }

    public static void createAccount(String login, String pwd, String eip, String macs) {
        Connection con;

        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception ex) {
            LOGGER.debug("创建账号异常", ex);
            return;
        }

        try {
            ResultSet rs;
            PreparedStatement ipc = con.prepareStatement("SELECT macs FROM accounts WHERE macs = ?");
            ipc.setString(1, macs);
            rs = ipc.executeQuery();
            if (!rs.first() || rs.last() && rs.getRow() < ACCOUNTS_PER_MAC) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, SessionIP) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, login);
                ps.setString(2, LoginCrypto.hexSha1(pwd));
                ps.setString(3, "autoregister@mail.com");
                ps.setString(4, "2016-04-10");
                ps.setString(5, macs);
                ps.setString(6, "/" + eip.substring(1, eip.lastIndexOf(':')));
                ps.executeUpdate();
                success = true;
            }
            //  
            //  ipc.close();
            if (rs.getRow() >= ACCOUNTS_PER_MAC) {
                mac = false;
            }
            rs.close();
        } catch (SQLException ex) {
            LOGGER.error("SQL 问题", ex);
        }
    }
}

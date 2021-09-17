package database;

import io.ebean.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);

    public static Connection getConnection() {
        try {
            return DB.getDefault().dataSource().getConnection();
        } catch (SQLException e) {
            LOGGER.error("获取数据库连接出错", e);
            throw new DatabaseException(e);
        }
    }

    public static void closeAll() throws SQLException {
        DB.getDefault().shutdown();
    }
}

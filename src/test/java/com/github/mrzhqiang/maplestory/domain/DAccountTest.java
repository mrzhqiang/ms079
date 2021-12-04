package com.github.mrzhqiang.maplestory.domain;

import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import io.ebean.DB;
import io.ebean.SqlQuery;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.Assert.*;

public class DAccountTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAccountTest.class);

    @Test
    public void nativeSql() {
        SqlQuery sqlQuery = DB.sqlQuery("select count(*) count from accounts");
        sqlQuery.findOneOrEmpty().ifPresent(it ->
                LOGGER.info("account count: {}", it.get("count")));
    }

    @Test
    public void curdBean() {
        String name = "fssd";
        DAccount account = new DAccount(name, "123456");

        DB.save(account);

        account.setGender(Gender.MALE);

        DB.save(account);

        assertEquals(Gender.MALE, account.getGender());

        DAccount fssd = new QDAccount().name.eq(name).findOne();
        assertNotNull(fssd);
        assertEquals(account, fssd);

        DB.delete(fssd);
    }

    @Test
    public void oneToMany() {
        DAccount account = new DAccount("fssd", "123456");

        DB.save(account);

        DCharacter character = new DCharacter(account);
        character.name = "放肆青春";

        DB.save(character);

        Optional<DAccount> fssd = new QDAccount().characters.name.eq("放肆青春").findOneOrEmpty();

        assertTrue(fssd.isPresent());

        assertEquals("fssd", fssd.get().name);
    }
}
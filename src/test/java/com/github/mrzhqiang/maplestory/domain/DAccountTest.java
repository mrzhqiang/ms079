package com.github.mrzhqiang.maplestory.domain;

import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import io.ebean.DB;
import io.ebean.SqlQuery;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class DAccountTest {

    @Test
    public void nativeSql() {
        SqlQuery sqlQuery = DB.sqlQuery("select * from accounts");
        sqlQuery.findOneOrEmpty().ifPresent(System.out::println);
    }

    @Test
    public void insertFindDelete() {
        DAccount account = new DAccount();
        account.name = "fssd";
        account.password = "123456";

        DB.save(account);

        DAccount fssd = new QDAccount().name.eq("fssd").findOne();
        assertNotNull(fssd);
        assertEquals(account.name, fssd.name);

        DB.delete(fssd);
    }

    @Test
    public void oneToMany() {
        DAccount account = new DAccount();
        account.name = "fssd";
        account.password = "123456";

        DB.save(account);

        DCharacter character = new DCharacter();
        character.account = account;
        character.name = "放肆青春";

        DB.save(character);

        Optional<DAccount> fssd = new QDAccount().characters.name.eq("放肆青春").findOneOrEmpty();

        assertTrue(fssd.isPresent());

        assertEquals("fssd", fssd.get().name);
    }
}
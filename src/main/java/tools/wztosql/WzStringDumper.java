package tools.wztosql;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;

import java.io.*;
import java.util.Collections;
import java.util.Optional;

public class WzStringDumper {

    public static void main(String[] args)
            throws FileNotFoundException, IOException {
//        File stringFile = WzManage.STRING_DIR;
//        MapleDataProvider stringProvider = MapleDataProviderFactory.getDataProvider(stringFile);


        Optional<WzFile> cash = WzData.STRING.directory().findFile("Cash.img");
        Optional<WzFile> consume = WzData.STRING.directory().findFile("Consume.img");
        Optional<WzElement<?>> eqp = WzData.STRING.directory().findFile("Eqp.img")
                .map(wzFile -> wzFile.content().find("Eqp"));
        Optional<WzElement<?>> etc = WzData.STRING.directory().findFile("Etc.img")
                .map(wzFile -> wzFile.content().find("Etc"));
        Optional<WzFile> ins = WzData.STRING.directory().findFile("Ins.img");
        Optional<WzFile> pet = WzData.STRING.directory().findFile("Pet.img");
        Optional<WzFile> map = WzData.STRING.directory().findFile("Map.img");
        Optional<WzFile> mob = WzData.STRING.directory().findFile("Mob.img");
        Optional<WzFile> skill = WzData.STRING.directory().findFile("Skill.img");
        Optional<WzFile> npc = WzData.STRING.directory().findFile("Npc.img");

        String output = args[0];

        File outputDir = new File(output);
        File cashTxt = new File(output + "\\Cash.txt");
        File useTxt = new File(output + "\\Use.txt");
        File eqpDir = new File(output + "\\Equip");
        File etcTxt = new File(output + "\\Etc.txt");
        File insTxt = new File(output + "\\Setup.txt");
        File petTxt = new File(output + "\\Pet.txt");
        File mapTxt = new File(output + "\\Map.txt");
        File mobTxt = new File(output + "\\Mob.txt");
        File skillTxt = new File(output + "\\Skill.txt");
        File npcTxt = new File(output + "\\NPC.txt");
        outputDir.mkdir();
        cashTxt.createNewFile();
        useTxt.createNewFile();
        eqpDir.mkdir();
        etcTxt.createNewFile();
        insTxt.createNewFile();
        petTxt.createNewFile();
        mapTxt.createNewFile();
        mobTxt.createNewFile();
        skillTxt.createNewFile();
        npcTxt.createNewFile();

        System.out.println("提取 Cash.img 數據...");
        PrintWriter writer = new PrintWriter(new FileOutputStream(cashTxt));
        for (WzElement<?> child : cash.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            WzElement<?> descData = child.find("desc");
            String name = "";
            String desc = "(無描述)";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            if (descData != null) {
                desc = (String) descData.value();
            }
            writer.println(child.name() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("Cash.img 提取完成.");

        System.out.println("提取 Consume.img 數據...");
        writer = new PrintWriter(new FileOutputStream(useTxt));
        for (WzElement<?> child : consume.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            WzElement<?> descData = child.find("desc");
            String name = "";
            String desc = "(無描述)";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            if (descData != null) {
                desc = (String) descData.value();
            }
            writer.println(child.name() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("Consume.img 提取完成.");

        System.out.println("提取 Eqp.img 數據...");
        /*
         * 9800
         */
        for (WzElement<?> child : eqp.map(WzElement::children).orElse(Collections.emptyList())) {
            System.out.println("提取 " + child.name() + " 數據...");
            File eqpFile = new File(output + "\\Equip\\" + child.name() + ".txt");
            eqpFile.createNewFile();
            PrintWriter eqpWriter = new PrintWriter(new FileOutputStream(eqpFile));
            for (WzElement<?> child2 : child.children()) {
                WzElement<?> nameData = child2.find("name");
                WzElement<?> descData = child2.find("desc");
                String name = "";
                String desc = "(無描述)";
                if (nameData != null) {
                    name = (String) nameData.value();
                }
                if (descData != null) {
                    desc = (String) descData.value();
                }
                eqpWriter.println(child2.name() + " - " + name + " - " + desc);
            }
            eqpWriter.flush();
            eqpWriter.close();
            System.out.println(child.name() + " 提取完成.");
        }
        System.out.println("Eqp.img 提取完成.");

        System.out.println("提取 Etc.img 數據...");
        writer = new PrintWriter(new FileOutputStream(etcTxt));
        for (WzElement<?> child : etc.map(WzElement::children).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            WzElement<?> descData = child.find("desc");
            String name = "";
            String desc = "(無描述)";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            if (descData != null) {
                desc = (String) descData.value();
            }
            writer.println(child.name() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("Etc.img 提取完成.");

        System.out.println("提取 Ins.img 數據...");
        writer = new PrintWriter(new FileOutputStream(insTxt));
        for (WzElement<?> child : ins.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            WzElement<?> descData = child.find("desc");
            String name = "";
            String desc = "(無描述)";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            if (descData != null) {
                desc = (String) descData.value();
            }
            writer.println(child.name() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("Ins.img 提取完成.");

        System.out.println("提取 Pet.img 數據...");
        writer = new PrintWriter(new FileOutputStream(petTxt));
        for (WzElement<?> child : pet.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            WzElement<?> descData = child.find("desc");
            String name = "";
            String desc = "(無描述)";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            if (descData != null) {
                desc = (String) descData.value();
            }
            writer.println(child.name() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("Pet.img 提取完成.");

        System.out.println("提取 Map.img 數據...");
        writer = new PrintWriter(new FileOutputStream(mapTxt));
        for (WzElement<?> child : map.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            writer.println(child.name());
            writer.println();
            for (WzElement<?> child2 : child.children()) {
                WzElement<?> streetData = child2.find("streetName");
                WzElement<?> mapData = child2.find("mapName");
                String streetName = "(無數據名)";
                String mapName = "(无地图名)";
                if (streetData != null) {
                    streetName = (String) streetData.value();
                }
                if (mapData != null) {
                    mapName = (String) mapData.value();
                }
                writer.println(child2.name() + " - " + streetName + " - " + mapName);
            }
            writer.println();
        }
        writer.flush();
        writer.close();
        System.out.println("Map.img 提取完成.");

        System.out.println("提取 Mob.img 數據...");
        writer = new PrintWriter(new FileOutputStream(mobTxt));
        for (WzElement<?> child : mob.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            String name = "";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            writer.println(child.value() + " - " + name);
        }
        writer.flush();
        writer.close();
        System.out.println("Mob.img 提取完成.");

        System.out.println("提取 Skill.img 數據...");
        writer = new PrintWriter(new FileOutputStream(skillTxt));
        for (WzElement<?> child : skill.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            WzElement<?> descData = child.find("desc");
            WzElement<?> bookData = child.find("bookName");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            if (descData != null) {
                desc = (String) descData.value();
            }
            if (bookData == null) {
                writer.println(child.name() + " - " + name + " - " + desc);
            }
        }
        writer.flush();
        writer.close();
        System.out.println("Skill.img 提取完成.");

        System.out.println("提取 Npc.img 數據...");
        writer = new PrintWriter(new FileOutputStream(npcTxt));
        for (WzElement<?> child : npc.map(WzFile::contentChildren).orElse(Collections.emptyList())) {
            WzElement<?> nameData = child.find("name");
            String name = "";
            if (nameData != null) {
                name = (String) nameData.value();
            }
            writer.println(child.name() + " - " + name);
        }
        writer.flush();
        writer.close();
        System.out.println("Npc.img 提取完成.");
    }
}

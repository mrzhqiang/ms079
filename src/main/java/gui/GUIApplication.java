package gui;

import client.LoginCrypto;
import client.MapleCharacter;
import client.inventory.Equip;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.di.Injectors;
import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.wz.WzData;
import constants.GameConstants;
import constants.ServerConstants;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scripting.PortalScriptManager;
import scripting.ReactorScriptManager;
import server.ApplicationStarter;
import server.CashItemFactory;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleShopFactory;
import server.Timer.EventTimer;
import server.life.MapleMonsterInformationProvider;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.net.URL;
import java.util.concurrent.ScheduledFuture;

/**
 * todo 改造为 JavaFX 应用
 */
@Singleton
public final class GUIApplication extends javax.swing.JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(GUIApplication.class);

    private final ApplicationStarter starter;

    private ScheduledFuture<?> ts = null;
    private Thread t = null;
    private boolean check = true;
    private int minutesLeft = 0;

    @Inject
    public GUIApplication(ApplicationStarter starter) {
        this.starter = starter;

        URL resource = this.getClass().getClassLoader().getResource("gui/Icon.png");
        if (resource != null) {
            ImageIcon icon = new ImageIcon(resource);
            setIconImage(icon.getImage());
        }
        setTitle("冒险岛服务端控制台");
        initComponents();
    }

    public static void main(String[] args) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            LOGGER.error("启动 GUI 应用程序出错", ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the form
         */
        GUIApplication application = Injectors.get(GUIApplication.class);
        java.awt.EventQueue.invokeLater(() -> application.setVisible(true));
    }

    private void initComponents() {
        canvas1 = new java.awt.Canvas();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatLog = new javax.swing.JTextPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jTextField22 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jTextField26 = new javax.swing.JTextField();
        checkbox1 = new java.awt.Checkbox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(chatLog);

        jButton10.setText("启动服务端");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jTextField22.setText("关闭服务器倒数时间");
        jTextField22.addActionListener(this::jTextField22ActionPerformed);

        jButton16.setText("关闭服务器");
        jButton16.addActionListener(this::jButton16ActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton10)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton16)))
                                .addContainerGap(343, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton10)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton16))
                                .addContainerGap(162, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("服务器配置", jPanel5);

        jButton7.setText("保存数据");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setText("保存雇佣");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jLabel2.setText("保存系列：");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jButton7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton8)))
                                .addContainerGap(400, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton7)
                                        .addComponent(jButton8))
                                .addContainerGap(182, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("保存数据", jPanel7);

        jButton9.setText("重载任务");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jButton1.setText("重载副本");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton5.setText("重载爆率");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton4.setText("重载商店");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton3.setText("重载传送门");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton2.setText("重载反应堆");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel1.setText("重载系列：");

        jButton6.setText("重载包头");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton12.setText("重载商城");
        jButton12.addActionListener(this::jButton12ActionPerformed);

        jButton19.setText("清除Sql連線");
        jButton19.addActionListener(this::jButton19ActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jButton6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton12))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2)
                                                .addGap(12, 12, 12)
                                                .addComponent(jButton3))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jButton9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4))
                                        .addComponent(jButton19))
                                .addContainerGap(196, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton3)
                                                .addComponent(jButton2))
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton1)
                                                .addComponent(jButton5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton9)
                                        .addComponent(jButton4))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton6)
                                        .addComponent(jButton12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton19)
                                .addContainerGap(89, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("重载系列", jPanel6);

        jButton11.setText("解卡玩家");
        jButton11.addActionListener(this::jButton11ActionPerformed);

        jTextField1.setText("输入玩家名字");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jTextField23.setText("输入账号");
        jTextField23.addActionListener(this::jTextField23ActionPerformed);

        jButton17.setText("解卡账号");
        jButton17.addActionListener(this::jButton17ActionPerformed);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton11))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton17)))
                                .addContainerGap(357, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton17))
                                .addContainerGap(174, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("卡号处理", jPanel8);

        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jButton13.setText("公告发布");
        jButton13.addActionListener(this::jButton13ActionPerformed);

        jTextField3.setText("玩家名字");

        jTextField4.setText("物品ID");

        jButton14.setText("给予物品");
        jButton14.addActionListener(this::jButton14ActionPerformed);

        jTextField5.setText("数量");

        jTextField6.setText("力量");

        jTextField7.setText("敏捷");

        jTextField8.setText("智力");

        jTextField9.setText("运气");

        jTextField10.setText("HP设置");

        jTextField11.setText("MP设置");

        jTextField12.setText("加卷次数");

        jTextField13.setText("制作人");

        jTextField14.setText("给予物品时间");

        jTextField15.setText("可以交易");

        jTextField16.setText("攻击力");

        jTextField17.setText("魔法力");

        jTextField18.setText("物理防御");

        jTextField19.setText("魔法防御");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton13))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextField13))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jTextField8)
                                                                        .addComponent(jTextField7))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField16)
                                                        .addComponent(jTextField15)
                                                        .addComponent(jTextField14)
                                                        .addComponent(jTextField17))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jTextField18)
                                                        .addComponent(jTextField19))))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton13))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton14))
                                .addContainerGap(50, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("指令/公告", jPanel1);

        jTextField20.setText("输入数量");
        jTextField20.addActionListener(this::jTextField20ActionPerformed);

        jTextField21.setText("1点卷/2抵用/3金币/4经验");

        jButton15.setText("发放全服点卷/抵用卷/金币/经验");
        jButton15.addActionListener(this::jButton15ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15)
                                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton15))
                                .addContainerGap(203, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("奖励系列", jPanel2);

        jTextField24.setText("账号");
        jTextField24.addActionListener(this::jTextField24ActionPerformed);

        jTextField25.setText("新密码");
        jTextField25.addActionListener(this::jTextField25ActionPerformed);

        jButton18.setText("修改密码");
        jButton18.addActionListener(this::jButton18ActionPerformed);

        jTextField26.setText("万能密码");
        jTextField26.addActionListener(this::jTextField26ActionPerformed);

        checkbox1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        checkbox1.setName("123"); // NOI18N
        checkbox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton18)
                                        .addComponent(checkbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(291, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(checkbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(171, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("修改", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jTabbedPane2)
                                .addGap(5, 5, 5)
                                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        for (ChannelServer instance1 : ChannelServer.getAllInstances()) {
            if (instance1 != null) {
                instance1.reloadEvents();
            }
        }
        String content = "[重载系统] 副本重载成功。";
        JOptionPane.showMessageDialog(null, "副本重载成功。");
        printChatLog(content);
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        MapleMonsterInformationProvider.getInstance().clearDrops();
        String content = "[重载系统] 爆率重载成功。";
        JOptionPane.showMessageDialog(null, "爆率重载成功。");
        printChatLog(content);
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        SendPacketOpcode.reloadValues();
        RecvPacketOpcode.reloadValues();
        String content = "[重载系统] 包头重载成功。";
        JOptionPane.showMessageDialog(null, "包头重载成功。");
        printChatLog(content);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        PortalScriptManager.getInstance().clearScripts();
        String content = "[重载系统] 传送门重载成功。";
        JOptionPane.showMessageDialog(null, "传送门重载成功。");
        printChatLog(content);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        MapleShopFactory.getInstance().clear();
        String content = "[重载系统] 商店重载成功。";
        JOptionPane.showMessageDialog(null, "商店重载成功。");
        printChatLog(content);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        ReactorScriptManager.getInstance().clearDrops();
        String content = "[重载系统] 反应堆重载成功。";
        JOptionPane.showMessageDialog(null, "反应堆重载成功。");
        printChatLog(content);
    }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {
        MapleQuest.clearQuests();
        String content = "[重载系统] 任务重载成功。";
        JOptionPane.showMessageDialog(null, "任务重载成功。");
        printChatLog(content);
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        int p = 0;
        for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
            p++;
            cserv.closeAllMerchant();
        }
        String content = "[保存雇佣商人系统] 雇佣商人保存" + p + "个频道成功。";
        JOptionPane.showMessageDialog(null, "雇佣商人保存" + p + "个频道成功。");
        printChatLog(content);
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        int p = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                p++;
                chr.saveToDB(true, true);
            }
        }
        String content = "[保存数据系统] 保存" + p + "个成功。";
        JOptionPane.showMessageDialog(null, content);
        printChatLog(content);
    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {
        if (check) {
            check = false;
            starter.startServer();
            String content = "[服务器] 服务器启动成功！";
            printChatLog(content);
        } else {
            JOptionPane.showMessageDialog(null, "[服务器] 无法重复运行。");
        }
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {
        sendNotice(0);
    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {
        CashItemFactory.getInstance().clearCashShop();
        String content = "[重载系统] 商城重载成功。";
        JOptionPane.showMessageDialog(null, "商城重载成功。");
        printChatLog(content);
    }

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        sendNoticeGG();
    }

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {
        makeItem();
    }

    private void jTextField20ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {
        giveReward();
    }

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {
        shutdownServer();
    }

    private void jTextField22ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {
        FixAcLogged();
    }

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField25ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {
        ChangePassWord();
    }

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField26ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void checkbox1MouseClicked(java.awt.event.MouseEvent evt) {
        boolean status = checkbox1.getState();
        ServerConstants.Super_password = status;
        if (!status) {
            ServerConstants.superpw = "";
        } else {
            ServerConstants.superpw = jTextField26.getText();
        }
    }

    private void ChangePassWord() {
        String account = jTextField24.getText();
        String password = jTextField25.getText();

        if (password.length() > 12) {
            JOptionPane.showMessageDialog(null, "密码过长");
            return;
        }

        DAccount one = new QDAccount().name.eq(account).findOne();
        if (one == null) {
            JOptionPane.showMessageDialog(null, "账号不存在");
            return;
        }

        one.password = LoginCrypto.hexSha1(password);
        one.save();
        printChatLog("更改账号: " + account + "的密码为 " + password);
    }

    private void shutdownServer() {
        try {
            String content = "关闭服务器倒数时间";
            minutesLeft = Integer.parseInt(jTextField22.getText());
            if (ts == null && (t == null || !t.isAlive())) {
                t = new Thread(starter.getShutdown());
                ts = EventTimer.getInstance().register(() -> {
                    if (minutesLeft <= 0) {
                        t.start();
                        ts.cancel(false);
                        return;
                    }
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0,
                            "服务器將在 " + minutesLeft + "分钟后关闭. 请尽快关闭雇佣商人安全下线.").getBytes());
                    LOGGER.info("服务器將在 " + minutesLeft + "分钟后关闭.");
                    minutesLeft--;
                }, 60000);
            }
            jTextField22.setToolTipText("关闭服务器倒数时间");
            printChatLog(content);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + e);
        }
    }

    private void giveReward() {
        try {
            int count;
            if ("输入数量".equals(jTextField20.getText())) {
                count = 0;
            } else {
                count = Integer.parseInt(jTextField20.getText());
            }

            int type;
            if ("1点卷/2抵用/3金币/4经验".equals(jTextField21.getText())) {
                type = 0;
            } else {
                type = Integer.parseInt(jTextField21.getText());
            }
            if (count <= 0 || type <= 0) {
                printChatLog("参数错误，请重新输入");
                return;
            }

            String content;
            int ret = 0;
            if (type == 1 || type == 2) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.modifyCSPoints(type, count);
                        String cash;
                        if (type == 1) {
                            cash = "点卷";
                        } else {
                            cash = "抵用卷";
                        }
                        mch.startMapEffect("管理员发放" + count + cash + "给在线的所有玩家！快感谢管理员吧！", 5121009);
                        ret++;
                    }
                }
            } else if (type == 3) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        // mch.modifyCSPoints(type, count);
                        mch.gainMeso(count, true);
                        mch.startMapEffect("管理员发放" + count + "冒险币给在线的所有玩家！快感谢管理员吧！", 5121009);
                        ret++;
                    }
                }
            } else if (type == 4) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.gainExp(count, true, false, true);
                        mch.startMapEffect("管理员发放" + count + "经验给在线的所有玩家！快感谢管理员吧！", 5121009);
                        ret++;
                    }
                }
            }
            String typeMessage = "";
            if (type == 1) {
                typeMessage = "点卷";
            } else if (type == 2) {
                typeMessage = "抵用卷";
            } else if (type == 3) {
                typeMessage = "金币";
            } else if (type == 4) {
                typeMessage = "经验";
            }
            content = "一共发放[" + count * ret + "]." + typeMessage + "!一共发放给了" + ret + "人！";
            jTextField20.setText("输入数量");
            jTextField21.setText("1点卷/2抵用/3金币/4经验");
            printChatLog(content);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + e);
        }
    }

    private void makeItem() {
        try {
            String name;
            if ("玩家名字".equals(jTextField3.getText())) {
                name = "";
            } else {
                name = jTextField3.getText();
            }

            int itemId;
            if ("物品ID".equals(jTextField4.getText())) {
                itemId = 0;
            } else {
                itemId = Integer.parseInt(jTextField4.getText());
            }

            int count;
            if ("数量".equals(jTextField5.getText())) {
                count = 0;
            } else {
                count = Integer.parseInt(jTextField5.getText());
            }

            int str;
            if ("力量".equals(jTextField6.getText())) {
                str = 0;
            } else {
                str = Integer.parseInt(jTextField6.getText());
            }

            int dex;
            if ("敏捷".equals(jTextField7.getText())) {
                dex = 0;
            } else {
                dex = Integer.parseInt(jTextField7.getText());
            }

            int aInt;
            if ("智力".equals(jTextField8.getText())) {
                aInt = 0;
            } else {
                aInt = Integer.parseInt(jTextField8.getText());
            }

            int luk;
            if ("运气".equals(jTextField9.getText())) {
                luk = 0;
            } else {
                luk = Integer.parseInt(jTextField9.getText());
            }

            int HP;
            if ("HP设置".equals(jTextField10.getText())) {
                HP = 0;
            } else {
                HP = Integer.parseInt(jTextField10.getText());
            }

            int MP;
            if ("MP设置".equals(jTextField11.getText())) {
                MP = 0;
            } else {
                MP = Integer.parseInt(jTextField11.getText());
            }
            int appendCount;
            if ("加卷次数".equals(jTextField12.getText())) {
                appendCount = 0;
            } else {
                appendCount = Integer.parseInt(jTextField12.getText());
            }

            String makeName;
            if ("制作人".equals(jTextField13.getText())) {
                makeName = "";
            } else {
                makeName = jTextField13.getText();
            }

            int makeTime;
            if ("给予物品时间".equals(jTextField14.getText())) {
                makeTime = 0;
            } else {
                makeTime = Integer.parseInt(jTextField14.getText());
            }

            String isTransaction = jTextField15.getText();

            int atk;
            if ("攻击力".equals(jTextField16.getText())) {
                atk = 0;
            } else {
                atk = Integer.parseInt(jTextField16.getText());
            }

            int mc;
            if ("魔法力".equals(jTextField17.getText())) {
                mc = 0;
            } else {
                mc = Integer.parseInt(jTextField17.getText());
            }

            int def;
            if ("物理防御".equals(jTextField18.getText())) {
                def = 0;
            } else {
                def = Integer.parseInt(jTextField18.getText());
            }

            int mdef;
            if ("魔法防御".equals(jTextField19.getText())) {
                mdef = 0;
            } else {
                mdef = Integer.parseInt(jTextField19.getText());
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(itemId);
            String contentA = "";
            String content = "玩家名字：" + name
                    + " itemId：" + itemId
                    + " count：" + count
                    + " 力量:" + str
                    + " 敏捷:" + dex
                    + " 智力:" + aInt
                    + " 运气:" + luk
                    + " HP:" + HP
                    + " MP:" + MP
                    + " 可加卷次数:" + appendCount
                    + " 制作人名字:" + makeName
                    + " 给予时间:" + makeTime
                    + " 是否可以交易:" + isTransaction
                    + " 攻击力:" + atk
                    + " 魔法力:" + mc
                    + " 物理防御:" + def
                    + " 魔法防御:" + mdef + "\r\n";
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (mch.getName().equals(name)) {
                        if (count >= 0) {
                            if (!MapleInventoryManipulator.checkSpace(mch.getClient(), itemId, count, "")) {
                                return;
                            }
                            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(itemId) && !GameConstants.isBullet(itemId)
                                    || type.equals(MapleInventoryType.CASH) && itemId >= 5000000 && itemId <= 5001000) {
                                final Equip item = (Equip) (ii.getEquipById(itemId));
                                if (ii.isCash(itemId)) {
                                    item.setUniqueId(1);
                                }
                                if (str > 0 && str <= 32767) {
                                    item.setStr((short) (str));
                                }
                                if (dex > 0 && dex <= 32767) {
                                    item.setDex((short) (dex));
                                }
                                if (aInt > 0 && aInt <= 32767) {
                                    item.setInt((short) (aInt));
                                }
                                if (luk > 0 && luk <= 32767) {
                                    item.setLuk((short) (luk));
                                }
                                if (atk > 0 && atk <= 32767) {
                                    item.setWatk((short) (atk));
                                }
                                if (mc > 0 && mc <= 32767) {
                                    item.setMatk((short) (mc));
                                }
                                if (def > 0 && def <= 32767) {
                                    item.setWdef((short) (def));
                                }
                                if (mdef > 0 && mdef <= 32767) {
                                    item.setMdef((short) (mdef));
                                }
                                if (HP > 0 && HP <= 30000) {
                                    item.setHp((short) (HP));
                                }
                                if (MP > 0 && MP <= 30000) {
                                    item.setMp((short) (MP));
                                }
                                if ("可以交易".equals(isTransaction)) {
                                    int flag = item.getFlag();
                                    if (item.getType() == MapleInventoryType.EQUIP.getType()) {
                                        flag |= ItemFlag.KARMA_EQ.getValue();
                                    } else {
                                        flag |= ItemFlag.KARMA_USE.getValue();
                                    }
                                    item.setFlag(flag);
                                }
                                if (makeTime > 0) {
                                    item.setExpiration(System.currentTimeMillis() + ((long) makeTime * 24 * 60 * 60 * 1000));
                                }
                                if (appendCount > 0) {
                                    item.setUpgradeSlots((byte) (appendCount));
                                }
                                if (makeName != null) {
                                    item.setOwner(makeName);
                                }
                                String itemName = ii.getName(itemId);
                                if (itemId / 10000 == 114 && itemName != null && itemName.length() > 0) { //medal
                                    final String msg = "你已获得称号 <" + itemName + ">";
                                    mch.getClient().getPlayer().dropMessage(5, msg);
                                    mch.getClient().getPlayer().dropMessage(5, msg);
                                }
                                MapleInventoryManipulator.addbyItem(mch.getClient(), item.copy());
                            } else {
                                MapleInventoryManipulator.addById(mch.getClient(), itemId, (short) count, "", null, makeTime, (byte) 0);
                            }
                        } else {
                            MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(itemId), itemId, -count, true, false);
                        }
                        mch.getClient().getSession().write(MaplePacketCreator.getShowItemGain(itemId, (short) count, true));
                        contentA = "[刷物品]:" + content;
                    }
                }
            }
            jTextField3.setText("玩家名字");
            jTextField4.setText("itemId");
            jTextField5.setText("count");
            jTextField6.setText("力量");
            jTextField7.setText("敏捷");
            jTextField8.setText("智力");
            jTextField9.setText("运气");
            jTextField10.setText("HP设置");
            jTextField11.setText("MP设置");
            jTextField12.setText("加卷次数");
            jTextField13.setText("制作人");
            jTextField14.setText("给予物品时间");
            jTextField15.setText("可以交易");
            jTextField16.setText("攻击力");
            jTextField17.setText("魔法力");
            jTextField18.setText("物理防御");
            jTextField19.setText("魔法防御");
            printChatLog(contentA);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + e);
        }
    }

    private void printChatLog(String str) {
        chatLog.setText(chatLog.getText() + str + "\r\n");
    }

    private void sendNoticeGG() {
        try {
            String str = jTextField2.getText();
            String content = "";
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect(str, 5121009);
                    content = "[公告]:" + str;
                }
            }
            jTextField2.setText("");
            printChatLog(content);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + e);
        }
    }

    private void FixAcLogged() {
        String account = jTextField23.getText().trim();
        DAccount one = new QDAccount().name.eq(account).findOne();
        if (one != null) {
            one.loggedIn = 0;
            one.save();
        }
        printChatLog("解除卡账号" + account);
        jTextField23.setText("");
    }

    private void sendNotice(int type) {
        try {
            String str = jTextField1.getText();
            String content = "";
            if (type == 0) {
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                        try {
                            ChannelServer.forceRemovePlayerByCharName(str);
                            if (chr.getName().equals(str) && chr.getMapId() != 0) {
                                chr.getClient().getSession().close();
                                chr.getClient().disconnect(true, false);
                                content = "[解卡系统] 成功断开" + str + "玩家！";
                            } else {
                                content = "[解卡系统] 玩家名字输入错误或者该玩家没有在线！";
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            jTextField1.setText("");
            printChatLog(content);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + e);
        }
    }

    private java.awt.Canvas canvas1;
    private javax.swing.JTextPane chatLog;
    private java.awt.Checkbox checkbox1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
}

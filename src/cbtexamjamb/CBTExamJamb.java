/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbtexamjamb;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import org.imgscalr.Scalr;

//import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 *
 * @author OLAWALE
 */
public class CBTExamJamb extends javax.swing.JFrame {

    /**
     * Creates new form CBTExamJamb
     */
    
    Connection con;
    PreparedStatement pt;
    ResultSet rs;
    Map<String, String> subjectMap = new HashMap<>();
    List<String> subjectNameList = new ArrayList<>();
    List<String> subjectIdList = new ArrayList<>();
    Map<String, Integer> totalQuestotalMap = new HashMap<>();

    private static boolean ind = false;
    public CBTExamJamb() {
         try {
             initComponents();
             jPanel1.setVisible(false);
             jPanel4.setVisible(false);
             errorLabel.setVisible(false);
             //        loadSubject();
             
             minComboBox.setSelectedIndex(30);
             
             timerButtonGroup.add(entireRadioButton);
             timerButtonGroup.add(fromToRadioButton);
             entireRadioButton.setSelected(true);
             fromSpinner.setEnabled(false);
             toSpinner.setEnabled(false);
             File file = new File("res/icons/bg_img.jpg");
             try {
                 BufferedImage bufferedImg = ImageIO.read(file); // load image
                 BufferedImage scaledImg = Scalr.resize(bufferedImg, 200, 200);
                 //                BufferedImage scaledImg = Scalr.resize(img, Method.QUALITY, 1280, 960);
                 Icon c  = new ImageIcon(scaledImg);
                 
                 jLabel14.setIcon(c);
//                 jLabel27.setIcon(c);
                 
             } catch (IOException ex) {
//                        Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
//empty
             }
             UIDefaults defs = UIManager.getDefaults();
             defs.put(jTextPane1.getBackground(), new ColorUIResource(Color.BLACK));
             defs.put(jTextPane1.getBackground(), new ColorUIResource(Color.BLACK));
             jTextPane1.setBackground(Color.red);
             
             
             con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
             String query = "SELECT * FROM config_tb";
             pt = con.prepareStatement(query);
             rs = pt.executeQuery();
             //NEW
             
             if(rs.next())
             {
//                 String keycode = getLicence1(rs);
                 
                 String keycode = getSerialNumber();
                 String salt = rs.getString("salt");
                 
                 String machine = Code.getSecurePassword(keycode, salt);
                 
                 if(machine.equals(rs.getString("machine")))
                 {
                 
                    if(rs.getObject("license")!=null)
                    {
                    if(rs.getString("license").equals(Code.gen(keycode)))
                    {
//                        licence
                        //Change Activate Button to proceed
                        ind=true;
                        activationState();
                    }
                    else
                    {
                        //get salt and insert into configtb
                        ind=false;
                        keyCodeTextField.setText(getLicence1(rs));
                    }
                    }
                    else
                    {
                        ind=false;
                        keyCodeTextField.setText(getLicence1(rs));
                    }
                    
                 }
                 else
                 {
                    ind=false;
                    keyCodeTextField.setText(getLicence1(rs));
                 }
             }
             else
             {
                ind=false;
                keyCodeTextField.setText(getLicence1(null));
             }
             
             
             //END
             
             
//             if(rs.next())
//             {
//                 if(!rs.getString("keycode").isEmpty() && rs.getString("salt")!=null)
//                 {
//                    String machine = Code.getSecurePassword(rs.getString("keycode"), rs.getString("salt"));
//                    
//                    if(machine.equals(rs.getString("machine")))
//                    {
////                        if(rs.getString("license").equals(Code.gen(machine)))
//                        if(rs.getString("license").equals(Code.gen(rs.getString("keycode"))))
//                        {
//                            //Change Activate Button to proceed
//                            ind=true;
//                            activationState();
//                            
//                        }
//                        else
//                        {
//                            ind=false;
//                            keyCodeTextField.setText(getLicence(rs));
//                        }
//                    }
//                    else
//                    {
//                        ind=false;
//                        keyCodeTextField.setText(getLicence(rs));
//                    }
//                 }
//                 else
//                 {
//                     ind=false;
//                     keyCodeTextField.setText(getLicence(rs));
//                 }
//             }
//             else
//             {
//                ind=false;
//                keyCodeTextField.setText(getLicence(null));
//                 
//             }
                        
                    } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
//                        Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                        //empty
                    } 
         catch (NoSuchProviderException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         try {
            setIconImage(ImageIO.read(new File("res/icons/icon_title.png")));
        } catch (IOException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        durationComboBox = new javax.swing.JComboBox();
        timerButtonGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel27 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        keyCodeTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        activationCodeTextField = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        minComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        hrComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        secComboBox = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        subjectComboBox = new javax.swing.JComboBox();
        yearComboBox = new javax.swing.JComboBox();
        entireSpinner = new javax.swing.JSpinner();
        fromSpinner = new javax.swing.JSpinner();
        entireRadioButton = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fromToRadioButton = new javax.swing.JRadioButton();
        toSpinner = new javax.swing.JSpinner();
        jButton9 = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jSeparator7 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        durationComboBox.setBackground(new java.awt.Color(110, 217, 163));
        durationComboBox.setForeground(new java.awt.Color(255, 255, 255));
        durationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select One", "Biology", "Physics", "Chemistry", "Geography", "Econmics", "Literature", "Agricultural Sci" }));

        jScrollPane1.setBackground(new java.awt.Color(0, 51, 51));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTextPane1.setBackground(new java.awt.Color(0, 51, 51));
        jTextPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTextPane1.setText("Exam Pro is a CBT application software to text the student on the efficiency of their preparation prio the examination");
        jScrollPane1.setViewportView(jTextPane1);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Exam Pro");
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));
        jPanel2.setLayout(null);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1);
        jSeparator1.setBounds(593, 0, 10, 503);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(110, 217, 163));
        jLabel1.setText(" KeyCode");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(613, 111, 106, 20);

        keyCodeTextField.setEditable(false);
        keyCodeTextField.setBackground(new java.awt.Color(0, 51, 51));
        keyCodeTextField.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        keyCodeTextField.setForeground(new java.awt.Color(255, 255, 255));
        keyCodeTextField.setBorder(null);
        jPanel2.add(keyCodeTextField);
        keyCodeTextField.setBounds(613, 137, 238, 31);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(110, 217, 163));
        jLabel2.setText("Activation Code");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(613, 193, 106, 14);
        jPanel2.add(jSeparator2);
        jSeparator2.setBounds(613, 173, 238, 2);
        jPanel2.add(jSeparator3);
        jSeparator3.setBounds(613, 256, 238, 2);

        jButton1.setBackground(new java.awt.Color(110, 217, 163));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(41, 53, 65));
        jButton1.setText("Activate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(613, 276, 158, 34);

        jButton3.setBackground(new java.awt.Color(110, 217, 163));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(41, 53, 65));
        jButton3.setText("Demo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);
        jButton3.setBounds(781, 276, 67, 34);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel14);
        jLabel14.setBounds(50, 130, 158, 121);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Exam Pro CBT EXAMINATION APPLICATION...");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(50, 50, 507, 20);

        jPanel8.setBackground(new java.awt.Color(0, 51, 51));

        jLabel18.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("designed to test the efficacy of students' preparation");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Exam Pro");

        jLabel17.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Exam Pro is a CBT (Computer Base Test) application");

        jLabel19.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("for JAMB...");

        jLabel20.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("About");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator11)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel2.add(jPanel8);
        jPanel8.setBounds(220, 120, 362, 140);

        activationCodeTextField.setBackground(new java.awt.Color(0, 51, 51));
        activationCodeTextField.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        activationCodeTextField.setForeground(new java.awt.Color(255, 255, 255));
        activationCodeTextField.setBorder(null);
        jPanel2.add(activationCodeTextField);
        activationCodeTextField.setBounds(613, 219, 238, 31);

        jPanel9.setBackground(new java.awt.Color(0, 51, 51));

        jLabel21.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("+234 8066473585");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Name");

        jLabel23.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Azeez Saliu");

        jLabel24.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("olawale444real@yahoo.com");

        jLabel25.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Developer");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Contact ");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("E-mail");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator10)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24))))
                        .addGap(0, 85, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel9);
        jPanel9.setBounds(220, 290, 362, 120);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jPanel3.setBackground(new java.awt.Color(110, 217, 163));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 503, Short.MAX_VALUE)
        );

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        jButton2.setBackground(new java.awt.Color(110, 217, 163));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(41, 53, 65));
        jButton2.setText("Start");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Step 1 of 3");

        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(0, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Timer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(110, 217, 163))); // NOI18N

        minComboBox.setBackground(new java.awt.Color(110, 217, 163));
        minComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sec");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Min");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Duration");

        hrComboBox.setBackground(new java.awt.Color(110, 217, 163));
        hrComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Hr");

        secComboBox.setBackground(new java.awt.Color(110, 217, 163));
        secComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hrComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(minComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)))
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hrComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Exam Mode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(110, 217, 163))); // NOI18N

        subjectComboBox.setBackground(new java.awt.Color(110, 217, 163));
        subjectComboBox.setForeground(new java.awt.Color(255, 255, 255));
        subjectComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                subjectComboBoxItemStateChanged(evt);
            }
        });

        yearComboBox.setBackground(new java.awt.Color(110, 217, 163));
        yearComboBox.setForeground(new java.awt.Color(255, 255, 255));
        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2017", "2016", "2015" }));
        yearComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                yearComboBoxItemStateChanged(evt);
            }
        });

        entireRadioButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        entireRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        entireRadioButton.setText("Take");
        entireRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                entireFromToRadioButtonItemStateChanged(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("from the entire exam");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("to");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Select Your Subject");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\OLAWALE\\Documents\\NetBeansProjects\\CBTExamJamb\\res\\icons\\Calendar_25px.png")); // NOI18N
        jLabel5.setText("Year");

        fromToRadioButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        fromToRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        fromToRadioButton.setText("Take question range from");
        fromToRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                entireFromToRadioButtonItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(entireRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(entireSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(fromToRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(entireSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(entireRadioButton)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromToRadioButton)
                    .addComponent(fromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton9.setBackground(new java.awt.Color(110, 217, 163));
        jButton9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton9.setForeground(new java.awt.Color(41, 53, 65));
        jButton9.setText("Log out");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        versionLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        versionLabel.setForeground(new java.awt.Color(255, 255, 255));
        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        versionLabel.setText("Version : Demo");

        errorLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        errorLabel.setForeground(java.awt.Color.red);
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        errorLabel.setText("to");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(205, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(111, 111, 111))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 748, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(35, 35, 35)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorLabel))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(41, 53, 65));

        jPanel5.setBackground(new java.awt.Color(110, 217, 163));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );

        jSeparator7.setForeground(new java.awt.Color(255, 255, 255));

        jButton4.setBackground(new java.awt.Color(110, 217, 163));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Submit");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Step 1 of 3");

        jSeparator9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Time: 2:30:00");

        jButton5.setBackground(new java.awt.Color(110, 217, 163));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Previouse");

        jButton6.setBackground(new java.awt.Color(110, 217, 163));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Next");

        jButton7.setBackground(new java.awt.Color(110, 217, 163));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Calculator");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 285, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jButton7)
                        .addGap(167, 167, 167)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING))))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 595, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    private void activationState()
    {
        jButton1.setText("Proceed");
        jLabel1.setVisible(false);
        keyCodeTextField.setVisible(false);
        jSeparator2.setVisible(false);

        activationCodeTextField.setText("Activated............");
        activationCodeTextField.setEditable(false);

        jButton3.setVisible(false);
    }
    
    private String getLicence1(ResultSet rst)
    {
        try {
            String mac = getSerialNumber();
            
            System.out.println("\n\n" + mac);

            byte salt[] = Code.getSalt();

//                    String saltStr = salt.toString();

            String saltStr = new String(salt);
            //            String saltStr = salt.toString();

            System.out.println("OK: " + salt);
            System.out.println("OK: " + saltStr);

            //            String macNoHash = Code.getSecurePassword(mac, salt);
            //            String macNoHash = Code.getSecurePassword(mac, salt);

            String macNoHash = Code.getSecurePassword(mac, saltStr);
            //            macNoHash = macNoHash.concat("gNiDoCnE" + saltStr);
            System.out.print(macNoHash);

            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
            String query="";
            if(rst!=null)
            {
//                query = "UPDATE config_tb set keycode=?, machine=?, salt=? WHERE id=?";
//                
//                pt = con.prepareStatement(query);
//                pt.setString(1, mac);
//                pt.setString(2, macNoHash);
//                pt.setString(3, saltStr);
//                pt.setInt(4, rst.getInt("id"));
                
                query = "UPDATE config_tb set machine=?, salt=? WHERE id=?";
                
                pt = con.prepareStatement(query);
//                pt.setString(1, mac);
                pt.setString(1, macNoHash);
                pt.setString(2, saltStr);
                pt.setInt(3, rst.getInt("id"));
            }
            else
            {
//                query = "INSERT INTO config_tb (keycode, machine, salt) VALUES(?, ?, ?)";
                query = "INSERT INTO config_tb (machine, salt) VALUES(?, ?)";
                
                pt = con.prepareStatement(query);
//                pt.setString(1, mac);
                pt.setString(1, macNoHash);
//                pt.setString(3, saltStr);
                pt.setString(2, saltStr);
            }
            
            pt.executeUpdate();

            query = "UPDATE config_tb SET license=?";
            pt = con.prepareStatement(query);
            pt.setString(1, "");
            pt.executeUpdate();
            
            return mac;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;
    }
    
    private String getLicence(ResultSet rst)
    {
        try {
            String mac = getSerialNumber();
            
            System.out.println("\n\n" + mac);

            byte salt[] = Code.getSalt();

//                    String saltStr = salt.toString();

            String saltStr = new String(salt);
            //            String saltStr = salt.toString();

            System.out.println("OK: " + salt);
            System.out.println("OK: " + saltStr);

            //            String macNoHash = Code.getSecurePassword(mac, salt);
            //            String macNoHash = Code.getSecurePassword(mac, salt);

            String macNoHash = Code.getSecurePassword(mac, saltStr);
            //            macNoHash = macNoHash.concat("gNiDoCnE" + saltStr);
            System.out.print(macNoHash);

            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
            String query="";
            if(rst!=null)
            {
                query = "UPDATE config_tb set keycode=?, machine=?, salt=? WHERE id=?";
                
                pt = con.prepareStatement(query);
                pt.setString(1, mac);
                pt.setString(2, macNoHash);
                pt.setString(3, saltStr);
                pt.setInt(4, rst.getInt("id"));
            }
            else
            {
                query = "INSERT INTO config_tb (keycode, machine, salt) VALUES(?, ?, ?)";
                
                pt = con.prepareStatement(query);
                pt.setString(1, mac);
                pt.setString(2, macNoHash);
                pt.setString(3, saltStr);
            }
            

            pt.executeUpdate();

            return mac;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;
    }
    
//    private String getLicenceOnly(ConfigTb configTb, ModelMap model)
//    {
//        try 
//        {
//            String mac = getMac();
//            
//            System.out.println("\n\n" + mac);
//
//            byte salt[] = Code.getSalt();
//
////                    String saltStr = salt.toString();
//
//            String saltStr = new String(salt);
//
//            System.out.println("OK: " + salt);
//            System.out.println("OK: " + saltStr);
//
//            String macNoHash = Code.getSecurePassword(mac, saltStr);
//            System.out.print(macNoHash);
//            model.addAttribute("reply", "get_licence");
////            model.addAttribute("activationCode", macNoHash + "gNiDoCnE" + saltStr);
//            model.addAttribute("activationCode", macNoHash);
//
////            ConfigTb config = new ConfigTb("Ideal Programing", "Ideal Programming", "Simple Code Makes Life Better And Enjoyable", "licenceKy", new Date(), new Date(), new Date(), salt.toString(), macNoHash);
//            
////            configTb.setSalt(salt);
//            configTb.setMacNo(macNoHash);
//            configTb.setSalt(saltStr);
////            ConfigTb config = new ConfigTb("Ideal Programing", "Ideal Programming", "Simple Code Makes Life Better And Enjoyable", "licenceKy", new Date(), new Date(), new Date(), salt.toString(), macNoHash);
//
//            configService.editConfig(configTb);
//
//
//            return "index";
//        } 
//        catch (NoSuchProviderException ex) {
//                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//                } 
//        catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (UnknownHostException ex) { 
//            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SocketException ex) {
//            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//                return null;
//            
//    }
    
    private String getSerialNumber()
    {
        String result = "";
        String s = System.getProperty("os.name");
        System.out.println(s);
        
        try {
            File f = File.createTempFile("realhowto", ".vbs");
            
            f.deleteOnExit();
            FileWriter fw = new FileWriter(f);
            String vbs =
            "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
              + "Set colItems = objWMIService.ExecQuery _ \n"
              + "   (\"Select * from Win32_BaseBoard\") \n"
              + "For Each objItem in colItems \n"
              + "    Wscript.Echo objItem.SerialNumber \n"
              + "    exit for  ' do the first cpu only! \n"
              + "Next \n";

            fw.write(vbs);
            fw.close();
            
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + f.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
               result += line;
            }
            input.close();

        } catch (IOException ex) {
//            Logger.getLogger(PasswordEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result.trim();
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        
        try {
        if(ind==true)
        {
            user = true;
            versionLabel.setText("Versiom : Licensed ");
            loadSubject();
            jPanel2.setVisible(false);
            jPanel1.setVisible(true);
        }
        else
        {
            
            //NEW
            
            
            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
            String query = "SELECT * FROM config_tb";
            pt = con.prepareStatement(query);
            ResultSet rs = pt.executeQuery();
            
            if(rs.next())
            {
                String keycode = getSerialNumber();
                String activationCode = this.activationCodeTextField.getText();
                String salt = rs.getString("salt");
                
                if(Code.gen(keycode).equals(activationCode))
                {
                    int id = rs.getInt("id");
                    query = "UPDATE config_tb SET license=? WHERE id=?";
                    pt = con.prepareStatement(query);
                    pt.setString(1, activationCode);
                    pt.setInt(2, id);
                    pt.executeUpdate();
                    
                    activationState();
                    ind=true;
                    JOptionPane.showMessageDialog(rootPane, "Exam-Pro activated\nProceed......");
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Invalide License\nTo obtain your License key, Contact the Programmer\nPhone number: 08066473585\nEmail: olawale444real@yahoo.com ");
                }
            }
            
            //END
            
//            String keyCode = keyCodeTextField.getText();
//            String activationCode = this.activationCodeTextField.getText();
//            
//            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
////            String query = "SELECT * FROM config_tb WHERE keycode=? AND license=?";
//            String query = "SELECT * FROM config_tb";
//                
////                conn = DriverManager.getConnection(
////                    "jdbc:ucanaccess://C:/PayRoll.accdb");
//            
//            pt = con.prepareStatement(query);
////            pt.setString(1, keyCode);
////            pt.setString(2, activationCode);
//            ResultSet rs = pt.executeQuery();
//            
//            if(rs.next())
//            {
//                String key = rs.getString("keycode");
//                String salt = rs.getString("salt");
////                if(Code.gen(Code.getSecurePassword(key, salt)).equals(activationCode))
//                if(Code.gen(key).equals(activationCode))
//                {
//                    int id = rs.getInt("id");
//                    query = "UPDATE config_tb SET license=? WHERE id=?";
//                    pt = con.prepareStatement(query);
//                    pt.setString(1, activationCode);
//                    pt.setInt(2, id);
//                    pt.executeUpdate();
//                    
//                    activationState();
//                    ind=true;
//                    JOptionPane.showMessageDialog(rootPane, "Exam-Pro activated\nProceed......");
//                }
//                else
//                {
//                    JOptionPane.showMessageDialog(rootPane, "Invalide License\nTo obtain your License key, Contact the Programmer\nPhone number: 08066473585\nEmail: olawale444real@yahoo.com ");
//                }
//            }
            
        }
        
            // TODO add your handling code here:

        } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void proceed()
    {
        //            String keyCode = keyCodeTextField.getText();
//            String activationCode = this.activationCodePasswordField.getText();
//            
////            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://exampro2_db.mdb");
////            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb", "", "schoolDAY123");
//            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
//            
////            String query = "SELECT * FROM Login WHERE keycode='" + keyCode + "'AND license='"+activationCodePasswordField+"'";
////              String query = "SELECT * FROM config_tb WHERE keycode='" + keyCode + "' AND license='"+activationCodePasswordField+"'";
//              String query = "SELECT * FROM config_tb WHERE keycode=? AND license=?";
//                
////                conn = DriverManager.getConnection(
////                    "jdbc:ucanaccess://C:/PayRoll.accdb");
//            
//            PreparedStatement pt = con.prepareStatement(query);
//            pt.setString(1, keyCode);
//            pt.setString(2, activationCode);
//            ResultSet rs = pt.executeQuery();
//            
//            if(rs.next())
//            {
//                jPanel2.setVisible(false);
//                jPanel1.setVisible(true);
//                
//                loadSubject();
//            }
//            else
//            {
//                JOptionPane.showMessageDialog(rootPane, "License Error");
//            }
    }
    
    private void loadSubject()
    {
        
        try {
//            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb");
            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
            
            pt = con.prepareStatement("SELECT * FROM subject_tb");
            rs = pt.executeQuery();
            
            subjectMap = new HashMap<>();
            totalQuestotalMap = new HashMap<>();
            
            subjectNameList = new ArrayList<>();
            subjectIdList = new ArrayList<>();
            
            subjectMap.put("none", "Select One");
            subjectNameList.add("Select One");
            subjectIdList.add("none");
            
            totalQuestotalMap.put("none", 0);
            
            while(rs.next())
            {
                subjectMap.put(rs.getString("subject_id"), rs.getString("subject_name"));
                subjectNameList.add(rs.getString("subject_name"));
                subjectIdList.add(rs.getString("subject_id"));
                
                totalQuestotalMap.put(rs.getString("subject_id"), rs.getInt("question_total"));
            }
//            subjectMap.put("none", "Select One");
//            subjectMap.put("bio_tb", "Biology");
//            subjectMap.put("phy_tb", "Physics");
//            subjectMap.put("che_tb", "Chemistry");
//        subjectMap.put("bio_tb", "Biology");
//        subjectMap.put("bio_tb", "Biology");



            for(int count=0; count<subjectMap.size(); ++count)
            {
                subjectComboBox.addItem(subjectNameList.get(count));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        setTitle("Exam Pro - Demo Version");
        versionLabel.setText("Versiom : Demo ");
        prepareDemo();
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private boolean user=false;
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
//        jPanel1.setVisible(false);
//        jPanel2.setVisible(false);
//        jPanel4.setVisible(true);

        int startNumber = 0;
        int endNumber = 0;

        int hr = Integer.parseInt(hrComboBox.getSelectedItem().toString());
        int min = Integer.parseInt(minComboBox.getSelectedItem().toString());
        int sec = Integer.parseInt(secComboBox.getSelectedItem().toString());

        this.setVisible(false);
        
        int selectSubjectNum = subjectComboBox.getSelectedIndex();
        String subjectId = subjectIdList.get(selectSubjectNum);
        String subjectName = subjectNameList.get(selectSubjectNum);
//        int questionTotal = totalQuestotalMap.get(subjectId);
        
        String year = yearComboBox.getSelectedItem().toString();
        subjectId = subjectId.concat("_"+year);
        
        if(entireRadioButton.isSelected())
        {
            startNumber=1;
            endNumber=(int) entireSpinner.getValue();
        }
        else
        {
            startNumber=(int) fromSpinner.getValue();
            endNumber=(int) toSpinner.getValue();
        }
        
        
        
//        ExamProgress examProgress = new ExamProgress(subjectId, subjectName, questionTotal, hr, min, sec);
        ExamProgress examProgress = new ExamProgress(subjectId, subjectName, year, startNumber, endNumber, user, hr, min, sec, this);
        examProgress.setVisible(true);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    
    private void updateQuestionNumberDemo()
    {
        try {
            int subjectIndex = subjectComboBox.getSelectedIndex();
            if(subjectIndex!=0)
            {
                String subjectId = subjectIdList.get(subjectIndex);
                subjectId = subjectId.concat("_"+yearComboBox.getSelectedItem().toString());

                
                con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");

                pt = con.prepareStatement("SELECT * FROM subject_deno_tb WHERE subject_deno_id=?");
                pt.setString(1, subjectId);
                rs = pt.executeQuery();

                if(rs.next())
                {
                    int totalQuestion = rs.getInt("question_total");
                    entireSpinner.setValue(10);
                    toSpinner.setValue(10);
                    fromSpinner.setValue(1);
                    
                    errorLabel.setVisible(false);
                    jButton2.setEnabled(true);
                    jButton2.setBackground(new Color(110,217,163));
                }
                else
                {
                    errorLabel.setText(subjectComboBox.getSelectedItem() + " subject of " +yearComboBox.getSelectedItem()+" is not available");
                    errorLabel.setVisible(true);
                    jButton2.setEnabled(false);
                    jButton2.setBackground(Color.RED);
//                    JOptionPane.showMessageDialog(rootPane, subjectComboBox.getSelectedItem() + " subject of " +yearComboBox.getSelectedItem()+" is not available");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void subjectComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_subjectComboBoxItemStateChanged
            // TODO add your handling code here:
            if(user)
            {
                updateQuestionNumber();
            }
            else
            {
                updateQuestionNumberDemo();
            }
            
//            int totalQuestion = totalQuestotalMap.get(subjectId);
            
            
            
            
//        int subjectIndex = subjectComboBox.getSelectedIndex();
//        String subjectId = subjectIdList.get(subjectIndex);
//        int totalQuestion = totalQuestotalMap.get(subjectId);
//        
//        entireSpinner.setValue(totalQuestion);
//        toSpinner.setValue(totalQuestion);
//        fromSpinner.setValue(1);
       
        
    }//GEN-LAST:event_subjectComboBoxItemStateChanged

    private void updateQuestionNumber()
    {
        try {
            int subjectIndex = subjectComboBox.getSelectedIndex();
            if(subjectIndex!=0)
            {
                String subjectId = subjectIdList.get(subjectIndex);
                subjectId = subjectId.concat("_"+yearComboBox.getSelectedItem().toString());

                
                con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");

                pt = con.prepareStatement("SELECT * FROM subject_deno_tb WHERE subject_deno_id=?");
                pt.setString(1, subjectId);
                rs = pt.executeQuery();

                if(rs.next())
                {
                    int totalQuestion = rs.getInt("question_total");
                    entireSpinner.setValue(totalQuestion);
                    toSpinner.setValue(totalQuestion);
                    fromSpinner.setValue(1);
                    
                    errorLabel.setVisible(false);
                    jButton2.setEnabled(true);
                    jButton2.setBackground(new Color(110,217,163));
                }
                else
                {
                    errorLabel.setText(subjectComboBox.getSelectedItem() + " subject of " +yearComboBox.getSelectedItem()+" is not available");
                    errorLabel.setVisible(true);
                    jButton2.setEnabled(false);
                    jButton2.setBackground(Color.RED);
//                    JOptionPane.showMessageDialog(rootPane, subjectComboBox.getSelectedItem() + " subject of " +yearComboBox.getSelectedItem()+" is not available");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void entireFromToRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_entireFromToRadioButtonItemStateChanged
        // TODO add your handling code here:
        if(entireRadioButton.isSelected())
        {
            fromSpinner.setEnabled(false);
            toSpinner.setEnabled(false);
            
            entireSpinner.setEnabled(true);
            
        }
        else
        {
            entireSpinner.setEnabled(false);
            
            fromSpinner.setEnabled(true);
            toSpinner.setEnabled(true);
        }
    }//GEN-LAST:event_entireFromToRadioButtonItemStateChanged

    private void yearComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_yearComboBoxItemStateChanged
        // TODO add your handling code here:
        if(subjectComboBox.getSelectedIndex()!=0)
        {
            if(user)
            {
                updateQuestionNumber();
            }
            else
            {
                updateQuestionNumberDemo();
            }
        }
    }//GEN-LAST:event_yearComboBoxItemStateChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void prepareDemo()
    {
        jPanel2.setVisible(false);
        jPanel1.setVisible(true);
        
        SpinnerNumberModel model1 = new SpinnerNumberModel(10, 1, 10, 1);  
        entireSpinner.setModel(model1);
        
        model1 = new SpinnerNumberModel(1, 1, 10, 1);  
        fromSpinner.setModel(model1);
        
        model1 = new SpinnerNumberModel(10, 1, 10, 1);  
        toSpinner.setModel(model1);
        
        loadSubject();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CBTExamJamb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CBTExamJamb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CBTExamJamb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CBTExamJamb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CBTExamJamb().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField activationCodeTextField;
    private javax.swing.JComboBox durationComboBox;
    private javax.swing.JRadioButton entireRadioButton;
    private javax.swing.JSpinner entireSpinner;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JSpinner fromSpinner;
    private javax.swing.JRadioButton fromToRadioButton;
    private javax.swing.JComboBox hrComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextField keyCodeTextField;
    private javax.swing.JComboBox minComboBox;
    private javax.swing.JComboBox secComboBox;
    private javax.swing.JComboBox subjectComboBox;
    private javax.swing.ButtonGroup timerButtonGroup;
    private javax.swing.JSpinner toSpinner;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JComboBox yearComboBox;
    // End of variables declaration//GEN-END:variables
}

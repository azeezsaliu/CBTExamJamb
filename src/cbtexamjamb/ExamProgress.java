/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbtexamjamb;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.imgscalr.Scalr;

/**
 *
 * @author OLAWALE
 */
public class ExamProgress extends javax.swing.JFrame {

    /**
     * Creates new form ExamProgress
     */
    public ExamProgress() {
        initComponents();
        
        organiseOptions();
        
    }
    
    private Random randomQuestion = new Random();
    private List<Integer> answeredQuestion = new ArrayList<>();
    
    private int endNumber;
    private int startNumber;
    private int counter=0;
    
    private int nextQuestion=1;
    private String subjectId;
    private String subjectName;
    private int questionTotal;
    private ResultSet rs;
    PreparedStatement pt;
    
    private int present=0;
    private int totalQuestion;
    private int totalHr;
    private int totalMin;
    private int totalSec;
    public ExamProgress(String subjectId, String subjectNane, int questionTotal, int hour, int minute, int second) {
        initComponents();
        this.subjectId=subjectId;
        this.subjectName=subjectName;
        this.questionTotal=questionTotal;
        this.hour=hour;
        this.minute=minute;
        this.second=second;
        
        
        organiseOptions();
        
        loadNextQuestion();
        
        displayNextQuestion();
        
        Thread th = new Thread(new CountDownTimer());
        th.start();
        
    }
    private String year;
    private static CBTExamJamb cBTExamJamb;
    public ExamProgress(String subjectId, String subjectName, String year, int startNumber, int endNumber, boolean user, int hour, int minute, int second, CBTExamJamb cBTExamJamb) {
        initComponents();
        resultPanel.setVisible(false);
        if(!user)
        {
            versionLabel.setText("Version : Demo ");
            setTitle("Exam Pro - Demo Versiom");
        }
        else
        {
            versionLabel.setText("Version : Licensed ");
        }
        this.year=year;
        ExamProgress.cBTExamJamb=cBTExamJamb;
        this.subjectId=subjectId;
        this.subjectName=subjectName;
//        this.questionTotal=questionTotal;
        this.hour=hour;
        this.second=(second==0?60:second);
        this.minute=(minute==0?60:minute);
        if(this.second==60)
        {
//            if(this.minute!=0)
//            {
                this.minute--;
//            }

                if(this.minute==59)
                {
                    if(this.hour!=0)
                    {
                        this.hour--;
                    }
                }

        }
//        else if(this.minute==60)
//        {
//            
//        }
//        this.minute=(minute==0?60:minute);
        
        totalHr = hour;
        totalMin = minute;
        totalSec = second;
        
//        JOptionPane.showMessageDialog(rootPane, subjectId);
        

        optionLabel1.setVisible(false);
        optionLabel2.setVisible(false);
        optionLabel3.setVisible(false);
        optionLabel4.setVisible(false);

        //Newly added
        
        
        
        nextQuestion = startNumber;
        this.endNumber=endNumber;
        this.startNumber=startNumber;
        
        if(startNumber==0)
        {
            totalQuestion=endNumber;
        }
        else
        {
            totalQuestion=(endNumber-startNumber)+1;
        }
        
        jLabel7.setText("Subject: " + subjectName);
        jLabel8.setText("Year: " + year);
        
//        nextQuestion = randomQuestion.nextInt(endNumber-startNumber)+startNumber;
//        answeredQuestion.add(nextQuestion);
        
        
        //Newly added
        
        organiseOptions();
        
//        if(counter==present)
//        {
            counter++;
//            present=counter;
//        }
        
        loadNextQuestion();
        
        displayNextQuestion();
        
        th = new Thread(new CountDownTimer());
        th.start();
        
         try {
            setIconImage(ImageIO.read(new File("res/icons/icon_title.png")));
        } catch (IOException ex) {
            Logger.getLogger(CBTExamJamb.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
         
    }
    Thread th;
    Connection con;
    private Map<String, Integer> correctAnswerMap = new HashMap<>();
    private Map<String, Integer> suggestedAnswerMap = new HashMap<>();
    private Map<String, Question> questionMap = new HashMap<>();
    private void loadNextQuestion()
    {
        try {
//            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb");
            con = DriverManager.getConnection("jdbc:ucanaccess://exampro_db.accdb;jackcessOpener=cbtexamjamb.CryptCodecOpener", "user", "schoolDAY123");
            
            String query = "SELECT * FROM " + subjectId + " WHERE question_no=?";

            pt = con.prepareStatement(query);
//            pt.setString(1, subjectId);
            pt.setInt(1, nextQuestion);
            rs = pt.executeQuery();

            if(rs.next())
            {
                Question question = new Question();
                question.setQuestion(rs.getString("question"));
                question.setAnswer(rs.getInt("answer"));
                question.setOption1(rs.getString("option1"));
                question.setOption2(rs.getString("option2"));
                question.setOption3(rs.getString("option3"));
                question.setOption4(rs.getString("option4"));
//                question.setOption5(rs.getString("option5"));
                question.setOption5(rs.getString("comment"));
                question.setImage(rs.getString("image"));
                
                
//                if(rs.getObject("comment")!=null)
//                {
//                    
//                }
                
//                question.setImageOptions(rs.getBoolean("image_options"));
                
                
//                File file = fileChooser.getSelectedFile();
                if(rs.getObject("image")!=null)
                {
                    File file = new File(question.getImage());
    //                if(file!=null)
    //                {
                        try {
                            BufferedImage bufferedImg = ImageIO.read(file); // load image
                            BufferedImage scaledImg = Scalr.resize(bufferedImg, 200, 200);
            //                BufferedImage scaledImg = Scalr.resize(img, Method.QUALITY, 1280, 960);
                            Icon c  = new ImageIcon(scaledImg);
                            iconLabel.setText("");
                            iconLabel.setIcon(c);
                        } catch (IOException ex) {
    //                        Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                            //empty
                            iconLabel.setIcon(null);
                            iconLabel.setText("No Image");
                        }

    //                }

                
                }
                else
                {
                    iconLabel.setIcon(null);
                    iconLabel.setText("No Image");
                }
//                ImageIcon icon = new ImageIcon("1.jpg");
//                
//                iconLabel.setIcon(icon);
//                iconLabel.setIcon(icon);
                
                questionMap.put(String.valueOf(nextQuestion), question);
                correctAnswerMap.put(String.valueOf(nextQuestion), question.getAnswer());
                
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "License Error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally{
            try {
                if(rs!=null)
                {
                    rs.close();
                }
                if(pt!=null)
                {
                    pt.close();
                }
                if(con!=null)
                {
                    con.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        
        
    }
    
    private void loadPreviouseQuestion()
    {
        Question question = questionMap.get(String.valueOf(nextQuestion));
        resetRadioButtons();
        
//        questionDisplayTextArea.setText("Question "+nextQuestion + ":\n\n"+question.getQuestion());
        questionDisplayTextPane.setText("Question "+counter + " of " + totalQuestion + ":\n\n "+question.getQuestion());
        optionRadioButton1.setText(question.getOption1());
        optionRadioButton2.setText(question.getOption2());
        optionRadioButton3.setText(question.getOption3());
        optionRadioButton4.setText(question.getOption4());
//        optionRadioButton5.setText(question.getOption5());
        
        if(question.getImage()!=null)
        {
            try
            {
                File file = new File(question.getImage());
                BufferedImage bufferedImg = ImageIO.read(file); // load image
                BufferedImage scaledImg = Scalr.resize(bufferedImg, 200, 200);
        //                BufferedImage scaledImg = Scalr.resize(img, Method.QUALITY, 1280, 960);
                Icon c  = new ImageIcon(scaledImg);
                iconLabel.setText("");
                iconLabel.setIcon(c);
            }
            catch(IOException ex)
            {
                iconLabel.setIcon(null);
                iconLabel.setText("No Image");
            }
        }
        else
        {
            iconLabel.setIcon(null);
            iconLabel.setText("No Image");
        }
        
        if(!direction)
        {
            jPanelSlider1.nextPanel(30, jPanel3, jPanelSlider1.left);
            direction = true;
        }
        
        if(question.getOption5()!=null)
        {
//            commectDisplayTextPane.setText("Passage / Comment\n\n"+question.getOption5());
            try
                {
//                commectDisplayTextPane.setText("Passage / Comment\n\n"+question.getOption5());
                BufferedReader reader = null;
//                File infile = new File("3.txt");
                File infile = new File(question.getOption5());
                StringBuffer buffer = new StringBuffer();
                String string = new String();
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile)));
                while ((string = reader.readLine()) != null) {
                    buffer.append(string);
                    buffer.append("\n");
                }  
//                System.out.println(buffer.toString());
                commectDisplayTextPane.setText("Passage / Comment\n\n"+buffer.toString());
                reader.close();
                
                jLabel9.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                }
            jLabel9.setVisible(true);
        }
        else
        {
            commectDisplayTextPane.setText("Passage / Comment");
            jLabel9.setVisible(false);
        }
        
        if(suggestedAnswerMap.containsKey(String.valueOf(nextQuestion)))
        {
            optionRadioButton1.setSelected(1==suggestedAnswerMap.get(String.valueOf(nextQuestion)));
            optionRadioButton2.setSelected(2==suggestedAnswerMap.get(String.valueOf(nextQuestion)));
            optionRadioButton3.setSelected(3==suggestedAnswerMap.get(String.valueOf(nextQuestion)));
            optionRadioButton4.setSelected(4==suggestedAnswerMap.get(String.valueOf(nextQuestion)));
//            optionRadioButton5.setSelected(5==suggestedAnswerMap.get(String.valueOf(nextQuestion)));
        }
        
    }
    
    private void displayNextQuestion()
    {
        Question question = questionMap.get(String.valueOf(nextQuestion));
        
//        questionDisplayTextArea.setText("Question "+nextQuestion + ":\n\n"+question.getQuestion());
        questionDisplayTextPane.setText("Question "+counter + " of " + totalQuestion + ":\n\n "+question.getQuestion());
        
//        JOptionPane.showMessageDialog(rootPane, question.isImageOptions());
//        if(question.isImageOptions())
        if(false)
        {
                
            optionLabel1.setText(null);
            optionLabel2.setText(null);
            optionLabel3.setText(null);
            optionLabel4.setText(null);
//            optionRadioButton5.setText(null);
            
            optionLabel1.setIcon(new ImageIcon(question.getOption1()));
            optionLabel2.setIcon(new ImageIcon(question.getOption2()));
            optionLabel3.setIcon(new ImageIcon(question.getOption3()));
            optionLabel4.setIcon(new ImageIcon(question.getOption4()));
//            optionRadioButton5.setIcon(new ImageIcon(question.getOption5()));
//            optionRadioButton1.setText(null);
//            optionRadioButton2.setText(null);
//            optionRadioButton3.setText(null);
//            optionRadioButton4.setText(null);
//            optionRadioButton5.setText(null);
//            
//            optionRadioButton1.setIcon(new ImageIcon(question.getOption1()));
//            optionRadioButton2.setIcon(new ImageIcon(question.getOption2()));
//            optionRadioButton3.setIcon(new ImageIcon(question.getOption3()));
//            optionRadioButton4.setIcon(new ImageIcon(question.getOption4()));
//            optionRadioButton5.setIcon(new ImageIcon(question.getOption5()));
        }
        else
        {
            
//            optionLabel1.setIcon(null);
//            optionLabel2.setIcon(null);
//            optionLabel3.setIcon(null);
//            optionLabel4.setIcon(null);
//            optionRadioButton5.setIcon(null);
            
//            optionLabel1.setText(question.getOption1());
//            optionLabel2.setText(question.getOption2());
//            optionLabel3.setText(question.getOption3());
//            optionLabel4.setText(question.getOption4());
//            optionRadioButton5.setText(question.getOption5());
            
           
            optionRadioButton1.setText(question.getOption1());
            optionRadioButton2.setText(question.getOption2());
            optionRadioButton3.setText(question.getOption3());
            optionRadioButton4.setText(question.getOption4());
//            optionRadioButton5.setText(question.getOption5());

            if(!direction)
            {
                jPanelSlider1.nextPanel(30, jPanel3, jPanelSlider1.left);
                direction = true;
            }

            if(question.getOption5()!=null)
            {
                try
                {
//                commectDisplayTextPane.setText("Passage / Comment\n\n"+question.getOption5());
                BufferedReader reader = null;
//                File infile = new File("3.txt");
                File infile = new File(question.getOption5());
                StringBuffer buffer = new StringBuffer();
                String string = new String();
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile)));
                while ((string = reader.readLine()) != null) {
                    buffer.append(string);
                    buffer.append("\n");
                }  
//                System.out.println(buffer.toString());
                commectDisplayTextPane.setText("Passage / Comment\n\n"+buffer.toString());
                reader.close();
                
                jLabel9.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                commectDisplayTextPane.setText("Passage / Comment");
                jLabel9.setVisible(false);
            }

        }
    }
    private void organiseOptions()
    {
        optionButtonGroup.add(optionRadioButton1);
        optionButtonGroup.add(optionRadioButton2);
        optionButtonGroup.add(optionRadioButton3);
        optionButtonGroup.add(optionRadioButton4);
        optionButtonGroup.add(optionRadioButton5);
    }

    private void resetRadioButtons()
    {
        optionButtonGroup.clearSelection();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        optionButtonGroup = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        optionRadioButton5 = new javax.swing.JRadioButton();
        optionLabel4 = new javax.swing.JLabel();
        optionLabel3 = new javax.swing.JLabel();
        optionLabel2 = new javax.swing.JLabel();
        optionLabel1 = new javax.swing.JLabel();
        iconLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        resultPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        previouseButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        calculatorButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        optionRadioButton2 = new javax.swing.JRadioButton();
        optionRadioButton3 = new javax.swing.JRadioButton();
        optionRadioButton4 = new javax.swing.JRadioButton();
        optionRadioButton1 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        showAnswerButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanelSlider1 = new diu.swe.habib.JPanelSlider.JPanelSlider();
        jPanel3 = new javax.swing.JPanel();
        iconLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        questionDisplayTextPane = new javax.swing.JTextPane();
        passagePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        commectDisplayTextPane = new javax.swing.JTextPane();
        versionLabel = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );

        optionRadioButton5.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        optionRadioButton5.setForeground(new java.awt.Color(255, 255, 255));
        optionRadioButton5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                optionsItemStateChanged(evt);
            }
        });

        optionLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        optionLabel4.setForeground(new java.awt.Color(255, 255, 255));
        optionLabel4.setText("jLabel2");

        optionLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        optionLabel3.setForeground(new java.awt.Color(255, 255, 255));
        optionLabel3.setText("jLabel2");

        optionLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        optionLabel2.setForeground(new java.awt.Color(255, 255, 255));
        optionLabel2.setText("jLabel2");

        optionLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        optionLabel1.setForeground(new java.awt.Color(255, 255, 255));
        optionLabel1.setText("jLabel2");

        iconLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Exam Pro");
        setResizable(false);

        resultPanel.setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("EXAMINATION RESULT");

        jPanel7.setBackground(new java.awt.Color(0, 51, 51));

        jPanel6.setBackground(new java.awt.Color(0, 51, 51));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(110, 217, 163), 5, true));

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Time Elapse");

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Failed Questions");

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Subject");

        jLabel16.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Passed Questions");

        jLabel17.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Year");

        jLabel18.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Total Questions");

        jLabel19.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Time Remaining");

        jLabel20.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Time Elapse");

        jLabel21.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Unattempt Questions");

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Subject");

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Year");

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Failed Questions");

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Passed Questions");

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Total Questions");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Time Remaining");

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Unattempt Questions");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        closeButton.setBackground(new java.awt.Color(110, 217, 163));
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(205, Short.MAX_VALUE))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        previouseButton.setBackground(new java.awt.Color(110, 217, 163));
        previouseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        previouseButton.setForeground(new java.awt.Color(41, 53, 65));
        previouseButton.setText("Previouse");
        previouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previouseButtonActionPerformed(evt);
            }
        });

        nextButton.setBackground(new java.awt.Color(110, 217, 163));
        nextButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        nextButton.setForeground(new java.awt.Color(41, 53, 65));
        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        calculatorButton.setBackground(new java.awt.Color(110, 217, 163));
        calculatorButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        calculatorButton.setForeground(new java.awt.Color(41, 53, 65));
        calculatorButton.setText("Calculator");
        calculatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorButtonActionPerformed(evt);
            }
        });

        submitButton.setBackground(new java.awt.Color(110, 217, 163));
        submitButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        submitButton.setForeground(new java.awt.Color(41, 53, 65));
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        jSeparator7.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Subject: Biology");

        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeLabel.setText("Time: 2:30:00");

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

        optionRadioButton2.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        optionRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        optionRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                optionsItemStateChanged(evt);
            }
        });

        optionRadioButton3.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        optionRadioButton3.setForeground(new java.awt.Color(255, 255, 255));
        optionRadioButton3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                optionsItemStateChanged(evt);
            }
        });

        optionRadioButton4.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        optionRadioButton4.setForeground(new java.awt.Color(255, 255, 255));
        optionRadioButton4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                optionsItemStateChanged(evt);
            }
        });

        optionRadioButton1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        optionRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        optionRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                optionsItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionRadioButton1)
                    .addComponent(optionRadioButton2)
                    .addComponent(optionRadioButton3)
                    .addComponent(optionRadioButton4))
                .addContainerGap(743, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(optionRadioButton1)
                .addGap(15, 15, 15)
                .addComponent(optionRadioButton2)
                .addGap(13, 13, 13)
                .addComponent(optionRadioButton3)
                .addGap(18, 18, 18)
                .addComponent(optionRadioButton4)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(110, 217, 163));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\OLAWALE\\Documents\\NetBeansProjects\\CBTExamJamb\\res\\icons\\Note_25px.png")); // NOI18N
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel9MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(57, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(434, Short.MAX_VALUE)))
        );

        showAnswerButton.setBackground(new java.awt.Color(110, 217, 163));
        showAnswerButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        showAnswerButton.setForeground(new java.awt.Color(41, 53, 65));
        showAnswerButton.setText("Show Answer");
        showAnswerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAnswerButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Year: 2017");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        iconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        questionDisplayTextPane.setEditable(false);
        questionDisplayTextPane.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        questionDisplayTextPane.setText("Question 1:");
        jScrollPane2.setViewportView(questionDisplayTextPane);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iconLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );

        jPanelSlider1.add(jPanel3, "card2");

        passagePanel.setBackground(new java.awt.Color(255, 255, 255));

        commectDisplayTextPane.setEditable(false);
        commectDisplayTextPane.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        commectDisplayTextPane.setText("Passage / Comment:");
        jScrollPane3.setViewportView(commectDisplayTextPane);

        javax.swing.GroupLayout passagePanelLayout = new javax.swing.GroupLayout(passagePanel);
        passagePanel.setLayout(passagePanelLayout);
        passagePanelLayout.setHorizontalGroup(
            passagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
        );
        passagePanelLayout.setVerticalGroup(
            passagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );

        jPanelSlider1.add(passagePanel, "card2");

        versionLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        versionLabel.setForeground(new java.awt.Color(255, 255, 255));
        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        versionLabel.setText("Version : Demo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(previouseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(calculatorButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(showAnswerButton)
                        .addGap(104, 104, 104))
                    .addComponent(jPanelSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 51, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(timeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 779, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(showAnswerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(versionLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13))
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previouseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(calculatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 431, Short.MAX_VALUE)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(42, 42, 42)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void optionsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_optionsItemStateChanged
        // TODO add your handling code here:
        
        if(optionRadioButton1.isSelected())
        {
            suggestedAnswerMap.put(String.valueOf(nextQuestion), 1);
        }
        else if(optionRadioButton2.isSelected())
        {
            suggestedAnswerMap.put(String.valueOf(nextQuestion), 2);
        }
        else if(optionRadioButton3.isSelected())
        {
            suggestedAnswerMap.put(String.valueOf(nextQuestion), 3);
        }
        else if(optionRadioButton4.isSelected())
        {
            suggestedAnswerMap.put(String.valueOf(nextQuestion), 4);
        }
        else if(optionRadioButton5.isSelected())
        {
            suggestedAnswerMap.put(String.valueOf(nextQuestion), 5);
        }
        
    }//GEN-LAST:event_optionsItemStateChanged

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        // TODO add your handling code here:
        
        hideAnswer();
       
        checkNoAnswerSelected();
        
//        if(counter==present)
//        {
//            
//            previouseButton.setEnabled(true);
//            if((counter+startNumber)!=endNumber)
//            {
//                nextButton.setEnabled(true);
//            }
//            else
//            {
//                nextButton.setEnabled(false);
//            }
//            
//            
//            counter++;
//            present=counter;
//            
//            while(true)
//            {
//                int temp = randomQuestion.nextInt(endNumber-startNumber)+startNumber;
//                if(!answeredQuestion.contains(temp))
//                {
//                    JOptionPane.showMessageDialog(rootPane, temp);
//                    nextQuestion=temp;
//                    answeredQuestion.add(nextQuestion);
//                    
//                    loadNextQuestion();
//        
//                    displayNextQuestion();
//                    resetRadioButtons();
//                    break;
//                }
//            }
//            
//        }
//        else
//        {
//            nextQuestion = answeredQuestion.get(counter++);
//            loadPreviouseQuestion();
//        }
        
        
        nextQuestion++;
        counter++;
        
        if(!questionMap.containsKey(String.valueOf(nextQuestion)))
        {
            loadNextQuestion();
            displayNextQuestion();
            resetRadioButtons();
            
            
        }
        else
        {
            //For Existing Question
            loadPreviouseQuestion();
        }
        
        previouseButton.setEnabled(true);
//        if(nextQuestion!=questionTotal)
        if(nextQuestion!=endNumber)
//        if((counter+startNumber)!=endNumber)
        {
            nextButton.setEnabled(true);
        }
        else
        {
            nextButton.setEnabled(false);
        }
        
    }//GEN-LAST:event_nextButtonActionPerformed

    private void checkNoAnswerSelected()
    {
        if(!optionRadioButton1.isSelected() && !optionRadioButton2.isSelected() && !optionRadioButton3.isSelected() && !optionRadioButton4.isSelected() && !optionRadioButton5.isSelected())
        {
            suggestedAnswerMap.put(String.valueOf(nextQuestion), 10);
        }
    }
    
    private void previouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previouseButtonActionPerformed
        // TODO add your handling code here:
        
        checkNoAnswerSelected();
        
        hideAnswer();
        nextQuestion--;
        counter--;
        
//        nextQuestion = answeredQuestion.get(counter--);
        
        loadPreviouseQuestion();
        displayPreviouseQuestion();
        
        nextButton.setEnabled(true);
//        if(nextQuestion!=1)
        if(nextQuestion!=startNumber)
//        if(endNumber-counter!=startNumber)
        {
            previouseButton.setEnabled(true);
        }
        else
        {
            previouseButton.setEnabled(false);
        }
    }//GEN-LAST:event_previouseButtonActionPerformed

    private void calculatorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorButtonActionPerformed
        // TODO add your handling code here:
        try
        {
            Runtime.getRuntime().exec("calc");
        }
        catch(Exception ex)
        {
            
        }
    }//GEN-LAST:event_calculatorButtonActionPerformed

    private void showAnswerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAnswerButtonActionPerformed
        // TODO add your handling code here:
        
        if(((JButton)evt.getSource()).getText().equalsIgnoreCase("Show Answer"))
        {
            showAnswer();
//            ((JButton)evt.getSource()).setText("Hide Answer  ");
        }
        else
        {
            hideAnswer();
//            ((JButton)evt.getSource()).setText("Show Answer");
        }
        
//        
        
    }//GEN-LAST:event_showAnswerButtonActionPerformed

    private boolean direction = true;
    private void jLabel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseEntered
        // TODO add your handling code here:
        jLabel9.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel9MouseEntered

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO add your handling code here:
        if(direction)
        {
            jPanelSlider1.nextPanel(30, passagePanel, jPanelSlider1.right);
            direction = false;
        }
        else
        {
            jPanelSlider1.nextPanel(30, jPanel3, jPanelSlider1.left);
            direction = true;
        }
    }//GEN-LAST:event_jLabel9MouseClicked

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        // TODO add your handling code here:
        checkNoAnswerSelected();
        resultStatus();
    }//GEN-LAST:event_submitButtonActionPerformed

    
    private void resultStatus()
    {
        int correctAnswer=0;
        int incorrectAnswer=0;
        int unAttempAnswer=0;
        
        th.stop();
        
        for(int count = 1; count <= correctAnswerMap.size(); ++count)
        {
            if(correctAnswerMap.get(String.valueOf(count))==(suggestedAnswerMap.get(String.valueOf(count))))
            {
                correctAnswer++;
            }
            else if(suggestedAnswerMap.get(String.valueOf(count))!=10)
            {
                incorrectAnswer++;
            }

        }
        
        unAttempAnswer = totalQuestion - (correctAnswer+incorrectAnswer);
        
        int hrLeft = Math.abs(totalHr - hour);
        int minLeft = Math.abs(totalMin - minute);
        int secLeft = Math.abs(totalSec - second);
        
        jLabel3.setText(subjectName);
        jLabel4.setText(year);
        jLabel5.setText("" + pastHr + ":" + pastMin + ":" + pastSec);
        jLabel6.setText("" + hour + ":" + minute + ":" + second);
        jLabel10.setText("" + totalQuestion);
        jLabel11.setText("" + correctAnswer);
        jLabel12.setText("" + incorrectAnswer);
        jLabel13.setText("" + unAttempAnswer);
        
        jPanel2.setVisible(false);
        resultPanel.setVisible(true);
        
        
    }
    
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        cBTExamJamb.setVisible(true);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void showAnswer()
    {
        optionRadioButton1.setForeground(Color.RED);
        optionRadioButton2.setForeground(Color.RED);
        optionRadioButton3.setForeground(Color.RED);
        optionRadioButton4.setForeground(Color.RED);
        optionRadioButton5.setForeground(Color.RED);
        
        if(correctAnswerMap.get(String.valueOf(nextQuestion))==1)
        {
            optionRadioButton1.setForeground(Color.GREEN);
        }
        else if(correctAnswerMap.get(String.valueOf(nextQuestion))==2)
        {
            optionRadioButton2.setForeground(Color.GREEN);
        }
        else if(correctAnswerMap.get(String.valueOf(nextQuestion))==3)
        {
            optionRadioButton3.setForeground(Color.GREEN);
        }
        else if(correctAnswerMap.get(String.valueOf(nextQuestion))==4)
        {
            optionRadioButton4.setForeground(Color.GREEN);
        }
        else if(correctAnswerMap.get(String.valueOf(nextQuestion))==5)
        {
            optionRadioButton5.setForeground(Color.GREEN);
        }
        
        showAnswerButton.setText("Hide Answer  ");
    }
    
    private void hideAnswer()
    {
        optionRadioButton1.setForeground(Color.WHITE);
        optionRadioButton2.setForeground(Color.WHITE);
        optionRadioButton3.setForeground(Color.WHITE);
        optionRadioButton4.setForeground(Color.WHITE);
        optionRadioButton5.setForeground(Color.WHITE);
        
        showAnswerButton.setText("Show Answer");
    }
    
    private void displayPreviouseQuestion()
    {
        
    }
    
    private int hour = 2;
    private int minute = 10;
    private int second = 59;
    
    private int pastHr;
    private int pastMin;
    private int pastSec;
    private class CountDownTimer implements Runnable
    {

        @Override
        public void run() {
            while(true)
            {
                try {
                    
                    if(second!=0)
                    {
                        timeLabel.setText("Time: "+hour+":"+minute+":"+--second);
                        pastSec++;
                    }
                    else if(minute!=0)
                    {
                        second=59;
                        pastSec=0;
                        timeLabel.setText("Time: "+hour+":"+--minute+":"+second);
                        pastMin++;
                    }
                    else if(hour!=0)
                    {
                        second=59;
                        minute=59;
                        pastSec=0;
                        pastMin=0;
                        timeLabel.setText("Time: "+--hour+":"+minute+":"+second);
                        pastHr++;
                    }
                    else
                    {
                        JOptionPane.showConfirmDialog(rootPane, "Time Up\nClick OK to submit your exam...", "Title", JOptionPane.OK_OPTION);
                    }
                    
                    
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExamProgress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
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
            java.util.logging.Logger.getLogger(ExamProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExamProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExamProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExamProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExamProgress().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculatorButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JTextPane commectDisplayTextPane;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JLabel iconLabel1;
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
    private diu.swe.habib.JPanelSlider.JPanelSlider jPanelSlider1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JButton nextButton;
    private javax.swing.ButtonGroup optionButtonGroup;
    private javax.swing.JLabel optionLabel1;
    private javax.swing.JLabel optionLabel2;
    private javax.swing.JLabel optionLabel3;
    private javax.swing.JLabel optionLabel4;
    private javax.swing.JRadioButton optionRadioButton1;
    private javax.swing.JRadioButton optionRadioButton2;
    private javax.swing.JRadioButton optionRadioButton3;
    private javax.swing.JRadioButton optionRadioButton4;
    private javax.swing.JRadioButton optionRadioButton5;
    private javax.swing.JPanel passagePanel;
    private javax.swing.JButton previouseButton;
    private javax.swing.JTextPane questionDisplayTextPane;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JButton showAnswerButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}

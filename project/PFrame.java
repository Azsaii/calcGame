package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import project.PEvent;
import project.PTimer;

public class PFrame extends JFrame {

    /*---------------타이머 레이블---------------*/
    static JLabel label_leftTime = new JLabel("시간");
    static JLabel label_second = new JLabel("99");
    static JLabel label_colon = new JLabel(":");
    static JLabel label_msec = new JLabel("9");

    /*--------------점수 레이블---------------*/
    static JLabel score = new JLabel("SCORE: 0");

    /*--------------문제 레이블---------------*/
    static JLabel op1 = new JLabel(" ");
    static JLabel calc = new JLabel(" ");
    static JLabel op2 = new JLabel("시작 버튼을 누르세요");
    static JLabel equal = new JLabel(" ");
    static JLabel answer = new JLabel("");

    /*--------------버튼---------------*/
    static JButton[] key_btn = new JButton[10]; // 키 버튼 배열
    static JRadioButton[] l_btn = new JRadioButton[3]; // 레벨 버튼 배열
    static JButton b_reset, b_submit, b_start, b_pause, b_cancel; // 기능 버튼들

    /*--------------랜덤 키패드 메뉴---------------*/
    static JMenuItem select_random = new JMenuItem("랜덤 키패드 켜기");

    /*--------------buildCenter()의 패널---------------*/
    static JPanel center = new JPanel(new GridLayout(5, 3, 5, 5));

    /*--------------이벤트 변수---------------*/
    static boolean start_state = false; // 시작 버튼에 사용됨
    static boolean pause_state = false; // 정지 버튼에 사용됨
    static boolean time_over = false; // 타임 오버시 타이머 조정에 사용됨
    static boolean random_state = false; // 랜덤 키패드 메뉴에 사용됨
    static int[] ranNum = new int[10]; // 랜덤 키패드 생성에 사용됨
    static String save_data = ""; // 정지 버튼에 사용됨
    static int level = 1; // 난이도
    static int h_score = 0; // 최고점수

    static Container c;

    PFrame(){ // 프레임 설정
        setTitle("GAME");
        setSize(500, 700);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 프레임 위치를 화면 중앙으로 설정
        setResizable(false); // 프레임 크기 조절 불가하도록 설정
        c = getContentPane();

        buildStartPanel();
        buildMenu();
        buildNorth();
        buildCenter();
    }

    public void designComponent(JComponent jc, String name, int style, int size, Color fg, Color bg){
        jc.setFont(new Font(name, style, size));
        jc.setForeground(fg);
        jc.setBackground(bg);
    }

    public void buildStartPanel(){ // 시작 화면 설정

        /*--------------패널 생성---------------*/
        JPanel topPanel = new JPanel(); // 제목이 들어가는 패널
        JPanel midPanel = new JPanel(); // 제목 이외 요소가 들어가는 패널
        JPanel imgPanel = new JPanel(); // 이미지가 들어가는 패널
        JPanel btnPanel = new JPanel(); // 난이도 버튼이 들어가는 패널
        JPanel playPanel = new JPanel(); // 플레이 버튼이 들어가는 패널

        /*--------------레이블 생성---------------*/
        JLabel title_label = new JLabel("암산 게임");
        JLabel level_text = new JLabel("난이도 선택");
        JButton play_btn = new JButton("PLAY");

        /*--------------이미지 설정---------------*/
        String imgSrc = System.getProperty("user.dir") + "\\src\\project\\img\\" + "controller.png";
        ImageIcon icon = new ImageIcon(imgSrc);
        Image img = icon.getImage();
        img = img.getScaledInstance(250, 230, Image.SCALE_SMOOTH); // 이미지 사이즈 조절
        ImageIcon ch_img = new ImageIcon(img);
        JLabel img_label = new JLabel(ch_img); // 이미지 레이블 생성
        imgPanel.add(img_label);

        /*--------------난이도 버튼 생성---------------*/
        ButtonGroup bg = new ButtonGroup(); // 난이도 선택 버튼 그룹
        String[] levelName = {"쉬움", "보통", "어려움"}; // 난이도 선택 버튼 이름
        btnPanel.add(level_text); // "난이도 선택" 텍스트가 있는 레이블을 붙인다

        for(int i = 0; i < 3; i++){
            l_btn[i] = new JRadioButton(levelName[i]);
            l_btn[i].setHorizontalAlignment(JRadioButton.CENTER);
            designComponent((JRadioButton)l_btn[i], "돋움", Font.BOLD, 30, Color.PINK, Color.GRAY);
            l_btn[i].addItemListener(new PEvent.RadioItemListener());
            bg.add(l_btn[i]);
            btnPanel.add(l_btn[i]);
        }
        l_btn[0].setSelected(true);

        /*--------------컴포넌트 디자인---------------*/
        designComponent((JLabel)title_label, "HY견고딕", Font.BOLD, 50, Color.PINK, Color.GRAY);
        designComponent((JLabel)level_text, "돋움", Font.PLAIN, 20, Color.PINK, Color.GRAY);
        designComponent((JButton)play_btn, "Goudy Stout", Font.BOLD, 40, Color.PINK, Color.DARK_GRAY);

        level_text.setHorizontalAlignment(JLabel.CENTER);
        midPanel.setLayout(new GridLayout(3, 1, 2, 10));
        btnPanel.setLayout(new GridLayout(4, 1));

        topPanel.setBackground(Color.GRAY);
        midPanel.setBackground(Color.GRAY);
        playPanel.setBackground(Color.GRAY);
        imgPanel.setBackground(Color.GRAY);
        btnPanel.setBackground(Color.GRAY);

        playPanel.setLayout(null);
        play_btn.setLocation(120, 30);
        play_btn.setSize(250, 110);
        play_btn.addActionListener(new PEvent.PlayActionListener(topPanel, midPanel));
        playPanel.add(play_btn);

        /*--------------패널 붙이기---------------*/
        topPanel.add(title_label);
        midPanel.add(imgPanel);
        midPanel.add(btnPanel);
        midPanel.add(playPanel);

        c.add(topPanel, BorderLayout.NORTH);
        c.add(midPanel, BorderLayout.CENTER);

        setContentPane(c);
    }

    public void buildMenu(){ // 메뉴 설정

        /*--------------메뉴 요소 생성---------------*/
        JMenuBar mb = new JMenuBar();
        JMenu level = new JMenu("난이도 변경");
        JMenu save = new JMenu("저장");
        JMenu exit = new JMenu("나가기");

        JMenuItem easy_level = new JMenuItem("쉬움");
        JMenuItem middle_level = new JMenuItem("보통");
        JMenuItem hard_level = new JMenuItem("어려움");

        JMenuItem checkScore = new JMenuItem("최고 점수 보기");
        JMenuItem saveScore = new JMenuItem("최고 점수 저장하기");
        JMenuItem clearScore = new JMenuItem("최고 점수 초기화");

        /*--------------메뉴 이벤트 설정---------------*/
        checkScore.addActionListener(new PFileIO.checkActionListener()); // 점수 확인 이벤트 연결
        saveScore.addActionListener(new PFileIO.FileActionListener()); // 저장하기 이벤트 연결
        clearScore.addActionListener(new PFileIO.ClearActionListener()); // 최고 점수 초기화 이벤트 연결
        exit.addMouseListener(new PMenu.exitMouseListener()); // 나가기 이벤트 연결

        easy_level.addActionListener(new PMenu.levelActionListener()); // 난이도 변경 이벤트 연결
        middle_level.addActionListener(new PMenu.levelActionListener());
        hard_level.addActionListener(new PMenu.levelActionListener());

        select_random.addActionListener(new PMenu.randomActionListener()); // 랜덤 키패드 이벤트 연결

        /*--------------메뉴 붙이기---------------*/
        level.add(easy_level);
        level.add(middle_level);
        level.add(hard_level);
        level.add(select_random);

        save.add(checkScore);
        save.add(saveScore);
        save.add(clearScore);

        mb.add(level);
        mb.add(save);
        mb.add(exit);

        setJMenuBar(mb);
    }

    public void buildNorth() { // 타이머, 문제, 점수 설정

        /*--------------패널 생성---------------*/
        JPanel north = new JPanel(new GridLayout(2, 1));
        JPanel north_north = new JPanel(new GridLayout(1, 2, 0, 0));
        JPanel north_center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel timer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pScore = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        /*--------------컴포넌트 디자인---------------*/
        north_north.setSize(700, 150);
        north_north.setLocation(0, 0);
        north_center.setSize(700, 150);
        north_center.setLocation(0, 150);

        label_leftTime.setFont(new Font("SansSerif", Font.BOLD, 20));
        label_leftTime.setForeground(Color.red);

        label_colon.setFont(new Font("SansSerif", Font.PLAIN, 30));
        label_second.setFont(new Font("SansSerif", Font.PLAIN, 30));
        label_msec.setFont(new Font("SansSerif", Font.PLAIN, 30));

        score.setFont(new Font("SansSerif", Font.ITALIC, 30));

        op1.setFont(new Font("SansSerif", Font.BOLD, 70));
        calc.setFont(new Font("SansSerif", Font.BOLD, 70));
        op2.setFont(new Font("SansSerif", Font.BOLD, 30));
        equal.setFont(new Font("SansSerif", Font.BOLD, 70));
        answer.setFont(new Font("SansSerif", Font.BOLD, 70));

        /*--------------패널 붙이기---------------*/
        timer.add(label_leftTime);
        timer.add(label_second);
        timer.add(label_colon);
        timer.add(label_msec);
        pScore.add(score);

        north_north.add(timer);
        north_north.add(pScore);

        north_center.add(op1);
        north_center.add(calc);
        north_center.add(op2);
        north_center.add(equal);
        north_center.add(answer);

        north.add(north_north);
        north.add(north_center);
        c.add(north, BorderLayout.NORTH);
    }

    public static void setButtonFont(JButton btn, int size) { // 버튼 폰트 설정
        btn.setFont(new Font("SansSerif", Font.BOLD, size));
    }
    public static void setButtonEnabled(JButton btn, boolean state) { // 버튼 활성화/비활성화 설정
        btn.setForeground(Color.white);
        if(state){
            btn.setEnabled(true);
            btn.setBackground(Color.PINK);
        }
        else {
            btn.setEnabled(false);
            btn.setBackground(Color.DARK_GRAY);
        }
    }

    public static void setKeyButton(boolean state, int[] numArray){ // 키패드 버튼 생성
        for(int i = 0; i < 10; i++) {
            key_btn[i] = new JButton(Integer.toString(numArray[i]));
            setButtonFont(key_btn[i], 70); // 버튼 폰트 설정
            setButtonEnabled(key_btn[i], state);
            key_btn[i].addActionListener(new PEvent.NumberActionListener());
            key_btn[i].setForeground(Color.white);
            if(i != 0) center.add(key_btn[i]);
        }
    }

    public void buildCenter(){ // 키패드와 각 버튼 설정

        for(int i = 0; i < 10; i++) ranNum[i] = i; // 키패드 배열 초기화
        setKeyButton(false, ranNum); // 키패드 버튼 생성

        /*--------------기능 버튼 생성---------------*/
        b_reset = new JButton("초기화"); setButtonFont(b_reset, 40);
        b_pause = new JButton("정지"); setButtonFont(b_pause, 40);
        b_cancel = new JButton("지우기"); setButtonFont(b_cancel, 40);
        b_start = new JButton("시작"); setButtonFont(b_start, 40);
        b_submit = new JButton("제출"); setButtonFont(b_submit, 40);

        /*--------------버튼 붙이기---------------*/
        center.add(b_reset); center.add(key_btn[0]); center.add(b_cancel);
        center.add(b_pause); center.add(b_start); center.add(b_submit);

        b_reset.addActionListener(new PEvent.ResetActionListener()); // 문제와 타이머 초기화
        b_cancel.addActionListener(new PEvent.CancelActionListener()); // 지우기 동작
        b_pause.addActionListener(new PEvent.PauseActionListener()); // 타이머 정지
        b_start.addActionListener(new PTimer.timerActionListener()); // 타이머 작동
        b_start.addActionListener(new PEvent.StartActionListener()); // 랜덤 문제 출제
        b_submit.addActionListener(new PEvent.SubmitActionListener()); // 답 제출

        /*--------------버튼 비활성화---------------*/
        setButtonEnabled(b_reset, false);
        setButtonEnabled(b_cancel, false);
        setButtonEnabled(b_pause, false);
        setButtonEnabled(b_start, true);
        setButtonEnabled(b_submit, false);

        center.setFont(new Font("SansSerif", Font.BOLD, 50));
        c.add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args){
        new PFrame();
    }
}

package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PMenu {

    static class levelActionListener implements ActionListener { // 난이도 메뉴 처리

        public void actionPerformed(ActionEvent e) {

            /*--------------선택한 메뉴에 따라 난이도 변수 변경---------------*/
            if(e.getActionCommand().equals("쉬움")){
                PFrame.level = 1;
                System.out.println("난이도 변경: 쉬움");
            }

            else if(e.getActionCommand().equals("보통")){
                PFrame.level = 2;
                System.out.println("난이도 변경: 보통");
            }

            else {
                PFrame.level = 3;
                System.out.println("난이도 변경: 어려움");
            }
        }
    }

    static class randomActionListener implements ActionListener { // 랜덤 키패드 메뉴 처리

        public void actionPerformed(ActionEvent e) {

            if(!PFrame.random_state){ // 랜덤 키패드가 꺼져 있으면 켜짐으로 변경
                PFrame.random_state = true;
                PFrame.select_random.setText("랜덤 키패드 끄기");
            }
            else {
                PFrame.random_state = false;
                PFrame.select_random.setText("랜덤 키패드 켜기");
            }
        }
    }

    static class exitMouseListener extends MouseAdapter { // 나가기 메뉴 처리

        public void mouseClicked(MouseEvent e) {

            int result = JOptionPane.showConfirmDialog(null,
                    "정말 종료하시겠습니까?", "EXIT", JOptionPane.YES_NO_OPTION);

            if(result == JOptionPane.YES_OPTION){
                System.exit(0);
            }

            else return;
        }
    }
}

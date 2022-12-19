package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import project.PTimer;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class PEvent {

    static class RadioItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {

            /*--------선택된 라디오 버튼 알아내서 난이도 변경--------*/
            for(int i = 0; i < 3; i++){
                if(PFrame.l_btn[i].isSelected()){
                    PFrame.l_btn[i].setBackground(Color.DARK_GRAY);
                    if(i == 0) PFrame.level = 1;
                    else if(i == 1) PFrame.level = 2;
                    else PFrame.level = 3;
                }
                else PFrame.l_btn[i].setBackground(Color.GRAY);
            }
        }
    }

    static class PlayActionListener implements ActionListener { // 게임 화면 보이기
        JPanel top, mid;

        PlayActionListener(JPanel top, JPanel mid){
            this.top = top;
            this.mid = mid;
        }

        public void actionPerformed(ActionEvent e) { // 바탕 화면 지우기
            PFrame.c.remove(top);
            PFrame.c.remove(mid);
            PFrame.c.revalidate();
        }
    }

    static class ResetActionListener implements ActionListener { // 초기화 이벤트

        public void actionPerformed(ActionEvent e) {

            /*--------------초기화 대화상자---------------*/
            int result = JOptionPane.showConfirmDialog(null,
                    "초기화하시겠습니까?", "RESET", JOptionPane.YES_NO_OPTION);

            if(result == JOptionPane.YES_OPTION){
                PFrame.score.setText("SCORE: 0");
                PTimer.thread = null;

                /*--------------텍스트 초기화---------------*/
                PFrame.label_second.setText("99");
                PFrame.label_msec.setText("9");
                PFrame.b_start.setText("시작");
                PFrame.b_start.setFont(new Font("SansSerif", Font.BOLD, 40));

                PFrame.op1.setText(" ");
                PFrame.op2.setText(" ");
                PFrame.calc.setText(" ");
                PFrame.equal.setText(" ");
                PFrame.answer.setText("");

                /*--------------버튼 활성화/비활성화---------------*/
                PFrame.setButtonEnabled(PFrame.b_reset, false);
                PFrame.setButtonEnabled(PFrame.b_cancel, false);
                PFrame.setButtonEnabled(PFrame.b_pause, false);
                PFrame.setButtonEnabled(PFrame.b_submit, false);
            }
            else return;
        }
    }

    static class NumberActionListener implements ActionListener { // 숫자 버튼 이벤트

        public void actionPerformed(ActionEvent e) {

            JButton btn = (JButton)e.getSource();
            JLabel result = PFrame.answer;

            String ans = result.getText(); // 정답란의 텍스트
            String clicked_text = btn.getText(); // 입력할 텍스트

            if(ans.length() == 4) {
                JOptionPane.showMessageDialog(null, "4자리까지만 입력 가능합니다.",
                        "Error!", ERROR_MESSAGE);
                return;
            }

            /*--------------숫자 입력 처리---------------*/
            switch(clicked_text){
                case "0":  ans = ans.concat("0"); break;
                case "1":  ans = ans.concat("1"); break;
                case "2":  ans = ans.concat("2"); break;
                case "3":  ans = ans.concat("3"); break;
                case "4":  ans = ans.concat("4"); break;
                case "5":  ans = ans.concat("5"); break;
                case "6":  ans = ans.concat("6"); break;
                case "7":  ans = ans.concat("7"); break;
                case "8":  ans = ans.concat("8"); break;
                case "9":  ans = ans.concat("9"); break;
            }

            /*--------------입력한 숫자의 첫번째 자리가 0이면 제거한다---------------*/
            if(ans.length() >=2 && ans.charAt(0) == '0')
                ans = ans.substring(1, ans.length());
            result.setText(ans);
        }
    }

    static class CancelActionListener implements ActionListener { // 숫자 입력 지우기 이벤트

        public void actionPerformed(ActionEvent e) {

            JLabel result = PFrame.answer;
            String text = result.getText();

            if(text.length() != 0) // 지울 숫자가 있으면 가장 끝의 숫자를 지운다
                text = text.substring(0, text.length() - 1);
            result.setText(text);
        }
    }

    static class PauseActionListener implements ActionListener { // 정지/재개 이벤트

        public void actionPerformed(ActionEvent e) {

            if(!PFrame.pause_state) {

                /*--------------정지 시 문제가 보이지 않도록 처리---------------*/
                PFrame.op1.setVisible(false);
                PFrame.op2.setVisible(false);
                PFrame.calc.setVisible(false);
                PFrame.equal.setVisible(false);

                PFrame.save_data = PFrame.answer.getText();
                PFrame.answer.setText("PAUSE");

                /*--------------버튼 활성화/비활성화---------------*/
                for (int i = 0; i < 10; i++) {
                    PFrame.setButtonEnabled(PFrame.key_btn[i], false);
                }

                PFrame.setButtonEnabled(PFrame.b_cancel, false);
                PFrame.setButtonEnabled(PFrame.b_submit, false);

                PTimer.thread = null; // 타이머 일시 정지
                PFrame.b_pause.setText("재개"); // 버튼 텍스트를 "재개"로 변경
                PFrame.pause_state = true; // 정지 상태 변수 설정
                return;
            }
            else {
                PTimer.thread = new PTimer.TimerThread();
                PTimer.thread.start();

                /*--------------재개 시 문제가 보이도록 처리---------------*/
                PFrame.op1.setVisible(true);
                PFrame.op2.setVisible(true);
                PFrame.calc.setVisible(true);
                PFrame.equal.setVisible(true);
                PFrame.answer.setText(PFrame.save_data);

                /*--------------버튼 활성화/비활성화---------------*/
                for (int i = 0; i < 10; i++) {
                    PFrame.setButtonEnabled(PFrame.key_btn[i], true);
                }

                PFrame.setButtonEnabled(PFrame.b_cancel, true);
                PFrame.setButtonEnabled(PFrame.b_submit, true);

                PFrame.b_pause.setText("정지"); // 버튼 텍스트를 "정지"로 변경
                PFrame.pause_state = false; // 정지 상태 변수 설정
            }
        }
    }

    static class StartActionListener implements ActionListener { // 시작 이벤트

        public void createRandomNumber() { // 0부터 9까지 중복되지 않는 랜덤 수 생성
            if(PFrame.random_state){
                boolean check = false;

                for(int i = 0; i < 10; i++){

                    while(true){
                        PFrame.ranNum[i] = (int)(Math.random() * 10);
                        check = false;

                        for(int k = 0; k < i; k++){
                            if(PFrame.ranNum[k] == PFrame.ranNum[i]){
                                check = true;
                                break;
                            }
                            else {
                                check = false;
                            }
                        }
                        if(!check) break;
                    }
                }
            }
        }

        public void setSortedNumber() { // 버튼 배열 정렬된 상태로 바꾸기
            for(int i = 0; i < 10; i++){
                PFrame.ranNum[i] = i;
            }
        }

        public void setKeyButton() { // 버튼 텍스트 재설정
            for(int i = 0; i < 10; i++){
                PFrame.key_btn[i].setText(Integer.toString(PFrame.ranNum[i]));
            }
        }

        public void actionPerformed(ActionEvent e) {

            if(!PFrame.start_state) // 게임이 진행중이라면 문제를 새로 만들지 않음
                PFrame.start_state = true;
            else return;

            if(PFrame.random_state) {
                createRandomNumber();
                setKeyButton();
            }
            else {
                setSortedNumber();
                setKeyButton();
            }

            PFrame.op2.setForeground(Color.black); // 정답 표시로 바뀐 색을 원래대로 초기화
            PFrame.op2.setFont(new Font("SansSerif", Font.BOLD, 70));

            PFrame.b_start.setText("다음 문제"); // "시작" 버튼의 텍스트를 "다음 문제"로 변경
            PFrame.b_start.setFont(new Font("SansSerif", Font.BOLD, 25));

            /*--------------버튼 활성화/비활성화---------------*/
            for (int i = 0; i < 10; i++) {
                PFrame.setButtonEnabled(PFrame.key_btn[i], true);
            }

            PFrame.setButtonEnabled(PFrame.b_start, false);
            PFrame.setButtonEnabled(PFrame.b_reset, false);
            PFrame.setButtonEnabled(PFrame.b_submit, true);
            PFrame.setButtonEnabled(PFrame.b_pause, true);
            PFrame.setButtonEnabled(PFrame.b_cancel, true);

            /*--------------난이도 별로 문제 랜덤 생성---------------*/
            JLabel num1 = PFrame.op1;
            JLabel num2 = PFrame.op2;
            JLabel ope_label = PFrame.calc;
            int operator, limit, r1, r2;
            if(PFrame.level == 1) {     // 난이도 쉬움
                limit = 10;
                operator = (int)(Math.random() * 10) % 4;
            }
            else if(PFrame.level == 2){ // 난이도 보통
                limit = 100;
                operator = (int)(Math.random() * 10) % 2;
            }
            else {                      // 난이도 어려움
                limit = 100;
                operator = (int)(Math.random() * 10) % 4;
            }

            r1 = (int)(Math.random() * limit);
            r2 = (int)(Math.random() * limit);
            if(operator == 1){ // 뺄셈인 경우 답이 양수만 나오도록 처리
                while(r1 < r2){
                    r1 = (int)(Math.random() * limit);
                }
            }
            if(operator == 3) { // 나눗셈일 때 0으로 나누는 오류 처리
                while(r2 == 0 || r1 / r2 <= 1){ // 결과가 1보다 크도록 추가 처리(난이도 조절)
                    r1 = (int)(Math.random() * limit);
                    r2 = (int)(Math.random() * limit);
                }
            }

            /*--------------문제 보이기---------------*/
            num1.setText(String.valueOf(r1));
            num2.setText(String.valueOf(r2));
            PFrame.equal.setText("=");
            switch(operator) {
                case 0:
                    ope_label.setText("+");
                    break;
                case 1:
                    ope_label.setText("-");
                    break;
                case 2:
                    ope_label.setText("X");
                    break;
                case 3:
                    ope_label.setText("÷");
                    break;
            }
        }
    }

    static class SubmitActionListener implements ActionListener { // 제출 이벤트

        public void actionPerformed(ActionEvent e) {

            /*--------------답을 안쓰고 제출 버튼 누른 경우 처리---------------*/
            int ans = 0;
            try{ ans = Integer.parseInt(PFrame.answer.getText()); }
            catch(NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "답을 입력하세요"
                        , "ERROR", JOptionPane.WARNING_MESSAGE);
                return;
            }

            /*--------------정답 계산---------------*/
            int n1 = Integer.parseInt(PFrame.op1.getText());
            int n2 = Integer.parseInt(PFrame.op2.getText());
            String op = PFrame.calc.getText();
            int res = 0;
            int score = Integer.parseInt(PFrame.score.getText().substring(7));

            switch(op){
                case "+": res = n1 + n2; break;
                case "-": res = n1 - n2; break;
                case "X": res = n1 * n2; break;
                case "÷": res = n1 / n2; break;
            }

            /*--------------제출한 답이 정답/오답인 경우 결과 처리---------------*/
            if(ans == res) {
                score++;
                PFrame.op2.setText("O");
                PFrame.op2.setForeground(Color.blue);
            }
            else {
                if(score != 0) score--; // 점수가 0 아래로는 내려가지 않게 한다.
                PFrame.op2.setText("X");
                PFrame.op2.setForeground(Color.red);
            }
            PFrame.score.setText("SCORE: " + Integer.toString(score));

            /*--------------문제 초기화---------------*/
            PFrame.op1.setText(" ");
            PFrame.calc.setText(" ");
            PFrame.equal.setText(" ");
            PFrame.answer.setText("");

            PFrame.start_state = false;
            PTimer.thread = null; // 타이머 정지

            /*--------------버튼 활성화/비활성화---------------*/
            for (int i = 0; i < 10; i++) {
                PFrame.setButtonEnabled(PFrame.key_btn[i], false);
            }

            PFrame.setButtonEnabled(PFrame.b_start, true);
            PFrame.setButtonEnabled(PFrame.b_pause, false);
            PFrame.setButtonEnabled(PFrame.b_submit, false);
            PFrame.setButtonEnabled(PFrame.b_cancel, false);
            PFrame.setButtonEnabled(PFrame.b_reset, true);
        }
    }
}

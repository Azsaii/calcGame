package project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import project.PFrame;

import javax.swing.*;

public class PTimer {
    static Thread thread;

    static public void initTimer(){
        /*--------------타이머 초기화---------------*/
        if(PFrame.level == 1){
            PFrame.label_second.setText("10");
        } else if(PFrame.level == 2){
            PFrame.label_second.setText("13");
        } else {
            PFrame.label_second.setText("15");
        }
        PFrame.label_msec.setText("0");
    }

    static class timerActionListener implements ActionListener { // 시작 시 타이머 잔향 이벤트

        @Override
        public void actionPerformed(ActionEvent e) {
            int sec = Integer.parseInt(PFrame.label_second.getText());
            int msec = Integer.parseInt(PFrame.label_msec.getText());

            initTimer(); // 타이머 초기화

            thread = new TimerThread();
            thread.start();
        }
    }

    static class TimerThread extends Thread { // 타이머에 사용되는 스레드

        public void run(){

            while(thread == currentThread()){

                try{
                    Thread.sleep(100);

                    JLabel sec = PFrame.label_second;
                    JLabel msec = PFrame.label_msec;

                    int ss = Integer.parseInt(sec.getText());
                    int ms = Integer.parseInt(msec.getText());

                    if(ms == 0){
                        ss--;
                        sec.setText(Integer.toString(ss));
                        ms = 9;
                        msec.setText(Integer.toString(ms));
                    }
                    else {
                        ms--;
                        msec.setText(Integer.toString(ms));
                        if(ms == 0 && ss == 0) {
                            /*--------------타임 오버 표시---------------*/
                            PFrame.time_over = true;
                            PFrame.op1.setText(" ");
                            PFrame.calc.setText(" ");
                            PFrame.op2.setText("TIME OVER!"); PFrame.op2.setForeground(Color.red);
                            PFrame.op2.setFont(new Font("SansSerif", Font.BOLD, 30));
                            PFrame.equal.setText(" ");
                            PFrame.answer.setText("");

                            /*--------------버튼 활성화/비활성화---------------*/
                            for (int i = 0; i < 10; i++) {
                                PFrame.key_btn[i].setEnabled(false);
                                PFrame.key_btn[i].setBackground(Color.DARK_GRAY);
                            }

                            PFrame.b_reset.setEnabled(true); PFrame.b_reset.setBackground(Color.PINK);
                            PFrame.b_start.setEnabled(true); PFrame.b_start.setBackground(Color.PINK);
                            PFrame.b_cancel.setEnabled(false); PFrame.b_cancel.setBackground(Color.DARK_GRAY);
                            PFrame.b_pause.setEnabled(false); PFrame.b_pause.setBackground(Color.DARK_GRAY);
                            PFrame.b_submit.setEnabled(false); PFrame.b_submit.setBackground(Color.DARK_GRAY);

                            initTimer(); // 타이머 초기화

                            PFrame.start_state = false;
                            thread = null;
                        }
                    }
                }
                catch(InterruptedException e1) {return;}
            }

        }
    }
}

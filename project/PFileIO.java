package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class PFileIO {

    static String srcFileName = System.getProperty("user.dir") + "\\src\\project\\" + "score.txt";

    static class checkActionListener implements ActionListener { // 점수 확인 메뉴 처리

        public void actionPerformed(ActionEvent e) {

            FileInputStream fis = null;
            DataInputStream dis = null;

            int data = 0;

            try{
                fis = new FileInputStream(srcFileName);
                dis = new DataInputStream(fis);
                data = dis.read();
            }

            catch (IOException ie) { ie.printStackTrace(); }

            finally {
                try{
                    if(dis != null) dis.close();
                    if(fis != null) fis.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            String str = "현재 최고점수는 " + (data - 48) + "점 입니다.";
            JOptionPane.showMessageDialog(null, str
                    , "High score", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static class FileActionListener implements ActionListener { // 저장 메뉴 처리

        public void actionPerformed(ActionEvent e) {

            int score = Integer.parseInt(PFrame.score.getText().substring(7));

            FileInputStream fis = null;
            DataInputStream dis = null;
            FileOutputStream fos = null;
            DataOutputStream dos = null;

            try {
                fis = new FileInputStream(srcFileName);
                dis = new DataInputStream(fis);

                int data = 0;
                String highScore = "";

                while(true) { // 최고점수 읽기
                    data = dis.read();
                    if (data == -1) break;
                    highScore = highScore.concat(Integer.toString(data - 48));
                }

                if(highScore.equals("")) highScore = "0"; // srcFileName가 비어있으면 최고점수는 0으로 초기화

                if(score > Integer.parseInt(highScore)){ // 최고점수 업데이트
                    highScore = Integer.toString(score);
                    String str = "최고 점수가 저장되었습니다.\n현재 최고점수는 " + highScore + "점 입니다.";
                    JOptionPane.showMessageDialog(null, str
                            , "SAVE", JOptionPane.INFORMATION_MESSAGE);
                }
                else { // 현재 점수가 최고 점수보다 크지 않으면 저장 안함
                    String str = "최고 점수만 저장가능합니다!\n현재 최고점수는 " + highScore + "점 입니다.";
                    JOptionPane.showMessageDialog(null, str
                            , "SAVE", JOptionPane.ERROR_MESSAGE);
                }

                fos = new FileOutputStream(srcFileName);
                dos = new DataOutputStream(fos);

                dos.writeBytes(highScore);
                PFrame.h_score = Integer.parseInt(highScore);
                System.out.println("현재 최고점수는 " + highScore + "점 입니다.");

            }

            catch (IOException ie) { ie.printStackTrace(); }

            finally {
                try{
                    if(dos != null) dos.close();
                    if(fos != null) fos.close();
                    if(dis != null) dis.close();
                    if(fis != null) fis.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    static class ClearActionListener implements ActionListener { // 최고 점수 초기화

        public void actionPerformed(ActionEvent e) {
            String srcFileName = System.getProperty("user.dir") + "\\src\\project\\" + "score.txt";
            FileOutputStream fos = null;
            DataOutputStream dos = null;

            try {
                fos = new FileOutputStream(srcFileName);
                dos = new DataOutputStream(fos);

                dos.writeBytes("0");

                String str = "최고 점수가 초기화되었습니다.";
                JOptionPane.showMessageDialog(null, str
                        , "CLEAR", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("최고 점수가 초기화되었습니다.");
            }

            catch(IOException fe) { fe.printStackTrace(); }

            finally {
                try{
                    if(dos != null) dos.close();
                    if(fos != null) fos.close();
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
            }
        }
    }
}

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.swing.*;
import java.util.Random;

class mypanels implements ActionListener{
    JFrame frame2 = new JFrame();
    JButton start= new JButton();
    JButton exit= new JButton();
    JLabel backround_label= new JLabel();
    JPanel heading_panel= new JPanel();
    JLabel img_label;
    JLabel back_label;
    ImageIcon img = new ImageIcon("Snake.gif");
    ImageIcon img2 = new ImageIcon("Snake2.png");

    mypanels(){
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setLocationRelativeTo(null);
        frame2.setSize(700,700);
        frame2.setTitle("TIC-TAC-TOE");
        frame2.setLayout(null);
        Image icon = Toolkit.getDefaultToolkit().getImage("tic-tac-toe.png");  
        frame2.setIconImage(icon);
        frame2.getContentPane().setBackground(Color.black);

        
        img_label = new JLabel();
        img_label.setIcon(img);
        img_label.setBackground(Color.black);
        img_label.setBounds(80, 20, 600, 80);
        img_label.setVerticalAlignment(JLabel.CENTER);

        back_label = new JLabel();
        back_label.setIcon(img2);
        back_label.setBounds(40, 150, 550, 200);

        exit.setBounds(380, 450, 150, 50);
        exit.setBackground(Color.red);
        exit.setForeground(Color.white);
        exit.setText("EXIT");
        exit.setFocusable(false);
        exit.setFont(new Font("Britannic Bold",Font.PLAIN,30));

        start.setBounds(140, 450, 150, 50);
        start.setBackground(new Color(25,255,0));
        start.setForeground(Color.white);
        start.setText("START");
        start.setFocusable(false);
        start.setFont(new Font("Britannic Bold",Font.PLAIN,30));   
        
        start.addActionListener(this);
        exit.addActionListener(this);
        
        frame2.add(img_label);
        frame2.add(back_label);
        frame2.add(start);
        frame2.add(exit);
        frame2.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==exit){
            System.exit(0);
        }
        if(e.getSource()==start){
           frame2.dispose();
           new Gameframe();
        }
        
        
    }

}

class Gamepanel extends JPanel implements ActionListener{
    final static int SCREEN_WIDTH = 600;
    final static int SCREEN_HEIGHT = 575;
    final static int UNIT_SIZE = 25;
    final static int delay = 90;
    final static int GAME_UNIT = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    final int x[] = new int[GAME_UNIT];
    final int y[] = new int[GAME_UNIT];
    int appleX;
    int appleY;
    int apple_eaten=0;
    char direction = 'D';
    int bodyparts = 6;
    boolean running = false;
    Random random = new Random();
    Timer timer = new Timer(delay,this);
    String soundName = "eh.wav"; 
    String soundName2= "gameover.wav";   
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
    Clip clip = AudioSystem.getClip();

       
    AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(new File(soundName2).getAbsoluteFile());
    Clip clip2 = AudioSystem.getClip();

    Gamepanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        startGame();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.addKeyListener(new mykeyadapter());
        this.setFocusable(true);
        clip.open(audioInputStream);
        clip2.open(audioInputStream2);
    }

    public void move(){
        for(int i=bodyparts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction){
            case 'W' : y[0]=y[0]-UNIT_SIZE;
            break;
            case 'S' : y[0]=y[0]+UNIT_SIZE;
            break;
            case 'A' : x[0]=x[0]-UNIT_SIZE;
            break;
            case 'D' : x[0]=x[0]+UNIT_SIZE;
            break;
        }
    }
    public void startGame(){
        newApple();
        running=true;
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        if(running){
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,45));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: "+apple_eaten,(SCREEN_WIDTH-metrics2.stringWidth("Score: "+apple_eaten))/2,g.getFont().getSize());
            /////
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for(int i=0;i<bodyparts;i++){
                if(i==0){
                    g.setColor(new Color(0,100,0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(0,128,0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        else{
            gameover(g);
        }
    }
    public void newApple(){
        clip.setMicrosecondPosition(220000);
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }
    public void checkApple(){
        if(x[0]==appleX && y[0]==appleY){
            clip.setMicrosecondPosition(200000);
            clip.start();
            bodyparts++;
            apple_eaten++;
            newApple();
        }
    }
    public void checkCollision(){
        for(int i=bodyparts;i>0;i--){
            if((x[0]==x[i]) && (y[0]==y[i])){
                running=false;
            }
        }
            if(x[0]<0){
                running=false;
            }
            if(x[0]>SCREEN_WIDTH){
                running=false;
            }
            if(y[0]<0){
                running=false;
            }
            if(y[0]>SCREEN_HEIGHT){
                running=false;
            }
        
            if(!running){
                timer.stop();
            }
    }
    public void gameover(Graphics g){
        clip2.start();
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,45));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+apple_eaten,(SCREEN_WIDTH-metrics2.stringWidth("Score: "+apple_eaten))/2,g.getFont().getSize());
        //GAme Over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics1.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
        
    }
    class mykeyadapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyChar()){
                case 'W' : direction='W';
                break;
                case 'w' : direction='W';
                break;
                case 'A' : direction='A';
                break;
                case 'a' : direction='A';
                break;
                case 'S' : direction='S';
                break;
                case 's' : direction='S';
                break;
                case 'D' : direction='D';
                break;
                case 'd' : direction='D';
                break;
            }
        }
    }
    
}
class Gameframe extends JFrame{
    Gameframe(){
        try {
            this.add(new Gamepanel());
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setTitle("Snake Game");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

public class SnakeGame {
    public static void main(String[] args) {
        new mypanels();
    }
}
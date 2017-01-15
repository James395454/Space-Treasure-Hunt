/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.swing.ImageIcon;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jawes
 */
public class GameApplet extends Applet implements KeyListener, Runnable, ActionListener {

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    private Thread clockThread = null;

    @Override
    public void start() {
        if (clockThread == null) {
            clockThread = new Thread(this, "Clock");
            clockThread.start();
        }
    }
    BufferedImage bf;
    Image spaceshipimg, staticobstacle, background, movingobstacle, treasure;
    Spaceship spaceship;
    int dx, dy, StaticObstacleSize, MovingObstacleSize, div, lives, successfulattempts, difficulty;
    StaticObstacle StaticObstacle[];
    MovingObstacle MovingObstacle[];
    Treasure Treasure;
    double counter, counter2;
    ImageIcon I, lost, won, start;
    //int spaceshipx = 0, spaceshipy = 300;
    boolean movedown, carrytreasure, gameover, win, mybutton, selected;
    Button easy, medium, hard;
    Font myFont;

    @Override
    public void init() {
        resize(1920, 953);

        /*BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("the-file-name.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line[] = new String[4];
            
            try {
                MovingObstacleSize = Integer.parseInt(br.readLine());
                MovingObstacle = new MovingObstacle[MovingObstacleSize];
             

                   
                        for (int j = 0; j < 4; j++) {
                            line[j] = br.readLine();
                        
                        Treasure = new Treasure(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]));
                    } 
                        for (int l = 0; l < MovingObstacleSize; l++) {
                            for (int k = 0; k < 4; k++) {
                                line[k] = br.readLine();
                            }
                            MovingObstacle[l] = new MovingObstacle(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]));
                        }
                    
                        spaceship = new Spaceship(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]));
                     else if (i == 3) {
                        for (int l = 0; l < MovingObstacleSize; l++) {

                            line[l] = br.readLine();
                            MovingObstacle[l].dy = Integer.parseInt(line[l]);
                        }
                    }
                
            } catch (IOException ex) {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        selected = false;
        easy = new Button("Easy");
        medium = new Button("Medium");
        hard = new Button("Hard");
        dx = 30;
        dy = 30;
        StaticObstacleSize = 77;
        spaceship = new Spaceship(0, 300, 100, 100);
        difficulty = 0;
        Treasure = new Treasure(1810, 300, spaceship.width, spaceship.height);
        StaticObstacle = new StaticObstacle[StaticObstacleSize];
        // setFocusable(true);
        //setFocusTraversalKeysEnabled(false);
        lives = 3;
        successfulattempts = 0;
        counter = 3;
        I = new ImageIcon("back2.jpg");
        start = new ImageIcon("start.jpg");
        lost = new ImageIcon("lost.jpg");
        won = new ImageIcon("won.jpg");

        easy.setActionCommand(
                "Easy");
        medium.setActionCommand(
                "Medium");
        hard.setActionCommand(
                "Hard");
        easy.addActionListener(
                this);
        medium.addActionListener(
                this);
        hard.addActionListener(
                this);
        myFont = new Font("TimesRoman", Font.BOLD, 30);
        movedown = true;

        addKeyListener(
                this);
        add(easy);

        add(medium);

        add(hard);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        spaceshipimg = toolkit.getImage("spaceship (2).jpg");
        staticobstacle = toolkit.getImage("StaticObstacle.jpg");
        movingobstacle = toolkit.getImage("asteroid (2).jpg");
        treasure = toolkit.getImage("treasure.jpg");
        carrytreasure = false;
        gameover = false;
        win = false;
        mybutton = true;
        int x = 0, y = 0;
        //setBackground(Color.red);
        for (int i = 0;i < StaticObstacleSize - 8; i++) {
            StaticObstacle[i] = new StaticObstacle(x, y, 50, 50);
            if (y + 50 != spaceship.y) {
                y += 50;
            } else {
                y += 150;
            }
            if (i == 17) {
                x = 50;
                y = 0;
            } else if (i == 34) {
                x = 1820;
                y = 0;
            } else if (i == 51) {
                x = 1870;
                y = 0;
            }

        }

        y = 0;
        x = div;

    }

    public void moveobstacles() {
        for (int i = 0; i < MovingObstacleSize; i++) {
            if (MovingObstacle[i].y + MovingObstacle[i].height + MovingObstacle[i].dy <= this.getHeight()) {
                MovingObstacle[i].dy *= -1;
            } else {
                MovingObstacle[i].y = this.getHeight();
            }
            if (MovingObstacle[i].y - MovingObstacle[i].dy >= 0) {
                MovingObstacle[i].dy *= -1;
            } else {
                MovingObstacle[i].y = 0;
            }

            MovingObstacle[i].y += MovingObstacle[i].dy;

        }
    }

    void update() {

        if (lives == 0) {
            gameover = true;
        } else if (successfulattempts == 5) {
            gameover = true;
            win = true;
        } else {
            moveobstacles();
            movingcollision();
            if ((carrytreasure) && (spaceship.x == 0) && (spaceship.y == 300) && (counter2 == 0) && (counter == 0)) {
                successfulattempts++;
                counter = 3;
                carrytreasure = false;
            }
            if (counter2 - 0.05 < 0) {
                counter2 = 0;
            }
            if ((counter2 > 0) && (spaceship.x == 0) && (spaceship.y == 300)) {
                counter2 -= 0.05;

            } /*else {
             counter2 = 3 - counter;
             }*/

        }
        /*if (gameover) {
         resize(1920, 953);
         } */
        repaint();
    }

    void save() {

        PrintWriter writer;
        try {
            writer = new PrintWriter("the-file-name.txt", "UTF-8");
            writer.println(MovingObstacleSize);
            writer.println(Treasure.x);
            writer.println(Treasure.y);
            writer.println(Treasure.width);
            writer.println(Treasure.height);

            for (int i = 0; i < MovingObstacleSize; i++) {
                writer.println(MovingObstacle[i].x);
                writer.println(MovingObstacle[i].y);
                writer.println(MovingObstacle[i].width);
                writer.println(MovingObstacle[i].height);
            }
            writer.println(spaceship.x);
            writer.println(spaceship.y);
            writer.println(spaceship.width);
            writer.println(spaceship.height);

            for (int i = 0; i < MovingObstacleSize; i++) {
                writer.println(MovingObstacle[i].dy);
            }
            writer.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameApplet.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GameApplet.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
        };

    }

    void noflickering(Graphics g) {
        //super.paint(g);
        if ((gameover) && (win)) {
            g.drawImage(won.getImage(), 0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight(), this);

        } else if ((gameover) && (!win)) {
            g.drawImage(lost.getImage(), 0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight(), this);

        } else {
            if (!selected) {
                g.drawImage(start.getImage(), 0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight(), this);

            } else {
                g.drawImage(I.getImage(), 0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight(), this);
                g.drawImage(spaceshipimg, spaceship.x, spaceship.y, spaceship.width, spaceship.height, this);
                if ((spaceship.x < Treasure.x) && (counter > 0)) {
                    g.drawImage(treasure, Treasure.x, Treasure.y, Treasure.width / (int) (4 - counter), Treasure.height / (int) (4 - counter), this);
                }
                for (int i = 0; i < StaticObstacleSize; i++) {
                    g.drawImage(staticobstacle, StaticObstacle[i].x, StaticObstacle[i].y, StaticObstacle[i].width, StaticObstacle[i].height, this);
                }
                for (int i = 0; i < MovingObstacleSize; i++) {
                    g.drawImage(movingobstacle, MovingObstacle[i].x, MovingObstacle[i].y, MovingObstacle[i].width, MovingObstacle[i].height, this);
                }
                g.setColor(Color.red);
                counter = new BigDecimal(counter).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                counter2 = new BigDecimal(counter2).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

                g.setFont(myFont);

                g.drawString("Time to fully collect Treasure: " + Double.toString(counter), 1350, 20);
                g.drawString("Lives: " + Integer.toString(lives), 120, 20);
                g.drawString("Time to unload Treasure: " + Double.toString(counter2), 300, 20);
                g.drawString("Successful Attempts: " + Integer.toString(successfulattempts), 900, 20);
                //g.drawString("Successful Attempts: " + Integer.toString((int) getBounds().getWidth()), 1300, 10);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        bf = new BufferedImage((int) getBounds().getWidth(), (int) getBounds().getHeight(), BufferedImage.TYPE_INT_RGB);
        try {
            noflickering(bf.getGraphics());
            g.drawImage(bf, 0, 0, null);
        } catch (Exception ex) {

        }

    }

    void movingcollision() {
        boolean collide = false;
        for (int i = 0; i < MovingObstacleSize; i++) {
            if ((spaceship.x + spaceship.width > MovingObstacle[i].x) && (spaceship.x + spaceship.width <= MovingObstacle[i].x + MovingObstacle[i].width) && (((spaceship.y >= MovingObstacle[i].y) && (spaceship.y < MovingObstacle[i].y + MovingObstacle[i].height)) || ((spaceship.y < MovingObstacle[i].y) && (spaceship.y + spaceship.height >= MovingObstacle[i].y)))) {
                collide = true;
            }

            if ((spaceship.x < MovingObstacle[i].x + MovingObstacle[i].width) && (spaceship.x >= MovingObstacle[i].x) && (((spaceship.y >= MovingObstacle[i].y) && (spaceship.y < MovingObstacle[i].y + MovingObstacle[i].height)) || ((spaceship.y < MovingObstacle[i].y) && (spaceship.y + spaceship.height > MovingObstacle[i].y)))) {
                collide = true;
            }
            if ((spaceship.y < MovingObstacle[i].y + MovingObstacle[i].height) && (spaceship.y >= MovingObstacle[i].y) && (((spaceship.x >= MovingObstacle[i].x) && (spaceship.x < MovingObstacle[i].x + MovingObstacle[i].width)) || ((spaceship.x < MovingObstacle[i].x) && (spaceship.x + spaceship.width > MovingObstacle[i].x)))) {
                collide = true;
            }
            if ((spaceship.y + spaceship.height > MovingObstacle[i].y) && (spaceship.y + spaceship.height <= MovingObstacle[i].y + MovingObstacle[i].height) && (((spaceship.x >= MovingObstacle[i].x) && (spaceship.x < MovingObstacle[i].x + MovingObstacle[i].width)) || ((spaceship.x < MovingObstacle[i].x) && (spaceship.x + spaceship.width > MovingObstacle[i].x)))) {
                collide = true;
            }
        }
        if (collide) {
            spaceship = new Spaceship(0, 300, 100, 100);
            counter2 = 0;
            lives--;
            counter = 3;

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (selected) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                boolean move = true;
                //static obstacle collision prevention
                for (int i = 0; i < StaticObstacleSize; i++) {
                    if ((spaceship.x + spaceship.width + dx > StaticObstacle[i].x) && (spaceship.x + spaceship.width + dx <= StaticObstacle[i].x + StaticObstacle[i].width) && (((spaceship.y >= StaticObstacle[i].y) && (spaceship.y < StaticObstacle[i].y + StaticObstacle[i].height)) || ((spaceship.y < StaticObstacle[i].y) && (spaceship.y + spaceship.height > StaticObstacle[i].y)))) {
                        move = false;
                    }
                }

                if (move) {
                    Toolkit toolkit = Toolkit.getDefaultToolkit();

                    if (spaceshipimg != toolkit.getImage("spaceship (2).jpg")) {
                        spaceshipimg = toolkit.getImage("spaceship (2).jpg");
                    }
                    if (spaceship.x + spaceship.width + dx < this.getWidth()) {
                        spaceship.x += dx;
                    } else {
                        spaceship.x += this.getWidth() - (spaceship.x + spaceship.width);
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

                boolean move = true;
                //static obstacle collision prevention
                for (int i = 0; i < StaticObstacleSize; i++) {
                    if ((spaceship.x - dx < StaticObstacle[i].x + StaticObstacle[i].width) && (spaceship.x - dx >= StaticObstacle[i].x) && (((spaceship.y >= StaticObstacle[i].y) && (spaceship.y < StaticObstacle[i].y + StaticObstacle[i].height)) || ((spaceship.y < StaticObstacle[i].y) && (spaceship.y + spaceship.height > StaticObstacle[i].y)))) {
                        move = false;
                    }
                }
                if (move) {
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    if (spaceshipimg != toolkit.getImage("spaceship (2)left.jpg")) {
                        spaceshipimg = toolkit.getImage("spaceship (2)left.jpg");
                    }
                    if (spaceship.x - dx >= 0) {
                        spaceship.x -= dx;
                    } else {
                        spaceship.x = 0;
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {

                boolean move = true;
                //static obstacle collision prevention
                for (int i = 0; i < StaticObstacleSize; i++) {
                    if ((spaceship.y - dy < StaticObstacle[i].y + StaticObstacle[i].height) && (spaceship.y - dy >= StaticObstacle[i].y) && (((spaceship.x >= StaticObstacle[i].x) && (spaceship.x < StaticObstacle[i].x + StaticObstacle[i].width)) || ((spaceship.x < StaticObstacle[i].x) && (spaceship.x + spaceship.width > StaticObstacle[i].x)))) {
                        move = false;
                    }
                }
                if (move) {
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    if (spaceshipimg != toolkit.getImage("spaceship (2)up.jpg")) {
                        spaceshipimg = toolkit.getImage("spaceship (2)up.jpg");
                    }
                    if (spaceship.y - dy >= 0) {
                        spaceship.y -= dy;
                    }

                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                boolean move = true;
                //static obstacle collision prevention
                for (int i = 0; i < StaticObstacleSize; i++) {
                    if ((spaceship.y + spaceship.height + dy > StaticObstacle[i].y) && (spaceship.y + spaceship.height + dy <= StaticObstacle[i].y + StaticObstacle[i].height) && (((spaceship.x >= StaticObstacle[i].x) && (spaceship.x < StaticObstacle[i].x + StaticObstacle[i].width)) || ((spaceship.x < StaticObstacle[i].x) && (spaceship.x + spaceship.width > StaticObstacle[i].x)))) {
                        move = false;
                    }
                }
                if (move) {
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    if (spaceshipimg != toolkit.getImage("spaceship (2)down.jpg")) {
                        spaceshipimg = toolkit.getImage("spaceship (2)down.jpg");
                    }
                    if (spaceship.y + spaceship.width + dy < this.getHeight()) {
                        spaceship.y += dy;
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                save();
                System.out.print("hehe");

            }
        }
    }

    // TODO overwrite start(), stop() and destroy() methods
    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {

        Thread myThread = Thread.currentThread();
        while (clockThread == myThread) {
            update();
            if (counter - 0.05 < 0) {
                counter = 0;
            }
            if ((counter > 0) && (spaceship.x >= Treasure.x) && (spaceship.y == Treasure.y)) {
                counter -= 0.05;
                counter2 += 0.05;
                carrytreasure = true;
            }

            try {
                if (!gameover) {
                    Thread.sleep(50);

                } else {
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {

            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Easy")) {
            difficulty = 1;
            MovingObstacleSize = 4;
        } else if (cmd.equals("Medium")) {
            difficulty = 2;
            MovingObstacleSize = 5;
        } else {
            difficulty = 3;
            MovingObstacleSize = 8;
        }
        remove(easy);
        remove(medium);
        remove(hard);
        selected = true;
        MovingObstacle = new MovingObstacle[MovingObstacleSize];
        if (difficulty == 3) {
            div = 1820 / ((MovingObstacleSize / 2) + 1);
        } else {
            div = 1820 / (MovingObstacleSize + 1);
        }
        int x = div, y = 0;
        if (difficulty != 3) {
            for (int i = 0; i < MovingObstacleSize; i++) {

                MovingObstacle[i] = new MovingObstacle(x, y, 50, 50);
                x += div;
            }
        } else {
            for (int i = 0; i < MovingObstacleSize / 2; i++) {

                MovingObstacle[i] = new MovingObstacle(x, y, 50, 50);
                x += div;
            }
            x = div + div / 2;
            y = this.getWidth() - 50;
            for (int i = MovingObstacleSize / 2; i < MovingObstacleSize; i++) {
                MovingObstacle[i] = new MovingObstacle(x, y, 50, 50);
                x += div;
            }
        }
        x = div + 80;
        y = 200;
        int j = 1;
        for (int i = StaticObstacleSize - 8; i < StaticObstacleSize; i++) {
            if (j % 2 == 0) {
                if (y == 200) {
                    y = 700;
                } else {
                    y = 200;
                }
            }
            StaticObstacle[i] = new StaticObstacle(x, y, 50, 50);
            if (j % 2 == 0) {
                x += div;
            }

            j++;
        }

    }

}

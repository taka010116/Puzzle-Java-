import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Puzzle extends JPanel {
    ArrayList<Figure> figs = new ArrayList<Figure>();
	ArrayList<Rect> rects = new ArrayList<Rect>();
    int blockSize = 30;
	int currentNumber = 0;
	boolean gameOver = false;
	int counter = 9;
	int stage = 1;
	int counterClick = 0;
	double tm = 0;
	JButton resetButton = new JButton("Reset");
	long tm0 = 0;
	int recordScore = 0;
	double recordTime = 0.0;
	
	Text t1 = new Text(20, 20, "Stage : 1", new Font("Arial", Font.BOLD, 15));
	Text t2 = new Text(120, 20, "Point : 0", new Font("Arial", Font.BOLD, 15));
	Text time = new Text(200, 20, "time : 0", new Font("Arial", Font.BOLD, 15));
	Text record = new Text(190, 100, "max score :", new Font("Arial", Font.BOLD, 15));
	
	int [][] number = {
		{1, 2, 3},
		{4, 5, 6},
		{7, 8, 9},
	};
		
	int [][] number1 = {
		{1, 2, 3},
		{4, 5, 6},
		{7, 8, 9},
	};
	
    int [][] number2 = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12},
        {13, 14, 15, 16},
    };
	
	int [][] number3 = {
		{1, 2, 3, 4, 5},
		{6, 7, 8, 9, 10},
		{11, 12, 13, 14, 15},
		{16, 17, 18, 19, 20},
		{21, 22, 23, 24, 25},
	};
	
    public Puzzle() {
    	setLayout(null);
    	figs.add(t1);
    	figs.add(t2);
    	figs.add(time);
    	figs.add(record);
    	add(resetButton);
    	resetButton.setBounds(190, 30, 70, 30);
    	randomNumber();
    	paintField();
    	tm0 = System.currentTimeMillis();
		javax.swing.Timer timer = new javax.swing.Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tm = 0.001 * (System.currentTimeMillis() - tm0);
				time.setText("Time :" + String.format("%4.2f", tm));
				repaint();
			}
		});
    	timer.start();
    	
    	resetButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent evt){
    			timer.stop();
    			resetGame();
    			tm0 = System.currentTimeMillis();
    			timer.restart();
    		}
    	});
    	
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                for (Rect r : rects) {
                    if (r.hit(evt.getX(), evt.getY())) {
                        int x = (r.getX() - 30) / blockSize;
                        int y = (r.getY() - 30) / blockSize;
                        int clickedNumber = number[y][x];
                    	if( clickedNumber == currentNumber + 1 ){
                    		currentNumber++;
                    		r.setColor(Color.GREEN);
                    		counterClick++;
                    	}else{
                    		gameOver = true;
                    		r.setColor(Color.RED);
                    		t1.setText("Game Over!");
                    		timer.stop();
                    		if( counterClick >= recordScore ){
                    			recordScore = counterClick;
                    		}
                    		record.setText("max score :" + recordScore);
                    		
                    	}
                    	if( currentNumber == counter ){
                    		if( stage == 1 ){
                    			counter = 16;
                    			number = number2;
                    			paintField();
                    			randomNumber();
                    			currentNumber = 0;
                    			stage++;
                    			t1.setText("Stage :" + stage);
                    		}else if (stage == 2 ){
                    			counter = 25;
                    			number = number3;
                    			paintField();
                    			randomNumber();
                    			currentNumber = 0;
                    			stage++;
                    			t1.setText("Stage :" + stage);
                    		}else{
                    			t1.setText("Game Clear!");
                    			timer.stop();
                    			if( tm >= recordTime ){
                    			recordTime = tm;
                    			}
                    			
                    			record.setText("max time: " + String.format("%4.2f", recordTime));
                    		}
                    	}
                    	t2.setText("Point :" + counterClick);
                        repaint();
                        break;
                    }
                }
            }
        });
    }

	public void paintField(){
		rects.clear();
		
		for (int y = 0; y < number.length; y++) {
            for (int x = 0; x < number[y].length; x++) {
                int fx = x * blockSize + 30;
                int fy = y * blockSize + 30;
                Rect rect = new Rect(Color.WHITE, fx, fy, blockSize, blockSize);
                rects.add(rect);
            	repaint();
            }
        }
	}
	
	public void resetGame() {
        tm = 0;
        currentNumber = 0;
        gameOver = false;
        counter = 9;
        stage = 1;
        counterClick = 0;
        t1.setText("Stage : 1");
        t2.setText("Point : 0");
        time.setText("Time : 0");
		number = number1;
        randomNumber();
        paintField();
		
    }
	
	public void randomNumber(){
		for( int i = 0; i < number.length; i++ ){
			for( int j = 0; j < number[i].length; j++ ){
				int random1 = (int)(Math.random()*(i+1));
				int random2 = (int)(Math.random()*(j+1));
				int tmp = number[i][j];
				number[i][j] = number[random1][random2];
				number[random1][random2] = tmp;
			}
		}
	}
	
	public void CountNumber(){
		for( int i = 0; i < number.length; i++ ){
    		for( int j = 0; j < number[i].length; j++ ){
    			counter++;
    		}
    	}
	}
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
    	
        for (Figure r : figs) {
            r.draw(g);
        }
    	for( Rect r : rects ){
    		r.draw(g);
    	}

        for (int y = 0; y < number.length; y++) {
            for (int x = 0; x < number[y].length; x++) {
                int fx = x * blockSize + 40;
                int fy = y * blockSize + 50;
                    g.setColor(Color.BLACK);
                    String stringX = String.valueOf(number[y][x]);
                    g.drawString(stringX, fx, fy);
            }
        }
    }

    public static void main(String[] args) {
        JFrame app = new JFrame();
        app.add(new Puzzle());
        app.setSize(350, 250);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

	interface Figure {
		public void draw(Graphics g);
	}
	
	static class Text implements Figure {
		int xpos, ypos;
		String txt;
		Font fn;

		public Text(int x, int y, String t, Font f) {
			xpos = x;
			ypos = y;
			txt = t;
			fn = f;
		}

		public void setText(String t) {
			txt = t;
		}

		public void draw(Graphics g) {
			g.setColor(Color.BLACK);
			g.setFont(fn);
			g.drawString(txt, xpos, ypos);
		}
	}
	
    static class Rect implements Figure {
        Color col;
        int xpos, ypos, width, height;

        public Rect(Color c, int x, int y, int w, int h) {
            col = c;
            xpos = x;
            ypos = y;
            width = w;
            height = h;
        }

        public boolean hit(int x, int y) {
            return x >= xpos && x <= xpos + width && y >= ypos && y <= ypos + height;
        }

        public void draw(Graphics g) {
            g.setColor(col);
            g.fillRect(xpos, ypos, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(xpos, ypos, width, height);
        }

        public void setColor(Color c) {
            col = c;
        }

        public int getX() {
            return xpos;
        }

        public int getY() {
            return ypos;
        }
    }
}
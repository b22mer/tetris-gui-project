package b22mer.project;
//�̸�: ����ö, �а�: ��ǻ�Ͱ��а� ,�й�:12162149
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.Timer;


import javax.swing.JLabel;
import javax.swing.JPanel;

public class tBoard extends JPanel implements KeyListener {

	// ���� ���¸� ��Ÿ���� ��Ÿ���� ������̴�. ������ ���¸� Ư�� ���ڷ� ���������ν� ������ ��Ȳ ������ �����ϴ�.
	public static int STATE_GAME_END = 0;
	public static int STATE_GAME_STOP = 1;
	public static int STATE_GAME_PLAYING = 2;
	private int state = STATE_GAME_PLAYING;
	
	
	// ������ �����ϴ� ������ ����� �������� ������̴�
	public static final int SET_tBOARD_HEI = 20; // ���� �� â ���̸� ����� ����
	public static final int SET_tBOARD_WID = 10; // ���� �� â �ʺ� ����� ����
	public static final int SET_tSPACE_SIZE = 30; // ������ ĭ�� 30���� ������ ����� ����
	
	
	// �⺻������ ����� �ڵ����� �����ϴ� �ð��� �����ص� �����
	private static int SET_DELAYTIME = 60 / 1000; // ������ Ÿ�� ����

	
	// ĭ�� �����ö� ���� ��� ���� �ٽ� ��ĥ�ϴ� ����̱⿡ ���Ӻ����� ���̿� �°� �����Ѵ�
	private Color[][] board = new Color[SET_tBOARD_HEI][SET_tBOARD_WID];


	private Random random; // ���ӳ����� ��Ʈ���� ������ �������� ������ ó���ϱ� ���� ���� �������
	private int x = 3, y = 0; // ������ x,y�� ��ǥ������� �����̵Ǳ⿡, �ʱ� ����ġ ��ǥ�� �������ش�.

	private int CHANGE_X; // Ű���� �̺�Ʈ�� ���� �¿� �̵��� (x��ǥ �̵�) ���� ������
	private boolean BUMPER = false; // BUMPER�� ���Ͽ� ���õ� �浹�� ���õ� boolean�̴�. ���� ���� ���θ� �¿��Ѵ�.

	private Timer dropping; //���� �ð� �ֱ⿡ ���� ���� �ݺ��� �ϰԵǴ� Timer Ŭ������ ������ �����Ѵ�. ������ �������� ������ Timer�� ���ش�.

	private Color[] colors = { Color.green, Color.cyan, Color.pink, Color.orange, Color.BLUE, Color.magenta,
			Color.YELLOW }; //���ӳ����� ����ϴ� 7������ ����� ���� �����ϴ� �÷� �迭�̴�. tShape[i] (0<i<7) ���ʷ� ���� �����ȴ�.

	private tShaping[] tShape = new tShaping[7]; // ��Ʈ���� ���ӿ� ���� �������� ��Ƶα����� �������� �迭�� �Ҵ�޴´�
	private tShaping curShape; 					// ������ ��� �ٷ� ���Ǵ� ����� ��Ÿ���ִ� ����
	public static int score = 0;				// ���� ���ھ �޾��� ������ ������

	public tBoard() {

		random = new Random(); // ���� �Ҵ�
		
		// ��Ʈ���� ����� ���� �÷��� �����ϴ� �����̴�
		tShape[0] = new tShaping(new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, // Z ���;
		}, this, colors[5]);
		
		tShape[1] = new tShaping(new int[][] { { 1, 1, 1 }, { 0, 0, 1 }, // �⿪�� ��� ;
		}, this, colors[3]);
		
		tShape[2] = new tShaping(new int[][] { { 1, 1, 1, 1 } // 1�� ���;
		}, this, colors[0]);
		
		tShape[3] = new tShaping(new int[][] { { 1, 1 }, { 1, 1 }, // �׸� ���
		}, this, colors[6]);

		tShape[4] = new tShaping(new int[][] { { 1, 1, 1 }, { 0, 1, 0 }, // T�� ���;
		}, this, colors[1]);
		
		tShape[5] = new tShaping(new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, // S ���;
		}, this, colors[4]);

		tShape[6] = new tShaping(new int[][] { { 1, 1, 1 }, { 1, 0, 0 }, // ������ �⿪�� ��� ;
		}, this, colors[2]);

		curShape = tShape[0]; // ���� ���۽� ù��°�� �����ϴ� ���� tShape[0]->Z ��� ������ �����Ѵ�. ���� ���� �������� ����.

		
		
		// ���� �ڵ����� �������� �ð��� �����ϴ� �����̴�.
		dropping = new Timer(SET_DELAYTIME, new ActionListener() { 

			public void actionPerformed(ActionEvent e) { // �ٽ� ��ĥ�� ���� ������� ����

				refresh();		// ���� �����ϰ� �����̴� �Ϳ� ���� UPDATE�ϴ� ����� ���� �޼����. �������� ������ �Ʒ��� Ȯ������
				repaint();		// �������� ������ ��ǥ�� ���� ä���־� ������ �ϴµ� ���Ͻ�(��ǥ�̵���) �ٽ� ���� �־� �������� ����� �޼����
				TetrisSave();   // ��Ʈ������ ���ӳ����� �����ϴ� �޼����. ���� ������ �Ʒ��� ���캸��

			}
		});

		dropping.start();		// Timer ����
	}



	public void setCurShape() {
		curShape = tShape[random.nextInt(tShape.length)]; // ���� �޼��带 ���� tShape�� ����� ���� (7��)�� �������� �ٷ� ���� ���� ����
		curShape.Reset(); // �ٷ� ������ ���� ��ǥ (x=3, y=0)�� ��� �ҷ��� �ִ� ����̴�					
		checkGameEnd();		//���� ����ġ �̻� �����Ǹ� ���� ��ǥ������ ħ���ϴ� ���� �Ǵ��� Gameover�� �ƴ��� Ȯ���� �ִ� �޼����. ���� ������ �Ʒ��� ��������.
		BlockFileSave();	//���� ���� ���� ���¸� �����ϴ� �޼����. �Ʒ��� ���� �ڵ带 Ȯ���غ���.
	}



	public void BlockFileSave() { // ���� ��������� �����ϴ� �޼����.
		
		String colorCheck = null;
		try {
			FileWriter out = new FileWriter("D:\\Tetris1.txt"); // ���� �����͸� ���� ������ ��ο� ������ �����ǵ����Ѵ�.
			for (int i = 0; i < board.length; i++) {			// 
				for (int j = 0; j < board[0].length; j++) {
					// 1, Color.green, 2, Color.cyan, 3, Color.pink,
					// 4, Color.orange,5, Color.BLUE, 6, Color.magenta, 7, Color.YELLOW

					if (board[i][j] == Color.green) {			// ���� �迭�� �÷��迭�̱⿡ ���Ͽ� �������ؼ� ������ ��ȯ�� �ʿ��ϴ�
						colorCheck = "1";						// �迭���� ����� �÷��� ������ ���ں����� �Ű� ���ڷ� ���Ͽ� write�� ���ش�.
					} else if (board[i][j] == Color.cyan) {		// ��ĭ�� ��쿡�� ������ 0�̶�� ���ڷ� ó���غ��Ҵ�.
						colorCheck = "2";
					} else if (board[i][j] == Color.pink) {
						colorCheck = "3";
					} else if (board[i][j] == Color.orange) {
						colorCheck = "4";
					} else if (board[i][j] == Color.BLUE) {
						colorCheck = "5";
					} else if (board[i][j] == Color.magenta) {
						colorCheck = "6";
					} else if (board[i][j] == Color.YELLOW) {
						colorCheck = "7";
					} else {
						colorCheck = "0";
					}
					out.write(colorCheck);
				}
			}
			out.close();
		} catch (IOException E) {
		}
	}
	
	
	public void BlockFileCall() { // ����������� �ҷ����� �޼ҵ��.

		Color[] colorzip= new Color[200];		//�÷����� ���� ������ �÷� �迭�� �Ҵ����ش�
		int color;								//������ ������ �о���� �� �̱⶧���� ������ ���� ���� ����
		int conut=0;							//�迭�� ���ʷ� �ݺ����� ���� ���� �����Ұ� �̱⿡ ī��Ʈ����
		Color color2 = null;
		try {
			File log_file = new File("D:\\Tetris1.txt"); // �ش��ο� ��ϵ� ������ �Ҵ�
			FileReader in = new FileReader(log_file);	
			int count=0;
			while (true) {
				color = in.read();						// ������ �ϳ��� �о� color�� �����Ѵ�.
				if (color == -1)						// ������ ���� ���̸� �ݺ����� ���� �б⸦ ���������ش�.
					break;

				if (color == 48) {						// ��ĭ�� ������ 0�̶�� �����߱⿡ �÷����� null ó���� ���ش�.
					color2 = null;
				} else if (color == 49) {				// ���ڸ� ������ �о���鼭 �ƽ�Ű�ڵ�� ��ȯ�̵Ǵµ� �� 7������
					color2 = Color.green;				// ���� �����ϱ⶧���� �� �ѹ����� ���� �ٽ� �����ش�
				} else if (color == 50) {
					color2 = Color.cyan;
				} else if (color == 51) {
					color2 = Color.pink;
				} else if (color == 52) {
					color2 = Color.orange;
				} else if (color == 53) {
					color2 = Color.BLUE;
				} else if (color == 54) {
					color2 = Color.magenta;
				} else if (color == 55) {
					color2 = Color.YELLOW;
				}
			
				colorzip[count]=color2;					// �����س��� �迭�� ���� �÷����� ��Ƶд�

				count++;
			}
			int k=0;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					board[i][j]=colorzip[k++];			// ���������� ���� �迭�� �÷��� ���ʴ�� ������ �����Ǿ��� �����
														// ��ϵ��� ã�ƿ´�
				}
			}
			
			in.close();
		} catch (IOException E) {
		}
	}
	
	
	//----------------------------------------------

	public void TetrisSave() { // ���� ������ �����ϴ� �޼ҵ��. ���� ��Ŀ������ �� �� ����.

		String colorCheck = null;

		try {
			File log_file = new File("D:\\Tetris.txt");  // ���� �Ҵ�
			FileWriter out = new FileWriter(log_file);
			String Rscore = Integer.toString(score);	// ���ڷ� ����ϱ� ���� ������ ���ھ  ���ڿ��� ��ȯ�� �����Ѵ�.
			out.write(Rscore);
			out.close();

		} catch (IOException E) {
		}
	}

	public void TetrisCall() { // ���� ������ �����ϴ� �޼ҵ��. ���� ��Ŀ������ �� �� ����.

		String check = "";
		String fcheck = "";
		try {

			File log_file = new File("D:\\Tetris.txt");// ���� �Ҵ�

			FileReader in = new FileReader(log_file);
			int Sscore;

			while (true) {
				Sscore = in.read();
				if (Sscore == -1) 	//�ش� ���ϳ����� ������ ���� �� �ֵ��� ó���Ѵ�.
					break;

				check += Character.toString((char) Sscore);	//���ڸ� �ϳ��� �޾ƿ��� ������ 10�� �ڸ��� �Ѿ��� ���ܰ� �Ͼ��
			}												//�ֱ� ������ �� ���ھ� �޾ƿ��� ���ڿ� + �������� ������ ó���Ѵ�. 
			score = Integer.parseInt(check);				//�޾ƿ� ���ڿ��� ������ ��ȯ�� ������ �־��ش�.
			
			in.close();

		} catch (IOException E) {
		}
	}
	


	private void checkGameEnd() {						// ������ �����Ǵ� ��Ȳ�� üũ�ϴ� �Լ��̴�. curShape.Reset();�� ���� �츮�� x=3, y=0���� 
		int[][] coor = curShape.getcoor();				// ���� �����Ǵ°��� �츮�� �˼��ִµ� ���⼭ ������ ����� ������ �ʱ� �����Ǵ� ���� ��ġ�� �浹�ϰԵǸ�
		for (int row = 0; row < coor.length; row++) {	// ������ ������ �ϴ� �޼����.
			for (int col = 0; col < coor[0].length; col++) {
				if (coor[row][col] != 0) {
					if (board[row + curShape.getY()][col + curShape.getX()] != null) {
						state = STATE_GAME_END;
					}
				}
			}
		}
	}

//----------------------------refresh------------------------------------------------------

	private void refresh() {			// �����ǿ����� ����� �����Ȳ�� ����ؼ� update �ϴ� ����� ������ �޼����. �ڼ��� ���������� 
		if (state == STATE_GAME_PLAYING)// shping.java���� Ȯ�� �� �� �ִ�.
			curShape.refresh();

	}

//-----------------------------paintcomponent------------------------------------------------------	
	@Override
	protected void paintComponent(Graphics g) { // �����ǿ��� ��ü���� ���� �������� �ð��ִ� ��Ʈ��.
		super.paintComponent(g);
		g.setColor(Color.white); 					// ����ȭ�鿡 ����� �÷��� ȭ��Ʈ�� �������־���.
		g.fillRect(0, 0, getWidth(), getHeight()); 	// �츮�� �����س��� ����ȭ�������(�ʺ�x����)�� ������ ������ ���� ä���.

		curShape.render(g);							//render()�޼ҵ�� �������� ����� ������Ű�� �޼ҵ��. �ڼ��� ���� �ڵ�� shping.java���� Ȯ�� �� ���ִ�.

		// ������ ��� ������ �ְ� ä���ִ� ������ �ϴ� ��Ʈ��.
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != null) {
					g.setColor(board[row][col]); // ������ ������ ������ ���������� �÷��� �������ְ� �� ĭ�� ������� ��,���� ���� ���� ������ ��ϻ��� ä���ش�.
					g.fillRect(col * SET_tSPACE_SIZE, row * SET_tSPACE_SIZE, SET_tSPACE_SIZE, SET_tSPACE_SIZE);
				}
			}
		}

		//���� ������� �޴� �� ���ھ�带 ��Ÿ���� ������ ���������ִ� ���� �ߴ� �����̴�.
		g.setColor(Color.black);
		for (int column = 0; column <= SET_tBOARD_WID; column++) {
			g.drawLine(300, 0, 300, SET_tBOARD_HEI * SET_tSPACE_SIZE);
		}


		g.setFont(new Font("Georgia", Font.BOLD, 20)); // GUI�� ���ӽ��ھ� ������ ����ϴ� �κ��̴�. 
		g.setColor(Color.black);					// �۾�ü�� ��Ʈ �۾� ũ�⸦ �����Ͽ� ������ ��ǥ�� ��� �����Ѵ�.
		g.drawString("GAME SCORE ", 310, 500);

		g.setFont(new Font("Georgia", Font.BOLD, 35));	//������ ����ϴ� �κ��̴�.
		g.drawString(Integer.toString(score), 365, 550);

		g.setFont(new Font("Georgia", Font.BOLD, 20));	//�޴�ǥ�ø� ����ϴ� �κ��̴�.
		g.setColor(Color.BLUE);
		g.drawString("[MENU]", 340, 50);

		g.setFont(new Font("Georgia", Font.BOLD, 18)); 	//�޴��ϴ� ������� ����ϴ� �κ��̴�.
		g.setColor(Color.black);
		g.drawString("PAUSE         ", 325, 100);		//�޴��ϴ� ������� ����ϴ� �κ��̴�.
		g.drawString(": Spacebar ", 325, 120);
		
		g.drawString("RESET            ", 325, 150);	//�޴��ϴ� ������� ����ϴ� �κ��̴�.	
		g.drawString(": R key   ", 325, 170);

		g.drawString("RESTART     ", 325, 200);			//�޴��ϴ� ������� ����ϴ� �κ��̴�.
		g.drawString(": S key  ", 325, 220);

		g.drawString("ROTATION   ", 325, 250);			//�޴��ϴ� ������� ����ϴ� �κ��̴�.
		g.drawString(": Up key   ", 325, 270);

		
		if (state == STATE_GAME_END) {					// ������ OVER�Ǵ� ������ ����� ������ �����ϴ� �κ��̴�.
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.setColor(Color.RED);
			g.drawString("[ GAME OVER ]", 90, 200);
			g.setFont(new Font("Georgia", Font.BOLD, 20));
			g.setColor(Color.black);
			g.drawString("DO YOU WANT  REGAME? PRESS [S] !", 20, 230);

		}

		if (state == STATE_GAME_STOP) {					// ������ PAUSE�Ǵ� ������ ����� ������ �����ϴ� �κ��̴�.

			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.setColor(Color.RED);
			g.drawString("[ GAME STOP ]", 90, 200);

			g.setFont(new Font("Georgia", Font.BOLD, 20));
			g.setColor(Color.black);
			g.drawString("Press spacebar to continue the game !", 20, 230);
		}
	}



	public Color[][] getBoard() {		// board ���� �޼���
		return board;
	}

// Ű���� �̺�Ʈ ó���� �����ϴ� part
	@Override
	public void keyPressed(KeyEvent e) {			//Ű����� �۵��ϴ� ��Ʈ�� �̺�Ʈ ó���س��� �κ��̴�. 
		if (e.getKeyCode() == KeyEvent.VK_DOWN) { 	//ȸ��,��,��,�� ��� ��Ʈ���� �����ϴ�.
			curShape.getFaster();					//�� ��Ʈ�� �޼ҵ�� �Ʒ��� Ȯ������.
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			curShape.crtleft();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			curShape.crtRight();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			curShape.rotationShape();				//�޼ҵ��� ������ ���� ���Ͽ��� ���캸��.
		}

		// ���������� RŰ�� ������ ������ �������� ��� �ʱ�ȭ �ȴ�.
		if (state == STATE_GAME_PLAYING) {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				score = 0;											// ���� �ʱ�ȭ
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board[0].length; col++) {
						board[row][col] = null;						// ������� ����� ����� ��ü ������ null ���� �־� ����ĭ�� ����ش�
					}
				}
				setCurShape();
				state = STATE_GAME_PLAYING;							
			}
		}

		// ���ӿ����� ���� SŰ�� ���� �����ǰ� �������� �ʱ�ȭ�ȴ�.
		if (state == STATE_GAME_END) {
			if (e.getKeyCode() == KeyEvent.VK_S) {
				score = 0;											// ���� �ʱ�ȭ
				for (int row = 0; row < board.length; row++) {		// ���� �ʱ�ȭ
					for (int col = 0; col < board[0].length; col++) {
						board[row][col] = null;		// ������� ����� ����� ��ü ������ null ���� �־� ����ĭ�� ����ش�
					}
				}
				setCurShape();
				state = STATE_GAME_PLAYING;
			}
		}
	
		// �����߰��� ������ �Ҽ��ִ� ����̴�.
		if (e.getKeyCode() == KeyEvent.VK_SPACE) { // �����̽��� �̺�Ʈ
			if (state == STATE_GAME_PLAYING) {
				state = STATE_GAME_STOP;			// ���� ����
			} else if (state == STATE_GAME_STOP) {
				state = STATE_GAME_PLAYING;			// ���� ����
			}

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			curShape.getSlower();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void addScore() {		//������ �����ϴ� �޼ҵ��. ������ ������ �޼ҵ峻����
		score++;					//�ڵ����� Score�� 0���� �ʱ�ȭ�Ѵ�. 
		if (state == STATE_GAME_END) {
			score = 0;
		}
	}

}

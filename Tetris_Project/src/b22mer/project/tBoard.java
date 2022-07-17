package b22mer.project;
//이름: 정원철, 학과: 컴퓨터공학과 ,학번:12162149
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

	// 게임 상태를 나타내는 나타내는 상수들이다. 게임의 상태를 특정 숫자로 지정함으로써 간단한 상황 설정이 가능하다.
	public static int STATE_GAME_END = 0;
	public static int STATE_GAME_STOP = 1;
	public static int STATE_GAME_PLAYING = 2;
	private int state = STATE_GAME_PLAYING;
	
	
	// 게임을 진행하는 공간의 사이즈를 설정해줄 상수들이다
	public static final int SET_tBOARD_HEI = 20; // 게임 내 창 높이를 상수로 선언
	public static final int SET_tBOARD_WID = 10; // 게임 내 창 너비를 상수로 선언
	public static final int SET_tSPACE_SIZE = 30; // 가상의 칸을 30으로 설정후 상수로 선언
	
	
	// 기본적으로 블록이 자동으로 낙하하는 시간을 지정해둔 상수다
	private static int SET_DELAYTIME = 60 / 1000; // 딜레이 타임 설정

	
	// 칸이 내려올때 마다 계속 색을 다시 색칠하는 방식이기에 게임보드의 넓이에 맞게 설정한다
	private Color[][] board = new Color[SET_tBOARD_HEI][SET_tBOARD_WID];


	private Random random; // 게임내에서 테트리스 도형이 랜덤으로 나오게 처리하기 위해 랜덤 상수선언
	private int x = 3, y = 0; // 게임은 x,y축 좌표기반으로 진행이되기에, 초기 블럭위치 좌표를 설정해준다.

	private int CHANGE_X; // 키보드 이벤트를 통한 좌우 이동에 (x좌표 이동) 쓰일 변수다
	private boolean BUMPER = false; // BUMPER는 상하와 관련된 충돌과 관련된 boolean이다. 블럭의 누적 여부를 좌우한다.

	private Timer dropping; //설정 시간 주기에 맞춰 같은 반복을 하게되는 Timer 클래스의 변수를 선언한다. 일종의 쓰레드의 역할을 Timer가 해준다.

	private Color[] colors = { Color.green, Color.cyan, Color.pink, Color.orange, Color.BLUE, Color.magenta,
			Color.YELLOW }; //게임내에서 사용하는 7가지의 블록의 색을 지정하는 컬러 배열이다. tShape[i] (0<i<7) 차례로 색이 설정된다.

	private tShaping[] tShape = new tShaping[7]; // 테트리스 게임에 사용될 도형들을 담아두기위한 사이즈의 배열을 할당받는다
	private tShaping curShape; 					// 게임중 당시 바로 사용되는 블록을 나타내주는 역할
	public static int score = 0;				// 게임 스코어를 받아줄 정수형 변수다

	public tBoard() {

		random = new Random(); // 랜덤 할당
		
		// 테트리스 블록의 모양과 컬레를 설정하는 구간이다
		tShape[0] = new tShaping(new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, // Z 모양;
		}, this, colors[5]);
		
		tShape[1] = new tShaping(new int[][] { { 1, 1, 1 }, { 0, 0, 1 }, // 기역자 모양 ;
		}, this, colors[3]);
		
		tShape[2] = new tShaping(new int[][] { { 1, 1, 1, 1 } // 1자 모양;
		}, this, colors[0]);
		
		tShape[3] = new tShaping(new int[][] { { 1, 1 }, { 1, 1 }, // 네모 모양
		}, this, colors[6]);

		tShape[4] = new tShaping(new int[][] { { 1, 1, 1 }, { 0, 1, 0 }, // T자 모양;
		}, this, colors[1]);
		
		tShape[5] = new tShaping(new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, // S 모양;
		}, this, colors[4]);

		tShape[6] = new tShaping(new int[][] { { 1, 1, 1 }, { 1, 0, 0 }, // 리버스 기역자 모양 ;
		}, this, colors[2]);

		curShape = tShape[0]; // 게임 시작시 첫번째로 등장하는 블럭은 tShape[0]->Z 모양 블럭으로 설정한다. 이후 블럭은 랜덤으로 설정.

		
		
		// 블럭이 자동으로 떨어지는 시간을 설정하는 구간이다.
		dropping = new Timer(SET_DELAYTIME, new ActionListener() { 

			public void actionPerformed(ActionEvent e) { // 다시 색칠해 가는 방식으로 진행

				refresh();		// 블럭의 낙하하고 움직이는 것에 관해 UPDATE하는 기능을 갖춘 메서드다. 다음구현 내용은 아래서 확인하자
				repaint();		// 전반적인 내용이 좌표에 색을 채워넣어 구성을 하는데 낙하시(좌표이동시) 다시 색을 넣어 떨어지듯 만드는 메서드다
				TetrisSave();   // 테트리스의 게임내용을 저장하는 메서드다. 구현 내용은 아래서 살펴보자

			}
		});

		dropping.start();		// Timer 시작
	}



	public void setCurShape() {
		curShape = tShape[random.nextInt(tShape.length)]; // 랜덤 메서드를 통해 tShape에 저장된 도형 (7개)중 랜덤으로 바로 사용될 블럭을 설정
		curShape.Reset(); // 바로 생성된 블럭의 좌표 (x=3, y=0)로 계속 불러와 주는 기능이다					
		checkGameEnd();		//블럭이 기준치 이상 누적되면 상하 좌표공간을 침범하는 것을 판단해 Gameover가 됐는지 확인해 주는 메서드다. 구현 내용은 아래서 참고하자.
		BlockFileSave();	//현재 블럭의 누적 상태를 저장하는 메서드다. 아래서 구현 코드를 확인해보자.
	}



	public void BlockFileSave() { // 블럭의 누적기록을 저장하는 메서드다.
		
		String colorCheck = null;
		try {
			FileWriter out = new FileWriter("D:\\Tetris1.txt"); // 파일 라이터를 통해 지정된 경로에 파일이 생성되도록한다.
			for (int i = 0; i < board.length; i++) {			// 
				for (int j = 0; j < board[0].length; j++) {
					// 1, Color.green, 2, Color.cyan, 3, Color.pink,
					// 4, Color.orange,5, Color.BLUE, 6, Color.magenta, 7, Color.YELLOW

					if (board[i][j] == Color.green) {			// 보드 배열은 컬러배열이기에 파일에 쓰기위해선 별도의 변환이 필요하다
						colorCheck = "1";						// 배열에서 추출된 컬러에 적당한 숫자별명을 매겨 문자로 파일에 write를 해준다.
					} else if (board[i][j] == Color.cyan) {		// 빈칸의 경우에는 별도로 0이라는 문자로 처리해보았다.
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
	
	
	public void BlockFileCall() { // 블럭누적기록을 불러오는 메소드다.

		Color[] colorzip= new Color[200];		//컬러들을 받을 별도의 컬러 배열을 할당해준다
		int color;								//파일을 정수로 읽어들일 것 이기때문에 별도의 정수 변수 설정
		int conut=0;							//배열에 차례로 반복문을 통해 값을 삽입할것 이기에 카운트설정
		Color color2 = null;
		try {
			File log_file = new File("D:\\Tetris1.txt"); // 해당경로에 기록된 파일을 할당
			FileReader in = new FileReader(log_file);	
			int count=0;
			while (true) {
				color = in.read();						// 파일을 하나씩 읽어 color에 삽입한다.
				if (color == -1)						// 파일의 끝이 보이면 반복문을 끊어 읽기를 마무리해준다.
					break;

				if (color == 48) {						// 빈칸을 위에서 0이라고 저장했기에 컬러기준 null 처리를 해준다.
					color2 = null;
				} else if (color == 49) {				// 문자를 정수로 읽어오면서 아스키코드로 변환이되는데 촛 7가지의
					color2 = Color.green;				// 색이 존재하기때문에 각 넘버마다 색을 다시 입혀준다
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
			
				colorzip[count]=color2;					// 선언해놓은 배열에 뽑은 컬러들을 모아둔다

				count++;
			}
			int k=0;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					board[i][j]=colorzip[k++];			// 마지막으로 보드 배열에 컬러를 차례대로 삽입해 누적되었던 블록의
														// 기록들을 찾아온다
				}
			}
			
			in.close();
		} catch (IOException E) {
		}
	}
	
	
	//----------------------------------------------

	public void TetrisSave() { // 게임 점수를 저장하는 메소드다. 구현 메커니즘은 위 와 같다.

		String colorCheck = null;

		try {
			File log_file = new File("D:\\Tetris.txt");  // 파일 할당
			FileWriter out = new FileWriter(log_file);
			String Rscore = Integer.toString(score);	// 문자로 기록하기 위해 정수인 스코어를  문자열로 변환해 저장한다.
			out.write(Rscore);
			out.close();

		} catch (IOException E) {
		}
	}

	public void TetrisCall() { // 게임 점수를 저장하는 메소드다. 구현 메커니즘은 위 와 같다.

		String check = "";
		String fcheck = "";
		try {

			File log_file = new File("D:\\Tetris.txt");// 파일 할당

			FileReader in = new FileReader(log_file);
			int Sscore;

			while (true) {
				Sscore = in.read();
				if (Sscore == -1) 	//해당 파일내용을 끝까지 읽을 수 있도록 처리한다.
					break;

				check += Character.toString((char) Sscore);	//문자를 하나씩 받아오기 때문에 10의 자리가 넘어갈경우 예외가 일어날수
			}												//있기 때문에 한 문자씩 받아온후 문자열 + 연산으로 점수를 처리한다. 
			score = Integer.parseInt(check);				//받아온 문자열을 정수로 변환해 점수로 넣어준다.
			
			in.close();

		} catch (IOException E) {
		}
	}
	


	private void checkGameEnd() {						// 게임이 오버되는 상황을 체크하는 함수이다. curShape.Reset();을 통해 우리는 x=3, y=0에서 
		int[][] coor = curShape.getcoor();				// 블럭이 생성되는것을 우리는 알수있는데 여기서 누적된 블록의 범위가 초기 생성되는 블럭의 위치가 충돌하게되면
		for (int row = 0; row < coor.length; row++) {	// 게임이 끝나게 하는 메서드다.
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

	private void refresh() {			// 게임판에서의 블록의 진행상황을 계속해서 update 하는 기능을 구현한 메서드다. 자세한 구현내용은 
		if (state == STATE_GAME_PLAYING)// shping.java에서 확인 할 수 있다.
			curShape.refresh();

	}

//-----------------------------paintcomponent------------------------------------------------------	
	@Override
	protected void paintComponent(Graphics g) { // 게임판에서 전체적인 외형 디잔인을 맡고있는 파트다.
		super.paintComponent(g);
		g.setColor(Color.white); 					// 바탕화면에 사용할 컬러를 화이트로 지정해주었다.
		g.fillRect(0, 0, getWidth(), getHeight()); 	// 우리가 설정해놓은 게임화면사이즈(너비x높이)에 위에서 설정한 색을 채운다.

		curShape.render(g);							//render()메소드는 내려오는 블록을 형성시키는 메소드다. 자세한 구현 코드는 shping.java에서 확인 할 수있다.

		// 도형을 계속 쌓을수 있게 채워주는 역할을 하는 파트다.
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != null) {
					g.setColor(board[row][col]); // 지정된 보드의 사이즈 범위내에서 컬러를 설정해주고 각 칸의 사이즈와 행,열의 수를 통해 누적된 블록색을 채워준다.
					g.fillRect(col * SET_tSPACE_SIZE, row * SET_tSPACE_SIZE, SET_tSPACE_SIZE, SET_tSPACE_SIZE);
				}
			}
		}

		//게임 진행란과 메뉴 및 스코어보드를 나타내는 영역을 구분지어주는 선을 긋는 영역이다.
		g.setColor(Color.black);
		for (int column = 0; column <= SET_tBOARD_WID; column++) {
			g.drawLine(300, 0, 300, SET_tBOARD_HEI * SET_tSPACE_SIZE);
		}


		g.setFont(new Font("Georgia", Font.BOLD, 20)); // GUI상에 게임스코어 문구를 기록하는 부분이다. 
		g.setColor(Color.black);					// 글씨체와 폰트 글씨 크기를 설정하여 적절한 좌표를 골라 설정한다.
		g.drawString("GAME SCORE ", 310, 500);

		g.setFont(new Font("Georgia", Font.BOLD, 35));	//점수를 기록하는 부분이다.
		g.drawString(Integer.toString(score), 365, 550);

		g.setFont(new Font("Georgia", Font.BOLD, 20));	//메뉴표시를 기록하는 부분이다.
		g.setColor(Color.BLUE);
		g.drawString("[MENU]", 340, 50);

		g.setFont(new Font("Georgia", Font.BOLD, 18)); 	//메뉴하단 설명란을 기록하는 부분이다.
		g.setColor(Color.black);
		g.drawString("PAUSE         ", 325, 100);		//메뉴하단 설명란을 기록하는 부분이다.
		g.drawString(": Spacebar ", 325, 120);
		
		g.drawString("RESET            ", 325, 150);	//메뉴하단 설명란을 기록하는 부분이다.	
		g.drawString(": R key   ", 325, 170);

		g.drawString("RESTART     ", 325, 200);			//메뉴하단 설명란을 기록하는 부분이다.
		g.drawString(": S key  ", 325, 220);

		g.drawString("ROTATION   ", 325, 250);			//메뉴하단 설명란을 기록하는 부분이다.
		g.drawString(": Up key   ", 325, 270);

		
		if (state == STATE_GAME_END) {					// 게임이 OVER되는 구간에 띄워질 문구를 설정하는 부분이다.
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.setColor(Color.RED);
			g.drawString("[ GAME OVER ]", 90, 200);
			g.setFont(new Font("Georgia", Font.BOLD, 20));
			g.setColor(Color.black);
			g.drawString("DO YOU WANT  REGAME? PRESS [S] !", 20, 230);

		}

		if (state == STATE_GAME_STOP) {					// 게임이 PAUSE되는 구간에 띄워질 문구를 설정하는 부분이다.

			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.setColor(Color.RED);
			g.drawString("[ GAME STOP ]", 90, 200);

			g.setFont(new Font("Georgia", Font.BOLD, 20));
			g.setColor(Color.black);
			g.drawString("Press spacebar to continue the game !", 20, 230);
		}
	}



	public Color[][] getBoard() {		// board 리턴 메서드
		return board;
	}

// 키보드 이벤트 처리를 구현하는 part
	@Override
	public void keyPressed(KeyEvent e) {			//키보드로 작동하는 파트를 이벤트 처리해놓은 부분이다. 
		if (e.getKeyCode() == KeyEvent.VK_DOWN) { 	//회전,하,좌,우 모두 컨트롤이 가능하다.
			curShape.getFaster();					//각 파트의 메소드는 아래서 확인하자.
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			curShape.crtleft();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			curShape.crtRight();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			curShape.rotationShape();				//메소드의 구현은 다음 파일에서 살펴보자.
		}

		// 게임진행중 R키를 누르면 점수와 게임판이 모두 초기화 된다.
		if (state == STATE_GAME_PLAYING) {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				score = 0;											// 점수 초기화
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board[0].length; col++) {
						board[row][col] = null;						// 행과열의 사이즈를 고려해 전체 내역에 null 값을 넣어 게임칸을 비워준다
					}
				}
				setCurShape();
				state = STATE_GAME_PLAYING;							
			}
		}

		// 게임오버가 된후 S키를 통해 게임판과 점수판이 초기화된다.
		if (state == STATE_GAME_END) {
			if (e.getKeyCode() == KeyEvent.VK_S) {
				score = 0;											// 점수 초기화
				for (int row = 0; row < board.length; row++) {		// 점수 초기화
					for (int col = 0; col < board[0].length; col++) {
						board[row][col] = null;		// 행과열의 사이즈를 고려해 전체 내역에 null 값을 넣어 게임칸을 비워준다
					}
				}
				setCurShape();
				state = STATE_GAME_PLAYING;
			}
		}
	
		// 게임중간에 정지를 할수있는 기능이다.
		if (e.getKeyCode() == KeyEvent.VK_SPACE) { // 스페이스바 이벤트
			if (state == STATE_GAME_PLAYING) {
				state = STATE_GAME_STOP;			// 게임 중지
			} else if (state == STATE_GAME_STOP) {
				state = STATE_GAME_PLAYING;			// 게임 진행
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

	public void addScore() {		//점수가 증가하는 메소드다. 게임이 끝나면 메소드내에서
		score++;					//자동으로 Score을 0으로 초기화한다. 
		if (state == STATE_GAME_END) {
			score = 0;
		}
	}

}

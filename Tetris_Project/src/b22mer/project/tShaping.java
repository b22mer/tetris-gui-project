package b22mer.project;
//이름: 정원철, 학과: 컴퓨터공학과 ,학번:12162149
import java.awt.Color;
import java.awt.Graphics;



public class tShaping {

	private int x = 3, y = 0; // 초기 블럭 생성 좌표를 선언해둔 구간이다.
	private int CHANGE_X; // 좌우 이동에 (x좌표 이동)쓰일 상수
	private boolean BUMPER = false; // X가 좌우 이동을 제한하는데에 사용됐다면 BUMPER는 상하와 관련된 boolean


	
	private int NORMAL=600;	  // 기본으로 설정된 시간 
	private int FASTEST = 40; // 딜레이 타임 설정 (fast)
	private int DELAYTIME_OPERATION = NORMAL; // 딜레이 타임에 기본시간을 넣어준다
	private long START_TIME;
	
	
	private int[][] coor;			// 별도의 정수 배열을 선언해둔다.
	private tBoard board;			// shaping 에서 사용할 보드객체선언
	private Color color;			// color 객체 선언

	public tShaping(int[][] coor, tBoard board, Color color) { // shaping 생성자를 통해 위에서 설정한 객 매개변수를 받아들인다.
		this.coor = coor;
		this.board = board;
		this.color = color;

	}

	public void setX(int x) {		// x축의 좌표 위치를 설정한다.
		this.x = x;
	}

	public void setY(int y) {		// y축의 좌표 위치를 설정한다.
		this.y = y;
	}

	public void Reset() { 			// 앞 tBoard에서 사용하는 예를 보았듯
		this.x = 3;					// 새로 생성되는 블럭의 초기 위치를 설정하는 구간이다.
		this.y = 0;
		BUMPER = false;

	}

	public void refresh() {			// 블록을 업데이트해주는 기능할 하는구간이다.
		if (BUMPER) {				// BUMPER -> 땅에 부딪힐때마다 -> 설정해둔 Y좌표에 닿을때마다
			for (int row = 0; row < coor.length; row++) {
				for (int col = 0; col < coor[0].length; col++) {
					if (coor[row][col] != 0) {
						board.getBoard()[y + row][x + col] = color; // 별도선언 coor 배열에 값이 없지 않다면 
																	// 게임판을 채우기위한 컬러를 삽입해준다.
						
					}
				}
			}

			checkLine(); // 한줄 체크해 모두채워져있는게 확인되면 삭제하는 함수이다.
			board.addScore();	// update 될때마다 스코어가 증가하게되는 함수이다.
			board.setCurShape(); // 현재의 테트리스 도형을 설정한다.	
			return;
		}
		
		

		// 좌우 위치, x축의 위치를 벗어 나는지 확인해주는 불리안이다.
		boolean moveX = true;
		
		// 옆으로 집어넣을수있게 처리하는 코드 배열구간의 길이와 축을 고려해 블럭이 누적될때 바닥뿐만 아니라 블럭의 사이까지도
		// 블록이 누적될 수 있게 구성했다.
		if (!(x + CHANGE_X + coor[0].length > tBoard.SET_tBOARD_WID) && !(x + CHANGE_X < 0)) {	
			for (int row = 0; row < coor.length; row++) { 					
				for (int col = 0; col < coor[row].length; col++) {
					if (coor[row][col] != 0) {										//* coor에는 컬러 board배열의 정보가 정수형으로 저장되어있다.
						if (board.getBoard()[y + row][x + CHANGE_X + col] != null) { // 해당 보드값이 벗어나는 파트가 존재하면
							moveX = false;											// false를 삽입해 진행
						}
					}
				}
			}
			if (moveX) {
				x = x + CHANGE_X; // 함수가 호출돼 이벤트가 있을 경우 현재 위치한 (x=3) 에서 설정된 상수만큼 연산된다.
			}
		}
		CHANGE_X = 0; // 연산후 다시 설정된 상수는 초기화 시키고 이벤트를 기다린다.
		
		

			// y좌표 관련 확인란 (y 하단 제한)
		if (System.currentTimeMillis() - START_TIME > DELAYTIME_OPERATION) {
			
			// 위아래 움직임을 컨트롤하는 구간이다.
			// 게임의 y좌표가 하단측으로 벗어나게되는경우 (블럭이 바닥과 맞닿는 경우) BUMPER값에는 true가
			// 들어가 블럭이 멈춘다 -> 블럭이 쌓일수 있도록 도와주는 파트
			if (!(y + 1 + coor.length > tBoard.SET_tBOARD_HEI)) { 		
				for (int row = 0; row < coor.length; row++) { 			
					for (int col = 0; col < coor[row].length; col++) {	
						if (coor[row][col] != 0) {
							if (board.getBoard()[y + 1 + row][x + CHANGE_X + col] != null) {
								BUMPER = true;
							}
						}
					}
				}
				if (!BUMPER) {	//바닥 좌표 (y축 바닥) 접하지 않는 파트에서 y축의 증가를 통해  블록이 계속 이동하다.
					y++;
				}
			} else {
				BUMPER = true;
			}

			START_TIME = System.currentTimeMillis();
		}
	}


	
	// 누적된 블럭의 한줄씩 확인,삭제하는 메소드다.
	private void checkLine() {
		int bottom = board.getBoard().length - 1;						//설정된 게임판의 바닥부터 누적된 블럭의 일치가 존재하는지 확인하고 
		for (int top = board.getBoard().length - 1; top > 0; top--) {	//차례로 행과열을 따라다니면서 탐색해 블록의 삭제를 돕니다. 
			int cnt = 0;												//시작은 아래블럭에서 첫번째 행 에서 한줄씩 차례로 체크를하고
			for (int col = 0; col < board.getBoard()[0].length; col++) {//그 다음줄, 그 다음줄 체크해가며 줄을 삭제한다.(bottom--)	
				if (board.getBoard()[top][col] != null) {
					cnt++;
				}	
				board.getBoard()[bottom][col] = board.getBoard()[top][col];
				
			}
			if (cnt < board.getBoard()[0].length) {
				bottom--;		
			}
		}
	}
	
	

	// 블록의 회전을 도와주는 파드다.
	public void rotationShape() {
		int[][] rotatedShape = transMatrix(coor);
		reverseRow(rotatedShape); // 기존에회전되었다고 한 rotatedShape은 배열의 순서만 반대로 배열한것이기에 실제로 
									// 회전을 해서 나올수 없는 형태가 발생한다. 리버스로우를 통해 이를 바로 잡는다. 아래 구현코드를  확인하자.
		
		// 회전할때 생기는 예외 (회전하면서 벽과 바닥을 뚫어버리는) 확인란이다.
		if (x + rotatedShape[0].length > tBoard.SET_tBOARD_WID || (y + rotatedShape.length > tBoard.SET_tBOARD_HEI)) {
			return;
		}

		// 회전중 다른 모양과 출동하여 벽과 마찬가지로 도형을 뚫어버리는걸 막는, 확인하는 란이다.
		for (int row = 0; row < rotatedShape.length; row++) {			//회전을 하면서 기존이 존재하는 블록의 가로세로는 
			for (int col = 0; col < rotatedShape[row].length; col++) {	//다르기때문에 블록이 어느위치에서 회전을 했냐에따라 
				if (rotatedShape[row][col] != 0) {						//필요로하는 공간이 다르다. 
					if (board.getBoard()[y + row][x + col] != null) {	
						return;
					}
				}
			}
		}

		coor = rotatedShape;
	}


	// 블록의 행과열을 반전시켜주는 구간
	public int[][] transMatrix(int[][] matrix) {				// 실제로 블록을 회전을 시키기 전, 1차적인 처리를 하는구간이다.
		int[][] temp = new int[matrix[0].length][matrix.length];// 다른 배열에 실제블록의 좌표값을 서로 행과열을 뒤바꿔넣어 
		for (int row = 0; row < matrix.length; row++) {			// 블록을 반전시킨다.
			for (int col = 0; col < matrix[0].length; col++) {	// 하지만 이는 회전을 해서 나올수가 없는 모양이기에 아래의 함수를 통해 
				temp[col][row] = matrix[row][col];				// 회전의 효과를 얻어낼수 있다.
			}
		}
		return temp;
	}

	public void reverseRow(int[][] matrix) {					// 트랜스에서 생성된 temp배열의 열의 위치 변경을
		int mid = matrix.length / 2;							// 뒤바꿈을 통해 회전을 효과를 얻어낸다.
		for (int row = 0; row < mid; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length - row - 1];
			matrix[matrix.length - row - 1] = temp;
		}

	}

	// 진행되는 블록의 형태를 갖추게 해주는 메소드다. 
	// 게임판에 나타나는 블록의 모습을 색으로 채워주는 역할을 진행한다.
	public void render(Graphics g) {
		for (int row = 0; row < coor.length; row++) {
			for (int col = 0; col < coor[0].length; col++) {
				if (coor[row][col] != 0) {
					g.setColor(color);
					g.fillRect(col * tBoard.SET_tSPACE_SIZE + x * tBoard.SET_tSPACE_SIZE, row * tBoard.SET_tSPACE_SIZE + y * tBoard.SET_tSPACE_SIZE,
							tBoard.SET_tSPACE_SIZE, tBoard.SET_tSPACE_SIZE);
				} //지정된 블록사이즈와 좌표 칸수를 계산해 외북에서 지정된 색을 선택된 좌표에 맞게 설정하고 채워준다.

			}
		}
	}

	// 서브 메소드
	public int[][] getcoor() {
		return coor;
	}

	public int getX() {
		return x;
	}					

	public int getY() {
		return y;
	}

	public void getFaster() {
		DELAYTIME_OPERATION = FASTEST;

	}

	public void getSlower() {
		DELAYTIME_OPERATION = NORMAL;
	}

	public void crtRight() {
		CHANGE_X = 1;
	}

	public void crtleft() {
		CHANGE_X = -1;
	}

}

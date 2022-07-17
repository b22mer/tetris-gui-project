package b22mer.project;
//�̸�: ����ö, �а�: ��ǻ�Ͱ��а� ,�й�:12162149
import java.awt.Color;
import java.awt.Graphics;



public class tShaping {

	private int x = 3, y = 0; // �ʱ� �� ���� ��ǥ�� �����ص� �����̴�.
	private int CHANGE_X; // �¿� �̵��� (x��ǥ �̵�)���� ���
	private boolean BUMPER = false; // X�� �¿� �̵��� �����ϴµ��� ���ƴٸ� BUMPER�� ���Ͽ� ���õ� boolean


	
	private int NORMAL=600;	  // �⺻���� ������ �ð� 
	private int FASTEST = 40; // ������ Ÿ�� ���� (fast)
	private int DELAYTIME_OPERATION = NORMAL; // ������ Ÿ�ӿ� �⺻�ð��� �־��ش�
	private long START_TIME;
	
	
	private int[][] coor;			// ������ ���� �迭�� �����صд�.
	private tBoard board;			// shaping ���� ����� ���尴ü����
	private Color color;			// color ��ü ����

	public tShaping(int[][] coor, tBoard board, Color color) { // shaping �����ڸ� ���� ������ ������ �� �Ű������� �޾Ƶ��δ�.
		this.coor = coor;
		this.board = board;
		this.color = color;

	}

	public void setX(int x) {		// x���� ��ǥ ��ġ�� �����Ѵ�.
		this.x = x;
	}

	public void setY(int y) {		// y���� ��ǥ ��ġ�� �����Ѵ�.
		this.y = y;
	}

	public void Reset() { 			// �� tBoard���� ����ϴ� ���� ���ҵ�
		this.x = 3;					// ���� �����Ǵ� ���� �ʱ� ��ġ�� �����ϴ� �����̴�.
		this.y = 0;
		BUMPER = false;

	}

	public void refresh() {			// ����� ������Ʈ���ִ� ����� �ϴ±����̴�.
		if (BUMPER) {				// BUMPER -> ���� �ε��������� -> �����ص� Y��ǥ�� ����������
			for (int row = 0; row < coor.length; row++) {
				for (int col = 0; col < coor[0].length; col++) {
					if (coor[row][col] != 0) {
						board.getBoard()[y + row][x + col] = color; // �������� coor �迭�� ���� ���� �ʴٸ� 
																	// �������� ä������� �÷��� �������ش�.
						
					}
				}
			}

			checkLine(); // ���� üũ�� ���ä�����ִ°� Ȯ�εǸ� �����ϴ� �Լ��̴�.
			board.addScore();	// update �ɶ����� ���ھ �����ϰԵǴ� �Լ��̴�.
			board.setCurShape(); // ������ ��Ʈ���� ������ �����Ѵ�.	
			return;
		}
		
		

		// �¿� ��ġ, x���� ��ġ�� ���� ������ Ȯ�����ִ� �Ҹ����̴�.
		boolean moveX = true;
		
		// ������ ����������ְ� ó���ϴ� �ڵ� �迭������ ���̿� ���� ����� ���� �����ɶ� �ٴڻӸ� �ƴ϶� ���� ���̱�����
		// ����� ������ �� �ְ� �����ߴ�.
		if (!(x + CHANGE_X + coor[0].length > tBoard.SET_tBOARD_WID) && !(x + CHANGE_X < 0)) {	
			for (int row = 0; row < coor.length; row++) { 					
				for (int col = 0; col < coor[row].length; col++) {
					if (coor[row][col] != 0) {										//* coor���� �÷� board�迭�� ������ ���������� ����Ǿ��ִ�.
						if (board.getBoard()[y + row][x + CHANGE_X + col] != null) { // �ش� ���尪�� ����� ��Ʈ�� �����ϸ�
							moveX = false;											// false�� ������ ����
						}
					}
				}
			}
			if (moveX) {
				x = x + CHANGE_X; // �Լ��� ȣ��� �̺�Ʈ�� ���� ��� ���� ��ġ�� (x=3) ���� ������ �����ŭ ����ȴ�.
			}
		}
		CHANGE_X = 0; // ������ �ٽ� ������ ����� �ʱ�ȭ ��Ű�� �̺�Ʈ�� ��ٸ���.
		
		

			// y��ǥ ���� Ȯ�ζ� (y �ϴ� ����)
		if (System.currentTimeMillis() - START_TIME > DELAYTIME_OPERATION) {
			
			// ���Ʒ� �������� ��Ʈ���ϴ� �����̴�.
			// ������ y��ǥ�� �ϴ������� ����ԵǴ°�� (���� �ٴڰ� �´�� ���) BUMPER������ true��
			// �� ���� ����� -> ���� ���ϼ� �ֵ��� �����ִ� ��Ʈ
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
				if (!BUMPER) {	//�ٴ� ��ǥ (y�� �ٴ�) ������ �ʴ� ��Ʈ���� y���� ������ ����  ����� ��� �̵��ϴ�.
					y++;
				}
			} else {
				BUMPER = true;
			}

			START_TIME = System.currentTimeMillis();
		}
	}


	
	// ������ ���� ���پ� Ȯ��,�����ϴ� �޼ҵ��.
	private void checkLine() {
		int bottom = board.getBoard().length - 1;						//������ �������� �ٴں��� ������ ���� ��ġ�� �����ϴ��� Ȯ���ϰ� 
		for (int top = board.getBoard().length - 1; top > 0; top--) {	//���ʷ� ������� ����ٴϸ鼭 Ž���� ����� ������ ���ϴ�. 
			int cnt = 0;												//������ �Ʒ������� ù��° �� ���� ���پ� ���ʷ� üũ���ϰ�
			for (int col = 0; col < board.getBoard()[0].length; col++) {//�� ������, �� ������ üũ�ذ��� ���� �����Ѵ�.(bottom--)	
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
	
	

	// ����� ȸ���� �����ִ� �ĵ��.
	public void rotationShape() {
		int[][] rotatedShape = transMatrix(coor);
		reverseRow(rotatedShape); // ������ȸ���Ǿ��ٰ� �� rotatedShape�� �迭�� ������ �ݴ�� �迭�Ѱ��̱⿡ ������ 
									// ȸ���� �ؼ� ���ü� ���� ���°� �߻��Ѵ�. �������ο츦 ���� �̸� �ٷ� ��´�. �Ʒ� �����ڵ带  Ȯ������.
		
		// ȸ���Ҷ� ����� ���� (ȸ���ϸ鼭 ���� �ٴ��� �վ������) Ȯ�ζ��̴�.
		if (x + rotatedShape[0].length > tBoard.SET_tBOARD_WID || (y + rotatedShape.length > tBoard.SET_tBOARD_HEI)) {
			return;
		}

		// ȸ���� �ٸ� ���� �⵿�Ͽ� ���� ���������� ������ �վ�����°� ����, Ȯ���ϴ� ���̴�.
		for (int row = 0; row < rotatedShape.length; row++) {			//ȸ���� �ϸ鼭 ������ �����ϴ� ����� ���μ��δ� 
			for (int col = 0; col < rotatedShape[row].length; col++) {	//�ٸ��⶧���� ����� �����ġ���� ȸ���� �߳Ŀ����� 
				if (rotatedShape[row][col] != 0) {						//�ʿ���ϴ� ������ �ٸ���. 
					if (board.getBoard()[y + row][x + col] != null) {	
						return;
					}
				}
			}
		}

		coor = rotatedShape;
	}


	// ����� ������� ���������ִ� ����
	public int[][] transMatrix(int[][] matrix) {				// ������ ����� ȸ���� ��Ű�� ��, 1������ ó���� �ϴ±����̴�.
		int[][] temp = new int[matrix[0].length][matrix.length];// �ٸ� �迭�� ��������� ��ǥ���� ���� ������� �ڹٲ�־� 
		for (int row = 0; row < matrix.length; row++) {			// ����� ������Ų��.
			for (int col = 0; col < matrix[0].length; col++) {	// ������ �̴� ȸ���� �ؼ� ���ü��� ���� ����̱⿡ �Ʒ��� �Լ��� ���� 
				temp[col][row] = matrix[row][col];				// ȸ���� ȿ���� ���� �ִ�.
			}
		}
		return temp;
	}

	public void reverseRow(int[][] matrix) {					// Ʈ�������� ������ temp�迭�� ���� ��ġ ������
		int mid = matrix.length / 2;							// �ڹٲ��� ���� ȸ���� ȿ���� ����.
		for (int row = 0; row < mid; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length - row - 1];
			matrix[matrix.length - row - 1] = temp;
		}

	}

	// ����Ǵ� ����� ���¸� ���߰� ���ִ� �޼ҵ��. 
	// �����ǿ� ��Ÿ���� ����� ����� ������ ä���ִ� ������ �����Ѵ�.
	public void render(Graphics g) {
		for (int row = 0; row < coor.length; row++) {
			for (int col = 0; col < coor[0].length; col++) {
				if (coor[row][col] != 0) {
					g.setColor(color);
					g.fillRect(col * tBoard.SET_tSPACE_SIZE + x * tBoard.SET_tSPACE_SIZE, row * tBoard.SET_tSPACE_SIZE + y * tBoard.SET_tSPACE_SIZE,
							tBoard.SET_tSPACE_SIZE, tBoard.SET_tSPACE_SIZE);
				} //������ ��ϻ������ ��ǥ ĭ���� ����� �ܺϿ��� ������ ���� ���õ� ��ǥ�� �°� �����ϰ� ä���ش�.

			}
		}
	}

	// ���� �޼ҵ�
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

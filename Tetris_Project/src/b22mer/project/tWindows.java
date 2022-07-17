package b22mer.project;
import javax.swing.JFrame;
//이름: 정원철, 학과: 컴퓨터공학과 ,학번:12162149
public class tWindows {
	private JFrame twindow;
	private tBoard board;

	public tWindows() {
		twindow = new JFrame("Woncheol's Tetris"); // jFrame을 통해 상단 title바에 적당한 이름을 설정한다
		twindow.setSize(480, 640); // 게임창 사이즈를 설정한다
		twindow.setResizable(false); // 창 설정후 마우스 드래그를 이용한 사이즈 크기 설정 불가
		twindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 실제 창의 종료 버튼인 x를 누르면 종료되는 기능
		twindow.setVisible(true); // 유저의 눈에 보이게 설정 화면 지정
		board = new tBoard(); // 보드 객체를 할당
		board.BlockFileCall(); //이전까지 저장되었던 테트리스게임의 블럭기록을 가져오는 메소드이다, 실행되는 순간 저장된 기록이 연속해서 실행된다
		board.TetrisCall(); // 이전까지 저장되었던 테트리스게임의 점수기록을 가져오는 메소드이다, 실행되는 순간 저장된 기록이 연속해서 실행된다
		twindow.addKeyListener(board); // tBoard에서 처리한 이벤트처리를 추가한다
		twindow.add(board); // 마찬가지로 보드의 전반적인 내용을 추가한다

	}

	public static void main(String[] args) {

		new tWindows(); // 생성자를 통한 게임실행

	}

}



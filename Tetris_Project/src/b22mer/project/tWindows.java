package b22mer.project;
import javax.swing.JFrame;
//�̸�: ����ö, �а�: ��ǻ�Ͱ��а� ,�й�:12162149
public class tWindows {
	private JFrame twindow;
	private tBoard board;

	public tWindows() {
		twindow = new JFrame("Woncheol's Tetris"); // jFrame�� ���� ��� title�ٿ� ������ �̸��� �����Ѵ�
		twindow.setSize(480, 640); // ����â ����� �����Ѵ�
		twindow.setResizable(false); // â ������ ���콺 �巡�׸� �̿��� ������ ũ�� ���� �Ұ�
		twindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���� â�� ���� ��ư�� x�� ������ ����Ǵ� ���
		twindow.setVisible(true); // ������ ���� ���̰� ���� ȭ�� ����
		board = new tBoard(); // ���� ��ü�� �Ҵ�
		board.BlockFileCall(); //�������� ����Ǿ��� ��Ʈ���������� ������� �������� �޼ҵ��̴�, ����Ǵ� ���� ����� ����� �����ؼ� ����ȴ�
		board.TetrisCall(); // �������� ����Ǿ��� ��Ʈ���������� ��������� �������� �޼ҵ��̴�, ����Ǵ� ���� ����� ����� �����ؼ� ����ȴ�
		twindow.addKeyListener(board); // tBoard���� ó���� �̺�Ʈó���� �߰��Ѵ�
		twindow.add(board); // ���������� ������ �������� ������ �߰��Ѵ�

	}

	public static void main(String[] args) {

		new tWindows(); // �����ڸ� ���� ���ӽ���

	}

}



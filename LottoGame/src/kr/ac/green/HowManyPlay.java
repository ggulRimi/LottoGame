package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class HowManyPlay extends JFrame {

	// 정상적으로 종료
	public static final int NORMAL = 0;
	// 에러로 인해 종료
	public static final int ERROR = -1;
	
	private JButton btnOk;
	private JButton btnCancel;
	private JLabel lblGameInfo;
	private JTextField tfGameInfo;
	private JLabel lblMoney;
	private JLabel lblLotto;
	
	// 게임 횟수
	public int numOfGame;

	public HowManyPlay() {
		init();
		setDisplay();
		addListener();
		showFrame();
	}

	public void init() {
		btnOk = new JButton("확인");
		btnCancel = new JButton("취소");
		lblGameInfo = new JLabel("몇 장 구입하시겠습니까?(1~5)");
		lblGameInfo.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		tfGameInfo = new JTextField(20);
		// 스마일 이미지 삽입
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("Money.png");
		img = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		lblMoney = new JLabel(new ImageIcon(img));
		// 우림복권 이미지 삽입
		Toolkit toolkit2 = Toolkit.getDefaultToolkit();
		Image img2 = toolkit2.getImage("LottoStart.png");
		img2 = img2.getScaledInstance(300, 120, Image.SCALE_SMOOTH);
		lblLotto = new JLabel(new ImageIcon(img2));
	}

	public void setDisplay() {

		JPanel pnlNorth = new JPanel(new GridLayout(1,1));
		pnlNorth.add(lblLotto);
		lblLotto.setBorder(new LineBorder(Color.GRAY, 2));
		
		JPanel pnlBtn = new JPanel();
		pnlBtn.add(btnOk);
		pnlBtn.add(btnCancel);

		JPanel pnlQmark = new JPanel();
		pnlQmark.add(lblMoney);

		JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
		pnlInfo.add(lblGameInfo);
		pnlInfo.add(tfGameInfo);

		//하나의 메인패널로 묶어서 크기 조절함.
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		pnlMain.add(pnlQmark, BorderLayout.WEST);
		pnlMain.add(pnlInfo, BorderLayout.CENTER);
		pnlMain.add(pnlBtn, BorderLayout.SOUTH);

		add(pnlMain);

	}

	public void addListener() {
		// X 창 닫을때
		addWindowListener(
		new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				closeWindow();
			}
		});
		ActionListener listener = new ActionListener() {
			// 취소버튼 눌렀을 때
			@Override
			public void actionPerformed(ActionEvent ae) {
				Object src = ae.getSource();
				// 버튼 Ok 누를때나 엔터키 입력할때 
				if (src == btnOk || src == tfGameInfo) {
					String input = tfGameInfo.getText();
					try {
						numOfGame = Integer.parseInt(input);	
						// 0 이하, 5 이상의 숫자를 입력 했을 때
						if (numOfGame > 5 | numOfGame <= 0) {
							showMessage("1 이상 5 이하의 숫자만 가능합니다.");
						} else {
							new LottoGame(numOfGame);
							setVisible(false);
						}
						// 숫자가 아닌 문자나 다른 기호 입력시
					} catch (NumberFormatException e) {
						showMessage("숫자만 입력가능합니다");
					}
				} else {
					closeWindow();
				}
			}
		};
		btnOk.addActionListener(listener);
		btnCancel.addActionListener(listener);
		tfGameInfo.addActionListener(listener);
	}

	public void showFrame() {
		setTitle("로또");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	// 메세지 뜨는창
	private void showMessage(String message) {
		JOptionPane.showConfirmDialog(
				this,
				message,
				"알림",
				JOptionPane.CLOSED_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
	}
	// 창 닫기
	public void closeWindow() {
		int result = JOptionPane.showConfirmDialog(
				this,
				"종료하시겠습니까?",
				"종료",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(NORMAL);
		}
	}

	public static void main(String[] args) {
		new HowManyPlay();

	}

}


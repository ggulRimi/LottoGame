package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class GameResult extends JDialog {

	private JLabel lblPhoto;
	// 당첨 결과 숫자 들어가는 레이블
	private JLabel[] lblResultNums;
	private JLabel lblPlus;
	private JLabel lblBonus;
	private JLabel lblInfo;
	
	private JPanel pnlResultNum;
	private JPanel pnlNorth;
	private JPanel pnlCenter;
	private JPanel pnlSouth;
	private LineBorder lBorder;

	private JButton btnClose;
	private JButton btnTrace;
	private JButton btnRetry;
	
	// MyLotto에서 한 게임씩 값을 받아오는 list<String>.
	private List<String> orderOfGame;
	
	AutoSelect resultSelect;
	List<String> result;
	String bonus;

	public GameResult(List<List> MyLotto) {
		makeResultNum();
		init();
		setDisplay();
		makeResultPanel();
		addListeners();
		showFrame();
	}
	private void makeResultNum() {
		// AutoSelect를 사용해서 결과 번호와 보너스 번호를 새로 만들기 위해 생성
		resultSelect = new AutoSelect();
		// autoSelect메서드를 한번 실행
		resultSelect.autoSelect();
		// 위의 실행으로 나온 6개의 숫자와 보너스 숫자를 result와 bonus에 대입 
		result = resultSelect.getAutoNum();
		bonus = resultSelect.getBonus();
	}

	private void init() {
		// 이미지를 가지고 오는 기능을 가진 객체
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("Lotto.png");
		img = img.getScaledInstance(474, 300, Image.SCALE_SMOOTH);
		lblPhoto = new JLabel(new ImageIcon(img));
		lblPhoto.setOpaque(true);
		lblPhoto.setBackground(Color.WHITE);
		// plus 레이블 
		lblPlus = new JLabel("+", JLabel.CENTER);
		lblPlus.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		
		// 레이블을 만들어서 위에서 만든 6개의 당첨번호를 집어넣고, 폰트와 사이즈를 설정하고 레이블 배열에 넣어두기
		lblResultNums = new JLabel[6];
		for (int i = 0; i < lblResultNums.length; i++) {
			JLabel lbl = new JLabel(result.get(i), JLabel.CENTER);
			lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 35));
			lbl.setPreferredSize(new Dimension(50, 50));
			lblResultNums[i] = lbl;
		}
		// 보너스 번호 삽입
		lblBonus = new JLabel(bonus, JLabel.CENTER);
		lblBonus.setFont(new Font(Font.DIALOG, Font.BOLD, 35));
		lblBonus.setPreferredSize(new Dimension(50, 50));
		lblBonus.setBorder(new LineBorder(Color.GRAY, 2));
		// 당첨번호 밑에 info 넣기
		lblInfo = new JLabel("-일치하는 번호는 노란색, 보너스번호는 초록색으로 표시됩니다-", JLabel.CENTER);
		lblInfo.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		lblInfo.setOpaque(true);
		lblInfo.setBackground(Color.WHITE);
		
		pnlNorth = new JPanel(new BorderLayout());
		pnlResultNum = new JPanel();
		pnlCenter = new JPanel(new GridLayout(0, 1));
		pnlSouth = new JPanel();
	
		lBorder = new LineBorder(Color.BLACK, 2);

		btnClose = new JButton("닫기");
		btnTrace = new JButton("트레이스");
		btnRetry = new JButton("한 번 더~?");
	}

	private void setDisplay() {
		pnlResultNum.setOpaque(true);
		pnlResultNum.setBackground(Color.WHITE);
		// 번호를 넣어서 셋팅해둔 레이블 배열에서 하나씩 꺼내서 패널에 집어넣기
		for (int i = 0; i < lblResultNums.length; i++) {
			lblResultNums[i].setBorder(new LineBorder(Color.GRAY, 2));
			pnlResultNum.add(lblResultNums[i]);
		}
		pnlResultNum.add(lblPlus);
		pnlResultNum.add(lblBonus);

		pnlNorth.setBorder(lBorder);
		pnlNorth.add(lblPhoto, BorderLayout.NORTH);
		pnlNorth.add(pnlResultNum, BorderLayout.CENTER);
		pnlNorth.add(lblInfo, BorderLayout.SOUTH);

		pnlSouth.add(btnClose);
		pnlSouth.add(btnTrace);
		pnlSouth.add(btnRetry);

		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
	}

	private void addListeners() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Object src = ae.getSource();
				String traceRank = "";
				// 닫기 누르면 종료
				if (src == btnClose) {
					System.exit(0);
				// 트레이스 버튼
				} else if (src == btnTrace) {
					
					int count = 0;
					boolean flag = true;
					int traceIdx = 0;
					do {
						AutoSelect asGoal = new AutoSelect();
						asGoal.autoSelect();
						List<String> goal = asGoal.getAutoNum();
						String goalBonus = asGoal.getBonus();
						
						for (int i = 0; i < LottoGame.times; i++) {
							List<String> trace = LottoGame.myLotto.get(i);
							count++;
							goal.retainAll(trace);
							// 6개 일치
							if (goal.size() == 6) {
								traceRank = "1등";
								traceIdx = (i + 1);
								flag = false;
							// 5개 일치하고, 보너스 번호 일치
							} else if (goal.size() == 5 && trace.contains(goalBonus)) {
								traceRank = "2등";
								traceIdx = (i + 1);
								flag = false;
							}							
							goal = asGoal.getAutoNum();
						}
						if (!flag) {
							
							// 트레이스 결과창
							int traceOption = JOptionPane.showConfirmDialog(
									GameResult.this,
									(traceIdx + "번 게임이 " + count + "번 만에 " + traceRank + "에 당첨되었습니다."),
									"트레이스",
									JOptionPane.CLOSED_OPTION,
									JOptionPane.PLAIN_MESSAGE);
						}
					} while(flag);
					// 그래도 한번더? 버튼 누르면
				} else if (src == btnRetry) {
					// 팝업창 띄우기
					new Popup();
					setVisible(false);
				}
			}

		};

		btnClose.addActionListener(listener);
		btnTrace.addActionListener(listener);
		btnRetry.addActionListener(listener);
	}

	private void showFrame() {
		setTitle("우림복권 당첨결과");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public void makeResultPanel() {
		// 자신의 로또번호가 담긴 판넬을 생성해서 패널센터에 하나씩 추가
		for (int i = 0; i < LottoGame.times; i++) {
			ResultPanel pnlrs = new ResultPanel(i);
			pnlCenter.add(pnlrs);
		}
	}

	class ResultPanel extends JPanel {
		private JLabel lblOrder;
		private JLabel lblWinOrLose;
		private JLabel[] lblMyNumResult;
		
		// 판넬 생성자
		public ResultPanel(int order) {
			resultSet(order);
		}

		private void resultSet(int order) {
			// 게임 순서
			lblOrder = new JLabel(String.valueOf(order + 1) + "번 게임");
			lblOrder.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
			// 자신의 로또번호 한 세트를 담는 레이블
			lblMyNumResult = new JLabel[6];
			// LottoGame에서 가져온 MyLotto의 번호6개를 
			// List<String>인 orderOfGame에 담는다.
			orderOfGame = LottoGame.myLotto.get(order);
			// lblCheck 레이블에 일치하는 번호는 노란색, 보너스 번호는 초록색으로 표시한다.
			for (int i = 0; i < lblMyNumResult.length; i++) {
				JLabel lblCheck = new JLabel(orderOfGame.get(i), JLabel.CENTER);
				lblCheck.setOpaque(true);
				lblCheck.setBackground(Color.WHITE);
				lblCheck.setBorder(lBorder);
				lblCheck.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
				lblCheck.setPreferredSize(new Dimension(40, 40));
				if(result.contains(orderOfGame.get(i))) {
					lblCheck.setBackground(Color.YELLOW);
				} else if(orderOfGame.get(i).equals(bonus)) {
					lblCheck.setBackground(Color.GREEN);
				}
				lblMyNumResult[i] = lblCheck;
			}
			// 등수 표현
			lblWinOrLose = new JLabel("", JLabel.CENTER);
			lblWinOrLose = setRank();
			lblWinOrLose.setPreferredSize(new Dimension(50, 40));
			lblWinOrLose.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
			add(lblOrder);
			add(lblWinOrLose);
			for(JLabel lamp : lblMyNumResult) {
				add(lamp);
			}
		}

		// 등수 마다 다른 색상으로 표시하기
		public JLabel setRank() {
			// 교집합을 구하기 위한 리스트<스트링>객체를 하나 만들어서
			List<String> tempList = new Vector<String>();
			// 일단 자신의 로또번호를 넣고
			for (int i = 0; i < 6; i++) {
				tempList.add(orderOfGame.get(i));
			}
			// 교집합을 구한다
			tempList.retainAll(resultSelect.getAutoNum());
			// 교집합(서로 같은 원소가 교집합이니까 즉, 맞는 숫자)의 사이즈 = 맞는 갯수
			int count = tempList.size();
			// 일단 꽝이라고 지정해두고
			JLabel lbltxt = new JLabel("꽝", JLabel.CENTER);
			lbltxt.setForeground(Color.RED);
			switch (count) {

			case 6:
				// 만약에 6개가 맞으면 1등
				lbltxt.setText("1등");
				lbltxt.setForeground(Color.YELLOW);
				break;
			case 5:
				// 5개가 맞으면 3등인데
				// 여기에 (일단 6개 중에 5개 맞았으니까) 남은 하나의 숫자가 보너스 번호와 일치하면 2등
				if (orderOfGame.contains(resultSelect.getBonus())) {
					lbltxt.setText("2등");
					lbltxt.setForeground(Color.GRAY);
				} else {
					lbltxt.setText("3등");
					lbltxt.setForeground(Color.ORANGE);
				}
				break;
			case 4:
				// 4개 맞으면 4등
				lbltxt.setText("4등");
				lbltxt.setForeground(Color.GREEN);
				break;
			case 3:
				// 3개 맞으면 5등
				lbltxt.setText("5등");
				lbltxt.setForeground(Color.BLUE);
				break;
			}
			return lbltxt;
		}
		

	}

}

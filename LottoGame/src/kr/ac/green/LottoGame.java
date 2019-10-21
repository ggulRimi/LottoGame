package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class LottoGame extends JFrame {
	static int times;
	// 리스트를 담는 리스트
	static List<List> myLotto;

	private JButton btnStart;
	private JButton btnReset;
	private JButton btnExit;

	private JPanel pnlCenter;
	private JPanel pnlSouth;

	// 게임 한번 할 때마다 자동,수동,추출버튼,레이블들을 담겨있는 테이블을 담는 배열
	private Table[] tables;

	public LottoGame(int num) {
		this.times = num;
		init();
		setDisplay();
		makeTable();
		addListener();
		showFrame();
	}

	private void init() {
		
		myLotto = new Vector<List>(times);

		// 시작버튼 비활성화
		btnStart = new JButton("시작");
		btnStart.setEnabled(false);
		btnReset = new JButton("초기화");
		btnExit = new JButton("나가기");

		tables = new Table[times];
	}

	private void setDisplay() {
		// 그리드를 사용해 아래방향으로 패널 붙이기
		pnlCenter = new JPanel(new GridLayout(0, 1));

		pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlSouth.add(btnStart);
		pnlSouth.add(btnReset);
		pnlSouth.add(btnExit);

		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);

	}

	public void makeTable() {
		// 게임횟수를 이용한 타이틀을 붙인패널을 게임횟수만큼 생성해서 LottoGame 프레임의 센터에 붙인다.
		for (int i = 0; i < times; i++) {
			Table tb = new Table();
			// 테이블을 만들면서 TitleBorder를 이용하여 i번째 게임 표시해준다.
			tb.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), (i + 1) + "번 게임"));
			// 테이블을 배열에 담아준다.
			tables[i] = tb;
			// 그리고 센터에 붙여준다.
			pnlCenter.add(tb);
		}
		
	}
	private void addListener() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Object src = arg.getSource();
				if (src == btnStart) {
					// 앞에서 담아둔 테이블의 myNum를 MyLotto 리스트에 담아준다.
					for (int i = 0; i < times; i++) {
						myLotto.add(tables[i].myNum);
					}
					// 시작버튼 누르면 myLotto(내번호) 가지고 GameResult창으로
					// 이동하고, 지금 떠있는 LottoGame창을 안보이게 한다.
					new GameResult(myLotto);
					setVisible(false);
				} else {
					dispose(); //Exit버튼 누르면 꺼지기
				}
			}
		};
		btnStart.addActionListener(listener);
		btnExit.addActionListener(listener);
	}


	private void showFrame() {
		setTitle("Lotto Game");
		setLocationRelativeTo(null);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

	// 추출버튼이 모두 눌러지면 Start버튼 활성화 시키기
	public boolean readyToStart() {
		int count = 0;
		boolean ready = false;
		// 모든 테이블의 추출버튼이 전부 비활성화가 되면 Start버튼이 활성화 된다.
		for(int i = 0; i < times; i++) {
			if(tables[i].btnBuy.isEnabled() == false) {
				count++;
			}
			if(count == times){
				ready = true;
			}
		} 
		return ready;
	}
	class Table extends JPanel {
		// 자동,수동 번호를 담는 리스트
		List<String> myNum;

		JRadioButton rbtnAuto;
		JRadioButton rbtnManual;
		JButton btnBuy;
		// X 자리에 숫자를 표시할 레이블들을 담는 배열
		// Table이 내부 클래스이므로 밖에서 불러서 쓰려고 private를 안썼음.
		JLabel[] lblMyNum;
		// AutoSelect를 쓰기위해 여기서 새롭게 객체생성.
		private AutoSelect as;

		public Table() {
			tableSet();
			addListener();
		}

		private void tableSet() {
			as = new AutoSelect();
			myNum = new Vector<String>();
			
			rbtnAuto = new JRadioButton("자동", true);
			rbtnManual = new JRadioButton("수동");
			btnBuy = new JButton("추출");
			// 자동, 수동중 하나만 선택하기 위해 그룹을 지어줌.
			ButtonGroup bg = new ButtonGroup();
			bg.add(rbtnAuto);
			bg.add(rbtnManual);
			// 배열길이가 6인 Mynum 레이블을 만들어줌.
			lblMyNum = new JLabel[6];
			LineBorder lBorder = new LineBorder(Color.BLACK, 1);
			// for문을 이용하여 레이블을 생성 및 서식을 지정하고,
			for (int i = 0; i < lblMyNum.length; i++) {
				JLabel dft = new JLabel("X", JLabel.CENTER);
				dft.setOpaque(true);
				dft.setBackground(Color.WHITE);
				dft.setBorder(lBorder);
				dft.setPreferredSize(new Dimension(40, 20));
				//레이블 배열(lblMyNum)에 넣음.
				lblMyNum[i] = dft;
			}
			add(rbtnAuto);
			add(rbtnManual);
			add(btnBuy);
			// 앞에서 지정한 레이블을 차례대로 붙여줌.(xxxxxx)
			for (JLabel temp : lblMyNum) {
				add(temp);
			}
		}
		// 리셋하는 메서드
		private void reset() {
			for (int i = 0; i < times; i++) {
				// 수동,자동 활성화
				// 자동이 선택되게
				// 수동이 선택안되게
				// 추출버튼이 보이게
				tables[i].rbtnAuto.setEnabled(true);
				tables[i].rbtnManual.setEnabled(true);
				tables[i].rbtnAuto.setSelected(true);
				tables[i].rbtnManual.setSelected(false);
				tables[i].btnBuy.setEnabled(true);
				// 다시 lblMyNum에 X를 차례대로 대입
				for (int j = 0; j < lblMyNum.length; j++) {
					tables[i].lblMyNum[j].setText("X");
				}

			}
		}
		
		public void addListener() {
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Object src = ae.getSource();
					// 추출버튼 눌렀을 때
					if (src == btnBuy) {
						// 자동버튼이 선택되면 lblMyNum에 차례대로 넣는다
						if (rbtnAuto.isSelected()) {
							// autoSelect를 실행하면 안에 
							// autoNum인 List<String>이 자동으로 6개의 번호 생성
							// 그 번호를 MyNum에 지정
							as.autoSelect();
							myNum = as.getAutoNum();
							for (int i = 0; i < 6; i++) {
								lblMyNum[i].setText(myNum.get(i));
							}
							// 자동,수동, 추출버튼 비활성화
							rbtnAuto.setEnabled(false);
							rbtnManual.setEnabled(false);
							btnBuy.setEnabled(false);
							// 모든 추출버튼이 비활성화 되면 시작버튼 활성화.
							btnStart.setEnabled(readyToStart());
						} else {
							// 수동을 체크했을떄
							new SelectNumbers(Table.this);
						}
					} else {
						// 리셋
						reset();
					}
				}
			};
			btnBuy.addActionListener(listener);
			btnReset.addActionListener(listener);
		}

	}
}

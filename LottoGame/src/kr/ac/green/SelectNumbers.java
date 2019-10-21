package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import kr.ac.green.LottoGame.Table;

public class SelectNumbers extends JDialog {
	// 수동 체크박스 번호45개
	public static final int NUM = 45;
	
	private JCheckBox[] numbers;
	private JButton btnOk;
	private JButton btnReset;
	private JButton btnCancel;
	
	private Table owner;

	public SelectNumbers(LottoGame.Table owner) {
		super();
		this.owner = owner;
	
		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	private void init() {
		// 체크박스 45개를 배열로 생성
		numbers = new JCheckBox[NUM];
		for (int idx = 0; idx < NUM; idx++) {
			numbers[idx] = new JCheckBox(String.valueOf(idx + 1));
		}

		btnOk = new JButton("확인");
		btnReset = new JButton("리셋");
		btnCancel = new JButton("취소");
	}

	private void setDisplay() {
		// CENTER는 숫자선택부/SOUTH는 버튼부
		
		// 숫자선택부는 GridLayout으로 (9, 5)로 나누고 가시성을 위해 세로간격을 10으로 추가
		JPanel pnlCenter = new JPanel(new GridLayout(9, 5, 10, 0));
		// LineBorder로 적용할 선 형식을 생성
		LineBorder lBorder = new LineBorder(Color.GRAY, 1);
		// TitledBorder로 Default위치인 좌측위에 "번호 선택"이라는 타이틀로 생성하며 동시에 위에서 생성한 lBorder를 적용
		TitledBorder tBorder = new TitledBorder(lBorder, "번호 선택");
		// tBorder를 판넬에 적용
		pnlCenter.setBorder(tBorder);
		// 만들어둔 체크박스 배열을 판넬에 부어넣기
		for (JCheckBox temp : numbers) {
			pnlCenter.add(temp);
		}
		// 버튼이 들어갈 pnlSouth를 만들고 순서대로 삽입
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnOk);
		pnlSouth.add(btnReset);
		pnlSouth.add(btnCancel);

		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
	}

	private void addListeners() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Object src = ae.getSource();
				// 버튼 Ok누르면 
				if (src == btnOk) {
					counter();
					owner.rbtnAuto.setEnabled(false);
					owner.rbtnManual.setEnabled(false);
					owner.btnBuy.setEnabled(false);
					
				} else if (src == btnReset) {
					for (JCheckBox temp : numbers) {
						if (temp.isSelected()) {
							temp.setSelected(false);
						}
					}
				// 즉 취소 버튼을 눌렀을 경우
				} else {
					dispose();
				}

			}
		};
		btnOk.addActionListener(listener);
		btnReset.addActionListener(listener);
		btnCancel.addActionListener(listener);
	}
	
	private void counter() {
		// 선택되었는지 확인 후 
		int count = 0;
		for (JCheckBox temp : numbers) {
			if (temp.isSelected()) {
				count++;
			}
		}
		// 6개가 선택되었으면 Owner의 myNum(list<String> 6개짜리)에 값을 대입
		if (count == 6) {
			for(JCheckBox temp : numbers) {
				if(temp.isSelected()) {
					owner.myNum.add(temp.getText());
				}
			}
			// X표에다가 list<String>의 값을 대입
			for (int i = 0; i < owner.lblMyNum.length; i++) {
				owner.lblMyNum[i].setText(owner.myNum.get(i));
			}
			// 다 넣었으니까 수동창 끄기
			dispose();
		// 선택이 6개 이하일때
		} else if (count < 6) {
			showMessage("선택번호가 부족합니다");
		// 선택이 6개 이상일때
		} else {
			showMessage("6개만 선택해야합니다");
		}
	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(
				SelectNumbers.this,
				message,
				"알림",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void showFrame() {
		setTitle("Select Numbers");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}

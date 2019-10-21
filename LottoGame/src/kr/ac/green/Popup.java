package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Popup extends JDialog {

	private JLabel lblPhoto1;
	private JButton btnExit;
	private JButton btnRetry;
	private JLabel lblPhotoRetry;

	public Popup() {
		init();
		setDisplay();
		addListener();
		showFrame();
	}

	private void init() {
		// Retry 이미지 넣기
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("Retry.png");
		img = img.getScaledInstance(629, 439, Image.SCALE_SMOOTH);
		lblPhoto1 = new JLabel(new ImageIcon(img));

		btnExit = new JButton("닫기");
		btnRetry = new JButton("그래도 다시");	
	}

	private void setDisplay() {
		JPanel pnlCenter = new JPanel();
		pnlCenter.add(lblPhoto1);
		
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnExit);
		pnlSouth.add(btnRetry);
		
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
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
		// 나가기 버튼
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Object src = ae.getSource();
				if (src == btnExit) {
					closeWindow();				
				} else {
					// 다시 HowManyPlay 초기창으로 가기
					new HowManyPlay();
					setVisible(false);
				}
			}
		};
		btnExit.addActionListener(listener);
		btnRetry.addActionListener(listener);
	}
	// 종료창
	public void closeWindow() {
		int result = JOptionPane.showConfirmDialog(
				this,
				"종료하시겠습니까?",
				"종료",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void showFrame() {
		setTitle("싸늘한 기운이 흐른다");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}
}
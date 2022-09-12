import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

class NumberLotsPanel extends JPanel implements ActionListener {

	JPanel main_panel;

	JButton bt_chooseNumRange, bt_drawingLots;

	JLabel lb_randomNum, lb_numRange;

	JMenuBar mb;
	JMenuItem mni_developmentInquiry, mni_developmentInfo;
	
	DefaultTableModel dtm;

	JTable tb_result;

	//---------------------------------------------------//
	
	String tbh_result[] = { "회차", "결과", "범위" };
	String contents[] = new String[3];

	int numRange, randomNum, time, count = 0, control = 0;

	boolean tf = false;

	String jop_message = "<프로그램 단축키>\n" +
						 "Space Bar : 뽑기\n" +
						 "R : 범위 지정\n" +
						 "(한번의 버튼 클릭이 있어야 단축키 사용이 가능합니다)\n\n" +
						 
						 "<프로그램 관련 정보>\n" +
						 "◆ 개발자: 성열암\n" +
						 "◆ 문의(카카오톡): sya3369";
	
	//--------------------------------------------------//
	
	Runtime runtime = Runtime.getRuntime();

	public NumberLotsPanel() {
		setLayout(new BorderLayout());
		// --------------------------//

		main_panel = new JPanel();
		main_panel.setLayout(null);

		// ------------------------//
		
		// 라벨 관련 정의 부분
		lb_randomNum = new JLabel();
		lb_randomNum.setOpaque(true);
		lb_randomNum.setBackground(Color.DARK_GRAY);
		lb_randomNum.setForeground(Color.WHITE);
		lb_randomNum.setFont(new Font("돋움", Font.BOLD, 100));

		lb_numRange = new JLabel(" 정해진 범위: 1~?");
		lb_numRange.setFont(new Font("돋움", Font.PLAIN, 15));
		lb_numRange.setOpaque(true);
		lb_numRange.setForeground(Color.WHITE);
		lb_numRange.setBackground(Color.BLACK);


		// 버튼 관련 정의 부분
		bt_chooseNumRange = new JButton("범위 설정");
		bt_chooseNumRange.setBackground(Color.BLACK);
		bt_chooseNumRange.setFont(new Font("바탕체", Font.BOLD, 18));
		bt_chooseNumRange.setFocusPainted(false); // 버튼 텍스트 포커스 표시 설정

		bt_drawingLots = new JButton("뽑기");
		bt_drawingLots.setBackground(Color.WHITE);
		bt_drawingLots.setFont(new Font("바탕체", Font.BOLD, 18));
		bt_drawingLots.setEnabled(false);
		bt_drawingLots.setFocusPainted(false);

		
		// 메뉴 관련 정의 부분
		mb = new JMenuBar();

		JMenu mn_info = new JMenu("  정 보  ");
		JMenu mn_exit = new JMenu("  종 료  ");
		mn_exit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});

		mni_developmentInquiry = new JMenuItem("개발 문의");
		mni_developmentInfo = new JMenuItem("프로그램 정보");

		
		// 테이블 관련 정의 부분
		dtm = new DefaultTableModel(null, tbh_result) {
			public boolean isCellEditable(int a, int b) {
				return false;
			}
		};

		tb_result = new JTable(dtm);
		tb_result.getTableHeader().setReorderingAllowed(false);
		tb_result.setFont(new Font(null, Font.PLAIN, 15));
		JScrollPane jsp_result = new JScrollPane(tb_result);

		// 컬럼 해더 높이
		JTableHeader header = tb_result.getTableHeader();
		header.setFont(new Font("함초롬바탕", Font.BOLD, 18));
		Dimension d = header.getPreferredSize();
		d.height = 25;
		header.setPreferredSize(d);

		// cell 높이
		tb_result.setRowHeight(20);

		// 셀 너비,셀 정렬 대한 설정
		DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
		cellCenter.setHorizontalAlignment(JLabel.CENTER);
		tb_result.getColumnModel().getColumn(0).setPreferredWidth(0);
		tb_result.getColumnModel().getColumn(0).setCellRenderer(cellCenter);
		tb_result.getColumnModel().getColumn(1).setPreferredWidth(150);
		tb_result.getColumnModel().getColumn(1).setCellRenderer(cellCenter);
		tb_result.getColumnModel().getColumn(2).setPreferredWidth(30);
		tb_result.getColumnModel().getColumn(2).setCellRenderer(cellCenter);

		// ------------------------------------//

		jsp_result.setBounds(8, 160, 480, 240);

		lb_randomNum.setBounds(8, 50, 500, 100);
		lb_numRange.setBounds(8, 8, 600, 30);
		bt_chooseNumRange.setBounds(120, 415, 120, 40);
		bt_drawingLots.setBounds(250, 415, 120, 40);

		// -----------------------------------//

		main_panel.add(jsp_result);

		main_panel.add(lb_randomNum);
		main_panel.add(lb_numRange);
		main_panel.add(bt_chooseNumRange);
		main_panel.add(bt_drawingLots);

		mn_info.add(mni_developmentInquiry);
		mn_info.addSeparator();
		mn_info.add(mni_developmentInfo);
		mb.add(mn_info);
		mb.add(mn_exit);

		add(main_panel);

		// ---------------------------------------//

		bt_chooseNumRange.addActionListener(this);
		bt_drawingLots.addActionListener(this);
		mni_developmentInquiry.addActionListener(this);
		mni_developmentInfo.addActionListener(this);
		
		bt_chooseNumRange.addKeyListener(new ShortCut());
		bt_drawingLots.addKeyListener(new ShortCut());


	}

	// 랜덤범위를 1~X 까지 선택되게 지정(다른값에 대해서는 반복을 통해제어)
	void chooseRange() {
		String StrNumRange;
		while (true) {
			UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("함초롬돋움", Font.PLAIN, 13)));
			StrNumRange = (String) JOptionPane.showInputDialog(null, "마지막 번호를 지정해 주세요(n≥1)");
      if (StrNumRange.equals(""))
        continue;
      if (Integer.parseInt(StrNumRange) > 1)
        break;
    }

		numRange = Integer.parseInt(StrNumRange);
		lb_numRange.setText(" 정해진 범위: 1~" + numRange);
	}

	@Override // 버튼 액션에 대한 처리 부분
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bt_chooseNumRange) {
			chooseRange();
			bt_drawingLots.setEnabled(true);
			tf = false;
		} else if (e.getSource() == bt_drawingLots) {
			bt_chooseNumRange.setEnabled(false);
			lb_randomNum.setVisible(true);
			randomNum = (int) (Math.random() * numRange + 1);
			tf = true;
			bt_drawingLots.setText("" + (time = 3));
		} else if (e.getSource() == mni_developmentInquiry) {
			try {
				runtime.exec("explorer.exe https://blog.naver.com/hotkimchi13/221319599879");
			} catch(IOException ex) {
				System.exit(0);
			}	
		} else if (e.getSource() == mni_developmentInfo) {
			UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("함초롬돋움", Font.PLAIN, 13)));
			JOptionPane.showMessageDialog(null, jop_message, "프로그램 정보", JOptionPane.INFORMATION_MESSAGE, null);
		}
	}
	
	// 단축키에 대한 처리 부분
	class ShortCut extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			String k = e.getKeyText(e.getKeyCode()); // 키값
			if(k.equals("r") || k.equals("R")) { bt_chooseNumRange.doClick(); return; }
			bt_drawingLots.doClick();
		}
	}
}

public class NumberLots extends JFrame {

	NumberLotsPanel cp = new NumberLotsPanel();

	public NumberLots() {
		setTitle("랜덤 번호 선택기");
		setSize(500, 523);
		setLocation(590, 250);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(cp.mb);

		add(cp);
		setVisible(true);

		// 시간의 흐름에 대한 정의 부분
		while (true) {
			if (cp.time == 0) {
				if (cp.tf) {
					cp.bt_chooseNumRange.setEnabled(true);
					cp.tf = false;
					cp.bt_drawingLots.setText("뽑기");
					cp.lb_randomNum.setText("" + cp.randomNum);
					cp.contents[0] = "" + (++cp.count);
					cp.contents[1] = "" + cp.randomNum;
					cp.contents[2] = "1~" + cp.numRange;
					cp.dtm.addRow(cp.contents);
				}
			}

			try {
				if (cp.tf)
					cp.bt_drawingLots.setText("" + cp.time--);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new NumberLots();
	}
}
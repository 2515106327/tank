package day02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @Discription 游戏开始界面，绘制背景图，添加开始游戏、退出游戏按钮。
 * @author 文成
 *
 */
public class StartFrame extends JFrame {

	private JPanel jp_center = new JPanel();// 创建一个背景图面板
	private JPanel jp_bottom = new JPanel();// 创建游戏开始界面按钮
	private JButton jp_1 = new JButton("开始游戏");
	private JButton jp_2 = new JButton("退出游戏");
	BackgroundPanel bgp;

	public StartFrame() {
		super("救世坦克大战");
		setSize(800, 450);
		setLocationRelativeTo(null);// 窗口居中
		setResizable(false);// 窗口不可变
		setDefaultCloseOperation(3);// 程序点击可退出;
		init();// 构造函数
		// 按钮上浮 和文字颜色
		jp_1.setContentAreaFilled(false);//按钮透明
		jp_1.setForeground(Color.ORANGE);//按钮颜色
		jp_2.setContentAreaFilled(false);
		jp_2.setForeground(Color.ORANGE);

		// 事件
		events();

		// 绘制背景图 
		Startbackground();

		setVisible(true);// 界面可见

	}

	void init() {

		setLayout(new BorderLayout());
		jp_bottom.add(jp_1);
		jp_bottom.add(jp_2);
	}

	//鍵盤事件方法
	void events() {
		// 退出按钮事件
		jp_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(jp_2)) {
					int i = JOptionPane.showConfirmDialog(jp_2, "是否要退出？");
					if (i == 0) {
						System.exit(0);
					}
				}

			}
		});
		// 开始游戏按钮事件
		jp_1.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// 单击鼠标左键
				if (e.getButton() == e.BUTTON1) {
					StartGame tc = new StartGame();
					tc.lauchFrame();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	//////////////////////////////////// 绘制背景图
	void Startbackground() {
		bgp = new BackgroundPanel((new ImageIcon("Recourse/Startimgs/开场背景.jpg")).getImage());
		bgp.setLayout(new BorderLayout());
		bgp.setSize(800, 450);
		add(bgp);
		// 前景改变
		jp_center.setOpaque(false);
		jp_bottom.setOpaque(false);
		bgp.add(jp_center, BorderLayout.CENTER);
		bgp.add(jp_bottom, BorderLayout.SOUTH);
	}

	// 背景面板
	class BackgroundPanel extends JPanel {
		Image im;

		public BackgroundPanel(Image im) {
			this.im = im;
			this.setOpaque(true); // 设置不可看见后景色
		}
		// Draw the back ground.
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);

		}
	}

	public static void main(String[] args) {
		new StartFrame();
	}
}

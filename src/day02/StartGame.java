package day02;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * @Class 游戏类
 * @author 文成
 */
public class StartGame extends Frame {

	//// 定义静态常量 游戲窗口大小
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HIGHT = 600;

	//添加我方坦克
	Tank myTank = new Tank(340, 560, true, Tank.Direction.STOP, this);
	//添加我方基地（Boss）
	Tank myBoss = new Tank(370, 530, true, Tank.Direction.STOP, this);

	List<Wall> walls = new ArrayList<>();
	// 添加墻
	Wall w1 = new Wall(100, 100, 20, 160, this);
	Wall w2 = new Wall(200, 300, 460, 20, this);
	Wall w3 = new Wall(540, 100, 20, 400, this);
	Wall w4 = new Wall(740, 100, 20, 400, this);
	Wall w5 = new Wall(50, 480, 200, 20, this);
	Wall w6 = new Wall(300, 400, 160, 20, this);
	Wall w7 = new Wall(200, 100, 20, 160, this);
	Wall w8 = new Wall(0, 380, 150, 20, this);
	Wall w9 = new Wall(300, 200, 150, 20, this);
	Wall w10 = new Wall(620, 500, 20, 100, this);
	// 炸弹 //ArrayList排序较快
	List<Explode> explodes = new ArrayList<Explode>();

	// 泛型 list 储存子弹
	List<Missile> missiles = new ArrayList<Missile>();

	// 敌机坦克
	List<Tank> tanks = new ArrayList<Tank>();

	// 双缓冲解决闪烁现象
	// 定义背后图片为空
	Image offScreenImage = null;

	// 绘制游戏内容
	public void paint(Graphics g) {
		// super.paint(g);//避免重画后原图片的残留

		// //显示子弹数量
		// g.drawString("missiles count:"+missiles.size(), 10,50);
		// //显示爆炸数量
		// g.drawString("explodes count"+explodes.size(), 10, 70);
		// 显示敌机坦克数量

		// 游戏玩法说明
		g.drawString("1.按方向键移动我方坦克", 10, 50);
		g.drawString("2.Space开火", 10, 70);
		g.drawString("3.Ctrl超级火炮", 10, 90);
		g.drawString("4.F2我方坦克原地复活 ", 180, 50);
		g.drawString("游戏任务： ", 380, 50);
		g.drawString("1.消灭所有入侵敌方", 380, 70);
		g.drawString("2.保护我方黄色基地", 380, 90);
		g.drawString("敌方坦克剩余数量： " + tanks.size(), 580, 50);
		// 繪製子弹
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);// 拿出每一发子弹
			m.hitTacks(tanks);// 我方坦克子弹可打击敌方坦克
			m.hitTack(myTank);// 敌方坦克可打击我方坦克
			m.hitTack(myBoss);// 敌方坦克可打击我方Boss

			// 子弹撞墙消失
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitWall(w3);
			m.hitWall(w4);
			m.hitWall(w5);
			m.hitWall(w6);
			m.hitWall(w7);
			m.hitWall(w8);
			m.hitWall(w9);
			m.hitWall(w10);
			// 画子弹
			m.draw(g);
		}

		// 绘制爆炸
		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		// 绘制坦克
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			// 敌方坦克不可过墙
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithWall(w3);
			t.collidesWithWall(w4);
			t.collidesWithWall(w5);
			t.collidesWithWall(w6);
			t.collidesWithWall(w7);
			t.collidesWithWall(w8);
			t.collidesWithWall(w9);
			t.collidesWithWall(w10);
			t.darwTank(g);
			t.collidesWithTanks(tanks);
		}

		// 画我方坦克
		myTank.darwTank(g);
		// 我方坦克不可过墙
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithWall(w3);
		myTank.collidesWithWall(w4);
		myTank.collidesWithWall(w5);
		myTank.collidesWithWall(w6);
		myTank.collidesWithWall(w7);
		myTank.collidesWithWall(w8);
		myTank.collidesWithWall(w9);
		myTank.collidesWithWall(w10);
		// 敌我坦克不可穿过
		myTank.collidesWithTanks(tanks);
		// 画墙
		for (Wall wall : walls) {
			wall.draw(g);
		}
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		w4.draw(g);
		w5.draw(g);
		w6.draw(g);
		w7.draw(g);
		w8.draw(g);
		w9.draw(g);
		w10.draw(g);
		myBoss.dorwBoss(g);

	}

	// 双缓冲解决闪烁现象
	public void update(Graphics g) {
		// 如果背景图为空
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HIGHT);
		}
		// 获取到图片上的画笔
		Graphics goffScreen = offScreenImage.getGraphics();
		// 刷新界面
		Color c = goffScreen.getColor();
		goffScreen.setColor(Color.GREEN);
		goffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HIGHT);
		goffScreen.setColor(c);

		paint(goffScreen);
		// 将后面的图片一次性拿到前面面板
		g.drawImage(offScreenImage, 0, 0, null);
	}

	// 游戏窗口启动
	public void lauchFrame() {

		// 添加多辆坦克
		for (int i = 0; i < 25; i++) {
			tanks.add(new Tank(30 * (i + 1), 50, false, Tank.Direction.D, this));
		}
		this.setSize(GAME_WIDTH, GAME_HIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setLocationRelativeTo(null);// 窗口居中
		this.setResizable(false);// 窗口不可变

		this.setBackground(Color.GREEN);

		// 添加鍵盤事件監聽
		this.addKeyListener(new KeyMonitor());

		// 启动线程
		new Thread(new PaintThread()).start();
		setVisible(true);
	}

	// 线程控制图片运动
	private class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();

				try { // 闪烁现象严重
					Thread.sleep(50);// 线程静态成员方法 重画
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// boss死亡退出游戏
				if (!myBoss.isLive()) {
					// 停止线程
					new Thread(new PaintThread()).stop();
					int i = JOptionPane.showConfirmDialog(null, "哦豁！游戏结束！！！");
					if (i == 0) {
						System.exit(0);
					}

				}

				// 赢
				if (tanks.size() <= 0) {
					new Thread(new PaintThread()).stop();
					int i = JOptionPane.showConfirmDialog(null, "哇哦！My heor! 你拯救了世界！！！");
					if (i == 0) {
						System.exit(0);
					}

				}

			}

		}

	}

	// 坦克键盘事件
	public class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);// 抬起键盘
		}

		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);// 按下键盘
		}

	}

	public static void main(String[] args) {
		StartGame tc = new StartGame();
		tc.lauchFrame();
	}
}

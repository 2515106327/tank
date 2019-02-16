package day02;

/**
 * @Class 子弹类 包含子弹自有的各种属性
 * @Discription 子弹自画  子弹运动 敌我子弹的存活 物理碰撞 返回子弹的物理范围
 * @author  文成
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {

	// 定义静态常量 子弹速度
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;

	// 定义静态常量 子弹大小
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	int x, y;// 定义子弹初始位置
	// 方向
	Tank.Direction dir;
	
	// 区分敌我子弹
	private boolean good;

	private boolean Live = true;// 子弹的存活情况
	private StartGame tc;/////////持有对方引用


	// 子弹Live 构造函数
	public boolean isLive() {
		return Live;
	}

	public void setLive(boolean live) {
		Live = live;
	}

	// 子弹类构造方法
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;

	}

	// 重载构造方法
	public Missile(int x, int y, boolean good, Tank.Direction dir, StartGame tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}

	// 画子弹
	public void draw(Graphics g) {

		// 提前判断子弹是否存活
		if (!Live) {
			tc.missiles.remove(this);
			return;
		}
		// 敌方坦克子弹颜色设置
		if (!good) {

			Color c = g.getColor();
			g.setColor(Color.BLACK);
			g.fillOval(x, y, WIDTH, HEIGHT);// 画圆圈 子弹
			g.setColor(c);// 画笔颜色还原
		}
		// 我方坦克子弹颜色设置
		else {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.fillOval(x, y, WIDTH, HEIGHT);// 画圆圈 子弹
			g.setColor(c);// 画笔颜色还原
		}
		move();// 子弹移动
	}

	// 子弹移动方法
	private void move() {

		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		}
		// 判断子弹是否存活 飞出屏幕边界 击中敌机坦克
		if (x < 0 || y < 0 || x > StartGame.GAME_WIDTH || y > StartGame.GAME_HIGHT) {
			{
				Live = false;
				// 子弹消失
			}
		}
	}

	// 碰撞辅助类 物理碰撞
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	// 子弹打击坦克
	public boolean hitTack(Tank t) {
		// 判断两个方块是否相交 且敌机坦克依然存活
		if (this.Live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			t.setLive(false);
			// 子弹也需要消失
			this.Live = false;

			// 在击中地方坦克的地方产生爆炸
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	// 打击坦克后坦克消失
	public boolean hitTacks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTack(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}

	// 子弹撞墙
	public boolean hitWall(Wall w) {
		if (this.Live && this.getRect().intersects(w.getRect())) {
			this.Live = false;
			return true;
		}

		return false;
	}

}

package day02;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * @Class 坦克类 包含坦克自有的各种属性
 * @Discription 坦克自画 坦克运动 炮筒 开火 超级开火 物理碰撞 返回坦克的物理范围
 * @author 文成
 */
class Tank {
	// 定义静态常量 坦克的移动速度
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;

	// 定义静态常量 坦克的大小
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private StartGame tc;
	// 坦克敌我区分
	private boolean good;

	// good 的get方法
	public boolean isGood() {
		return good;
	}

	// 坦克是否存活
	private boolean live = true;

	// live 的get方法
	public boolean isLive() {
		return live;
	}

	// live 的set方法
	public void setLive(boolean live) {
		this.live = live;
	}

	private int x, y;// 定义坦克的起始位置
	private int oldx, oldy;// 定义坦克的上一步位置

	// 敌机坦克随机动
	private static Random r = new Random();

	private boolean bL = false, bU = false, bR = false, bD = false;

	// 枚举 可解决坦克方向改变迟钝的现象
	enum Direction {
		L, U, R, D, STOP
	};

	private Direction dir = Direction.STOP;// 坦克的停止位置（方向）
	private Direction ptDir = Direction.D;// 炮筒方向

	// 记录敌方坦克运动到第几步 随机步
	private int step = r.nextInt(12) + 3;

	// Tack类的构造方法
	public Tank(int x, int y, boolean good) {

		this.x = x;
		this.y = y;

		this.good = good;
	}

	// 调用上面的构造方法 持有对方的引用
	public Tank(int x, int y, boolean good, Direction dir, StartGame tc) {
		this(x, y, good);
		this.dir = dir;
		this.oldx = x;
		this.oldy = y;
		this.tc = tc;
	}

	// 画坦克
	public void darwTank(Graphics g) {
		if (!live)// 如果没活着 就停止重画
		{
			if (!good) {
				tc.tanks.remove(this);// 不存活就删除
			}
			return;
		}
		Color c = g.getColor();
		if (good)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);// 画一个实心圆圈
		g.setColor(c);
		// 根据坦克方位绘制炮筒
		switch (ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
			;
			break;
		case D:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT);
			break;
		case STOP:
			break;
		}

		move();// 移动
	}

	// 画我方基地Boss
	public void dorwBoss(Graphics g) {
		if (!live)// 如果没活着 就停止重画
		{
			if (!good) {
				tc.tanks.remove(this);// 不存活就删除
			}
			return;
		}
		Color c = g.getColor();
		if (good)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.BLUE);
		g.fillOval(x, y, 60, 60);// 画一个圆圈
		g.setColor(c);

	}

	// 坦克移动
	public void move() {
		this.oldx = x;
		this.oldy = y;// 记录坦克上一步所在的位置
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
		case STOP:
			break;
		}
		// 判断炮筒的方向 坦克不停 炮筒一直重绘
		if (this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}

		// 判断坦克不能出界
		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x + Tank.WIDTH > StartGame.GAME_WIDTH)
			x = StartGame.GAME_WIDTH - Tank.WIDTH;
		if (y + Tank.HEIGHT > StartGame.GAME_HIGHT)
			y = StartGame.GAME_HIGHT - Tank.HEIGHT;

		// 敌机坦克实现智能运动
		if (!good) {

			// 将 Direction 中的值转换为数
			Direction[] dirs = Direction.values();
			if (step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);// 在dir范围内变换;
				dir = dirs[rn];
			}

			step--;
			// 子弹发射频率
			if (r.nextInt(40) > 38)
				this.fire();
		}

	}

	// 按下键 后的事件
	public void KeyPressed(KeyEvent e) {
		// 复活我方坦克
		if (e.getKeyCode() == KeyEvent.VK_F2) {
			if (!this.live) {
				this.live = true;
			}
		}
		// 如果按了空格键
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			superFire();
		}

		// 如果按了上键
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			bU = true;
		}
		// 如果按了下键
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			bD = true;
			;

		}
		// 如果按了左键
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			bL = true;
		}
		// 如果按了右键
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			bR = true;
		}
		locateDirecation();
	}

	// 根据四个布尔值判断我方坦克改变的方向
	void locateDirecation() {
		if (bL && !bU && !bR && !bD) {
			dir = Direction.L;
		} else if (!bL && bU && !bR && !bD) {
			dir = Direction.U;
		} else if (!bL && !bU && bR && !bD) {
			dir = Direction.R;
		} else if (!bL && !bU && !bR && bD) {
			dir = Direction.D;
		} else if (!bL && !bU && !bR && !bD) {
			dir = Direction.STOP;
		}
	}

	// 返回坦克上一步
	private void stay() {
		x = oldx;
		y = oldy;
	}

	// 抬起键 后事件
	public void keyReleased(KeyEvent e) {
		// 如果按了kongge键
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			fire();
		}

		// 如果抬起上键
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			bU = false;
		}
		// 如果抬起下键
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			bD = false;
			;

		}
		// 如果抬起左键
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			bL = false;
		}
		// 如果抬起右键
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			bR = false;
		}
		locateDirecation();
	}

	// 发射子弹
	public Missile fire() {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, ptDir, this.tc);
		tc.missiles.add(m);
		return m;
	}

	// 指定方向发射子弹
	public Missile fire(Direction dir) {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}

	// 超级子弹
	public void superFire() {
		Direction[] dirs = Direction.values();
		for (int i = 0; i < 4; i++) {
			tc.missiles.add(fire(dirs[i]));
		}
	}

	// 获取矩形的范围
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	// 坦克撞墙
	public boolean collidesWithWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}

		return false;
	}

	// 坦克不能互相穿过
	public boolean collidesWithTanks(java.util.List<Tank> tacks) {
		for (int i = 0; i < tacks.size(); i++) {
			Tank t = tacks.get(i);
			if (this != t) {
				if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
}

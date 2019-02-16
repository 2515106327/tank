package day02;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @Discription 墙体类 并返回墙体的矩形物理范围
 * @author 文成
 *
 */
public class Wall {

	int x, y, w, h;// 定义墙体的位置，大小
	StartGame tc;

	// 墙体类的初始化
	public Wall(int x, int y, int w, int h, StartGame tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}

	// 画墙
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);// 画实心矩形
	}

	// 作碰撞检测 返回一个矩形的范围
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
}
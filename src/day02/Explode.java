package day02;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {

	int x,y;
	//炸弹的消亡
	private boolean live=true;
	
	private StartGame tc;
	//圆的直径
	int []diameter={4,7,12,18,26,32,49,30,14,6,};
	//圆目前的进度
	int step=0;
	
	public Explode(int x,int y,StartGame tc){
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	
	public void draw(Graphics g){
		if(!live){
			//删除当前爆炸对象
			tc.explodes.remove(this);
			return;
		}
		
		
		if(step==diameter.length){
			live=false;
			step=0;
			return;
		}
		
		Color c=g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);//画笔颜色归零
		step++;
	}
}

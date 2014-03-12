package com.example.csci_5370_assignment_5;

import java.util.Random;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

public class PoolThread extends Thread {
	private volatile boolean running = true;
	private SurfaceHolder holder;
	private int		w,
					h;
	
	private Vector<Ball> balls;
	//------------------------------------------------------------------
	public PoolThread(SurfaceHolder holder, int w, int h) {
		this.holder = holder;
		this.w = w;
		this.h = h;
		balls = new Vector<PoolThread.Ball>();
		Ball b = new Ball();
		balls.add(b);
	}
	//------------------------------------------------------------------
	public void setRunning(boolean b){
		running = b;
	}
	
	//------------------------------------------------------------------
	public boolean isRunning(){
		return running;
	}
	
	public void addBall(int x, int y){
		Ball b = new Ball(x, y);
		synchronized (balls) {
			balls.add(b);
		}
	}
	
	//------------------------------------------------------------------
	public void run() {
		Canvas canvas = null;
		Paint 	backPaint = new Paint(),
				frontPaint = new Paint(),
				borderPaint = new Paint();
		
		backPaint.setColor(Color.WHITE);
		frontPaint.setColor(Color.RED);
		frontPaint.setTextSize(50);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStyle(Paint.Style.STROKE);
		
		long prevTime = System.currentTimeMillis();
		while (running) {
			try{
				canvas = holder.lockCanvas(null);
				if (canvas != null) {
					synchronized (holder) {
						long currTime = System.currentTimeMillis();
						long ellapsedTime = currTime - prevTime;
						if (ellapsedTime > 25) {
							prevTime = currTime;
							synchronized (balls) {
								for (Ball ball : balls) 
									ball.nextPosition();
							}
							
							
						}
						canvas.drawRect(0, 0, w, h, backPaint);
						canvas.drawRect(0, 0, w-1, h-1, borderPaint);
						canvas.drawText("Tap!", w/2 - 50, h/2 - 150, frontPaint);
						
						synchronized (balls) {
							for (Ball ball : balls) {
								Paint p = ball.getPaint();
								p.setShadowLayer(ball.getRadius() - 5, 10, 20, 0xff555555);
								canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), p);
								
							}
							
						}
					}
					
				}
				
			}finally{
				if (canvas != null) 
					holder.unlockCanvasAndPost(canvas);
			}
			
		}
	}
	
	//------------------------------------------------------------------	
	class Ball{
		private int	x,
					y,
					radius;
		private int[] dirs = {-1, 0, 1};
		private Point iter,
					originalPosition;
		private Random rand;
		private Paint paint;
		public Ball(){
			create();
			x = rand.nextInt(w - radius - 1);
			y = rand.nextInt(h - radius - 1);
			originalPosition.x = x;
			originalPosition.y = y;
		
		}
		
		public Ball(int x, int y){
			create();
			this.x = x;
			this.y = y;
			if (x <= 0)
				this.x += 150;
			
			else if (x >= w) 
				this.x -= 150;
			
			
			if (y <= 0) 
				this.y += 150;
			
			else if (y >= w) 
				this.y -= 150;
			
			originalPosition.x = this.x;
			originalPosition.y = this.y;
		}
		
		private void create(){
			originalPosition = new Point();
			rand = new Random();
			radius = rand.nextInt(50) + 20;
			iter = new Point(	(dirs[rand.nextInt(2)] * (rand.nextInt(100)) + 20), 
								(dirs[rand.nextInt(2)] * (rand.nextInt(100)) + 20)
							);
			paint = new Paint();
			paint.setARGB(	255,
							rand.nextInt(240) + 10 ,
							rand.nextInt(240) + 10,
							rand.nextInt(240) + 10
							);
		}
		
		private void nextPosition(){
			x += iter.x;
			y += iter.y;
			if (x + radius >= w || x - radius <= 0)
				iter.x *= -1;
				
			if (y + radius >= h || y - radius <= 0)
				iter.y *= -1;
		}
		
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
		public int getRadius(){
			return radius;
		}
		public Paint getPaint(){
			return new Paint(paint);
		}
		
		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append(x);
			sb.append(" ");
			sb.append(y);
			return sb.toString();
		}
		
		public Point getPoint(){
			return new Point(x, y);
		}
		public Point getOrigPoint() {
			return new Point(originalPosition);
		}
	}
}

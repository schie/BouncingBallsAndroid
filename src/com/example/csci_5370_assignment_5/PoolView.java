package com.example.csci_5370_assignment_5;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PoolView extends SurfaceView implements SurfaceHolder.Callback {

	volatile PoolThread thread;
	volatile SurfaceHolder holder;
	
	public PoolView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PoolView(Context context, AttributeSet attrs) {
		super(context, attrs);
 		getHolder().addCallback(this);
	}

	public PoolView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me){
		thread.addBall((int)me.getX(), (int)me.getY());
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		thread = new PoolThread(holder, this.getWidth(), this.getHeight());
		thread.start();
		
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//	stop thread from running
		thread.setRunning(false);
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

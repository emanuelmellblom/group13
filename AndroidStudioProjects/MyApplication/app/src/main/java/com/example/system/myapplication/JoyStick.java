package com.example.system.myapplication;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class JoyStick extends SurfaceView implements SurfaceHolder.Callback {
	
	MySurfaceThread thread;
	String tag = "debugging";
	Paint paint1;
	boolean run = true;
	Bitmap ball, max;
	int zeroX, zeroY;
	float x, y, dx, dy, c, angle;
	int radius;
	public JoyStick(Context context){
	   super(context);
	    
	   init();
    }
    
    public JoyStick(Context context, AttributeSet attrs){
    	super(context, attrs);
    	
    	init();
    }
    
    public JoyStick(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    	
    	init();
	}
    
    
    
    private void init() {
    	thread = new MySurfaceThread(getHolder(), this);
    	getHolder().addCallback(this);
    	paint1 = new Paint();
    	paint1.setTextSize(40); 
    	int radius = 125;
    	paint1.setColor(Color.rgb(255, 0, 0));
    	dx = dy = 0;
    	ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    	max = BitmapFactory.decodeResource(getResources(), R.drawable.max);
    	
    }
    
    public void SurfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){}
    
    public void surfaceCreated(SurfaceHolder arg0){
    	thread.execute((Void[])null);
    }
    
    public void surfaceDestroyed(SurfaceHolder arg0){}
    
protected void onDraw(Canvas canvas, float x, float y, int zerox, int zeroy){
	
	dx = x-zeroy;
	dy = y-zeroy;
	
	canvas.drawRGB(255, 255, 255);
	canvas.drawBitmap(max, canvas.getWidth()/2-max.getWidth()/2, canvas.getHeight()/2-max.getHeight()/2, null);
	canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), paint1);
	canvas.drawText(Float.toString(x), 20, 40, paint1);
	canvas.drawText(Float.toString(y), 20, 100, paint1);
	if(x ==0 && y ==0)
	canvas.drawBitmap(ball, canvas.getWidth()/2-ball.getWidth()/2, canvas.getHeight()/2-ball.getHeight()/2, null);
	else
		canvas.drawBitmap(ball, x-ball.getWidth()/2, y-ball.getHeight()/2, null);
    }

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

   public class MySurfaceThread extends AsyncTask<Void, Void, Void>{
	   
	   SurfaceHolder mSurfaceHolder;
	   JoyStick cSurfaceView;
	   float x, y;
	   public MySurfaceThread(SurfaceHolder sh, JoyStick csv){
		   mSurfaceHolder = sh;
		   cSurfaceView = csv;
		   x = y = 0;
		   csv.setOnTouchListener(new OnTouchListener() {

			   @Override
			   public boolean onTouch(View v, MotionEvent e){
				   
				   x = e.getX();
				   y = e.getY();
				   
				   calculateValues(x, y);
				   switch(e.getAction()& MotionEvent.ACTION_MASK){
				   case MotionEvent.ACTION_DOWN:
					   break;
				   case MotionEvent.ACTION_UP:
					   x =y =0;
					  
					 dx = dy = 0;
					   break;
				   case MotionEvent.ACTION_CANCEL:
					   break;
				   case MotionEvent.ACTION_MOVE:
					   break;
				   
				   
				   }
				   
				   return true;
			   }
			   
			   private void calculateValues(float xx, float yy){
				  // float zeroX;
				  // float zeroY;
				 dx = xx - zeroX;
				 dy = yy - zeroY;
				    angle = (float)Math.atan(Math.abs(dy/dx));
				    c = (float)Math.sqrt(dx * dx + dy * dy);
				   
				   Log.i(tag," xx: "+Float.toString(xx)+"yy:"+Float.toString(yy)+"c: "+Float.toString(c)+"angle:"+Float.toString(angle)+"dx:"+Float.toString(dx)+"dy:");
				   if(c > radius){
					   if(dx > 0 && dy > 0){//bottom right
					   xx = (float)(zeroX + (radius*(float)Math.cos(angle)));
					   yy = (float)(zeroY + (radius*(float)Math.sin(angle)));
					   }
					   else if(dx>0 && dy<0){//top right
						   xx = (float)(zeroX + (radius*(float)Math.cos(angle)));
						   yy = (float)(zeroY - (radius*(float)Math.sin(angle)));
					   }
					   else if(dx < 0 && dy < 0){//top left
						   xx = (float)(zeroX - (radius*(float)Math.cos(angle)));
						   yy = (float)(zeroY - (radius*(float)Math.sin(angle)));
					   }
					   else if(dx<0 && dy>0){//bottom left
						   xx = (float)(zeroX - (radius*(float)Math.cos(angle)));
						   yy = (float)(zeroY + (radius*(float)Math.sin(angle)));
					   }
						   
						   
					   
			       }
				   else{
					   xx = zeroX + dx;
					   yy = zeroY + dy;
				   }
				x = xx;
				y = yy;
			         }
				   
		   });
		   
		}
	   
	   @SuppressLint("WrongCall")
	@Override
		protected Void doInBackground(Void... params) {
			//boolean run;
			// TODO Auto-generated method stub
		   Canvas canvas = null;
			while(run){
			
			try{
				canvas = mSurfaceHolder.lockCanvas(null);
				synchronized(mSurfaceHolder){
					int zeroX = canvas.getWidth()/2;
					int zeroY = canvas.getHeight()/2;
					cSurfaceView.onDraw(canvas, x, y, zeroX, zeroY);
				}
				Thread.sleep(10);
			}
			catch(InterruptedException e){
			
			}
			finally{
				if(canvas != null){
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		  }
		   return null;
							
		}
	   
      }
   }
package eu.arttu.canvastest;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CanvasPanel extends SurfaceView implements SurfaceHolder.Callback {
    private final int CANV_WIDTH;
    private final int CANV_HEIGHT;

    private SurfaceHolder sh;
    private Context context;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private CanvasDrawingThread thread;

	public CanvasPanel(Context context) {
		super(context);
		sh = getHolder();
		sh.addCallback(this);
		this.context = context;
		setFocusable(true);
		
		Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		CANV_WIDTH = disp.getWidth();
		Log.d("Canv width", String.valueOf(CANV_WIDTH));
		CANV_HEIGHT = disp.getHeight();
		Log.d("Canv height", String.valueOf(CANV_HEIGHT));
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		thread.setSurfaceSize(CANV_WIDTH, CANV_HEIGHT);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	    thread = new CanvasDrawingThread(sh, context, new Handler(), paint, CANV_WIDTH, CANV_HEIGHT);
	    thread.setRunning(true);
	    thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	    boolean retry = true;
	    thread.setRunning(false);
	    while (retry) {
	      try {
	        thread.join();
	        retry = false;
	      } 
	      catch (InterruptedException e) { }
	      catch (Exception e){
	    	  Log.e("Exception joining thread",e.getMessage());
	      }
	    }
	}

}

/*
    Copyright 2013 Arthur Peka
    This file is part of CanvasTest.
    CanvasTest is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, version 3 of the License.
    CanvasTest is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
    You should have received a copy of the GNU General Public License along with CanvasTest. If not, see http://www.gnu.org/licenses/.
 */
package eu.arttu.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class CanvasDrawingThread extends Thread {
	  private int canvasWidth;
	  private int canvasHeight;
	  private static final int CIRCLE_RADIUS = 50;
	  private boolean running = false;
	  private float circleX;
	  private float circleY;
	  private int speedX = 2;
	  private int speedY = 1;
	  
	  private SurfaceHolder sh;
	  private final Paint paint;
	  
	  
	  public CanvasDrawingThread(SurfaceHolder surfaceHolder, Paint paint, 
			  int canvWidth, int canvHeight){
		  this.sh = surfaceHolder;
		  this.paint = paint;
		  
		  //Note, that this is rather unnecessary, as width and height are reset in setSurfaceSize()
		  this.canvasWidth = canvWidth;
		  this.canvasHeight = canvHeight;
		  paint.setColor(Color.RED);
	  }
	  
	  public void setInitialPositions() {
		    synchronized (sh) {
		      circleX = canvasWidth / 2;
		      circleY = canvasHeight / 2;
		    }
	  }
	  
	  public void run() {
		  	Canvas canv = null;
		    while (running) { 
		      try {
		        canv = sh.lockCanvas(null);
		        synchronized (sh) {
		          drawBubble(canv);
		        }
		      } 
		      finally {
		        if (canv != null)
		          sh.unlockCanvasAndPost(canv);
		      }
		    }
	  }
		    
		  public void setRunning(boolean running) { 
		    this.running = running;
		  }
		  
		  public void setSurfaceSize(int width, int height) {
		    synchronized (sh) {
		      this.canvasWidth = width;
		      this.canvasHeight = height;
		      setInitialPositions();
		    }
		  }
		  
		  private void drawBubble(Canvas canvas) {
		    circleX += speedX;
		    if((circleX - CIRCLE_RADIUS) < 0 || (circleX + CIRCLE_RADIUS) >= canvasWidth)
		    	speedX = (-1)*speedX;
		    circleY += speedY;
		    if((circleY - CIRCLE_RADIUS) < 0 || (circleY + CIRCLE_RADIUS) >= canvasHeight)
		    	speedY = (-1)*speedY;
		    canvas.restore();
		    canvas.drawColor(Color.BLACK);
		    canvas.drawCircle(circleX, circleY, CIRCLE_RADIUS, paint);
		  }
}

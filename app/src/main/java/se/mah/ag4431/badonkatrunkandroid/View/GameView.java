package se.mah.ag4431.badonkatrunkandroid.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import se.mah.ag4431.badonkatrunkandroid.Entities.Star;


public class GameView extends SurfaceView implements Runnable {

    // This is an instance of an inner class
    // See end of this class for code
    HUD hud;

    PointF tempPointF = new PointF();


    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused at the start
    private boolean paused = true;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;
    // This variable tracks the game frame rate
    private long fps;

    private Star[] stars = new Star[5000];
    private int numStars = 0;
    Viewport vp;

    // The constructor
    public GameView(Context context, int screenX, int screenY) {

        super(context);

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        // Initialize the Viewport
        vp = new Viewport(screenX, screenY);

        hud = new HUD(screenX, screenY);




        vp.setWorldCentre(0,140);
        for(int i = 0; i < 5000; i++){
            stars[i] = new Star(100,150);
            numStars++;
        }
    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }
            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        for(Star star : stars){
            star.update();
        }
        vp.moveRight();

    }


    private void draw() {
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            //canvas.drawColor(Color.argb(255, 87, 73, 122));
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 255, 255, 0));
            for (int i = 0; i < numStars; i++) {

                if (stars[i].getVisibility()) {
                    tempPointF = vp.worldToScreenPoint(stars[i].getX(), stars[i].getY());
                    canvas.drawPoint(tempPointF.x, tempPointF.y, paint);
                }
            }


            //Display FPS
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(45);
            canvas.drawText("FPS = " + fps, 20, 70, paint);


            // Change paint color
            // Lower alpha value to make buttons transparent
            paint.setColor(Color.argb(80, 255, 255, 255));


            //Rectangle for the "pause"-button
            RectF rf = new RectF(hud.pause.left, hud.pause.top, hud.pause.right, hud.pause.bottom);
            canvas.drawRoundRect(rf, 15f, 15f, paint);


            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        hud.handleInput(motionEvent);
        return true;
    }

    class HUD {
        Rect pause;



        HUD(int screenWidth, int screenHeight) {
            int buttonWidth = screenWidth / 8;
            int buttonHeight = screenHeight / 7;
            int buttonPadding = screenWidth / 80;

          pause = new Rect(screenWidth - buttonPadding - buttonWidth,
                    buttonPadding,
                    screenWidth - buttonPadding,
                    buttonPadding + buttonHeight);
        }

        public void handleInput(MotionEvent motionEvent) {

            int x = (int) motionEvent.getX(0);
            int y = (int) motionEvent.getY(0);

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    if(pause.contains(x, y)) {
                        paused = !paused;
                    }
                    break;


                case MotionEvent.ACTION_UP:



            }

        }
    }
}


package se.mah.ag4431.badonkatrunkandroid;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import se.mah.ag4431.badonkatrunkandroid.View.GameView;

public class MainActivity extends Activity {
    GameView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        view = new GameView(this,size.x,size.y);
        setContentView(view);
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        view.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        view.pause();
    }
}

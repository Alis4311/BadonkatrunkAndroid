package se.mah.ag4431.badonkatrunkandroid.Entities;

import java.util.Random;


public class Star{

    private int x;
    private int y;

    private boolean isVisible = true;

    Random random;

    public Star(int mapWidth, int mapHeight){
        random = new Random();
        x= random.nextInt(mapWidth);
        y = random.nextInt(mapHeight);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void update(){
        int n = random.nextInt(5000);
        if(n == 0){
            isVisible = !isVisible;
        }


    }

    public boolean getVisibility(){
        return isVisible;
    }

}
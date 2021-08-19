package com.abstrakt.game;

import com.abstrakt.proton.*;
import com.abstrakt.proton.gfx.*;

public class GameManager extends AbstractGame
{
    private ImageTile image;

    public GameManager()
    {
        image = new ImageTile("/Texture1.png", 16, 16);
    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        temp += dt * 20;

        if (temp > 3) {temp = 0;}
    }

    float temp = 0;

    @Override
    public void render(GameContainer gc, Renderer r)
    {
        r.drawImageTile(image, gc.getInput().getMouseX() - 8, gc.getInput().getMouseY() - 8, (int) temp, 0);
    }

    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}

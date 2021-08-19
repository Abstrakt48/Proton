package com.abstrakt.proton;

import com.abstrakt.proton.gfx.*;
import java.awt.image.DataBufferInt;

public class Renderer
{
    private int pW, pH;
    private int[] p;

    public Renderer(GameContainer gc)
    {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }

    public void clear()
    {
        // Clears screen with the color 'black' or 0
        for (int i = 0; i < p.length; i++)
        {
            p[i] = 0;
        }
    }

    public void setPixel(int x, int y, int value)
    {
        // Checks if the value is ugly pink, if so, don't return and don't render that pixel
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || value == 0xffff00ff)
        {
            return;
        }

        // If the value is not ugly pink, render the pixel
        p[x + y * pW] = value;
    }

    public void drawImage(Image image, int offX, int offY)
    {
        // Don't render what so ever if the image is completely off screen
        if (offX < -image.getW()) return;
        if (offY < -image.getH()) return;
        if (offX >= pW) return;
        if (offY >= pH) return;

        // variables for x,y,width and height
        int newX = 0;
        int newY = 0;
        int newWidth = image.getW();
        int newHeight = image.getH();

        // Clip the image
        if (offX < 0) {newX -= offX;}
        if (offY < 0) {newY -= offY;}
        if (newWidth + offX >= pW) {newWidth -= (newWidth + offX - pW); }
        if (newHeight + offY >= pH) {newHeight -= (newHeight + offY - pH);}

        // Scan through every single pixel and draw the value
        for (int y = newY; y < newHeight; y++)
        {
            for (int x = newX; x < newWidth; x++)
            {
                setPixel(x + offX, y + offY, image.getP()[ x + y * image.getW()]);
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY)
    {
        // Don't render what so ever if the image is completely off screen
        if (offX < -image.getTileW()) return;
        if (offY < -image.getTileH()) return;
        if (offX >= pW) return;
        if (offY >= pH) return;

        // variables for x,y,width and height
        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        // Clip the image
        if (offX < 0) {newX -= offX;}
        if (offY < 0) {newY -= offY;}
        if (newWidth + offX >= pW) {newWidth -= (newWidth + offX - pW); }
        if (newHeight + offY >= pH) {newHeight -= (newHeight + offY - pH);}

        // Scan through every single pixel and draw the value
        for (int y = newY; y < newHeight; y++)
        {
            for (int x = newX; x < newWidth; x++)
            {
                setPixel(x + offX, y + offY, image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);
            }
        }
    }
}

package com.abstrakt.proton.engine;

import com.abstrakt.proton.gfx.*;
import com.abstrakt.proton.math.Vector2;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer
{
    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();

    private int pixelWidth, pixelHeight; // pixelWidth, pixelHeight
    private int[] pixel; // pixel
    private int[] zBuffer; // zBuffer

    private int zDepth = 0;
    private boolean processing = false;

    public Renderer(ProtonEngine gc)
    {
        pixelWidth = gc.getWidth();
        pixelHeight = gc.getHeight();
        pixel = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[pixel.length];
    }

    public void clear()
    {
        // Clears screen with the color 'black' or 0
        for (int i = 0; i < pixel.length; i++)
        {
            pixel[i] = 0;
            zBuffer[i] = 0;
        }
    }

    public void process()
    {
        processing = true;

        Collections.sort(imageRequest, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest i0, ImageRequest i1)
            {
                if (i0.zDepth < i1.zDepth)
                    return -1;
                if (i0.zDepth > i1.zDepth)
                    return 1;
                 return 0;
            }
        });

        for (int i = 0; i < imageRequest.size(); i++)
        {
            ImageRequest ir = imageRequest.get(i);
            setZDepth(ir.zDepth);
            drawImage(ir.image, new Vector2(ir.offX, ir.offY));
        }

        imageRequest.clear();
        processing = false;
    }

    public void setPixel(Vector2 position, int value)
    {
        int alpha = ((value >> 24) & 0xff);

        if ((position.x < 0 || position.x >= pixelWidth || position.y < 0 || position.y >= pixelHeight) || alpha == 0)
        {
            return;
        }

        int index = (int) (position.x + position.y * pixelWidth);

        if(zBuffer[index] > zDepth)
            return;

        zBuffer[index] = zDepth;

        if (alpha == 255)
        {
            pixel[index] = value;
        }
        else
        {
            int pixelColor = pixel[index];

            int newRed = ((pixelColor >> 16) & 0xff) - (int) ((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int) ((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff) - (int) (((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

            pixel[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    public void drawText(String text, Vector2 position, int color)
    {
        text = text.toUpperCase();
        int offset = 0;

        for (int i = 0; i < text.length(); i++)
        {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < font.getFontImage().getH(); y++)
            {
                for (int x = 0; x < font.getWidths()[unicode]; x++)
                {
                    if (font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff)
                    {
                        setPixel(new Vector2((int) (x + position.x + offset),(int) (y + position.y)),color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    public void drawImage(Image image, Vector2 position)
    {
        if (image.isAlpha() && !processing)
        {
            imageRequest.add(new ImageRequest(image, zDepth, new Vector2(position)));
            return;
        }

        // Don't render what so ever if the image is completely off screen
        if (position.x < -image.getW()) return;
        if (position.y < -image.getH()) return;
        if (position.x >= pixelWidth) return;
        if (position.y >= pixelHeight) return;

        // variables for x,y,width and height
        int newX = 0;
        int newY = 0;
        int newWidth = image.getW();
        int newHeight = image.getH();

        // Clip the image
        if (position.x < 0) {newX -= position.x;}
        if (position.y < 0) {newY -= position.y;}
        if (newWidth + position.x >= pixelWidth) {newWidth -= (newWidth + position.x - pixelWidth); }
        if (newHeight + position.y >= pixelHeight) {newHeight -= (newHeight + position.y - pixelHeight);}

        // Scan through every single pixel and draw the value
        for (int y = newY; y < newHeight; y++)
        {
            for (int x = newX; x < newWidth; x++)
            {
                setPixel(new Vector2(x + position.x, y + position.y), image.getP()[ x + y * image.getW()]);
            }
        }
    }

    public void drawImageTile(ImageTile image, Vector2 position, Vector2 tile)
    {
        if (image.isAlpha() && !processing)
        {
            imageRequest.add(new ImageRequest(image.getTileImage(new Vector2(tile.x, tile.y)), zDepth, new Vector2(position.x, position.y)));
            return;
        }

        // Don't render what so ever if the image is completely off screen
        if (position.x < -image.getTileW()) return;
        if (position.y < -image.getTileH()) return;
        if (position.x >= pixelWidth) return;
        if (position.y >= pixelHeight) return;

        // variables for x,y,width and height
        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        // Clip the image
        if (position.x < 0) {newX -= position.x;}
        if (position.y < 0) {newY -= position.y;}
        if (newWidth + position.x >= pixelWidth) {newWidth -= (newWidth + position.x - pixelWidth); }
        if (newHeight + position.y >= pixelHeight) {newHeight -= (newHeight + position.y - pixelHeight);}

        // Scan through every single pixel and draw the value
        for (int y = newY; y < newHeight; y++)
        {
            for (int x = newX; x < newWidth; x++)
            {
                setPixel(new Vector2(x + position.x, y + position.y), image.getP()[(int) ((int)(x + tile.x * image.getTileW()) + (y + tile.y * image.getTileH()) * image.getW())]);
            }
        }
    }

    public void drawRect(Vector2 position, Vector2 dimensions, int color)
    {
        for (int y = 0; y < (int) dimensions.y; y++)
        {
            setPixel(new Vector2(position.x, y + position.y), color);
            setPixel(new Vector2(position.x + dimensions.x, y + position.y), color);
        }

        for (int x = 0; x < (int) dimensions.x; x++)
        {
            setPixel(new Vector2(x + position.x, position.y), color);
            setPixel(new Vector2(x + position.x, position.y + dimensions.y), color);
        }
    }

    public void drawFillRect(Vector2 position, Vector2 dimensions, int color)
    {
        // Don't render what so ever if the image is completely off screen
        if (position.x < -dimensions.x) return;
        if (position.y < -dimensions.y) return;
        if (position.x >= pixelWidth) return;
        if (position.y >= pixelHeight) return;

        // variables for x,y,width and height
        int newX = 0;
        int newY = 0;
        int newWidth = (int) dimensions.x;
        int newHeight = (int) dimensions.y;

        // Clip the image
        if (position.x < 0) {newX -= position.x;}
        if (position.y < 0) {newY -= position.y;}
        if (newWidth + position.x >= pixelWidth) {newWidth -= (newWidth + position.x - pixelWidth); }
        if (newHeight + position.y >= pixelHeight) {newHeight -= (newHeight + position.y - pixelHeight);}

        for (int y = newY; y <= newHeight; y++)
        {
            for (int x = newX; x <= newWidth; x++)
            {
                setPixel(new Vector2(x + position.x, y + position.y), color);
            }
        }
    }

    public int getZDepth() {
        return zDepth;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}

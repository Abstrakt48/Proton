package com.abstrakt.proton.gfx;

import com.abstrakt.proton.math.Vector2;

public class ImageTile extends Image
{
    private int tileW, tileH;

    public ImageTile(String path, Vector2 tile)
    {
        super(path);
        this.tileW = (int) tile.x;
        this.tileH = (int) tile.y;
    }

    public Image getTileImage(Vector2 tile)
    {
        int[] p = new int[tileW * tileH];

        for (int y = 0; y < tileH; y++)
        {
            for (int x = 0; x < tileW; x++)
            {
                p[x + y * tileW] = this.getP()[(int) ((x + tile.x * tileW) + (y + tile.y * tileH) * this.getW())];
            }
        }

        return new Image(p, tileW, tileH);
    }

    public int getTileW()
    {
        return tileW;
    }

    public void setTileW(int tileW)
    {
        this.tileW = tileW;
    }

    public int getTileH()
    {
        return tileH;
    }

    public void setTileH(int tileH)
    {
        this.tileH = tileH;
    }
}

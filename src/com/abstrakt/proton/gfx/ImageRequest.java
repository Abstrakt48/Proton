package com.abstrakt.proton.gfx;

import com.abstrakt.proton.math.Vector2;

public class ImageRequest
{
    public Image image;
    public int zDepth;
    public int offX, offY;

    public ImageRequest(Image image, int zDepth, Vector2 position)
    {
        this.image = image;
        this.zDepth = zDepth;
        this.offX = (int) position.x;
        this.offY = (int) position.y;
    }
}

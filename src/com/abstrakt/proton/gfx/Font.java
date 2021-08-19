package com.abstrakt.proton.gfx;

public class Font
{
    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    public Font(String path)
    {
        fontImage = new Image(path);

        offsets = new int[58];
    }
}

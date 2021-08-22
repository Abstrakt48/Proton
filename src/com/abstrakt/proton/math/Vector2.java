package com.abstrakt.proton.math;

public class Vector2
{
    public float x;
    public float y;

    public Vector2()
    {
        x = zero().x;
        y = zero().y;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vec2)
    {
        x = vec2.x;
        y = vec2.y;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize()
    {
        float length = length();

        x /= length;
        y /= length;

        return this;
    }

    public Vector2 zero()
    {
        return new Vector2(0,0);
    }

    public String toString()
    {
        return "[" + this.x + "," + this.y + "]";
    }
}

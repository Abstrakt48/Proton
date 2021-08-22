package com.abstrakt.proton.math;

public class Vector3
{
    public float x;
    public float y;
    public float z;

    public Vector3()
    {
        x = zero().x;
        y = zero().y;
        z = zero().z;
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 vec3)
    {
        x = vec3.x;
        y = vec3.y;
        z = vec3.y;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalize()
    {
        float length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    public Vector3 zero()
    {
        return new Vector3(0,0,0);
    }

    public String toString()
    {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }
}

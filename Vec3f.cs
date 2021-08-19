using System;
using System.Collections.Generic;
using System.Text;
using static System.Console;

namespace ProtonEngine
{
	public class Vec3f {
	
		public float X;
		public float Y;
		public float Z;
		
		public Vec3f()
		{
			this.setX(0);
			this.setY(0);
			this.setZ(0);
		}
		
		public Vec3f(float x, float y, float z)
		{
			this.setX(x);
			this.setY(y);
			this.setZ(z);
		}
		
		public Vec3f(Vec3f v)
		{
			this.X = v.getX();
			this.Y = v.getY();
			this.Z = v.getZ();
		}
		
		public float length()
		{
			return (float) Math.Sqrt(X*X + Y*Y + Z*Z);
		}
		
		public float dot(Vec3f r)
		{
			return X * r.getX() + Y * r.getY() + Z * r.getZ();
		}
		
		public Vec3f cross(Vec3f r)
		{
			float x = Y * r.getZ() - Z * r.getY();
			float y = Z * r.getX() - X * r.getZ();
			float z = X * r.getY() - Y * r.getX();
			
			return new Vec3f(x,y,z);
		}
		
		public Vec3f normalize()
		{
			float length = this.length();
			
			X /= length;
			Y /= length;
			Z /= length;
			
			return this;
		}
		
		public Vec3f rotate(float angle, Vec3f axis)
		{
			float SinHalfAngle = (float)Math.Sin(toRadians(angle / 2));
			float CosHalfAngle = (float)Math.Cos(toRadians(angle / 2));
			
			float rX = axis.getX() * SinHalfAngle;
			float rY = axis.getY() * SinHalfAngle;
			float rZ = axis.getZ() * SinHalfAngle;
			float rW = CosHalfAngle;
			
			Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
			Quaternion conjugate = rotation.conjugate();
			
			Quaternion w = rotation.mul(this).mul(conjugate);
			
			X = w.getX();
			Y = w.getY();
			Z = w.getZ();
			
			return this;
		}
		
		public Vec3f add(Vec3f r)
		{
			return new Vec3f(this.X + r.getX(), this.Y + r.getY(), this.Z + r.getZ());
		}
		
		public Vec3f add(float r)
		{
			return new Vec3f(this.X + r, this.Y + r, this.Z + r);
		}
		
		public Vec3f sub(Vec3f r)
		{
			return new Vec3f(this.X - r.getX(), this.Y - r.getY(), this.Z - r.getZ());
		}
		
		public Vec3f sub(float r)
		{
			return new Vec3f(this.X - r, this.Y - r, this.Z - r);
		}
		
		public Vec3f mul(Vec3f r)
		{
			return new Vec3f(this.X * r.getX(), this.Y * r.getY(), this.Z * r.getZ());
		}
		
		public Vec3f mul(float x, float y, float z)
		{
			return new Vec3f(this.X * x, this.Y * y, this.Z * z);
		}
		
		public Vec3f mul(float r)
		{
			return new Vec3f(this.X * r, this.Y * r, this.Z * r);
		}
		
		public Vec3f div(Vec3f r)
		{
			return new Vec3f(this.X / r.getX(), this.Y / r.getY(), this.getZ() / r.getZ());
		}
		
		public Vec3f div(float r)
		{
			return new Vec3f(this.X / r, this.Y / r, this.Z / r);
		}
		
		public Vec3f Abs()
		{
			return new Vec3f(Math.Abs(X), Math.Abs(Y), Math.Abs(Z));
		}
		
		public bool equals(Vec3f v)
		{
			if (X == v.getX() && Y == v.getY() && Z == v.getZ())
				return true;
			else return false;
		}
		
		public String toString()
		{
			return "[" + this.X + "," + this.Y + "," + this.Z + "]";
		}

		public float getX() {
			return X;
		}

		public void setX(float x) {
			X = x;
		}

		public float getY() {
			return Y;
		}

		public void setY(float y) {
			Y = y;
		}

		public float getZ() {
			return Z;
		}

		public void setZ(float z) {
			Z = z;
		}

		public static double toRadians (double degrees)
		{
		    double radians = (Math.PI / 180) * degrees;
		    return (radians);
		}
	}
}
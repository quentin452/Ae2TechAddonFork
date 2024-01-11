package com.fireball1725.ae2tech.helpers;

import org.lwjgl.util.vector.Vector;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Holds a 3-tuple vector.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$ $Id$
 */

public class Vector3n extends Vector {

    private static final long serialVersionUID = 1L;

    public int x, y, z;

    /**
     * Constructor for Vector3n.
     */
    public Vector3n() {
        super();
    }

    /**
     * Constructor
     */
    public Vector3n(Vector3n src) {
        set(src);
    }

    /**
     * Constructor
     */
    public Vector3n(int x, int y, int z) {
        set(x, y, z);
    }

    /**
     * Add a vector to another vector and place the result in a destination
     * vector.
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @param dest  The destination vector, or null if a new vector is to be
     *              created
     * @return the sum of left and right in dest
     */
    public static Vector3n add(Vector3n left, Vector3n right, Vector3n dest) {
        if (dest == null) return new Vector3n(left.x + right.x, left.y + right.y, left.z + right.z);
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
        return dest;
    }

    /**
     * Subtract a vector from another vector and place the result in a
     * destination vector.
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @param dest  The destination vector, or null if a new vector is to be
     *              created
     * @return left minus right in dest
     */
    public static Vector3n sub(Vector3n left, Vector3n right, Vector3n dest) {
        if (dest == null) return new Vector3n(left.x - right.x, left.y - right.y, left.z - right.z);
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
        return dest;
    }

    /**
     * The cross product of two vectors.
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @param dest  The destination result, or null if a new vector is to be
     *              created
     * @return left cross right
     */
    public static Vector3n cross(Vector3n left, Vector3n right, Vector3n dest) {

        if (dest == null) dest = new Vector3n();

        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);

        return dest;
    }

    /**
     * The dot product of two vectors is calculated as v1.x * v2.x + v1.y * v2.y
     * + v1.z * v2.z
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @return left dot right
     */
    public static int dot(Vector3n left, Vector3n right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    /**
     * Calculate the angle between two vectors, in radians
     *
     * @param a A vector
     * @param b The other vector
     * @return the angle between the two vectors, in radians
     */
    public static float angle(Vector3n a, Vector3n b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1f)
            dls = -1f;
        else if (dls > 1.0f) dls = 1.0f;
        return (float) Math.acos(dls);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lwjgl.util.vector.WritableVector2f#set(int, int)
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lwjgl.util.vector.WritableVector3n#set(int, int, int)
     */
    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Load from another Vector3n
     *
     * @param src The source vector
     * @return this
     */
    public Vector3n set(Vector3n src) {
        x = src.getX();
        y = src.getY();
        z = src.getZ();
        return this;
    }

    /**
     * @return the length squared of the vector
     */
    @Override
    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Translate a vector
     *
     * @param x The translation in x
     * @param y the translation in y
     * @return this
     */
    public Vector3n translate(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Negate a vector
     *
     * @return this
     */
    @Override
    public Vector negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Negate a vector and place the result in a destination vector.
     *
     * @param dest The destination vector or null if a new vector is to be
     *             created
     * @return the negated vector
     */
    public Vector3n negate(Vector3n dest) {
        if (dest == null) dest = new Vector3n();
        dest.x = -x;
        dest.y = -y;
        dest.z = -z;
        return dest;
    }

    /**
     * Normalise this vector and place the result in another vector.
     *
     * @param dest The destination vector, or null if a new vector is to be
     *             created
     * @return the normalised vector
     */
    public Vector3n normalise(Vector3n dest) {
        float l = length();

        if (dest == null)
            dest = new Vector3n((int) (x / l), (int) (y / l), (int) (z / l));
        else
            dest.set((int) (x / l), (int) (y / l), (int) (z / l));

        return dest;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lwjgl.vector.Vector#load(intBuffer)
     */
    public Vector load(IntBuffer buf) {
        x = buf.get();
        y = buf.get();
        z = buf.get();
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lwjgl.vector.Vector#scale(int)
     */
    public Vector scale(int scale) {

        x *= scale;
        y *= scale;
        z *= scale;

        return this;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.lwjgl.vector.Vector#store(intBuffer)
     */
    public Vector store(IntBuffer buf) {

        buf.put(x);
        buf.put(y);
        buf.put(z);

        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);

        sb.append("Vector3n<");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(", ");
        sb.append(z);
        sb.append('>');
        return sb.toString();
    }

    /**
     * @return x
     */
    public final int getX() {
        return x;
    }

    /**
     * Set X
     *
     * @param x
     */
    public final void setX(int x) {
        this.x = x;
    }

    /**
     * @return y
     */
    public final int getY() {
        return y;
    }

    /**
     * Set Y
     *
     * @param y
     */
    public final void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    /**
     * Set Z
     *
     * @param z
     */
    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public Vector load(FloatBuffer buf) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector store(FloatBuffer buf) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector scale(float scale) {
        // TODO Auto-generated method stub
        return null;
    }
}
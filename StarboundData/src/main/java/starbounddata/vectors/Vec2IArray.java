/*
* Copyright (C) 2014 www.StarNub.org - Underbalanced
*
* This utilities.file is part of org.starnub a Java Wrapper for Starbound.
*
* This above mentioned StarNub software is free software:
* you can redistribute it and/or modify it under the terms
* of the GNU General Public License as published by the Free
* Software Foundation, either version  3 of the License, or
* any later version. This above mentioned CodeHome software
* is distributed in the hope that it will be useful, but
* WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
* the GNU General Public License for more details. You should
* have received a copy of the GNU General Public License in
* this StarNub Software.  If not, see <http://www.gnu.org/licenses/>.
*/

package starbounddata.vectors;

import io.netty.buffer.ByteBuf;
import starbounddata.variants.VLQ;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Represents a Vec2I Array which contains 0-SomeNumber of 2 dimensional integer vector of (x, y)
 * <p>
 * Note: This Array is not a synchronized collection. This should not make a deference as only one thread should be handling this packet at a time
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0 Beta
 */
public class Vec2IArray extends ArrayList<Vec2I> {

    private final static int ARRAY_INCOMING_CAP = 50;
    private final ArrayList<Vec2I> VEC2I_POOL = buildVec2ICache(ARRAY_INCOMING_CAP);

    public Vec2IArray() {
        super();
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will create a vector array using the two vectors provided. This creates vectors between the two points to form a
     * square or rectangle using some math stuff.
     *
     * @param vector1 Vec2I representing the first vector that you want to start your array
     * @param vector2 Vec2I representing the second vector that you want to end your array
     * @throws ArrayIndexOutOfBoundsException
     */
    public Vec2IArray(Vec2I vector1, Vec2I vector2) throws ArrayIndexOutOfBoundsException {
        int lowX = vector1.getX() > vector2.getX() ? vector2.getX() : vector1.getX();
        int highX = vector1.getX() > vector2.getX() ? vector1.getX() : vector2.getX();
        int lowY = vector1.getY() > vector2.getY() ? vector2.getY() : vector1.getY();
        int highY = vector1.getY() > vector2.getY() ? vector1.getY() : vector2.getY();
        int xLen = highX - lowX;
        int yLen = highY - lowY;
        int size = xLen * yLen;
//        if (VEC2I_POOL.size() < size){
//
//        }
        while (lowX <= highX) {
            int tempY = lowY;
            while (tempY <= highY) {
                Vec2I vec2I = new Vec2I(lowX, tempY);
                this.add(vec2I);
                tempY++;
            }
            lowX++;
        }
    }

    /**
     * @param in ByteBuf data to be read into the Vec2I Array. 100 is set as a cap for data to prevent attacks against the starnubserver. This is still a sizable area
     */
    public Vec2IArray(ByteBuf in) throws ArrayIndexOutOfBoundsException {
        readVec2IArray(in);
    }

    public void readVec2IArray(ByteBuf in){
        int arrayLength = VLQ.readUnsignedFromBufferNoObject(in);
        if (arrayLength > ARRAY_INCOMING_CAP) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for (int i = 0; i < arrayLength; i++) {
            Vec2I vec2I = VEC2I_POOL.get(i);
            vec2I.setVec2I(in);
            this.add(vec2I);
        }
    }

    /**
     * @param out ByteBuf out representing a {@link io.netty.buffer.ByteBuf} to write this Vec2IArray to
     */
    public void writeVec2IArray(ByteBuf out) {
        out.writeBytes(VLQ.createVLQNoObject((long) this.size()));
        for (Vec2I vec2I : this) {
            vec2I.writeVec2I(out);
        }
        this.clear();
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will take this Vec2IArray and break it up into smaller ones. I.E, if you have 200 Vec2I in this array
     * and set the groupSize to 50 it will return a HashSet of 4 Vec2IArrays each containing 50 Vec2Is each.
     *
     * @param groupSize int representing the size of each Vec2IArray
     * @return HashSet containing Vec2IArray's
     */
    public HashSet<Vec2IArray> getVec2IGroups(int groupSize) {
        HashSet<Vec2IArray> groups = new HashSet<>();
        while (this.size() > 0) {
            Vec2IArray group = new Vec2IArray();
            while (group.size() < groupSize && this.size() > 0) {
                group.add(this.remove(0));
            }
            groups.add(group);
        }
        return groups;
    }

    private ArrayList<Vec2I> buildVec2ICache(int cacheSize){
        ArrayList<Vec2I> vec2IHashSet = new ArrayList<>();
        for (int i = 0; i < cacheSize; i++) {
            Vec2I vec2I = new Vec2I(0,0);
            vec2IHashSet.add(vec2I);
        }
        return vec2IHashSet;
    }

    @Override
    public String toString() {
        return "Vec2IArray{} " + super.toString();
    }
}

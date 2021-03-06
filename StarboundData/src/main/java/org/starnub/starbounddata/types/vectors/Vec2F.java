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

package org.starnub.starbounddata.types.vectors;

import io.netty.buffer.ByteBuf;
import org.starnub.starbounddata.types.SbData;

/**
 * Represents a 2 dimensional floating point vector of (x, y)
 * <p>
 * Starbound 1.0 Compliant (Versions 622, Update 1)
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0 Beta
 */
public class Vec2F extends SbData<Vec2F>{

    private float x;
    private float y;

    public Vec2F() {
    }

    public Vec2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2F(ByteBuf in) {
        super(in);
    }

    public Vec2F(Vec2F vec2F) {
        this.x = vec2F.getX();
        this.y = vec2F.getY();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void read(ByteBuf in){
        this.x = in.readFloat();
        this.y = in.readFloat();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
    }

    @Override
    public String toString() {
        return "Vec2F{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

/*
 * Copyright (C) 2014 www.StarNub.org - Underbalanced
 *
 * This file is part of org.starnub a Java Wrapper for Starbound.
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

package org.starnub.starbounddata.packets.liquid;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.starnub.starbounddata.packets.Packets;
import org.starnub.starbounddata.packets.Packet;
import org.starnub.starbounddata.types.vectors.Vec2IArray;

/**
 * Represents the DamageTileGroup and methods to generate a packet data for StarNub and Plugins
 * <p>
 * Notes: This packet can be edited freely. Please be cognisant of what values you change and how they will be interpreted by the starnubclient.
 * <p>
 * Packet Direction: Client -> Server
 * <p>
 * Starbound 1.0 Compliant (Versions 622, Update 1)
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0 Beta
 */
public class CollectLiquidPacket extends Packet {

    private Vec2IArray tilePositions = new Vec2IArray();
    private byte liquidId;

    /**
     * Recommended: For internal use with StarNub Player Sessions
     * <p>
     * Uses: This is used to pre-construct packets for a specific side of a connection
     * <p>
     * @param DIRECTION       Direction representing the direction the packet is heading
     * @param SENDER_CTX      ChannelHandlerContext which represents the sender of this packets context (Context can be written to)
     * @param DESTINATION_CTX ChannelHandlerContext which represents the destination of this packets context (Context can be written to)
     */
    public CollectLiquidPacket(Direction DIRECTION, ChannelHandlerContext SENDER_CTX, ChannelHandlerContext DESTINATION_CTX) {
        super(DIRECTION, Packets.COLLECTLIQUID.getPacketId(), SENDER_CTX, DESTINATION_CTX);
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This is used to construct a packet for with no destination. This CAN ONLY be routed by using (routeToGroup, routeToGroupNoFlush) methods
     * <p>
     * @param tilePositions
     * @param liquidId
     */
    public CollectLiquidPacket(Vec2IArray tilePositions, byte liquidId) {
        super(Packets.COLLECTLIQUID.getDirection(), Packets.COLLECTLIQUID.getPacketId());
        this.tilePositions = tilePositions;
        this.liquidId = liquidId;
    }

    /**
     * Recommended: For internal StarNub use with copying
     * <p>
     * Uses: This will construct a new packet from a packet
     *
     * @param packet DamageTileGroupPacket representing the packet to construct from
     */
    public CollectLiquidPacket(CollectLiquidPacket packet) {
        super(packet);
        this.tilePositions = packet.getTilePositions().copy();
        this.liquidId = packet.getLiquidId();
    }

    public Vec2IArray getTilePositions() {
        return tilePositions;
    }

    public void setTilePositions(Vec2IArray tilePositions) {
        this.tilePositions = tilePositions;
    }

    public byte getLiquidId() {
        return liquidId;
    }

    public void setLiquidId(byte liquidId) {
        this.liquidId = liquidId;
    }

    /**
     * This will provide a new object while copying all of the internal data as well into this
     * new Object
     *
     * @return DamageTileGroupPacket the new copied object
     */
    @Override
    public CollectLiquidPacket copy() {
        return new CollectLiquidPacket(this);
    }

    /**
     * Recommended: For internal use with StarNub Player Sessions
     * <p>
     * Uses: This method will read in a {@link io.netty.buffer.ByteBuf} into this packets fields
     * <p>
     * Note: This particular read will discard the packet if the tile radius exceed that of the {@link org.starnub.starbounddata.types.vectors.Vec2IArray} constructor
     *
     * @param in ByteBuf representing the reason to be read into the packet
     */
    @Override
    public void read(ByteBuf in) {
        try {
            this.tilePositions.read(in);
            this.liquidId = (byte) in.readUnsignedByte();
        } catch (ArrayIndexOutOfBoundsException e) {
            super.recycle();
            in.skipBytes(in.readableBytes());
        }
    }

    /**
     * Recommended: For internal use with StarNub Player Sessions
     * <p>
     * Uses: This method will write to a {@link io.netty.buffer.ByteBuf} using this packets fields
     * <p>
     *
     * @param out ByteBuf representing the space to write out the packet reason
     */
    @Override
    public void write(ByteBuf out) {
        this.tilePositions.write(out);
        out.writeByte(this.liquidId);
    }

    @Override
    public String toString() {
        return "CollectLiquidPacket{" +
                "tilePositions=" + tilePositions +
                ", liquidId=" + liquidId +
                "} " + super.toString();
    }
}
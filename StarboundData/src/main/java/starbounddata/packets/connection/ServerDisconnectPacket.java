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

package starbounddata.packets.connection;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import starbounddata.packets.Packet;
import starbounddata.packets.Packets;

import static starbounddata.packets.StarboundBufferReader.readStringVLQ;
import static starbounddata.packets.StarboundBufferWriter.writeStringVLQ;

/**
 * Represents the ServerDisconnectPacket and methods to generate a packet data for StarNub and Plugins
 * <p>
 * Notes: This packet can be edited freely. This packet will send the starnubclient a disconnect notification
 * <p>
 * Packet Direction: Server -> Client
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0 Beta
 */
public class ServerDisconnectPacket extends Packet {

    private String reason;

    /**
     * Recommended: For internal StarNub usage.
     * <p>
     * Uses: This is used to pre-construct packets for a specific side of a connection
     * <p>
     *
     * @param DIRECTION       Direction representing the direction the packet flows to
     * @param SENDER_CTX      ChannelHandlerContext which represents the sender of this packets context (Context can be written to)
     * @param DESTINATION_CTX ChannelHandlerContext which represents the destination of this packets context (Context can be written to)
     */
    public ServerDisconnectPacket(Direction DIRECTION, ChannelHandlerContext SENDER_CTX, ChannelHandlerContext DESTINATION_CTX) {
        super(DIRECTION, Packets.DISCONNECTRESPONSE.getPacketId(), SENDER_CTX, DESTINATION_CTX);
    }

    /**
     * Recommended: For internal StarNub usage.
     * <p>
     * Uses: This method will be used to send a packet to the client with the server version. You only need the destination in order t
     * router this packet
     * <p>
     *
     * @param DESTINATION_CTX ChannelHandlerContext which represents the destination of this packets context (Context can be written to)
     * @param reason          String representing the reason
     */
    public ServerDisconnectPacket(ChannelHandlerContext DESTINATION_CTX, String reason) {
        super(Packets.DISCONNECTRESPONSE.getDirection(), Packets.DISCONNECTRESPONSE.getPacketId(), null, DESTINATION_CTX);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Recommended: For internal StarNub usage.
     * <p>
     * Uses: This method will read in a {@link io.netty.buffer.ByteBuf} into this packets fields
     * <p>
     *
     * @param in ByteBuf representing the reason to be read into the packet
     */
    @Override
    public void read(ByteBuf in) {
        this.reason = readStringVLQ(in);
    }

    /**
     * Recommended: For internal StarNub usage.
     * <p>
     * Uses: This method will write to a {@link io.netty.buffer.ByteBuf} using this packets fields
     * <p>
     *
     * @param out ByteBuf representing the space to write out the packet reason
     */
    @Override
    public void write(ByteBuf out) {
        writeStringVLQ(out, this.reason);
    }

    @Override
    public String toString() {
        return "ServerDisconnectPacket{" +
                "reason='" + reason + '\'' +
                "} " + super.toString();
    }
}
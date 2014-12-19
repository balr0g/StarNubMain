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

package starbounddata.types.variants;

import io.netty.buffer.ByteBuf;

/**
 * Represents a Variant Length Quantity(VLQ).
 * <p>
 * This is a complex data type. More information can be found here.
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @see <a href="http://en.wikipedia.org/wiki/Variable-length_quantity">VLQ's in Action</a>
 * @since 1.0 Beta
 */
public class VLQ {

    /**
     * This is the length of the VLQ field
     */
    private int length;

    /**
     * This is the actual value of the VLQ which represents how many bytes follow the VLQ
     */
    private long value;

    private byte [] bytes;

    public VLQ() {
    }

    public VLQ(int length, long value) {
        this.length = length;
        this.value = value;
    }


    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will created a s{@link starbounddata.types.variants.VLQ} from a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will create a VLQ object and SHOULD NOT be used when possible
     * <p>
     *
     * @param in ByteBuf in which is to be read
     * @return VLQ which represent how many reason bytes exist after the {@link starbounddata.types.variants.VLQ}
     */
    public static VLQ signedFromBuffer(ByteBuf in) {
        VLQ value = unsignedFromBuffer(in);
        long val = value.getValue();
        if ((value.getValue() & 1) == 0x00)
            val = (long) val >> 1;
        else
            val = -((long) (val >> 1) + 1);
        value.setValue(val);
        return value;
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will created a u{@link starbounddata.types.variants.VLQ} from a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will create a VLQ object and SHOULD NOT be used when possible
     * <p>
     *
     * @param in ByteBuf in which is to be read
     * @return VLQ which represent how many reason bytes exist after the {@link starbounddata.types.variants.VLQ}
     * @throws IndexOutOfBoundsException if the {@link starbounddata.types.variants.VLQ} ran out of bytes while reading
     */
    public static VLQ unsignedFromBuffer(ByteBuf in) throws IndexOutOfBoundsException {
        long value = 0L;
        int length = 0;
        while (length <= 10) {
            byte tmp = in.readByte();
            value = (value << 7) | (long) (tmp & 0x7f);
            length++;
            if ((tmp & 0x80) == 0)
                break;
        }
        return new VLQ(length, value);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    ///////////////////     REPRESENTS VLQ OBJECT CREATION METHODS     ///////////////////

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will created a s{@link starbounddata.types.variants.VLQ} from a long
     * <p>
     * Notes: This will create a VLQ object and SHOULD NOT be used when possible
     * <p>
     *
     * @param value long the size of the bites that will precede this VLQ
     * @return byte[] which is the actual sVLQ field
     */
    public static byte[] createSignedVLQNoObject(long value) {
        long result;
        if (value < 0) {
            result = ((-(value + 1)) << 1) | 1;
        } else {
            result = value << 1;
        }
        return createVLQNoObject(result);
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will created a u{@link starbounddata.types.variants.VLQ} from a long
     * <p>
     * Notes: This will create a VLQ object and SHOULD NOT be used when possible
     * <p>
     *
     * @param value long the size of the bites that will precede this VLQ
     * @return byte[] which is the actual VLQ field
     */
    public static byte[] createVLQNoObject(long value) {
        int numRelevantBits = 64 - Long.numberOfLeadingZeros(value);
        int numBytes = (numRelevantBits + 6) / 7;
        if (numBytes == 0)
            numBytes = 1;
        byte[] output = new byte[numBytes];
        for (int i = numBytes - 1; i >= 0; i--) {
            int curByte = (int) (value & 0x7F);
            if (i != (numBytes - 1))
                curByte |= 0x80;
            output[i] = (byte) curByte;
            value >>>= 7;
        }
        return output;
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will read a s{@link starbounddata.types.variants.VLQ} from a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param in ByteBuf representing the bytes to be read for a reason length from a signed vlq
     * @return int representing the reason length
     */
    public static int readSignedFromBufferNoObject(ByteBuf in) {
        int payloadLength = readUnsignedFromBufferNoObject(in);
        if ((payloadLength & 1) == 0x00) {
            payloadLength = payloadLength >> 1;
        } else {
            payloadLength = -((payloadLength >> 1) + 1);
        }
        return payloadLength;
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will read a u{@link starbounddata.types.variants.VLQ} from a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param in ByteBuf representing the bytes to be read for a reason length from a vlq
     * @return int representing the reason length
     */
    public static int readUnsignedFromBufferNoObject(ByteBuf in) {
        int vlqLength = 0;
        int payloadLength = 0;
        while (vlqLength <= 10) {
            int tmpByte = in.readByte();
            payloadLength = (payloadLength << 7) | (tmpByte & 0x7f);
            vlqLength++;
            if ((tmpByte & 0x80) == 0) {
                break;
            }
        }
        return payloadLength;
    }

    ///////////////////     REPRESENTS NO VLQ OBJECT CREATION METHODS     ///////////////////

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will read a s{@link starbounddata.types.variants.VLQ} from a byte[]
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param in byte[] representing the bytes to be read for a reason length from a signed vlq
     * @return int representing the reason length
     */
    public static int readSignedFromBufferNoObject(byte[] in) {
        int payloadLength = readUnsignedFromBufferNoObject(in);
        if ((payloadLength & 1) == 0x00) {
            payloadLength = payloadLength >> 1;
        } else {
            payloadLength = -((payloadLength >> 1) + 1);
        }
        return payloadLength;
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will read a u{@link starbounddata.types.variants.VLQ} from a byte[]
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param in byte[] representing the bytes to be read for a reason length from a vlq
     * @return int representing the reason length
     */
    public static int readUnsignedFromBufferNoObject(byte[] in) {
        int vlqLength = 0;
        int payloadLength = 0;
        while (vlqLength <= 10) {
            int tmpByte = in[vlqLength];
            payloadLength = (payloadLength << 7) | (tmpByte & 0x7f);
            vlqLength++;
            if ((tmpByte & 0x80) == 0) {
                break;
            }
        }
        return payloadLength;
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will write a s{@link starbounddata.types.variants.VLQ} to a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param out   ByteBuf in which is to be read
     * @param value long representing the VLQ value to be written out
     */
    public static void writeSignedVLQNoObjectPacketEncoder(ByteBuf out, long value) {
        if (value < 0) {
            value = ((-(value + 1)) << 1) | 1;
        } else {
            value = value << 1;
        }
        writeVLQNoObjectPacketEncoder(out, value);
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will write a u{@link starbounddata.types.variants.VLQ} to a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param out   ByteBuf in which is to be read
     * @param value long representing the VLQ value to be written out
     */
    public static void writeVLQNoObjectPacketEncoder(ByteBuf out, long value) {
        int numRelevantBits = 64 - Long.numberOfLeadingZeros(value);
        int numBytes = (numRelevantBits + 6) / 7;
        if (numBytes == 0) {
            numBytes = 1;
        }
        out.writerIndex(numBytes + 1); /* Sets the write index at the number of bytes + 1 byte for packet id */
        for (int i = numBytes - 1; i >= 0; i--) {
            int curByte = (int) (value & 0x7F);
            if (i != (numBytes - 1)) {
                curByte |= 0x80;
            }
            out.setByte(i + 1, curByte); /* Sets the byte at index + 1 byte for packet id */
            value >>>= 7;
        }
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will write a u{@link starbounddata.types.variants.VLQ} to a {@link io.netty.buffer.ByteBuf}
     * <p>
     * Notes: This will not create a VLQ object and should be used
     * <p>
     *
     * @param out   ByteBuf in which is to be read
     * @param value long representing the VLQ value to be written out
     */
    public static void writeVLQNoObject(ByteBuf out, int offSet, long value) {
        int numRelevantBits = 64 - Long.numberOfLeadingZeros(value);
        int numBytes = (numRelevantBits + 6) / 7;
        if (numBytes == 0) {
            numBytes = 1;
        }
        out.writerIndex(numBytes + offSet); /* Sets the write index at the number of bytes + offSet byte for */
        for (int i = numBytes - 1; i >= 0; i--) {
            int curByte = (int) (value & 0x7F);
            if (i != (numBytes - 1)) {
                curByte |= 0x80;
            }
            out.setByte(i + offSet, curByte); /* Sets the byte at index + offSet byte for */
            value >>>= 7;
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "VLQ{" +
                "length=" + length +
                ", value=" + value +
                '}';
    }
}
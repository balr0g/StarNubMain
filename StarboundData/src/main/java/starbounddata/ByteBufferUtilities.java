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

package starbounddata;

import io.netty.buffer.ByteBuf;
import starbounddata.types.variants.VLQ;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

public class ByteBufferUtilities {

    public static void search(ByteBuf in, String string){
        search(in, string.getBytes(Charset.forName("UTF-8")));
    }


    public static void search(ByteBuf in, UUID uuid) throws IOException {
        System.err.println(uuid);
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        byte[] uuidByteArray;

        try(
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        ){
            dos.writeLong(mostSignificantBits);
            dos.writeLong(leastSignificantBits);
            uuidByteArray = baos.toByteArray();
        }
        System.err.println(Arrays.toString(uuidByteArray));
    }

    public static void search(ByteBuf in, byte[] byteArray){
        byte[] searchable = in.array();
        int searchBytesSize = byteArray.length;
        int searchableBytesSize = searchable.length;
        byte[] vlqByteArray = VLQ.createVLQNoObject(searchBytesSize);
        int vlqLength = vlqByteArray.length;
        System.err.println("Searchable Bytes Size: " + searchableBytesSize + ". Search Bytes Size: " + searchBytesSize);
        System.err.println("VLQ Byte Pattern: " + Arrays.toString(vlqByteArray) + ". VLQ Length: " + vlqLength +  ". VLQ Value: " + searchBytesSize);
        System.err.println("Search Bytes: " + Arrays.toString(byteArray));
        System.err.println("Searchable Byte Array: " + Arrays.toString(searchable));
        int indexVLQStartIndex = 0;
        int indexVLQEndIndex = 0;
        int searchBytesStartIndex = 0;
        int searchBytesEndIndex = 0;


    }
}
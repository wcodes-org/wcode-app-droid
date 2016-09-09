package org.wcodes.core;

import java.nio.ByteBuffer;

/**
 * Created by Ujjwal on 28-08-2015.
 */
public class Package {

    byte[] ar;
    int lBD;
    int lbT;

    Package() {

    }

    public static void packInt(byte[] data, int i) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        System.arraycopy(b.array(), 0, data, 0, 4);
    }

    public static int unpackInt(byte[] data) {
        ByteBuffer b = ByteBuffer.wrap(data);
        return b.getInt();
    }

}

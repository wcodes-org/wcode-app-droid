package org.wcodes.core;

/**
 * Created by Ujjwal on 20-09-2015.
 */
public class Utility {

    /* 8-bit CRC value using polynomial  X^8 + X^5 + X^4 + 1 */
    static byte POLYVAL = (byte)0x8C;
    static byte crc8(byte[] data, int len) {
        byte crc = 0;
        for(int i = 0; i < len; i++) {
            byte c;
            byte x = data[i];
            c = crc;
            for(int j = 0; j < 8; j++) {
                if(((c ^ x) & 1) != 0)
                    c = (byte)((c >> 1 ) ^ POLYVAL);
                else
                    c >>= 1;
                x >>= 1;
            }
            crc = c;
        }
        return crc;
    }

    /*
    int GetBitSize(int max) {
        DOUBLE bit = log((float)max)/log((float)2);
        return ((int)ceil(bit));
    }
    */

    static int getBitSize(int val) {
        int i;
        for(i = 0; i < Constant.LONG_BITS; i++) {
            if((0x80000000 & (val<<i)) != 0)
                break;
        }
        return Constant.LONG_BITS-i;
    }

}

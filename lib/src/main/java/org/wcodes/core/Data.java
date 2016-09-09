package org.wcodes.core;

/**
 * Created by Ujjwal on 20-09-2015.
 */
public class Data {

    byte[] dataArray;
    int lByteData;
    int lBitTrail;
    int lbBlock;
    int nBlock;

    Header header;
    boolean bChecksum;

    public Data(Header header, boolean bChecksum, int lbBlock) {
        this.header = header;
        this.bChecksum = bChecksum;
        this.lbBlock = lbBlock;
    }

    public void initDataArray(int l) {
        dataArray = new byte[l];
    }

    public void initDataArray(int lBData, int lbTrail) {
        lByteData = lBData;
        lBitTrail = lbTrail;
        dataArray = new byte[lBData];
    }

    public byte getByte(int i) {
        return dataArray[i];
    }

    public byte[] getByteArray() {
        return dataArray;
    }

    public byte[] getFinalByteArray() {
        byte[] properArray = new byte[lByteData];
        System.arraycopy(dataArray, 0, properArray, 0, lByteData);
        return properArray;
    }

    public int bitLength() {
        return nBlock*lbBlock;
    }


    void pack(byte[] vIn) {
        int i;
        int lbB = vIn.length*lbBlock;
        initDataArray(lbB/Constant.BYTE_BITS + 1);
        for(i = 0; i < lbB/Constant.BYTE_BITS; i++)
            dataArray[i] = vIn[i];
        lBitTrail = (Constant.BYTE_BITS-lbB% Constant.BYTE_BITS) % Constant.BYTE_BITS;
        if(lBitTrail != 0) {
            lBitTrail = i+1;
            dataArray[i] = (byte) (vIn[i] << lBitTrail);
        }
        else
            lByteData = i;
    }

    void unpackbyte(byte[] arIn, int lBD, int lbT) {
        int lbB = lBD* Constant.BYTE_BITS-lbT;
        int i;
        for(i = 0; i < lbB/ Constant.BYTE_BITS; i++)
            dataArray[i] = arIn[i];
        if(lbT != 0)
            dataArray[i] = (byte) (arIn[i] >> lbT);
    }

    void pack(int lbB, byte[] arIn, int l) {
        int iCE = 0;
        int iCI = 0;
        byte bfr = 0;
        for(int i = 0; i < l; i++) {
            byte val = arIn[i];
            if(iCI + lbB > Constant.BYTE_BITS) {
                int lbNxt = iCI + lbB - Constant.BYTE_BITS;
                dataArray[iCE++] = (byte) (bfr | (val >> lbNxt));
                bfr = (byte) (val << (Constant.BYTE_BITS-lbNxt));
                iCI = lbNxt;
            }
            else {
                int lbRmn = Constant.BYTE_BITS - (iCI + lbB);
                bfr |= val << lbRmn;
                if(lbRmn != 0)
                    iCI += lbB;
                else { // == Constant.BYTE_BITS
                    iCI = 0;
                    dataArray[iCE++] = bfr;
                    bfr = 0;
                }
            }
        }
        if(iCI != 0) {
            dataArray[iCE++] = bfr;
            lBitTrail = Constant.BYTE_BITS-iCI;
        }
        else
            lBitTrail = 0;
        lByteData = iCE;
    }

    // lbB - length in bits of a block
    void unpack(byte[] arIn, int lBD, int lbT, int lbB) throws CustomException {
        int iCE = 0;
        int iCI = 0;
        int j = 0;
        for(int i = 0; iCE < lBD; i++) {
            byte val;
            if(iCE == lBD-1)
                if(Constant.BYTE_BITS-iCI == lbT)
                    break;
            if(iCI + lbB > Constant.BYTE_BITS) {
                int lbNxt = iCI + lbB - Constant.BYTE_BITS;
                byte bfr = (byte) ( (arIn[iCE] << lbNxt) & ~(0xFF << lbB) );
                iCE++;

                if(iCE == lBD)
                    throw new CustomException("Exception");// Exception

                val = (byte) (bfr | arIn[iCE] >> (Constant.BYTE_BITS - lbNxt));
                iCI = lbNxt;
            }
            else {
                int lbRmn = Constant.BYTE_BITS - (iCI + lbB);
                val = (byte) ((arIn[iCE] >> lbRmn) & ~(0xFF << lbB));
                if(lbRmn != 0)
                    iCI += lbB;
                else { // iCodeBit : Constant.BYTE_BITS
                    iCE++;
                    iCI = 0;
                }
            }
            dataArray[j++] = val;
        }
        this.lByteData = j;
//	szBase32[j] = NULL;
    }
}

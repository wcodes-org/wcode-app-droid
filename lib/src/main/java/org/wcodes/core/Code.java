package org.wcodes.core;

/**
 * Created by Ujjwal on 11-09-2015.
 */
public class Code {

    //	int tN;
    int trailIndexWidth;
    Header header;

    Dictionary dictionary;

    int[] code;
    int nCode;
    int iCodeElement;
    int iCodeBit;

    boolean bChecksum;
    int lbChecksum;

    public Code(Dictionary dictionary, Header header, boolean bChecksum) {
        this.header = header;
        this.dictionary = dictionary;
        trailIndexWidth = Constant.INDEX_BITS;//GetBitSize(N_WORDS);
        this.bChecksum = bChecksum;
        if(bChecksum)
            lbChecksum = Constant.CHECKSUM_SIZE;
        else
            lbChecksum = 0;
    }

    void initCodeLength(int lDataArray, int lbDataArrayTrail) {
        int l = (lbChecksum + (lDataArray*8-lbDataArrayTrail))/Constant.BYTE_BITS + 1;
        code = new int[l];
    }

//    int getDataLength() {
//        int lbTrail =
//        return (nCode*Constant.INDEX_BITS-lbChecksum-lbTrail)/Constant.BYTE_BITS+1;
//    }

    void encode(Data data) {
        code[0] = 0;
        setHeader(data);

        for(int i = 0; i < data.lByteData-1; i++)
            setBits(data.getByte(i), Constant.BYTE_BITS);
        setBits((byte) (data.getByte(data.lByteData-1) >> data.lBitTrail), Constant.BYTE_BITS - data.lBitTrail);

        if(iCodeBit != 0)
            nCode = iCodeElement +1;
        else
            nCode = iCodeElement;
    }

    public boolean decode(Data data) {
        iCodeElement = 0;
        iCodeBit = 0;
        getHeader(data.lbBlock);

        int lb = (nCode* Constant.INDEX_BITS)-(header.nTrailingBlocks *data.lbBlock)-((iCodeElement * Constant.INDEX_BITS)+ iCodeBit);
        lb = lb - (lb % Constant.INDEX_BITS); // removing the extra unused bits beside trail
        data.nBlock =  lb / data.lbBlock;
//        int lb = data.bitLength();
        int dlBitTrail;
        int dlByteData;
        if(lb % Constant.BYTE_BITS == 0) {
            dlBitTrail = 0;
            dlByteData = lb/ Constant.BYTE_BITS;
        }
        else {
            dlBitTrail = Constant.BYTE_BITS-lb% Constant.BYTE_BITS;
            dlByteData = lb/ Constant.BYTE_BITS+1;
        }
        data.initDataArray(dlByteData, dlBitTrail);

        for (int i = 0; i < data.lByteData; i++)
            getBits(data.getByteArray(), i, Constant.BYTE_BITS);

        if(bChecksum)
            if(header.checksum != Utility.crc8(data.getByteArray(), data.lByteData))
                return false;

        return true;
    }

    int getCode(int i) {
        return code[i];
    }

    String printCode() {
        StringBuffer message = new StringBuffer();
        int i = 0;
        while(true) {
            message.append(outCode(code[i]));
            if(++i == nCode)
                break;
            else
                message.append(Constant.SPACE_STR);
        }
        return message.toString();
    }

    int inCode(String token) {
        return dictionary.index(token);
    }

    String outCode(int code) {
        return dictionary.token(code);
    }

    boolean readCode(String[] token) {
        int i;
        for(i = 0; i < token.length; i++) {
            int x = inCode(token[i]);
            if(x == -1)
                return false;
            else
                code[i] = x;
        }
        nCode = i;
        return true;
    }

//  Set the code data
    void setHeader(Data data) {
//	    Header.header = 0;
//	    Header.dataHeaderP = 0;

//	    setBits(code, iCodeElement, iCodeBit, Header.header, HEADER_SIZE);			// Header			//iCodeBit = 4;
//	    setBits(code, iCodeElement, iCodeBit, Header.checksumType, CHECKSUM_TYPE_SIZE);// CheckSum Type	//iCodeBit = 6;
        if(bChecksum) {
//		header.checksumType = 0;
            header.checksum = Utility.crc8(data.getByteArray(), data.lByteData);
            setBits(header.checksum, Constant.BYTE_BITS);			// CheckSum
        }
        int lbMaxTrailingBlocks = Utility.getBitSize(header.maxNTrailBlock(data.lbBlock));
        header.nTrailingBlocks = (byte) nTrailBlock(iCodeBit +lbMaxTrailingBlocks/*+DATA_HEADER_P_SIZE*/, data.lbBlock, data.nBlock);
        setBits(header.nTrailingBlocks, lbMaxTrailingBlocks);		// Trailing Zeroes
//	    setBits(code, iCodeElement, iCodeBit, Header.dataHeaderP, DATA_HEADER_P_SIZE);// Header Bit
    }

    void getHeader(int lbBlock) {
//	    getBits(Header.header, HEADER_SIZE, code, iCodeElement, iCodeBit);
//	    getBits(Header.checksumType, CHECKSUM_TYPE_SIZE, code, iCodeElement, iCodeBit);
        byte[] dataArray = new byte[1];
        if (bChecksum) {
            getBits(dataArray, 0, Constant.CHECKSUM_SIZE);
            header.checksum = dataArray[0];
        }
        // length of bits required to record maximum trailing blocks; e.g. At most 7 bits could nTrailingBlocks in the last byte for 1 bit data, which requires 3 bits to record
        int lbMaxTrailingBlocks = Utility.getBitSize(header.maxNTrailBlock(lbBlock));
        getBits(dataArray, 0, lbMaxTrailingBlocks);
        header.nTrailingBlocks = dataArray[0];
//	    getBits(Header.dataHeaderP, DATA_HEADER_P_SIZE, code, iCodeElement, iCodeBit);
    }

    void setBits(byte data, int bitSz) { //BitSz < 8 - LSB data
        int bfr;
        if(iCodeBit + bitSz > trailIndexWidth) {
            int lBitNxt = iCodeBit + bitSz - trailIndexWidth;
            bfr = (data & 0xFF) >> lBitNxt;
            code[iCodeElement] |= bfr;
            code[++iCodeElement] = 0;
            bfr = data & ~(0xFFFFFFFF << lBitNxt);
            code[iCodeElement] |= bfr << (trailIndexWidth - lBitNxt);
            iCodeBit = lBitNxt;
        }
        else {
            int lBitLft = trailIndexWidth - (iCodeBit + bitSz);
            code[iCodeElement] |= (data << lBitLft);
            iCodeBit += bitSz;
            if(iCodeBit == trailIndexWidth) {
                code[++iCodeElement] = 0;
                iCodeBit = 0;
            }
        }
    }

    void getBits(byte[] dataArray, int index, int bitSz) {
        byte bfr;
        if(iCodeBit + bitSz > trailIndexWidth) {
            int lBitNxt = iCodeBit + bitSz - trailIndexWidth;
            bfr = (byte)(getCode(iCodeElement) << lBitNxt);
            dataArray[index] = (byte)(bfr & ~(0xFF << bitSz));
            iCodeElement++;
            if(iCodeElement < nCode) {
                bfr = (byte)(getCode(iCodeElement) >> (trailIndexWidth - lBitNxt));
                dataArray[index] |= bfr;
            }	//iCodeBit now contributes to lbT
            iCodeBit = lBitNxt;
        }
        else {
            int lBitRmn = trailIndexWidth - (iCodeBit + bitSz);
            bfr = (byte)(getCode(iCodeElement) >> lBitRmn);
            dataArray[index] = (byte)(bfr & ~(0xFF << bitSz));
            iCodeBit += bitSz;
        }
    }

    int nTrailBlock(int iCI, int lbBlock, int nBlock) {
        int tlVb = (iCI+lbBlock*nBlock)% trailIndexWidth;
        if(tlVb != 0) {
            int tlb = trailIndexWidth -tlVb;
            return tlb/lbBlock;
        }
        else
            return 0;
    }

    public String in(Data data, byte[] x, int lbTrail) {
        initCodeLength(x.length, lbTrail);
        data.pack(x);
        encode(data);
        return printCode();
    }

    public byte[] out(Data data, String message) {
        String[] tokens = message.split(" ");
        code = new int[tokens.length];
        readCode(tokens);
        decode(data);
        return data.getFinalByteArray();
    }

}

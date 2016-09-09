package org.wcodes.core;

/**
 * Created by Ujjwal on 21-09-2015.
 */
public class Header {
//	    byte header;
//	    byte checksumType;
    byte checksum;
    byte nTrailingBlocks;
//	    byte dataHeaderP;
//  bit Size of index : GetBitSize(N_WORDS); 10 for base 1024
    int lbCodeIndex = Constant.INDEX_BITS;
    int maxNTrailBlock(int lbBlock) {
        return lbCodeIndex / lbBlock;
    }
}

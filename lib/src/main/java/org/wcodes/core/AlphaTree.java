package org.wcodes.core;

/**
 * Created by Ujjwal on 05-07-2015.
 */
public class AlphaTree {

    static final int N_ALPHABETS = 26;
    static final char ALPHA_OFFSET = 'a';

    Branch[] trunk;
    Branch[] traced;
    Trace trace;
    boolean bValid;

    public AlphaTree() {
        traced = trunk = new Branch[N_ALPHABETS];
        for(int i = 0; i < N_ALPHABETS; i++) {
            traced[i] = new Branch();
        }
        trace = new Trace();
    }

    public void load(Dictionary dictionary) {
        Branch[] traced;
        for(int i = 0; i < dictionary.nToken(); i++) {
            traced = trunk;
            String dictWord = dictionary.token(i);
            int lDictWord = dictionary.tokenLen(i);
            for(int j = 0; j < lDictWord-1; j++) {
                int index = dictWord.charAt(j)-ALPHA_OFFSET;
                if(traced[index].arBranch == null) {
                    traced[index].arBranch = new Branch[N_ALPHABETS];
                    for (int k = 0; k < N_ALPHABETS; k++) {
                        traced[index].arBranch[k] = new Branch();
                    }
                }
                traced = traced[index].arBranch;
            }
            traced[dictWord.charAt(lDictWord-1)-ALPHA_OFFSET].bValid = true;
        }
    }

    public void reset() {
        traced = trunk;
        bValid = false;
        trace.reset(traced);
    }

    public boolean traceC(char c) {
        if(traced == null)
            return false;
        else {
            if (traced[c - ALPHA_OFFSET].arBranch != null || traced[c - ALPHA_OFFSET].bValid) {
                bValid = traced[c - ALPHA_OFFSET].bValid;
                traced = traced[c - ALPHA_OFFSET].arBranch;
                trace.reset(traced);
                return true;
            } else
                return false;
        }
    }

    public boolean isValid() {  // traced so far
        return bValid;
    }

    public char[] getNextRemaining() {
        if(traced == trunk)
            return null;
        try {
            while(trace.getNext());
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return trace.getTraced();
    }

}

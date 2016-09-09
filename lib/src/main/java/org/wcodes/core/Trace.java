package org.wcodes.core;

/**
 * Created by Ujjwal on 05-07-2015.
 */
public class Trace {

    static char alphaCode(int index) {
        return (char)(index+ALPHA_OFFSET);
    }

    class TraceStackStruct {
        Branch[] arBranch; //Branch
        int index;
    };

    TraceStackStruct[] traceStack;
    int lTraceStack;

    static final int MAX_TRACE_DEPTH = 64;
    static final int N_ALPHABETS = 26;
    static final int ALPHA_OFFSET = 'a';
    static final int MAX_MID_LEN = 50;

    char[] sTraced;

    boolean bValid;
    boolean bBranch;

    public Trace() {
        traceStack = new TraceStackStruct[MAX_TRACE_DEPTH];
        for (int i = 0; i < N_ALPHABETS; i++) {
            traceStack[i] = new TraceStackStruct();
        }
        sTraced = new char[MAX_MID_LEN];
        //for (int i = 0; i < MAX_MID_LEN; i++) {
            //sTraced[i] = new char();
    }

    boolean push(Branch[] arBranch, int index) {
        if(lTraceStack == MAX_TRACE_DEPTH)
            return false;
        else {
            traceStack[lTraceStack].arBranch = arBranch;
            traceStack[lTraceStack].index = 0;
            lTraceStack++;
            return true;
        }
    }

    boolean pop() {
        if(lTraceStack > 1) {
            lTraceStack--;
            return true;
        }
        else
            return false;
    }

    public void reset(Branch[] arBranch) {
        traceStack[0].arBranch = arBranch;
        traceStack[0].index = 0;
        lTraceStack = 1;
    }

    public boolean getNext() throws org.wcodes.core.CustomException {
        while(lTraceStack != 0) {
            int top = lTraceStack-1;
            Branch[] xBranch = traceStack[top].arBranch;
            if(xBranch == null) {
                bValid = false;
                return false;
            }
            int index = traceStack[top].index;

            if(traceStack[top].index < N_ALPHABETS-1)
                (traceStack[top].index)++;
            else
            if(!pop()) {
                bValid = false;
                return false;
            }

            bValid = (xBranch[index]).bValid;
            bBranch = (xBranch[index]).arBranch != null;
            if(bValid || bBranch) {
                sTraced[lTraceStack-1] = alphaCode(index);
                if (bBranch)
                    if(!push((xBranch[index]).arBranch, 0))	//if(lTraceStack)
                        throw new org.wcodes.core.CustomException("ERROR_PUSH_DEPTH");//exit(ERROR_PUSH_DEPTH);
                if(bValid)
                    return false;
                else
                    return true;
            }
        }
        return false;
    }

    public boolean getTraced(char[] szTraced) {
        if(bValid) {
            int l;

            if(bBranch)
                l = lTraceStack - 1;
            else
                l = lTraceStack;

            for(int i = 0; i < l; i++)
                szTraced[i] = sTraced[i];
            //szTraced[l] = null;
            return true;
        }
        else
            return false;
    }

}

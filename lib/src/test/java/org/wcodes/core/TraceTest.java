package org.wcodes.core;

import org.testng.Assert;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by Ujjwal on 7/13/2016.
 */
public class TraceTest {

    final String number = "9886181804";

    org.wcodes.core.Dictionary dictionary;

    public TraceTest() {
        dictionary = new org.wcodes.core.Dictionary("wordlist.txt");
    }

    static String in(org.wcodes.core.Dictionary dictionary, byte[] x) {
        org.wcodes.core.Header header = new org.wcodes.core.Header();
        org.wcodes.core.Code code = new org.wcodes.core.Code(dictionary, header, false);
        org.wcodes.core.Data data = new org.wcodes.core.Data(header, false, 8);

        code.in(data, x, 0);
        return code.printCode();
    }

    static byte[] out(org.wcodes.core.Dictionary dictionary, String message) {
        org.wcodes.core.Header header = new org.wcodes.core.Header();
        org.wcodes.core.Code code = new org.wcodes.core.Code(dictionary, header, false);
        org.wcodes.core.Data data = new org.wcodes.core.Data(header, false, 8);

        code.out(data, message);
        return data.getByteArray();
    }

    @org.testng.annotations.Test
    public void dictionaryTest() {
        Assert.assertEquals(dictionary.nToken(), 1024, "Failure: "+"not 1024");
    System.out.println(dictionary.token(0));
    }

    @org.testng.annotations.Test
    public void traceTest() {
        AlphaTree alphaTree = new AlphaTree();
        alphaTree.load(dictionary);

        alphaTree.traceC('a');
        alphaTree.traceC('l');
        char[] s;
        s = alphaTree.getNextRemaining();

        Assert.assertEquals(new String(s), "arm", "Failure: "+"Traced not: "+"al-arm");
    System.out.println(s);
    }

    @org.testng.annotations.Test
    public void stringTest() {
        String input = number;
        String message;
        String output = "";
        {
            byte[] inData = input.getBytes();

            message = in(dictionary, inData);
        System.out.println(message);
        }
        {
            byte[] outData = out(dictionary, message);
            try {
                output = new String(outData, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        System.out.println(output);
        }
        Assert.assertEquals(input, output, "Failure: ");
    }

    @org.testng.annotations.Test
    public void numTest() {
        String input = number;
        String message;
        String output;
        {
            byte[] inData;
            BigInteger bgI = new BigInteger(input);
            inData = bgI.toByteArray();

            message = in(dictionary, inData);
        System.out.println(message);
        }
        {
            byte[] outData = out(dictionary, message);
            BigInteger bgI = new BigInteger(outData);
            output = bgI.toString();//String.valueOf(Package.unpackInt(outData));

        System.out.println(output);
        }
        Assert.assertEquals(input, output, "Failure: ");
    }

}
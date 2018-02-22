package org.wcodes.core;

/**
 * Created by Ujjwal on 05-07-2015.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Dictionary {

    private ArrayList<String> tokens;
    int max_word_length;

//    private final String UTF8_BOM = "\uFEFF";

    public void Create() {
        tokens = new ArrayList<>();
    }

//    public Dictionary() {
//        Create();
//    }

    public Dictionary(String configFile){
        max_word_length = 0;
        Create();
        load(configFile);
    }

    public Dictionary(byte[] dictionaryFile) {
        Create();
        load(dictionaryFile);
    }

    void load(byte[] dictionaryFileData) {
        InputStream is = null;
        BufferedReader bufferedReader;
        try {
            is = new ByteArrayInputStream(dictionaryFileData);
            bufferedReader = new BufferedReader(new InputStreamReader(is));

//            check_UTF8_BOM(bufferedReader);
            String token;
            while((token = bufferedReader.readLine()) != null){
                if(token.length() > max_word_length)
                    max_word_length = token.length();
                this.tokens.add(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(is != null) is.close();
            } catch (Exception ex){

            }
        }
    }

//    void check_UTF8_BOM(BufferedReader bufferedReader) throws java.io.IOException {
//        char[] cBuf = new char[2];
//        bufferedReader.read(cBuf, 0, 2);    // to proceed the buffer pointer
////        if(!cBuf.toString().equals(UTF8_BOM)) {  // FFFD FFFD for Android byteArray load
////            System.out.println("Incorrect UTF-8 file; no BOM");
////            throw new java.io.IOException();
////        }
//    }

    void load(String configFilePath) {
        try {
            //FileReader fileReader = new FileReader(configFile, "UTF8");
            InputStreamReader iStreamReader = new InputStreamReader(new FileInputStream(configFilePath), "UTF8");
            BufferedReader bufferedReader = new BufferedReader(iStreamReader);

            //check_UTF8_BOM(bufferedReader);

            String token;
            while ((token = bufferedReader.readLine()) != null) {
                this.tokens.add(token);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int nToken() {
        return tokens.size();
    }

    public String token(int i) {
        return tokens.get(i);
    }

    int tokenLen(int i) {
        return tokens.get(i).length();
    }

    int index(String token) {
        return tokens.indexOf(token);
    }

    public int maxWordLength() {
        return max_word_length;
    }

}

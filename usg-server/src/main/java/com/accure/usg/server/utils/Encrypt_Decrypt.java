/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.server.utils;

import com.accure.finance.dto.BankName;
import com.accure.usg.common.manager.DBManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Asif
 */
public class Encrypt_Decrypt {
    
    public static String getBase64EncodeString(String input) throws Exception {
        String output = null;
        if (input == null || input.isEmpty()) {
            return output;
        }
        byte[] inputEncoded = Base64.encodeBase64(input.getBytes());
        output = new String(inputEncoded, Charset.forName("UTF-8"));
        return output;
    }
    
    public static String getBase64DecodeString(String input) throws Exception {
        String output = null;
        if (input == null || input.isEmpty()) {
            return output;
        }
        byte[] inputDecoded = Base64.decodeBase64(input);
        output = new String(inputDecoded, Charset.forName("UTF-8"));
        return output;
    }
    public static void main(String[] args) throws Exception {
         String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_NAME_TABLE, "575e8b83d6bd21928acbdfb9");
        List<BankName> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<BankName>>() {
        }.getType());
        BankName relation = relationlist.get(0);
        String str=new Gson().toJson(relation);
        String en=getBase64EncodeString(str);
        String de=getBase64DecodeString(en);
        //System.out.println(en);
        //System.out.println(de);
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.reportformat;

import com.accure.finance.dto.BankReconcilation;
import com.accure.finance.manager.BankReconcilationManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CreateWordFile {
    public static void main(String[] args)throws Exception 
   {
        BankReconcilationManager manager1=new BankReconcilationManager();
        List<BankReconcilation> bankDetails1= manager1.fetchAllBankReconcilation();
   }
    
     public static  String createWordforReconcilation(String data) throws FileNotFoundException
     {
   //Blank Document
   
         try{
             JSONArray arrayJson1 = new JSONArray(data);
             CDL.toString(arrayJson1);
             XWPFDocument document1= new XWPFDocument();
         
        
   //Write the Document in file system
   FileOutputStream out = new FileOutputStream(
   new File("D:/reconcilationBankWord.docx"));
        
   //create table
   XWPFTable table = document1.createTable();
   XWPFTableRow tableRowOne = table.getRow(0);
   tableRowOne.getCell(0).setText("From Date");
   tableRowOne.addNewTableCell().setText("To Date");
   tableRowOne.addNewTableCell().setText("Location");
   tableRowOne.addNewTableCell().setText("Ledger");
   tableRowOne.addNewTableCell().setText("Reconcilation status");
for(int j=0;j<arrayJson1.length();j++){ 
    JSONObject obj1 = arrayJson1.getJSONObject(j);
//create first row
   
   
   //create second row
   XWPFTableRow tableRowTwo = table.createRow();
   tableRowTwo.getCell(0).setText(obj1.getString("fromDate"));
   tableRowTwo.getCell(1).setText(obj1.getString("toDate"));
   tableRowTwo.getCell(2).setText(obj1.getString("locationName"));
   tableRowTwo.getCell(2).setText(obj1.getString("ledgerName"));
   tableRowTwo.getCell(2).setText(obj1.getString("reconcilationStatus"));
   
  
}	
   document1.write(out);
   out.close();
   return "success";
   ////System.out.println("reconcilationBank.docx written successully in 'D' drive");
         }catch(Exception e){
            e.printStackTrace();
          return null;
        } 
}
}
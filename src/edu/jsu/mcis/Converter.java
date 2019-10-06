package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            boolean firstLine = true;
            JSONObject jsonFile = new JSONObject();
            JSONArray genData = new JSONArray();
            JSONArray colData = new JSONArray();
            JSONArray rowData = new JSONArray();
            JSONArray workData = new JSONArray();
            
            for(String[] set : full){
                if(firstLine == true){
                    for(int num = 0; num < 5; num++){
                        colData.add(set[num]);
                    }
                    firstLine = false;
                }
                else{
                    rowData.add(set[0]);
                    for(int num = 1; num < 5; num++){
                        workData.add(Integer.parseInt(set[num]));
                    }
                    genData.add(workData.clone());
                    workData.clear();
                }
                jsonFile.put("colHeaders", colData);
                jsonFile.put("rowHeaders", rowData);
                jsonFile.put("data", genData);
            }
            results = jsonFile.toJSONString();
        }                
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONParser reader = new JSONParser();
            Object parseObj = reader.parse(jsonString);
            JSONObject jsonFile = (JSONObject) parseObj;
            
            JSONArray colHead;
            JSONArray rowHead;
            JSONArray data;     
            
            String thisLine = null;
            
            String[] workString = null;
            String[] colString = null;
            String[] dataString = null;
            
            List<String> list = new ArrayList<String>();
            
            
            colHead = (JSONArray) jsonFile.get("colHeaders");
            rowHead = (JSONArray) jsonFile.get("rowHeaders");
            data = (JSONArray) jsonFile.get("data");
            
            for(int num = 0; num < colHead.size(); num++){
                list.add(colHead.get(num).toString());
            }
            colString = list.toArray(new String[list.size()]);
            list.clear();
            csvWriter.writeNext(colString);
            
            for(int rowNum = 0; rowNum < rowHead.size(); rowNum++){
                list.add(rowHead.get(rowNum).toString());
                thisLine = data.get(rowNum).toString();
                thisLine = thisLine.replace("[",",").replace("]","");
                workString = (thisLine.split(","));
                for(int setNum = 1; setNum < colHead.size(); setNum++){
                    list.add(workString[setNum]);
                }
                dataString = list.toArray(new String[list.size()]);
                list.clear();
                csvWriter.writeNext(dataString.clone());
                dataString = null;
            }
            csvWriter.close();
            
            results = writer.toString();
        } 
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}
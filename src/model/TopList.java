package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TopList {

    private ArrayList<TopListEntry> entries;
    private int minScore=0;
    private int itemsInList=0;
    private int maxNumberOfItems=5;

    public static final String defaultPathToJSON = "./toplist.json";

    public TopList(){
        entries = new ArrayList<TopListEntry>();

        try {
            this.importToplistFromJSON(defaultPathToJSON);
        } catch (FileNotFoundException e) {
            this.exportToplistToJSON(defaultPathToJSON);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void add(int score,String name){
        entries.add(new TopListEntry(score,name));
        Collections.sort(entries);
        if (entries.size() > maxNumberOfItems){
            entries.remove(entries.size()-1);
        }
        itemsInList = entries.size();
        minScore =  entries.get(itemsInList-1).getScore();
        exportToplistToJSON(defaultPathToJSON);
    }

    public void importToplistFromJSON(String path) throws IOException,ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(path));
        JSONObject jsonObject =  (JSONObject) obj;
        JSONArray toplist = (JSONArray)jsonObject.get("toplist");
        for (Object o:toplist){
            JSONObject listItem = (JSONObject) o;
            this.add(((Long) listItem.get("score")).intValue(),(String) listItem.get("name"));
        }
    }

    public void exportToplistToJSON(String path){
        JSONObject obj = new JSONObject();
        JSONArray jsonlist = new JSONArray();
        for (TopListEntry e : this.entries) {
            JSONObject entry = new JSONObject();
            entry.put("score", e.getScore());
            entry.put("name", e.getName());
            jsonlist.add(entry);
        }
        obj.put("toplist", jsonlist);
        try (FileWriter file = new FileWriter(path)) {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public int getMinScore() {
        return minScore;
    }

    public int getItemsInList(){
        return itemsInList;
    }

    public TopListEntry getEntry(int index){
        return entries.get(index);
    }
    public ArrayList<TopListEntry> getEntries() {
        return entries;
    }

}

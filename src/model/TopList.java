package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TopList {

    private ArrayList<TopListEntry> entries;
    private int minScore = 0;
    private int itemsInList = 0;
    private int maxNumberOfItems = 5;

    private static final String defaultPathToJSON = "./toplist.json";

    /**
     * Toplist constructor, reads toplist from file or initializes with empty list and creates file.
     */
    public TopList() {
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

    /**
     * Adds new item to current toplist
     *
     * @param score
     * @param name
     */
    public void add(int score, String name) {
        entries.add(new TopListEntry(score, name));
        Collections.sort(entries);
        if (entries.size() > maxNumberOfItems) {
            entries.remove(entries.size() - 1);
        }
        itemsInList = entries.size();
        minScore = entries.get(itemsInList - 1).getScore();
        exportToplistToJSON(defaultPathToJSON);
    }

    /**
     * Opens file to store toplist
     *
     * @param path Path to file
     * @throws IOException    throws exception if cannot open file
     * @throws ParseException
     */
    public void importToplistFromJSON(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(path));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray toplist = (JSONArray) jsonObject.get("toplist");
        for (Object o : toplist) {
            JSONObject listItem = (JSONObject) o;
            this.add(((Long) listItem.get("score")).intValue(), (String) listItem.get("name"));
        }
    }

    /**
     * Saves toplist to file in path.
     *
     * @param path
     */
    public void exportToplistToJSON(String path) {
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

    /**
     * Getter method for minimum score
     *
     * @return
     */
    public int getMinScore() {
        return minScore;
    }

    /**
     * Getter method for items in list.
     *
     * @return
     */
    public int getItemsInList() {
        return itemsInList;
    }

    /**
     * Getter method for an entry.
     *
     * @param index
     * @return entry
     */
    public TopListEntry getEntry(int index) {
        return entries.get(index);
    }

    /**
     * Setter method for entries list
     *
     * @return Entries list
     */
    public ArrayList<TopListEntry> getEntries() {
        return entries;
    }

}

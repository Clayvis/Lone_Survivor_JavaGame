package com.lonesurvivor.Utils;

import com.lonesurvivor.Models.Location;
import com.lonesurvivor.Models.NPC;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * reads and parses all the json files using Simple
 */
public class JSONParserClass {

    private static JSONParserClass parser = null;

    private JSONParser jsonParser;
    private BufferedReader in;
    private FileReader locReader;
    private FileReader commReader;
    private FileReader infoReader;
    //private FileReader outsReader;

    private JSONArray locFile;
    //private JSONObject locFile;
    private JSONArray commFile;
    private JSONObject infoFile;
    //private JSONArray outsFile;

    private List<Location> locations;
    private Location location;

    private List<JSONArray> commands;

    private JSONObject verbObj;
    private JSONObject nounObj;
    private JSONObject commObj;

    private JSONArray verbList;
    private JSONArray nounList;
    private JSONArray commList;

    private String gameInfo;

    public static JSONParserClass getInstance(){
        if(parser == null){
            try{
                parser = new JSONParserClass();
            }catch (IOException | ParseException e){
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
        return parser;
    }

    public JSONParserClass() throws IOException, ParseException {
        InputStream is;
        InputStream is2 = getFileFromResourceAsStream("json/CommandList.json");
        InputStreamReader isr2 = new InputStreamReader(is2);

        //locations = new ArrayList<>();
        commands = new ArrayList<>();
        in = new BufferedReader(new InputStreamReader(System.in));
        jsonParser = new JSONParser();

        //locReader = new FileReader("src/Java/External_Files/PlaneCrash.json");
        //commReader = new FileReader("src/Java/External_Files/CommandList.json");
        //infoReader = new FileReader("src/Java/External_Files/GameInfo.json");
        //outsReader = new FileReader("src/Java/External_Files/Outside.json");

        //locFile = (JSONArray) jsonParser.parse(locReader);

        //commFile = (JSONArray) jsonParser.parse(commReader);
        //infoFile = (JSONObject) jsonParser.parse(infoReader);
        //outsFile = (JSONArray) jsonParser.parse(outsReader);

        //locFile = (JSONArray) jsonParser.parse(isr);
        commFile = (JSONArray) jsonParser.parse(isr2);

    }

    public ArrayList<NPC> npcGenerator(String file) {
        jsonParser = new JSONParser();
        ArrayList<NPC> npcs = new ArrayList<>();

        try {
            InputStream is = getFileFromResourceAsStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            locFile = (JSONArray) jsonParser.parse(isr);
        } catch (Exception e) { //If there is no file being passed send message and exit
            System.out.println("File not found");
            System.exit(0);
        }

        for (Object o : locFile) {
            JSONObject obj = (JSONObject) o;

            JSONObject npcInLocation = (JSONObject) obj.get("locationNPC");

            NPC newNpc = new NPC((String) npcInLocation.get("name"), (Double) npcInLocation.get("power"));
            npcs.add(newNpc);
        }
        return npcs;
    }

    public List<Location> locationParser(String file){
        jsonParser = new JSONParser();
        int counter = 0;
        try {
            InputStream is = getFileFromResourceAsStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            locFile = (JSONArray) jsonParser.parse(isr);
        } catch (Exception e) { //If there is no file being passed send message and exit
            locations = null;
            System.out.println("File not found");
            System.exit(0);
        }
        locations = new ArrayList<>();

        for (Object o : locFile) {
            JSONObject obj = (JSONObject) o;

            // creating location object by passing it's respective paramaters with their data types.
            location = new Location((String) obj.get("locationName"), (String) obj.get("locationDescription"), (String) obj.get("locationImage")
                    , (JSONArray) obj.get("locationItems"), npcGenerator("json/PlaneCrash.json").get(counter),(JSONObject) obj.get("locationDirections"));

            //adding the newly created location object into an arraylist
            locations.add(location);
            counter++;
        }
        return locations;
    }


    public List<JSONArray> commandParser() {
        verbObj = (JSONObject) commFile.get(0);
        nounObj = (JSONObject) commFile.get(1);
        commObj = (JSONObject) commFile.get(2);

        verbList = (JSONArray) verbObj.get("verb");
        nounList = (JSONArray) nounObj.get("noun");
        commList = (JSONArray) commObj.get("valid commands");

        commands.add(0, verbList);
        commands.add(1, nounList);
        commands.add(2, commList);

        return commands;
    }



    private static InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = JSONParserClass.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

}
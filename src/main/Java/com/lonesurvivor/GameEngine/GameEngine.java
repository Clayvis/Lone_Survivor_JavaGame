package com.lonesurvivor.GameEngine;

import com.lonesurvivor.Models.Location;
import com.lonesurvivor.Models.MusicClass;
import com.lonesurvivor.Models.NPC;
import com.lonesurvivor.Models.Player;
import com.lonesurvivor.Utils.JSONParserClass;
import com.lonesurvivor.Utils.TextParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.List;

import static com.lonesurvivor.Views.LocationFrame.textDisplayGui;

public class GameEngine {

    private static GameEngine engine = null;
    private static final String LOCATION_PATH="json/PlaneCrash.json";

    private TextParser parser;
    private List<Location> locations;
    private NPC npc;
    private List<String> command;
    public static int dayCount = 1;
    private boolean hasWon;

    public static GameEngine getInstance(){
        if(engine == null){
            try{
                engine = new GameEngine();
            }catch (IOException | ParseException e){
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
        return engine;
    }

    public GameEngine() throws IOException, ParseException {
        // initializes the object and sets the locations list and default player location
        parser = new TextParser();
        locations = JSONParserClass.getInstance().locationParser(LOCATION_PATH);
        Player.getInstance().setLocations(locations);
        Player.getInstance().setPlayerLocation(locations.get(2));

    }

    public void startGame() throws IOException, ParseException, java.text.ParseException {

        while (!hasWon) {
            checkWin();
            dayTracker();
        }
    }


    public void dayTracker(){

    //for every 10 action points, a day pass and it get reset.
        if (Player.getInstance().getActionTracker() > 10){
            dayCount++;
            Player.getInstance().setActionTracker(0);
        }
    }

    public void checkWin() {
        if(Player.getInstance().getPlayerLocation() == locations.get(8)) {
            textDisplayGui("Congratulations , you won !!!");
                hasWon = true;
            }
        //TODO: either handle for rescued win on day 3 OR decide that they die
        //FIXME: edit gameinfo.json to reflect these results
        else if (dayCount == 4) {
            textDisplayGui("The trekking through uncharted forest takes its toll on you over 3 days and you succumb to your fatigue and injuries.\n You died. Thank you for playing.");
            System.exit(0);
        }

    }

    public void handleInput(String input){// public method handleInput if you give it input from GUI (copy and paste from above)
        try {
            parser.InitialInput(input);

            if (parser.getValidCommand().size() == 2) {
                command = parser.getValidCommand();
                //processor.processCommand(command);
                commandProcessor();
            }
            //TODO update printed text to UI
            //checkWin();TODO ???
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    public void commandProcessor() throws IOException, ParseException, java.text.ParseException {

        //converted if statements to a switch. And moved commands to player class
        //move engine
        switch (command.get(0)) {
            case "go":
                try {
                    Player.getInstance().moveEngine(command.get(1));
                    MusicClass.soundFx(command.get(0));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "get": try {
                Player.getInstance().getEngine(command.get(1));
                MusicClass.soundFx(command.get(0));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
                break;
            case "look": try{
                Player.getInstance().lookEngine(command.get(1));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
                break;
            case "use": try{
                Player.getInstance().useEngine(command.get(1));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            case "quit": try{
                Player.getInstance().quitEngine(command.get(1));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
                break;
            case "help": try{
                Player.getInstance().helpEngine(command.get(1));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
                break;
            case "attack":
                //conflict engine
                Player.getInstance().attackEngine(command.get(1));
        }
    }
    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        GameEngine.dayCount = dayCount;
    }
}

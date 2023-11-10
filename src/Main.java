import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static Map<String, String> playerLog = new HashMap<>();
    private static Place startPlace;
    private static Place current;
    private static List<String> inventory = new ArrayList<>();
    private static boolean HasSaved = true;
    private static Database player;

    private static Place move(String direction) {
        HasSaved = false;
        switch (direction) {
            case "north": return current.getNorth();
            case "east": return current.getEast();               
            case "south": return current.getSouth();
            case "west": return current.getWest();
            default: return null;
        }
    }

    private static void take(String item) {
        List<String> items = new ArrayList<>();
        items = current.getItems();
        if (!items.isEmpty() && items.contains(item)) {
            inventory.add(item);
            current.getItems().remove(item);
            playerLog.put(String.format("%s, %s", current.getRoom(), item), "remove");
            System.out.printf(">> You added %s to your inventory\n", item);
            HasSaved = false;
        } else {
            System.out.println("Invalid command");
        }
    }

    private static void enterRoom() {
        System.out.println(String.format(">> %s", current.getName()));
        System.out.println(current.getDescription());
        if (!current.getItems().isEmpty()) {
            for (String items : current.getItems()) {
                System.out.printf(">> You see a %s\n", items);
            }
        }
        if (current.getNorth() != null) {
            System.out.printf(">>> North: %s\n", current.getNorth().getName());
        }
        if (current.getEast() != null) {
            System.out.printf(">>> East: %s\n", current.getEast().getName());
        }
        if (current.getSouth() != null) {
            System.out.printf(">>> South: %s\n", current.getSouth().getName());
        }
        if (current.getWest() != null) {
            System.out.printf(">>> West: %s\n", current.getWest().getName());
        }
    }

    public static void main(String[] args) throws Exception{
        
        if (args.length > 0) {
            startPlace = Place.setting(args[0]);
        } else {
            System.out.println("No file found");
            System.exit(0);
        }

        Console cons = System.console();
        String input = "";
        System.out.println("Welcome explorer, please login");
        input = cons.readLine("> ");

        while (!input.equals("quit")) {

            if (input.startsWith("login")) {
                if (!HasSaved) {
                    player.save();
                }

                playerLog.clear();
                Place.clearRooms();;
                startPlace = Place.setting(args[0]);
                String username = input.substring(6);
                player = new Database(username.trim());
                player.login();
                playerLog = player.getUserInfo();
                if (playerLog.isEmpty()) {
                    current = startPlace;
                } else {
                    if (playerLog.containsKey("start")) {
                        current = Place.findPlace(playerLog.get("start").trim());
                    }
                    if (playerLog.containsKey("inventory")) {
                        inventory.clear();
                        String[] items = playerLog.get("inventory").trim().split(" ");
                        for (String item : items) {
                            inventory.add(item);
                        }
                    }
                    if (playerLog.values().contains(" remove")) {
                        for (String k : playerLog.keySet()) {
                            if (playerLog.get(k).equals(" remove")) {
                                String[] roomItem = k.split(",");
                                List<String> itemsInRoom = Place.findPlace(roomItem[0]).getItems();
                                itemsInRoom.remove(roomItem[1].trim());
                                Place.findPlace(roomItem[0]).setItems(itemsInRoom);
                            }
                        }
                            
                    }
                }
                
                enterRoom();
                input = cons.readLine("> ");

            } else if (input.equals("save")) {
                playerLog.put("start", current.getRoom());
                String items = "";
                for (String str : inventory) {
                    items += String.format("%s ", str);
                }
                playerLog.put("inventory", items);
                player.save();
                HasSaved = true;
                System.out.println("Your progress has been saved");
                input = cons.readLine("> ");

            } else if (input.startsWith("move")) {
                String[] line = input.trim().split(" ");
                if (move(line[1]) != null) {
                    current = move(line[1]);
                    enterRoom();
                } else {
                    System.out.println(">> Invalid move");
                }
                input = cons.readLine("> ");

            } else if (input.startsWith("take")) {
                String[] line = input.trim().split(" ");
                for (int i = 1; i < line.length; i++) {
                    take(line[i]);
                }
                input = cons.readLine("> ");

            } else if (input.startsWith("inventory")) {
                if (inventory.isEmpty()) {
                    System.out.println(">> Your inventory is empty");
                } else {
                    System.out.printf(">> Your inventory contains:\n");
                    for (String item : inventory) {
                        System.out.printf(">> %s\n", item);
                    }
                }
                input = cons.readLine("> ");
            
            } else {
                    System.out.println("Invalid command");
                    input = cons.readLine("> ");
            }

        }

        System.out.println("Thank you for playing");
    }
}

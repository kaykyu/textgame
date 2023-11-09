import java.io.Console;

public class Main {

    private static Place startPlace;
    private static Place current;

    private static Place move(String direction) {
        switch (direction) {
            case "north": return current.getNorth();
            case "east": return current.getEast();               
            case "south": return current.getSouth();
            case "west": return current.getWest();
            default: return null;
        }
    }


    private static void enterRoom() {
        System.out.println(String.format(">> %s", current.getName()));
        System.out.println(current.getDescription());
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
        System.out.println("Welcome, explorer. Enter 'start' to begin your journey.");
        input = cons.readLine("> ");

        if (input.equals("start")) {
            current = startPlace;
            enterRoom();
            input = cons.readLine("> ");

            while (!input.equals("quit")) {

                if (input.startsWith("move")) {
                    String[] line = input.trim().split(" ");
                    if (move(line[1]) != null) {
                        current = move(line[1]);
                        enterRoom();
                    } else {
                        System.out.println("Invalid move");
                    }
                    input = cons.readLine("> ");

                    } else if (input.startsWith("something")) {

                    } else {
                        System.out.println("Invalid command");
                        input = cons.readLine("> ");
                    }

            }
        }

        System.out.println("Invalid command");
        }

    
}
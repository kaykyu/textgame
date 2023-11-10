import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Place {

    private String room;
    private String name;
    private String description;
    private Place north;
    private Place south;
    private Place east;
    private Place west;
    private boolean isStart;
    private List<String> items;
    private static List<Place> places = new ArrayList<>();

    public Place(String room, String name, String description) {
        this.room = room;
        this.name = name;
        this.description = description.replaceAll("<break>", "\n");
        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
        this.isStart = false;
        this.items = new ArrayList<>();
    }

    public String getRoom() {return room;}
    public void setRoom(String room) {this.room = room;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Place getNorth() {return north;}
    public void setNorth(Place north) {this.north = north;}

    public Place getSouth() {return south;}
    public void setSouth(Place south) {this.south = south;}

    public Place getEast() {return east;}
    public void setEast(Place east) {this.east = east;}

    public Place getWest() {return west;}
    public void setWest(Place west) {this.west = west;}

    public boolean isStart() {return isStart;}
    public void setStart(boolean isStart) {this.isStart = isStart;}

    public List<String> getItems() {return items;}
    public void setItems(List<String> items) {this.items = items;}

    public static Place findPlace(String room) {
        for (Place rm : places) {
            String roomName = rm.getRoom();
            if (room.equals(roomName)) {
                return rm;
            }
        }
        return null;
    }

    public static void clearRooms() {
        places.clear();
    }

    public static Place setting(String fileName) throws Exception {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        String line = "";
        List<List<String>> map = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            List<String> room = new ArrayList<>();
            while (line != null && line.length() > 0) {
                String[] str = line.trim().split(":");
                room.add(str[1].trim());
                line = br.readLine();
                }
            map.add(room);
            
        }

        for (int i = 0; i < (map.size() - 1); i++){
            List<String> rooms = map.get(i);
            Place place = new Place(rooms.get(0), rooms.get(1), rooms.get(2));
            places.add(place);
            }

        for (int i = 0; i < places.size(); i++) {
            for (int j = 3; j < (map.get(i).size()); j++) {
                String[] dir = map.get(i).get(j).split(" ");
                switch (dir[0]) {
                    case "north": places.get(i).setNorth(findPlace(dir[1]));
                     break;
                    case "south": places.get(i).setSouth(findPlace(dir[1]));
                     break;
                    case "east": places.get(i).setEast(findPlace(dir[1]));
                     break;
                    case "west": places.get(i).setWest(findPlace(dir[1]));
                     break;    
                    default: places.get(i).getItems().add(dir[0]);
                    break;               
                }
            }
        }

        String start = map.get(map.size()-1).get(0);
        Place startPlace = findPlace(start.trim());        
        startPlace.setStart(true);

        br.close();
        return startPlace;        
    }

}

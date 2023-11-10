import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Database {
    
    private String username;
    private Map<String, String> userInfo;
    private static String path = "userdb";
    private File userDB;

    public Database(String name) {
        this.username = name;
        this.userInfo = new HashMap<>();
        this.userDB = new File (String.format("%s/%s.txt", path, name));
    }

    public String getUser() {return username;}
    public void setUser(String username) {this.username = username;}

    public Map<String, String> getUserInfo() {return userInfo;}
    public void setUserInfo(Map<String, String> userInfo) {this.userInfo = userInfo;}
        
    public File getUserDB() {return userDB;}
    public void setUserDB(File userDB) {this.userDB = userDB;}

    public void login() throws Exception {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        File file = userDB;
        if (file.createNewFile()) {
            System.out.printf("Welcome new explorer, %s\n", username);
        } else {
            System.out.printf("Welcome back, %s\n", username);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] lines = line.trim().split(":");
                userInfo.put(lines[0], lines[1]);
                // userInfo = reader.nextLine()
                //     .map(str -> str.trim().split(":"))
                //     .map(k -> line[0], v -> line[1])
                //     ;
            }
            reader.close();
        }
    }

    public void save() throws Exception {
        File file = userDB;
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String str : userInfo.keySet()) {
            bw.write(String.format("%s: %s\n", str, userInfo.get(str)));
        }

        bw.flush();
        bw.close();
    }
}

package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class User {
    private final String name;
    private final String email;
    private final String phoneNo;
    final String bitsId;
    private String password;

    public User(String name, String email, String phoneNo, String bitsId, String password) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.bitsId = bitsId;
        this.password = password;
    }

    public User(String bitsId, String password) throws FileNotFoundException {
        this.bitsId = bitsId;
        this.password = password;
        String nm = null;
        String eml=null;
        String pn=null;
        String currentLine;
        Scanner reader=new Scanner (new FileReader("User.txt"));
        while (reader.hasNextLine()) {
            currentLine=reader.nextLine();
            String trimmedLine = currentLine.trim();
            if(!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                nm = line[0];
                eml = line[1];
                pn = line[2];
            }
        }
        this.name=nm;
        this.email=eml;
        this.phoneNo=pn;
    }

    public String getName() {
        return name;
    }
}

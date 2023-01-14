package application;

import java.io.*;
import java.util.Scanner;

public class Menu {
    String name;
    File file;
    PrintWriter pw;

    public Menu(String name) throws IOException {
        this.name=name+"Menu.txt";
        file = new File(this.name);
        if (!file.createNewFile())
        {
            System.out.println("Already registered!");
        }
    }
    public Menu(String name,String s)
    {
        this.name=name+"Menu.txt";
        this.file= new File(this.name);
    }



    public String getName() {
        return name;
    }

    synchronized public void addItem(Item item) throws IOException {
        pw =new PrintWriter(new FileWriter(name,true));
        pw.println(item.getName()+';'+item.getItemId()+';'+item.getPrice()+';'+item.getQuantityLeft());
        pw.close();
    }

    synchronized public void removeItem(String itemName) throws IOException {
        pw =new PrintWriter(new FileWriter("Transition.txt",false));
        Scanner reader = new Scanner(new FileReader(name));
        String currentLine;

        while(reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine=reader.nextLine();
            String trimmedLine = currentLine.trim();
            if(!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[0].equals(itemName)) continue;
                pw.println(trimmedLine);
            }
        }
        pw.close();
        CpPas(name);
    }

    synchronized public void setPrice(String itemName,int price) throws IOException {
        pw =new PrintWriter(new FileWriter("Transition.txt",false));
        Scanner reader = new Scanner(new FileReader(name));
        String currentLine;

        while(reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine=reader.nextLine();
            String trimmedLine = currentLine.trim();
            if(!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[0].equals(itemName)) {
                    currentLine = line[0] + ';' + line[1] + ';' + price + ';' + line[3];
                    pw.println(currentLine);
                }
            }
        }
        pw.close();
        CpPas(name);
    }

    synchronized public void setQuantityLeft(String itemName,int quantityLeft) throws IOException {
        pw =new PrintWriter(new FileWriter("Transition.txt",false));
        Scanner reader = new Scanner(new FileReader(name));
        String currentLine;

        while(reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine=reader.nextLine();
            String trimmedLine = currentLine.trim();
            String s;
            if(!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[0].equals(itemName)) {
                    s=line[0] + ';' + line[1] + ';' + line[2] + ';' + quantityLeft;
                }
                else
                {
                    s=trimmedLine;
                }
                pw.println(s);
            }
        }
        pw.close();
        CpPas(name);
    }

    synchronized private String getFilename(String s) {
        String fileName = null;
        Scanner reader = new Scanner("rediInfo.txt");
        String currentLine;

        while (reader.hasNextLine()) {
            currentLine= reader.nextLine();
            String trimmedLine = currentLine.trim();
            if(!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[0].equals(s)) {
                    fileName = line[1];
                }
            }
        }
        return fileName;
    }

    synchronized private void CpPas(String s) throws IOException {
        PrintWriter pw =new PrintWriter(new FileWriter(s,false));
        Scanner reader=new Scanner(new FileReader("Transition.txt"));
        String currentLine = null;
        while(reader.hasNextLine())
        {
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                pw.println(trimmedLine);
            }
        }
        pw.close();
    }
}

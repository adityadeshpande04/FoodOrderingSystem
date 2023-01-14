package application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Owner extends User implements Runnable {
    Redi redi;
    public Owner(String name, String email, String phoneNo, String bitsId, String password, String rediName) throws IOException {
        super(name, email, phoneNo, bitsId, password);
        this.redi = new Redi(rediName);
    }

    public Owner(String bitsId, String password, String rediName, double totalRevenue) throws FileNotFoundException {
        super(bitsId, password);
        this.redi = new Redi(rediName, totalRevenue);
    }

    public Redi getRedi() {
        return redi;
    }

    @Override
    public void run() {
        try {
            work();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void work() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader sc = new BufferedReader(isr);
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.println("Enter 1 to get total revenue.\nEnter 2 to change quantity.\nEnter 3 to add item.\nEnter 4 to remove item.\nEnter 5 to view Orders.");
        int choice = Integer.parseInt(sc.readLine().trim());
        if (choice == 1) {
            System.out.println(redi.getTotalRevenue());
            work();
        } else if (choice == 2) {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.println("Enter name of item to change its quantity : ");
            printMenu(this.redi.getMenu().getName());
            String iN = sc.readLine().trim();
            System.out.println("Enter quantity Left : ");
            int n = Integer.parseInt(sc.readLine().trim());
            this.redi.getMenu().setQuantityLeft(iN, n);
            System.out.println("Quantity changed successfully!");
            printMenu(this.redi.getMenu().getName());
            work();
        } else if (choice == 3) {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.println("Enter the name of the Item : ");
            String nm = sc.readLine();
            System.out.println("Enter Item Id : ");
            String iD = sc.readLine();
            System.out.println("Enter price to be set : ");
            double price = Double.parseDouble(sc.readLine().trim());
            System.out.println("Enter available Quantity : ");
            int quan = Integer.parseInt(sc.readLine().trim());
            Item item = new Item(nm, iD, price, quan);
            this.redi.getMenu().addItem(item);
            System.out.println("Item Added Successfully");
            printMenu(this.redi.getMenu().getName());
            work();
        } else if (choice == 4) {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.println("Enter the name of the Item to be removed : ");
            printMenu(this.redi.getMenu().getName());
            String nm = sc.readLine();
            this.redi.getMenu().removeItem(nm);
            printMenu(this.redi.getMenu().getName());
            work();
        } else if (choice == 5) {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.println("To View all orders Enter 1 :\nTo view current month orders Enter 2 :");
            choice = Integer.parseInt(sc.readLine());
            if (choice == 1) {
                printOrders();
                work();
            }
            else {
                double exp= getMonthlyExpense();
                System.out.println("Monthly Revenue is : "+exp);
                work();
            }
        }
    }

    synchronized private void printOrders() throws FileNotFoundException {
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        Scanner reader = new Scanner(new FileReader(this.redi.getName().trim() + "Orders.txt"));
        String currentLine = null;

        while (reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            String st;
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                System.out.println("Date : " + line[0] + "Time : " + line[1] + " -> Customer Name : " + line[2] + " ->Item Ordered : " + line[3] + " -> Quantity Ordered : " + line[4] + " ->Total Bill amount :  " + line[5]);
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
    }

    synchronized private double getMonthlyExpense() throws FileNotFoundException {
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        double x = 0;
        Scanner reader = new Scanner(new FileReader(this.redi.getName().trim() + "Orders.txt"));
        String currentLine = null;
        LocalDateTime dt = LocalDateTime.now();
        while (reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            String st;
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                String[] line1 = line[0].trim().split("/");
                int mon = dt.getMonthValue();
                if (mon == Integer.parseInt(line1[1])) {
                    System.out.println("Date : " + line[0] + "Time : " + line[1] + " -> Redi : " + line[2] + " ->Item Ordered : " + line[3] + " -> Quantity Ordered : " + line[4] + " ->Total Bill amount :  " + line[5]);
                    x += Double.parseDouble(line[5].trim());
                }
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        return x;
    }

    synchronized private void printMenu(String fileName) throws IOException {
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        String currentLine;
        Scanner reader = new Scanner(new FileReader(fileName));
        currentLine = null;
        int n = 1;
        while (reader.hasNextLine()) {
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                System.out.println((n + ")" + " Item id : " + line[1] + " : " + line[0] + "->" + "Rupees " + line[2] + " |Quantity left : " + line[3]).trim());
                n++;
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
    }

    synchronized private String getFilename(String s) throws FileNotFoundException {
        String fileName = null;
        Scanner reader = new Scanner(new FileReader("rediInfo.txt"));
        String currentLine;

        while (reader.hasNextLine()) {
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[0].equals(s)) {
                    fileName = line[1];
                }
            }
        }
        return fileName;
    }
}

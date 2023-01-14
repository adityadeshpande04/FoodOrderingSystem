package application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Customer extends User implements Runnable {
    Double totalExpense;
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader sc = new BufferedReader(isr);
    File file;

    public Customer(String name, String email, String phoneNo, String bitsId, String password) throws IOException {
        super(name, email, phoneNo, bitsId, password);
        this.totalExpense = (double) 0;
        this.file = new File(name.trim() + "Orders.txt");
        if (!file.createNewFile()) {
            System.out.println("Already registered!");
        }
    }

    public Customer(String bitsId, String password, double totalExpense) throws FileNotFoundException {
        super(bitsId, password);
        this.totalExpense = totalExpense;
        this.file = new File(super.getName().trim() + "Orders.txt");
    }

    @Override
    public void run() {
        try {
            order();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void order() throws IOException {
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.println("Enter 1 to Select a redi.\nEnter 2 to get total Expense.\nEnter 3 to get orders summary\nany other key to exit.");
        int choice = Integer.parseInt(sc.readLine().trim());
        if (choice == 2) {
            System.out.println(getTotalExpense());
            order();
        } else if (choice == 1) {
            try {
                orderFood();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (choice == 3) {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.println("Enter 1 to get total order summary\nEnter 2 to get Rediwise order summary\nEnter 3 to get Current month order.");
            choice = Integer.parseInt(sc.readLine().trim());
            if (choice == 1) {
                printOrdersSummary();
                order();
            } else if (choice == 2) {
                System.out.println("Enter Redi Name to view orders : ");
                printRediList();
                String rediName = sc.readLine().trim();
                double totalExp = getRediExpense(rediName);
                System.out.println("Total Expense at " + rediName + " is " + totalExp);
                order();
            } else if (choice == 3) {
                double exp = getMonthlyExpense();
                System.out.println("Expense is : " + exp);
                order();
            }
        }
    }

    private double getMonthlyExpense() throws FileNotFoundException {
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        double x = 0;
        Scanner reader = new Scanner(new FileReader(super.getName() + "Orders.txt"));
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
                int mon=dt.getMonthValue();
                if (mon==Integer.parseInt(line1[1])) {
                    System.out.println("Date : " + line[0] + "Time : " + line[1] + " -> Redi : " + line[2] + " ->Item Ordered : " + line[3] + " -> Quantity Ordered : " + line[4] + " ->Total Bill amount :  " + line[5]);
                    x += Double.parseDouble(line[5].trim());
                }
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        return x;
    }

     synchronized private double getRediExpense(String rediName) throws FileNotFoundException {
         System.out.println("--------------------------------------------------------------------------------------------------------------------");
         double x = 0;
        synchronized (this) {
            Scanner reader = new Scanner(new FileReader(super.getName() + "Orders.txt"));
            String currentLine = null;

            while (reader.hasNextLine()) {
                // trim newline when comparing with lineToRemove
                currentLine = reader.nextLine();
                String trimmedLine = currentLine.trim();
                String st;
                if (!trimmedLine.equals("")) {
                    String[] line = trimmedLine.split(";");
                    if (line[2].equals(rediName)) {
                        System.out.println("Date : " + line[0] + " Time : " + line[1] + " -> Redi : " + line[2] + " ->Item Ordered : " + line[3] + " -> Quantity Ordered : " + line[4] + " ->Total Bill amount :  " + line[5]);
                        x += Double.parseDouble(line[5].trim());
                    }
                }
            }
        }
         System.out.println("--------------------------------------------------------------------------------------------------------------------");
         return x;
    }

   synchronized private void printOrdersSummary() throws FileNotFoundException {
       System.out.println("--------------------------------------------------------------------------------------------------------------------");
       Scanner reader = new Scanner(new FileReader(super.getName() + "Orders.txt"));
        String currentLine = null;

        while (reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            String st;
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                System.out.println("Date : " + line[0] + " Time : " + line[1] + " -> Redi : " + line[2] + " ->Item Ordered : " + line[3] + " -> Quantity Ordered : " + line[4] + " ->Total Bill amount :  " + line[5]);
            }
        }
       System.out.println("--------------------------------------------------------------------------------------------------------------------");
   }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void orderFood() throws IOException {
        System.out.println("Enter name of redi : ");
        printRediList();
        String fileName;
        String rediName = sc.readLine().trim();
        fileName = getFilename(rediName);
        printMenu(fileName);
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.println("Enter Item Id to order : ");
        String itemid = sc.readLine();
        System.out.println("Enter quantity to order : ");
        int quantity = Integer.parseInt(sc.readLine().trim());
        String itemname = null;
        double price = 0;
        int availablequantity = 0;
        String currentLine;
        Scanner reader = new Scanner(new FileReader(fileName));
        synchronized (this) {
            while (reader.hasNextLine()) {
                // trim newline when comparing with lineToRemove
                currentLine = reader.nextLine();
                String trimmedLine = currentLine.trim();
                if (!trimmedLine.equals("")) {
                    String[] line = trimmedLine.split(";");
                    if (line[1].trim().equals(itemid.trim())) {
                        availablequantity = Integer.parseInt(line[3].trim());
                        price = Double.parseDouble(line[2].trim());
                        itemname = line[0].trim();
                        break;
                    }
                }
            }
        }
        if (availablequantity < quantity) {
            System.out.println("Item Requested Less In Quantity!");
            orderFood();
        } else {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.println("Item Ordered Successfully!");
            LocalDateTime lcl = LocalDateTime.now();
            availablequantity -= quantity;
            double expense = quantity * price;
            totalExpense += (quantity * price);
            synchronized (this) {
                PrintWriter pw = new PrintWriter(new FileWriter("Transition.txt", false));
                reader = new Scanner(new FileReader(fileName));
                currentLine = null;


                while (reader.hasNextLine()) {
                    // trim newline when comparing with lineToRemove
                    currentLine = reader.nextLine();
                    String trimmedLine = currentLine.trim();
                    String st;
                    if (!trimmedLine.equals("")) {
                        String[] line = trimmedLine.split(";");
                        if (line[1].trim().equals(itemid.trim())) {
                            st = line[0] + ';' + line[1] + ';' + line[2] + ';' + availablequantity;
                        } else {
                            st = trimmedLine;
                        }
                        pw.println(st);
                    }
                }
                pw.close();
                CpPas(fileName);

                pw = new PrintWriter(new FileWriter("Transition.txt", false));
                reader = new Scanner(new FileReader("Customer.txt"));
                currentLine = null;
                while (reader.hasNextLine()) {
                    // trim newline when comparing with lineToRemove
                    currentLine = reader.nextLine();
                    String trimmedLine = currentLine.trim();
                    if (!trimmedLine.equals("")) {
                        String[] line = trimmedLine.split(";");
                        if (line[3].trim().equals(bitsId.trim())) {
                            pw.println(line[0] + ';' + line[1] + ';' + line[2] + ';' + line[3] + ";" + line[4] + ";" + totalExpense);
                        } else pw.println(trimmedLine);
                    }
                }
                pw.close();
                CpPas("Customer.txt");

                pw = new PrintWriter(new FileWriter("Transition.txt", false));
                reader = new Scanner(new FileReader("Owner.txt"));
                currentLine = null;
                while (reader.hasNextLine()) {
                    // trim newline when comparing with lineToRemove
                    currentLine = reader.nextLine();
                    String trimmedLine = currentLine.trim();
                    if (!trimmedLine.equals("")) {
                        String[] line = trimmedLine.split(";");
                        if (line[5].trim().equals(rediName.trim())) {
                            double d = (Double.parseDouble(line[6].trim()) + expense);
                            pw.println(line[0] + ';' + line[1] + ';' + line[2] + ';' + line[3] + ";" + line[4] + ";" + line[5] + ";" + d);
                        } else pw.println(currentLine.trim());
                    }
                }
                pw.close();
                CpPas("Owner.txt");

                pw = new PrintWriter(new FileWriter(super.getName() + "Orders.txt", true));
                String dt = lcl.getDayOfMonth() + "/" + lcl.getMonthValue();
                String time = lcl.getHour() + " : " + lcl.getMinute();
                pw.println((dt + ";" + time + ";" + rediName + ";" + itemname + ";" + quantity + ";" + expense).trim());
                pw.close();

                pw = new PrintWriter(new FileWriter(rediName + "Orders.txt", true));
                pw.println((dt + ";" + time + ";" + super.getName() + ";" + itemname + ";" + quantity + ";" + expense).trim());
                pw.close();
                order();
            }
        }
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
                if (line[0].trim().equals(s.trim())) {
                    fileName = line[1].trim();
                }
            }
        }
        return fileName;
    }

   synchronized private void printRediList() throws IOException {
       System.out.println("--------------------------------------------------------------------------------------------------------------------");
        String currentLine;
        Scanner reader = new Scanner(new FileReader("rediInfo.txt"));
        currentLine = null;
        int n = 1;
        while (reader.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                System.out.println(n + " : " + line[0]);
                n++;
            }
        }
       System.out.println("--------------------------------------------------------------------------------------------------------------------");

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

   synchronized private void CpPas(String s) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(s, false));
        Scanner reader = new Scanner(new FileReader("Transition.txt"));
        String currentLine = null;
        while (reader.hasNextLine()) {
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                pw.println(trimmedLine);
            }
        }
        pw.close();
    }

}

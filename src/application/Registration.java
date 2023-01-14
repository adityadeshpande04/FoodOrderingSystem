package application;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Registration {
    public static void register() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        System.out.println("Enter (1) to Register and (2) to Login");
        int choice = Integer.parseInt(br.readLine().trim());
        if (choice == 1) {
            System.out.println("Enter (1) if you are Customer.\nEnter (2) if you are Redi-Owner.");
            choice = Integer.parseInt(br.readLine().trim());
            if (choice == 1) {
                String name = null;
                String emailId = null;
                String password = null;
                String phoneNo = null;
                String bitsId = null;
                System.out.println("Enter name : ");
                name = br.readLine();
                System.out.println("Enter emailId : ");
                emailId = br.readLine();
                System.out.println("Enter password : ");
                password = br.readLine();
                System.out.println("Enter phoneNo : ");
                phoneNo = br.readLine();
                System.out.println("Enter bitsId : ");
                bitsId = br.readLine();
                registrationCustomer(name, emailId, phoneNo, bitsId, password);
            } else {
                String name = null;
                String emailId = null;
                String password = null;
                String phoneNo = null;
                String bitsId = null;
                System.out.println("Enter name : ");
                name = br.readLine();
                System.out.println("Enter emailId : ");
                emailId = br.readLine();
                System.out.println("Enter password : ");
                password = br.readLine();
                System.out.println("Enter phoneNo : ");
                phoneNo = br.readLine();
                System.out.println("Enter bitsId : ");
                bitsId = br.readLine();
                String rediName;
                System.out.println("Enter Redi Name : ");
                rediName = br.readLine().trim();
                registrationOwner(name, emailId, phoneNo, bitsId, password, rediName);
                PrintWriter pw = new PrintWriter(new FileWriter("rediInfo.txt", true));
                pw.println(rediName + ";" + (rediName + "Menu.txt"));
                pw.close();
            }
        } else if (choice == 2) {
            System.out.println("Enter (1) if you are Customer.\nEnter (2) if you are Redi-Owner.");
            choice = Integer.parseInt(br.readLine().trim());
            if (choice == 1) {
                String bitsId = null;
                String password = null;
                System.out.println("Enter bitsId : ");
                bitsId = br.readLine();
                System.out.println("Enter password : ");
                password = br.readLine();
                loginCustomer(bitsId, password);
            } else {
                String bitsId = null;
                String password = null;
                System.out.println("Enter bitsId : ");
                bitsId = br.readLine();
                System.out.println("Enter password : ");
                password = br.readLine();
                String rediName;
                System.out.println("Enter Redi Name : ");
                rediName = br.readLine().trim();
                loginOwner(bitsId, password, rediName);
            }
        } else {
            System.out.println("Enter a valid choice!");
            register();
        }
    }

    public synchronized static void registrationOwner(String name, String emailId, String phoneNo, String bitsId, String password, String rediName) throws IOException {
        Scanner sc = new Scanner(System.in);
        if ((emailId.contains("@pilani.bits-pilani.ac.in"))) {
            PrintWriter writer = new PrintWriter(new FileWriter("Owner.txt", true));
            String x = name + ";" + emailId + ";" + phoneNo + ";" + bitsId + ";" + password + ";" + rediName + ";0";
            writer.println(x.trim());
            writer.close();
            writer = new PrintWriter(new FileWriter("User.txt", true));
            writer.println(x.trim());
            writer.close();
            Owner o = new Owner(name, emailId, phoneNo, bitsId, password, rediName);
            Thread t = new Thread(o);
            t.start();
            Thread.currentThread().interrupt();
        } else {
            System.out.println("Wrong email address");
            System.out.println("Enter a valid email address");
            String newEmail = sc.nextLine();
            registrationOwner(name, newEmail, phoneNo, bitsId, password, rediName);
        }
    }

    public synchronized static void registrationCustomer(String name, String emailId, String phoneNo, String bitsId, String password) throws IOException {
        Scanner sc = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(new FileWriter("Customer.txt", true));
        if ((emailId.contains("@pilani.bits-pilani.ac.in"))) {
            String x = name + ";" + emailId + ";" + phoneNo + ";" + bitsId + ";" + password + ";0";
            writer.println(x.trim());
            writer.close();
            writer = new PrintWriter(new FileWriter("User.txt", true));
            writer.println(x.trim());
            writer.close();
            System.out.println("Registration Successful!");
            Customer c = new Customer(name, emailId, phoneNo, bitsId, password);
            Thread t = new Thread(c);
            t.start();
            Thread.currentThread().interrupt();
        } else {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("Wrong email address");
            System.out.println("Enter a valid email address");
            String newEmail = sc.nextLine();
            registrationCustomer(name, newEmail, phoneNo, bitsId, password);
        }
    }

    public synchronized static void loginCustomer(String bitsId, String password) throws IOException {
        Scanner reader = new Scanner(new FileReader("Customer.txt"));
        String currentLine;
        int x = 0;
        double totalExp = 0;
        while (reader.hasNextLine()) {
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[3].trim().equals(bitsId.trim()) && line[4].trim().equals(password.trim())) {
                    x = 1;
                    totalExp = Double.parseDouble(line[5].trim());
                }
            }
        }
        Scanner in = new Scanner(System.in);
        if (x == 0) {
            System.out.println("Incorrect Credentials");
            System.out.println("If you are not registered yet Enter 1,to Re-enter credentials Enter 2");
            int choice = Integer.parseInt(in.nextLine().trim());
            if (choice == 1) {
                register();
            } else {
                System.out.println("Enter valid BITS ID : ");
                String id = reader.nextLine();
                System.out.println("Enter password : ");
                String pw = reader.nextLine();
                loginCustomer(id, pw);
            }
        }
        Customer c = new Customer(bitsId, password, totalExp);
        Thread t = new Thread(c);
        t.start();
        Thread.currentThread().interrupt();
    }

    public synchronized static void loginOwner(String bitsId, String password, String rediName) throws IOException {
        Scanner reader = new Scanner(new FileReader("Owner.txt"));
        String currentLine;
        int x = 0;
        double totalRevenue = 0;
        while (reader.hasNextLine()) {
            currentLine = reader.nextLine();
            String trimmedLine = currentLine.trim();
            if (!trimmedLine.equals("")) {
                String[] line = trimmedLine.split(";");
                if (line[3].trim().equals(bitsId.trim()) && line[4].trim().equals(password.trim())) {
                    x = 1;
                    totalRevenue = Double.parseDouble(line[6].trim());
                }
            }
        }
        if (x == 0) {
            System.out.println("Incorrect Credentials");
            System.out.println("If you are not registered yet Enter 1,to Re-enter credentials Enter 2");
            int choice = Integer.parseInt(reader.nextLine().trim());
            if (choice == 1) {
                register();
            } else {
                System.out.println("Enter valid BITS ID : ");
                String id = reader.nextLine().trim();
                System.out.println("Enter password : ");
                String pw = reader.nextLine().trim();
                loginOwner(id, pw, rediName);
            }
        }
        Owner o = new Owner(bitsId, password, rediName, totalRevenue);
        Thread t = new Thread(o);
        t.start();
        Thread.currentThread().interrupt();
    }

    public static void main(String args[]) throws IOException {
        register();
    }
}



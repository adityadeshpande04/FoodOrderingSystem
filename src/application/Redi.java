package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Redi {
    private String name;
    private Menu menu;
    private double totalRevenue;

    PrintWriter pw;
    File file;


    public Redi(String name) throws IOException {
        this.name = name;
        this.menu = new Menu(name);
        this.totalRevenue=0;
        this.file=new File(name+"Orders.txt");
        if (!file.createNewFile())
        {
            System.out.println("Already registered!");
        }
    }
    public Redi(String name,double totalRevenue)
    {
        this.name=name;
        this.menu=new Menu(name,"s");
        this.totalRevenue=totalRevenue;
        file=new File(name+"Orders.txt");
    }


    public String getName() {
        return name;
    }

    public Menu getMenu() {
        return menu;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}

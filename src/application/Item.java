package application;

public class Item {
    public final String name;
    private final String itemId;
    private double price;
    private int quantityLeft;

    public Item(String name, String itemId, double price, int quantityLeft) {
        this.name = name;
        this.itemId = itemId;
        this.price = price;
        this.quantityLeft = quantityLeft;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityLeft() {
        return quantityLeft;
    }

    public String getItemId() {
        return itemId;
    }

}

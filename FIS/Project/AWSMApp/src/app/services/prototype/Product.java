package app.services.prototype;

public abstract class Product {

    public int id;
    public int categoryId;
    public String categoryName;
    public String name;
    public int amount;
    public int price;
    public Boolean paid;
    public String date;
    public String image;

    public Product(
            int id,
            int categoryId,
            String categoryName,
            String name,
            int amount,
            int price,
            Boolean paid,
            String date,
            String image
    ) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.paid = paid;
        this.date = date;
        this.image = image;
    }

    public String getDeleteJSON() {
        return "{\n" +
                "   \"id\": " + id + ",\r\n" +
                "   \"name\": \"" + name + "\"\n}";
    }
}
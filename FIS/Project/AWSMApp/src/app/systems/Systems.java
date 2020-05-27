package app.systems;
import app.services.prototype.Product;


public class Systems extends Product {
    public int orders;
    public int delivers;
    public int warranty;
    public String categoryParent;
    public String components;

    public Systems(Systems systems) {
            super(systems.id,
                    systems.categoryId,
                    systems.categoryName,
                    systems.name,
                    systems.amount,
                    systems.price,
                    systems.paid,
                    systems.date,
                    systems.image);

        this.orders = systems.orders;
        this.delivers = systems.delivers;
        this.warranty = systems.warranty;
        this.categoryParent = systems.categoryParent;
        this.components = systems.components;
    }
    public Systems(
            int id,
            int categoryId,
            String categoryName,
            String name,
            int amount,
            int price,
            Boolean paid,
            String date,
            String image,
            int orders,
            int delivers,
            int warranty,
            String categoryParent,
            String components) {
        super(id, categoryId, categoryName, name, amount, price, paid, date, image);
        this.orders = orders;
        this.delivers = delivers;
        this.warranty = warranty;
        this.categoryParent = categoryParent;
        this.components = components;
    }
}

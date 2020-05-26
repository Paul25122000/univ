package app.promotions;
public class Promotion {

    public String category, name, provider, image;
    public int id, systemId, amount;
    public Promotion(int id, int systemId, String category, String name, int amount, String provider, String image) {
        this.id=id;
        this.systemId = systemId;
        this.category=category;
        this.name=name;
        this.amount=amount;
        this.provider=provider;
        this.image=image;
    }
}

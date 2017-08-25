package ke.co.mapp.mapp;

/**
 * Created by phares on 7/26/17.
 */

public class Product {
    private int id;
    private String idremote;
    private String name;
    private String price;
    private int quantity;
    private String image;
    public Product() {
    }
    public Product(int id, String idremote, String name, String price, int quantity, String image)
    {
        this.id=id;
        this.idremote=idremote;
        this.name=name;
        this.price=price;
        this.quantity=quantity;
        this.image=image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdremote(String idremote){
        this.idremote = idremote;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getId() {
        return id;
    }
    public String getIdremote(){
        return idremote;
    }
    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getImage(){
        return image;
    }
}

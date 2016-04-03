package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 3/26/2016.
 */
public class Stock implements ItemElement {
    private long id;

    private String title;
    private String author;
    private int price;
    private String category;


    public Stock(String title, String author, int price, String category) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;

    }

    public Stock() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }
    public Memento save(){
        return new Memento(this.title, this.author,this.price, this.category);
    }

    public void undoToLastSave(Object obj) {
        Memento memento = (Memento) obj;
        this.title = memento.title;
        this.author =memento.author;
        this.price = memento.price;

    }
    private class Memento {
        private String title;
        private String author;
        private int price;
        private String category;

        public Memento(String title, String author,int price, String category) {
            this.title = title;
            this.author=author;
            this.price=price;
            this.category=category;

        }
    }
}


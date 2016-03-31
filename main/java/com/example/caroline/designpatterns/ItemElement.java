package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 3/31/2016.
 */
public interface ItemElement {

    public int accept(ShoppingCartVisitor visitor);
}

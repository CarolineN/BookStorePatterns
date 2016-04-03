package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 4/3/2016.
 */
public class FileWriterCaretaker {

    private Object obj;

    public void save(Stock stock) {
        this.obj = stock.save();
    }

    public void undo(Stock stock) {
        stock.undoToLastSave(obj);
    }
}

package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 4/10/2016.
 */
public class ReviewCaretaker {

    private Object obj;

    public void save(Review review) {
        this.obj = review.save();
    }

    public void undo(Review review) {
        review.undoToLastSave(obj);
    }
}

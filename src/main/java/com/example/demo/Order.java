package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
    private final StringProperty prid;
    private final StringProperty prde;

    public Order() {
        this(null, null);
    }

    public String getPrid() {
        return prid.get();
    }

    public void setPrid(String prid) {
        this.prid.set(prid);
    }

    public StringProperty pridProperty() {
        return prid;
    }

    public String getPred() {
        return prde.get();
    }

    public void setPrde(String prde) {
        this.prde.set(prde);
    }

    public StringProperty prdeProperty() {
        return prde;
    }

    public Order(String prid, String prde) {
        this.prid = new SimpleStringProperty(prid);
        this.prde = new SimpleStringProperty(prde);
    }
}

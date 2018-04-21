package network;

import java.io.Serializable;

class testobject implements Serializable {
    int value;
    String id;

    public testobject(int v, String s) {
        this.value = v;
        this.id = s;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
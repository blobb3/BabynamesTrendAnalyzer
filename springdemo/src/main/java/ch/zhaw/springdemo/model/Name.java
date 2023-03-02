package ch.zhaw.springdemo.model;

public class Name {
    private String name;
    private String geschlecht;
    private Integer anzahl;

    // constructor
    public Name(String name, String geschlecht, Integer anzahl) {
        this.name = name;
        this.geschlecht = geschlecht;
        this.anzahl = anzahl;
    }
    

    //getter and setter methods
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getGeschlecht() {
        return geschlecht;
    }
    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }
    
    public Integer getAnzahl() {
        return anzahl;
    }
    public void setAnzahl(Integer anzahl) {
        this.anzahl = anzahl;
    }


    @Override
    public String toString() {
        return "Name [name=" + name + ", geschlecht=" + geschlecht + ", anzahl=" + anzahl + "]";
    }


}

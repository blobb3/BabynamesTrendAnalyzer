package ch.zhaw.springdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Constructor für alle Attribute
@AllArgsConstructor
//Getter- und Setter-Methoden für alle Attribute
@Getter
@Setter
// toString-Methode
@ToString

public class Name {
    private String name;
    private String geschlecht;
    private Integer anzahl;
}

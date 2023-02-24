
package ch.zhaw.springdemo.model.controller;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.opencsv.CSVReader;

import ch.zhaw.springdemo.model.Name;

@RestController
public class NameController {
    private ArrayList<Name> listOfNames;

    @GetMapping("/names")
    public ArrayList<Name> getNames() {
        return listOfNames;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws Exception {
        listOfNames = new ArrayList<>();
        Path path = Paths.get(ClassLoader.getSystemResource("data/babynames.csv").toURI());
        System.out.println("Read from: " + path);
        try (Reader reader = Files.newBufferedReader(path)) {
            // Pro gelesene Zeile instanziieren wir jeweils ein Objekt des Typs «Name» und
            // fügen diese einer ArrayList hinzu
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    // fügt einen neuen Eintrag zur listOfNames hinzu, falls das line-Array
                    // mindestens zwei Elemente enthält
                    if (line.length >= 2) {
                        listOfNames.add(new Name(line[0], line[1], Integer.parseInt(line[2])));
                    }
                }
                System.out.println("Read " + listOfNames.size() + " names");
            }
        }
    }

    // Aufgabe i + j
    @GetMapping("/names/count")
    public long getCount(@RequestParam(required = false) String sex) {
        if (sex != null) {
            if ("m".equals(sex)) {
                return listOfNames.stream()
                        .filter(name -> "m".equals(name.getGeschlecht()))
                        .count();
            } else if ("w".equals(sex)) {
                return listOfNames.stream()
                        .filter(name -> "w".equals(name.getGeschlecht()))
                        .count();
            } else {
                // Invalid sex parameter
                return 0;
            }
        } else {
            return listOfNames.size();
        }
    }

    // Aufgabe k
    @GetMapping("/names/frequency")
    public Integer getFrequency(@RequestParam String name) {
        return listOfNames.stream()
                .filter(x -> name.equals(x.getName()))
                .mapToInt(Name::getAnzahl)
                // startwert => 0, elemente sollten auf die summe der integerklasse reduziert
                // werden
                .reduce(0, Integer::sum); 
    }

    // Aufgabe l
    @GetMapping("/names/name")
    public ArrayList<String> getNamesByLengthAndStart(@RequestParam String sex, @RequestParam int length,
            @RequestParam String start) {
        return listOfNames.stream()
                .filter(name -> sex.equals(name.getGeschlecht()) && name.getName().startsWith(start)
                        && name.getName().length() == length)
                .map(Name::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
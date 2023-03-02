package ch.zhaw.springdemo.model.controller;

import ch.zhaw.springdemo.model.Name;
import com.opencsv.CSVReader;
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
    Path path = Paths.get(
      ClassLoader.getSystemResource("data/babynames.csv").toURI()
    );
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
            listOfNames.add(
              new Name(line[0], line[1], Integer.parseInt(line[2]))
            );
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
        return listOfNames
          .stream()
          .filter(name -> "m".equals(name.getGeschlecht()))
          .count();
      } else if ("w".equals(sex)) {
        return listOfNames
          .stream()
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
    return listOfNames
      .stream()
      .filter(x -> name.equals(x.getName()))
      .mapToInt(Name::getAnzahl)
      // startwert => 0, elemente sollten auf die summe der integerklasse reduziert
      // werden
      .reduce(0, Integer::sum);
  }

  // Woche 2 , Aufgabe p
  @GetMapping("/names/name")
  public ResponseEntity<List<String>> filterNames(
    @RequestParam String sex,
    @RequestParam String start,
    @RequestParam int length
  ) {
    // Überprüfen Sie den Parameter "sex" auf Gültigkeit
    if (!sex.equals("m") && !sex.equals("w")) {
      return ResponseEntity.badRequest().build();
    }

    // Überprüfen Sie den Parameter "length" auf Gültigkeit
    if (length <= 0) {
      return ResponseEntity.status(422).build();
    }

    // Filtern Sie die Namen basierend auf den Parametern
    List<String> filteredNames = listOfNames
      .stream()
      .filter(name ->
        sex.equals(name.getGeschlecht()) &&
        name.getName().startsWith(start) &&
        name.getName().length() == length
      )
      .map(Name::getName)
      .collect(Collectors.toCollection(ArrayList::new));

    // Geben Sie die Namen zurück
    return new ResponseEntity<>(filteredNames, HttpStatus.OK);
  }
}

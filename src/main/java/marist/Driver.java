package marist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author tyler
 */
public class Driver {

  public static void main(String[] args) {

    // load file containing test sentences
    String testsFileName = "input.txt";
    Scanner scanner;
    try {
      scanner = new Scanner(new File(testsFileName));
    } catch (FileNotFoundException ex) {
      System.out.println("File " + testsFileName + " not found");
      return;
    }

    // load test sentences from file
    List<String> examples = new ArrayList<>();
    while (scanner.hasNext()) {
      examples.add(scanner.nextLine());
    }

    // run tests and print the error if one exists
    examples.forEach(sentence -> {
      SentenceAnalyzer analyzer = new SentenceAnalyzer(sentence);
      if (!analyzer.isValid()) {
        System.out.println(analyzer.getErrorMessage());
      }
    });
  }
}

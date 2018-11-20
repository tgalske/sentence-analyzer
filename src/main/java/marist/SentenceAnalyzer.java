package marist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Tyler Galske - tyler.galske1@marist.edu
 */

/*
 * Determines if a sentence is valid based on the following rules
 * Starts with a capital letter
 * Has an even number of quotation marks
 * Ends with a period
 * Has no periods besides the last one
 * Numbers below 13 are spelled out (two, three, ...)
 */
public class SentenceAnalyzer {

  private final String sentence;
  private final List<String> words;
  private final int MIN_NUM_NOT_SPELLED = 13;
  private final HashMap<String, Boolean> numbersMap = new HashMap<>();
  private String errorMessage;

  // Error explanations
  private final String ERROR_STARTS_CAPITAL = "Sentence must start with a capital letter.";
  private final String ERROR_QUOTATIONS = "Sentence must have an even number of quotations.";
  private final String ERROR_ENDS_PERIOD = "Sentence must end with a period.";
  private final String ERROR_PERIOD_END_ONLY = "Periods may only be used at the end of a sentence.";
  private final String ERROR_NUMBERS_SPELLED = "Numbers below " + this.MIN_NUM_NOT_SPELLED + " must be spelled out.";

  public SentenceAnalyzer(String sentence) {
    this.sentence = sentence;
    this.words = Arrays.asList(sentence.split(" "));
  }

  /*
   * Runs one or many checks to determine sentence validity
   * If an error is found, the error message is set and no further checks are run
   */
  boolean isValid() {
    // check for empty string
    if (this.sentence.length() == 0) {
      return false;
    }
    
    if (!startsWithCapital()) {
      this.errorMessage = this.ERROR_STARTS_CAPITAL;
      return false;
    }

    if (!endsWithPeriod()) {
      this.errorMessage = this.ERROR_ENDS_PERIOD;
      return false;
    }

    if (findNumberError().isPresent()) {
      this.errorMessage = this.ERROR_NUMBERS_SPELLED;
      return false;
    }

    char quotation = '"';
    if (countCharacter(quotation) % 2 == 1) {
      this.errorMessage = this.ERROR_QUOTATIONS;
      return false;
    }

    char period = '.';
    if (countCharacter(period) > 1) {
      this.errorMessage = this.ERROR_PERIOD_END_ONLY;
      return false;
    }

    return true;
  }

  /*
   * Returns true if the first letter of the first word is capitalized
   * Determined by letter's interger representation in ASCII character set
   */
  private boolean startsWithCapital() {
    final int A = 65;
    final int Z = 90;
    char firstLetter = words.get(0).charAt(0);
    int asciiLetterRep = (int) firstLetter;
    return A <= asciiLetterRep && asciiLetterRep <= Z;
  }

  /*
   * Returns true if last character in last word is a period
   * Determined by character's integer representation in ACII character set
   */
  private boolean endsWithPeriod() {
    final String period = ".";
    return this.sentence.substring(sentence.length() - 1, sentence.length()).equals(period);
  }

  /*
   * Puts numbers (0, 1, ...) as type String into the map ("0", "1", ...)
   * Range of numbers is from 0 to MIN_NUM_NOT_SPELLED
   */
  private void initiateSpelledNumsMap() {
    IntStream stream = IntStream.range(0, this.MIN_NUM_NOT_SPELLED);
    stream.forEach(num -> {
      this.numbersMap.put(String.valueOf(num), Boolean.TRUE);
    });
  }

  /*
   * Attempts to find the first occurrence of a number below the threshold not spelled out
   */
  private Optional<String> findNumberError() {
    initiateSpelledNumsMap();
    return this.words.stream().filter(word -> numbersMap.get(word) != null).findFirst();
  }

  /*
   * Returns number of times a specified character appears in the sentence
   */
  private long countCharacter(char characterToCount) {
    return this.sentence.chars().filter(ch -> ch == characterToCount).count();
  }

  public String getErrorMessage() {
    if (this.errorMessage == null) {
      return "Sentence has not been analyzed";
    }
    StringBuilder errorBuilder = new StringBuilder();
    errorBuilder.append("Invalid sentence: ").append(this.sentence)
            .append("\n\t").append("Reason: ").append(this.errorMessage)
            .append("\n");

    return errorBuilder.toString();
  }

}

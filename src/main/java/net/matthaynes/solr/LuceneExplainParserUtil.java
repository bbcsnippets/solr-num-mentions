package net.matthaynes.solr;

import java.util.HashMap;
import java.util.regex.*;
import org.apache.solr.common.util.NamedList;

public class LuceneExplainParserUtil {

  // 34.432575 = (MATCH) fieldWeight(transcript:cabbag in 13481), product of:
  // 30.314222 = fieldWeight(transcript:"cabbag soup" in 11012), product of:
  final private static Pattern fieldWeightPattern = Pattern.compile("fieldWeight\\(([^:]+):(\\\".+\\\"|[^\\s]+)");

  // 7.0 = tf(termFreq(transcript:cabbag)=49)
  final private static Pattern termFreqPattern    = Pattern.compile("termFreq\\([^\\)]+\\)=(\\d+)");

  // 3.3166249 = tf(phraseFreq=11.0)
  final private static Pattern phraseFreqPattern  = Pattern.compile("phraseFreq=(\\d+)");

  public static NamedList<NamedList> parse(String explain) {

    NamedList<NamedList> results = new NamedList<NamedList>();

    String explainLines[] = explain.split("\\r?\\n");

    for (int i = 0; i < explainLines.length; i++) {

      // Match a line like the below, that always ~seems~ to
      // come before a tf field ...
      // fieldWeight(transcript:cabbag in 13481)
      Matcher fieldWeightMatcher = fieldWeightPattern.matcher(explainLines[i]);

      if (fieldWeightMatcher.find()) {

        // Retrieve field and value from match
        String field = fieldWeightMatcher.group(1);
        String term  = fieldWeightMatcher.group(2);

        // Make sub node in named list if non-existent
        if (results.get(field) == null) {
          results.add(field, new NamedList<Integer>());
        }

        // Get tf value from next line
        String nextLine = explainLines[i + 1];

        Matcher termFreqMatcher = termFreqPattern.matcher(nextLine);

        if (termFreqMatcher.find()) {
          results.get(field).add(term, Integer.parseInt(termFreqMatcher.group(1)));
        } else {

          // Get phraseFreq value from next line
          Matcher phraseFreqMatcher = phraseFreqPattern.matcher(nextLine);

          if (phraseFreqMatcher.find()) {
            results.get(field).add(term, Integer.parseInt(phraseFreqMatcher.group(1)));
          }

        }

        // Increment line, we parsed the next one OK so can skip it on the next iter
        i++;

      }

    }

    return results;
  }

}
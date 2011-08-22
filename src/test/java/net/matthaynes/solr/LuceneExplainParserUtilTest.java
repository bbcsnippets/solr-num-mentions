package net.matthaynes.solr;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import org.apache.solr.common.util.NamedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneExplainParserUtilTest {

  final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

  @Test
  public void testParsesSingleTerm() {
    NamedList<NamedList> results = parseExplain(
        "34.432575 = (MATCH) fieldWeight(transcript:cabbag in 13481), product of:\n"
      + "  7.0 = tf(termFreq(transcript:cabbag)=49)\n"
      + "  4.918939 = idf(docFreq=5578, maxDocs=280886)\n"
      + "  1.0 = fieldNorm(field=transcript, doc=13481)\n");

    assertEquals(49, results.get("transcript").get("cabbag"));
  }

  @Test
  public void testParsesMultipleTerms() {
    NamedList<NamedList> results = parseExplain(
      "27.019493 = (MATCH) sum of:\n" +
      "  17.902338 = (MATCH) weight(transcript:cabbag in 11012), product of:\n" +
      "    0.7588822 = queryWeight(transcript:cabbag), product of:\n" +
      "      4.918939 = idf(docFreq=5578, maxDocs=280886)\n" +
      "      0.15427762 = queryNorm\n" +
      "    23.590405 = (MATCH) fieldWeight(transcript:cabbag in 11012), product of:\n" +
      "      4.7958317 = tf(termFreq(transcript:cabbag)=23)\n" +
      "      4.918939 = idf(docFreq=5578, maxDocs=280886)\n" +
      "      1.0 = fieldNorm(field=transcript, doc=11012)\n" +
      "  9.117156 = (MATCH) weight(transcript:soup in 11012), product of:\n" +
      "    0.6512279 = queryWeight(transcript:soup), product of:\n" +
      "      4.221143 = idf(docFreq=11209, maxDocs=280886)\n" +
      "      0.15427762 = queryNorm\n" +
      "    13.999948 = (MATCH) fieldWeight(transcript:soup in 11012), product of:\n" +
      "      3.3166249 = tf(termFreq(transcript:soup)=11)\n" +
      "      4.221143 = idf(docFreq=11209, maxDocs=280886)\n" +
      "      1.0 = fieldNorm(field=transcript, doc=11012)\n");

    assertEquals(23, results.get("transcript").get("cabbag"));
    assertEquals(11, results.get("transcript").get("soup"));
  }

  @Test
  public void testParsesQuotedTerms() {
    NamedList<NamedList> results = parseExplain(
      "30.314222 = (MATCH) weight(transcript:\"cabbag soup\" in 11012), product of:\n" +
      "  1.0 = queryWeight(transcript:\"cabbag soup\"), product of:\n" +
      "    9.140081 = idf(transcript: cabbag=5578 soup=11209)\n" +
      "    0.10940822 = queryNorm\n" +
      "  30.314222 = fieldWeight(transcript:\"cabbag soup\" in 11012), product of:\n" +
      "    3.3166249 = tf(phraseFreq=11.0)\n" +
      "    9.140081 = idf(transcript: cabbag=5578 soup=11209)\n" +
      "    1.0 = fieldNorm(field=transcript, doc=11012)\n");

    assertEquals(11, results.get("transcript").get("\"cabbag soup\""));
  }

  @Test
  public void testParsesMultipleTermsAndPhrases() {
    NamedList<NamedList> results = parseExplain(
      "40.357384 = (MATCH) sum of:\n" +
      "  10.355943 = (MATCH) weight(transcript:cabbag in 11037), product of:\n" +
      "    0.43898964 = queryWeight(transcript:cabbag), product of:\n" +
      "      4.918939 = idf(docFreq=5578, maxDocs=280886)\n" +
      "      0.08924478 = queryNorm\n" +
      "    23.590405 = (MATCH) fieldWeight(transcript:cabbag in 11037), product of:\n" +
      "      4.7958317 = tf(termFreq(transcript:cabbag)=23)\n" +
      "      4.918939 = idf(docFreq=5578, maxDocs=280886)\n" +
      "      1.0 = fieldNorm(field=transcript, doc=11037)\n" +
      "  5.2739897 = (MATCH) weight(transcript:soup in 11037), product of:\n" +
      "    0.37671497 = queryWeight(transcript:soup), product of:\n" +
      "      4.221143 = idf(docFreq=11209, maxDocs=280886)\n" +
      "      0.08924478 = queryNorm\n" +
      "    13.999948 = (MATCH) fieldWeight(transcript:soup in 11037), product of:\n" +
      "      3.3166249 = tf(termFreq(transcript:soup)=11)\n" +
      "      4.221143 = idf(docFreq=11209, maxDocs=280886)\n" +
      "      1.0 = fieldNorm(field=transcript, doc=11037)\n" +
      "  24.72745 = (MATCH) weight(transcript:\"cabbag soup\" in 11037), product of:\n" +
      "    0.8157046 = queryWeight(transcript:\"cabbag soup\"), product of:\n" +
      "      9.140081 = idf(transcript: cabbag=5578 soup=11209)\n" +
      "      0.08924478 = queryNorm\n" +
      "    30.314222 = fieldWeight(transcript:\"cabbag soup\" in 11037), product of:\n" +
      "      3.3166249 = tf(phraseFreq=11.0)\n" +
      "      9.140081 = idf(transcript: cabbag=5578 soup=11209)\n" +
      "      1.0 = fieldNorm(field=transcript, doc=11037)\n");

    assertEquals(23, results.get("transcript").get("cabbag"));
    assertEquals(11, results.get("transcript").get("soup"));
    assertEquals(11, results.get("transcript").get("\"cabbag soup\""));
  }

  @Test
  public void testParsesMultipleFields() {
    NamedList<NamedList> results = parseExplain(
      "24.340008 = (MATCH) sum of:\n" +
      "  16.196447 = (MATCH) weight(transcript:cabbag in 14769), product of:\n" +
      "    0.47038153 = queryWeight(transcript:cabbag), product of:\n" +
      "      4.918939 = idf(docFreq=5578, maxDocs=280886)\n" +
      "      0.09562662 = queryNorm\n" +
      "    34.432575 = (MATCH) fieldWeight(transcript:cabbag in 14769), product of:\n" +
      "      7.0 = tf(termFreq(transcript:cabbag)=49)\n" +
      "      4.918939 = idf(docFreq=5578, maxDocs=280886)\n" +
      "      1.0 = fieldNorm(field=transcript, doc=14769)\n" +
      "  8.14356 = (MATCH) weight(description:cabbag in 14769), product of:\n" +
      "    0.88246316 = queryWeight(description:cabbag), product of:\n" +
      "      9.228216 = idf(docFreq=74, maxDocs=280886)\n" +
      "      0.09562662 = queryNorm\n" +
      "    9.228216 = (MATCH) fieldWeight(description:cabbag in 14769), product of:\n" +
      "      1.0 = tf(termFreq(description:cabbag)=1)\n" +
      "      9.228216 = idf(docFreq=74, maxDocs=280886)\n" +
      "      1.0 = fieldNorm(field=description, doc=14769)\n");

    assertEquals(49, results.get("transcript").get("cabbag"));
    assertEquals(1,  results.get("description").get("cabbag"));
  }

  // Helpers

  private NamedList<NamedList> parseExplain(String explain) {
    return LuceneExplainParserUtil.parse(explain);
  }

}
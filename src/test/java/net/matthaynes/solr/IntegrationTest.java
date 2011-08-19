package net.matthaynes.solr;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.util.AbstractSolrTestCase;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

import org.yaml.snakeyaml.*;

public class IntegrationTest extends AbstractSolrTestCase {

  final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

  private SolrServer server;

  @Override
  public String getSchemaFile() {
    return "solr/conf/schema.xml";
  }

  @Override
  public String getSolrConfigFile() {
    return "solr/conf/solrconfig.xml";
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    server = new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
    addTestDocs();
  }

  @Test
  public void testThatNoResultsAreReturned() {
    assertEquals(0L, querySolr("non-existent-string").getResults().getNumFound());
  }

  @Test
  public void testAddsNumMentionsNodeToResponse() {
    assertTrue(querySolr().getResponse().indexOf("numMentions", 0) > -1);
  }

  @Test
  public void testNumMentionsHasCorrectNumberOfDocsIn() {
    NamedList numMentions = (NamedList) querySolr().getResponse().get("numMentions");
    assertEquals(querySolr("index against").getResults().size(), numMentions.size());
  }

  // Helpers
  private QueryResponse querySolr() {
    return querySolr("text");
  }

  private QueryResponse querySolr(String query) {
    try {
      SolrQuery params = new SolrQuery(query);
      QueryResponse response = server.query(params);
      return response;
    } catch (SolrServerException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Error querying solr");
    }
  }

  private List<Map> testDocs;

  private List<Map> getTestDocs() {
    if (testDocs == null) {
      try {
        InputStream input = new FileInputStream(new File("src/test/java/net/matthaynes/solr/docs.yml"));
        Yaml yaml = new Yaml();
        testDocs = (List<Map>) yaml.load(input);
      } catch(Exception e) {
        throw new RuntimeException("Cannot load YAML file for test docs");
      }
    }
    return testDocs;
  }

  private void addTestDocs() {
    try {

      List<Map> data = getTestDocs();

      for (int i = 0; i < data.size(); i++) {
        Map obj = data.get(i);
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", i);
        doc.addField("features", obj.get("features"));
        doc.addField("includes", obj.get("includes"));
        server.add(doc);
      }

      server.commit();

    } catch(Exception e) {
      throw new RuntimeException("Error uploading docs to solr");
    }
  }

}
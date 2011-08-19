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
  public void testThatNoResultsAreReturned() throws SolrServerException {
    SolrParams params = new SolrQuery("that is not found");
    QueryResponse response = server.query(params);
    assertEquals(0L, response.getResults().getNumFound());
  }

  @Test
  public void testAddsNumeMentionsToResponse() throws SolrServerException {
    SolrQuery params = new SolrQuery("text");
    // params.setShowDebugInfo(true);
    QueryResponse response = server.query(params);
    logger.info("****************************************************************");
    logger.info("****************************************************************");
    logger.info("****************************************************************");
    logger.info("****************************************************************");
    logger.info(response.getResponse().toString());
    logger.info("****************************************************************");
    logger.info("****************************************************************");
    logger.info("****************************************************************");
    logger.info("****************************************************************");
    assertTrue(response.getResponse().indexOf("numMentions", 0) > -1);
  }

  // Helpers
  private List<Map> loadTestDocs() {
    try {
      InputStream input = new FileInputStream(new File("src/test/java/net/matthaynes/solr/docs.yml"));
      Yaml yaml = new Yaml();
      return (List<Map>) yaml.load(input);
    } catch(Exception e) {
      throw new RuntimeException("Cannot load YAML file for test docs");
    }
  }

  private void addTestDocs() {
    try {

      List<Map> data = loadTestDocs();

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
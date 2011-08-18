package net.matthaynes.solr;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.util.AbstractSolrTestCase;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

public class IntegrationTest extends AbstractSolrTestCase {

  final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

  private SolrServer server;

  @Override
  public String getSchemaFile() {
    return "/Users/matth/work/solr_num_mentions/solr/conf/schema.xml";
  }

  @Override
  public String getSolrConfigFile() {
    return "/Users/matth/work/solr_num_mentions/solr/conf/solrconfig.xml";
  }

  @Before
  @Override
  public void setUp() throws Exception {
    logger.info("testA being run...");
    super.setUp();
    server = new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
  }

  @Test
  public void testThatNoResultsAreReturned() throws SolrServerException {
    SolrParams params = new SolrQuery("text that is not found");
    QueryResponse response = server.query(params);
    assertEquals(0L, response.getResults().getNumFound());
  }

}
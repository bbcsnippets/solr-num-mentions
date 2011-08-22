package net.matthaynes.solr;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.apache.solr.util.plugin.*;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.util.SolrPluginUtils;
import org.apache.lucene.search.Explanation;

public class NumMentionsSearchComponent extends SearchComponent {

  @Override
  public void prepare(ResponseBuilder rb) throws IOException {

  }

  @Override
  public void process(ResponseBuilder rb) throws IOException {

    NamedList<Explanation> complexExplanations = SolrPluginUtils.getExplanations(rb.getQuery(), rb.getResults().docList,
      rb.req.getSearcher(), rb.req.getSchema());
    NamedList<NamedList> numMentions = new NamedList<NamedList>();

    for (int i = 0; i < complexExplanations.size(); i++) {
      numMentions.add(complexExplanations.getName(i), LuceneExplainParserUtil.parse(complexExplanations.getVal(i).toString()));
    }

    rb.rsp.add("numMentions", numMentions);
  }

  /////////////////////////////////////////////
  ///  SolrInfoMBean
  ////////////////////////////////////////////

  @Override
  public String getDescription() {
    return "Number of mentions";
  }

  @Override
  public String getVersion() {
    return "$Version: TODO $";
  }

  @Override
  public String getSourceId() {
    return "$Id: TODO $";
  }

  @Override
  public String getSource() {
    return "$URL: TODO $";
  }

  @Override
  public URL[] getDocs() {
    return null;
  }

}

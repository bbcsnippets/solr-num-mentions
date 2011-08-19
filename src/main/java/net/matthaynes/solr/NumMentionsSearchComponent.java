package net.matthaynes.solr;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.apache.solr.util.plugin.*;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.common.util.NamedList;

public class NumMentionsSearchComponent extends SearchComponent {

  @Override
  public void prepare(ResponseBuilder rb) throws IOException {

  }

  @Override
  public void process(ResponseBuilder rb) throws IOException {
    NamedList numMentions = new NamedList();
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

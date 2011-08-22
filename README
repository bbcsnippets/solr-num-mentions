# solr-num-mentions

This plugin adds the number of mentions for each search term to the solr response.

## Build + Install

    mvn compile

The copy the jar in dist/ to you solr lib directory, and add this to your solrconfig.xml

    <searchComponent name="numMentions" class="net.matthaynes.solr.NumMentionsSearchComponent" />

And this in your request handler

    <arr name="last-components">
      <str>numMentions</str>
    </arr>











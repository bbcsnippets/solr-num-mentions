require 'fileutils'

namespace :example do

  namespace :solr

    desc "Run example solr server"
    task :run do
      FileUtils.cd('solr') do
        sh "java -Dsolr.solr.home=./solr -jar start.jar"
      end
    end

  end

end




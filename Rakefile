require 'fileutils'

namespace :example do

  namespace :solr

    desc "Run example solr server"
    task :run do
      FileUtils.cd('solr') do
        sh "java -Dsolr.solr.home=./solr -jar start.jar"
      end
    end

    desc "Populate example solr server"
    task :populate do

    end

    desc "Drop example solr database"
    task :drop do

    end

  end

end




#!/usr/bin/env ruby

[ 'open-uri', 'nokogiri', 'ruby-debug', 'enumerator',
  'ap', # => # awesome_print
  'lib/string'].each(&method(:require))
# pp - prettyprint
# puts array.inspect

$doc = nil

class Application
  attr_accessor :line, :BASE_URL, :url

  BASE_URL = 'http://www.terangdunia.net/'
  @@line   = 1
  @@url    = Array[ 'injil-tuhan-yesus' ]

  def run
    fetch_urls_then_doc
#    @@url.each { |text| puts text }
    generate
  end
  
  def generate
    debug = case
    when @@line == 0
      puts "generating TOC..."
      generate_toc
    when @@line == 1
      puts "generating Chapter 1..."
      generate_ch1
    else
      puts "generating Chapter #{@@line}..."
      generate_ch
      return
    end
  end

  def generate_ch
    verseIdx = 2 # manually insert chapter:verse reference

    $doc.xpath("//table[@class='contentpaneopen']/tr[3]/td/p[position() > 1 and contains(@style, 'text-align: justify') and not(child::em)]").each_with_index do |node, index|
      text = node.text.gsub(/[^[:alnum:]]/, '')
      unless text.empty? or text.nil?
        myarray = node.text.scan(/\d{0,2}\D+/)
#        ap myarray
        myarray.each { |elem|
          elem = elem.gsub(/(^[0-9]{1,2})/, '\1 ')
          puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(4, #{verseIdx}, '#{elem}');"
          verseIdx += 1
        }
      end
    end
  end

  def generate_ch1
    lines = []
    $doc.xpath("//table[@class='contentpaneopen']/tr[3]/td/p[position() > 1 and contains(@style, 'text-align: justify')]").each_with_index do |node, index|
      text = node.text.gsub(/[^[:alnum:]]/, '')
      unless text.empty? or text.nil?
        lines <<= "INSERT INTO book ('chapter', 'verse', 'content') VALUES(1, #{index+1},'#{node.text}');\n"
        p "#{lines.last}\n"
        # debug in hex
#        node.text.each_byte { |c| puts "0x%x" % c }
      end
    end

    # write out to file
    filename = "ch1.sql"
    File.open(filename, 'w') { |f| f.write(lines) }
  end

  def generate_toc
    $doc.xpath("//tr[@class='sectiontableentry1' or @class='sectiontableentry2']/td[2]/a").each do |node, index|
      ns = String.new(node.text.strip!)
      print "INSERT INTO toc ('id', 'title') VALUES('#{index}','#{ns.titlecase}');\n"
    end
  end

  def fetch_urls_then_doc
    $doc = Nokogiri::HTML(open(BASE_URL + @@url[0]), nil, 'UTF-8')
    $doc.xpath("//tr[@class='sectiontableentry1' or @class='sectiontableentry2']/td[2]/a").each { |node| @@url <<= node["href"] }
=begin
  puts doc.errors
  print "=======\n"
  print doc
  print "=======\n"
=end
    $doc = Nokogiri::HTML(open(BASE_URL + @@url[@@line]), nil, 'UTF-8')
  end
end

if __FILE__ == $PROGRAM_NAME
  Application.new.run()
end

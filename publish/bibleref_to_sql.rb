#!/opt/local/bin/ruby1.9

require "scanf"
require "net/http"
require "pathname"
require "uri"
require "pp"
require "nokogiri"
require "awesome_print"
require "json"

REFERENCE     = "Kisah Para Rasul 9:1-6"
ENCLOSING_TAG = "" # valid: nil/b/i for none/bold/italic respectively
ITJ_CHAPTER   = 59
$START_NO     = 1
TOC_URL       = "http://bibledbdata.org/onlinebibles/indonesian_tb/index.htm"
DEBUG         = false

class Application
  def run()
    # split up list of references (if multiple), to parse each one in turn
    references = REFERENCE.split(',')
    references.each { |item| parse(item.strip, item != references.last) }
  end

  # break REFERENCE down to sub-components
  def parse(ref, trailingNL)
    # first extra book
    theBook, ref = ref.scan(/^(Kejadian|Keluaran|Imamat|Bilangan|Ulangan|Yosua|Hakim-Hakim|Rut|1 Samuel|2 Samuel|1 Raja-Raja|2 Raja-Raja|1 Tawarikh|2 Tawarikh|Ezra|Nehemia|Ester|Ayub|Mazmur|Amsal|Pengkhotbah|Kidung Agung|Yesaya|Yeremia|Ratapan|Yehezkiel|Daniel|Hosea|Yoel|Amos|Obaja|Yunus|Mikha|Nahum|Habakuk|Zefanya|Hagai|Zakharia|Maleakhi|Matius|Markus|Lukas|Yohanes|Kisah Para Rasul|Roma|1 Korintus|2 Korintus|Galatia|Efesus|Filipi|Kolose|1 Tesalonika|2 Tesalonika|1 Timotius|2 Timotius|Titus|Filemon|Ibrani|Yakobus|1 Petrus|2 Petrus|1 Yohanes|2 Yohanes|I2 Yohanes|Yudas|Wahyu)\s(.*)/i).flatten

    # format across chapters
    start_chapter, start_verse, end_chapter, end_verse \
      = ref.scan(/(\d+):(\d+)-(\d+):(\d+)/).flatten

    unless start_chapter.nil?
      if Integer(start_chapter) > Integer(end_chapter)
        raise "Error in chapter range"
      end

      # loop for each chapter
      chapter = Integer(start_chapter)
      begin
        runOnce(true, true, theBook, chapter.to_s, start_verse, getEndChapter(theBook, chapter))
        chapter += 1
      end while chapter < Integer(end_chapter)
      runOnce(true, trailingNL, theBook, end_chapter, 1, end_verse)

      return
    end

    if start_chapter.nil?
      # format range within a chapter
      chapter, start_verse, end_verse = ref.scan(/(\d+):(\d+)-(\d+)/).flatten

      if end_verse.nil?
        # format single verse
        chapter, start_verse = ref.scan(/(\d+):(\d+)/).flatten
        end_verse = start_verse
      elsif Integer(start_verse) > Integer(end_verse)
        raise "Error in verse range"
      end
    end

    runOnce(true, trailingNL, theBook, chapter, start_verse, end_verse)
  end

  def runOnce(header, footer, theBook, chapter, start_verse, end_verse)
    # 1st HTTP request
    url = URI.parse(TOC_URL)
    req = Net::HTTP::Get.new(url.path)
    res = Net::HTTP.start(url.host, url.port) { |http| http.request(req) }

    # DEBUG
    if DEBUG
      books = res.body.gsub("\n", "").scan(/<br><br>([^\[]+)\s+?\[/).flatten
      ap [ books.index(theBook.upcase), theBook ]
    end

    # parse TOC for book
    book  = res.body.gsub("\n", "").scan(/<br><br>#{theBook.upcase}\s+?\[<[^b]*br><br>/)[0]
    doc   = Nokogiri::HTML::fragment(book)
    elem  = doc.xpath(".//a").select { |item| item.text == "#{chapter}" }

    # 2nd HTTP request +parse
    url = File.join(Pathname.new(TOC_URL).dirname, elem[0]["href"])

    verses = fetch(url).gsub("\n", "").scan(/<blockquote>.*<\/blockquote>/)[0].scan(/#{chapter}:[^<]+<br>/).unshift("")[Integer(start_verse)..Integer(end_verse)]
    
    # generate SQL
    start_no = $START_NO
    
    if false #header
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '(#{theBook} #{chapter}:#{start_verse}-#{end_verse})');"
      start_no += 1
    end

    start_tag = (ENCLOSING_TAG.empty? && "") || "<#{ENCLOSING_TAG}>"
    end_tag = (ENCLOSING_TAG.empty? && "") || "</#{ENCLOSING_TAG}>"

    # content
    verses.each do |line|
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '#{start_tag}#{line[(line.index(':')+1)..-1].gsub("<br>","")}#{end_tag}');"
      start_no += 1
    end

    if false #footer
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '');"
      start_no += 1
    end
    
    $START_NO = start_no
  end

  def getEndChapter(theBook, chapter)
    # 1st HTTP request
    url = URI.parse(TOC_URL)
    req = Net::HTTP::Get.new(url.path)
    res = Net::HTTP.start(url.host, url.port) { |http| http.request(req) }

    # parse TOC for book
    book  = res.body.gsub("\n", "").scan(/<br><br>#{theBook.upcase}\s+?\[<[^b]*br><br>/)[0]
    doc   = Nokogiri::HTML::fragment(book)
    elem  = doc.xpath(".//a").select { |item| item.text == "#{chapter}" }

    # 2nd HTTP request
    url = File.join(Pathname.new(TOC_URL).dirname, elem[0]["href"])
    verses = fetch(url).gsub("\n", "").scan(/<blockquote>.*<\/blockquote>/)[0].scan(/#{chapter}:[^<]+<br>/).unshift("")
    last_verse = verses[-1].scan(/#{chapter}:[0-9]+/)[0].strip.split(':')[1]
    
    return Integer(last_verse)
  end

  def fetch(uri_str, limit = 10)
    # You should choose better exception.
    raise ArgumentError, 'HTTP redirect too deep' if limit == 0
    response = Net::HTTP.get_response(URI.parse(uri_str))
    case response
    when Net::HTTPSuccess     then response
    when Net::HTTPRedirection then fetch(response['location'], limit - 1)
    else
      response.error!
    end
    return response.body
  end
end
  
if __FILE__ == $PROGRAM_NAME
  app = Application.new
  app.run()
end

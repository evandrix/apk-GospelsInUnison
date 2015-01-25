#!/usr/bin/env ruby

class String
  def starts_with?(characters)
    return self.match(/^#{characters}/)
  end
    
  def titlecase
    downcase.split.map {|w| capitalization_exceptions.include?(w) ? w : w.capitalize}.join(" ").upfirst
  end

  def upfirst
    self[0,1].capitalize + self[1,length-1]
  end

  private
    def capitalization_exceptions
      ['of','a','the','and','an','or','nor','but','if','then','else','when','up','at','from','by','on','off','for','in','out','over','to']
    end	
end

#encoding: utf-8
require 'singleton'
module ModeloQytetet
  class Dado
    include Singleton
    def tirar
      return (1 + rand(6))
    end
  end
end

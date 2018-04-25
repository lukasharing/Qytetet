#encoding: utf-8
require_relative "TipoSorpresa"
module ModeloQytetet
  class Sorpresa
    attr_reader :texto, :tipo, :valor;
    def initialize(_texto, _valor, _tipo)
      @texto  = _texto;
      @valor  = _valor;
      @tipo   = _tipo;
    end
    
    def to_s
      "Existe una sorpresa de tipo #{@tipo} con un efecto de #{@valor}";
    end
  end
end
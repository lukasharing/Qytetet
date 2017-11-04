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
      "Texto: #{@texto} \n Valor: #{@valor} \n Tipo: #{@tipo}"
    end
    
    private 
    attr_writer :texto, :tipo, :valor;
  end
end
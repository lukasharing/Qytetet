module ModeloQytetet
  class Casilla
    attr_accessor:titulo, :numHoteles, :numCasas, :numeroCasilla, :coste, :tipo
    def self.new1(_tipo, _numero)
      obj = new
      obj.numeroCasilla  = _numero;
      obj.numHoteles     = 0;
      obj.numCasas       = 0;
      obj.tipo           = _tipo;
      obj.titulo         = nil;
      obj.coste          = 0;
      obj
    end
    
    def self.new2(_titulo, _numero, _coste)
      obj = new
      obj.numeroCasilla  = _numero;
      obj.numHoteles     = 0;
      obj.numCasas       = 0;
      obj.tipo           = TipoCasilla::CALLE;
      obj.titulo         = _titulo;
      obj.coste          = _coste;
      obj
    end
    
    def getprecioedificar
      @titulo.precioedificar
    end
    
    def soyedificable
      @tipo == TipoCasilla::CALLE
    end
    def estahipotecada
      titulo.hipotecada
    end
    def tengopropietario
      titulo.tengopropietario;
    end
    def asignarpropietario(_propietario)
        titulo.propietario = _propietario;
        return titulo;
    end
    
    #def calcularValorHipoteca() end
    #def cancelarHipoteca() end
    #def cobrarAlquiler() end
    #def edificarCasa() end
    #def edificarHotel() end
    #def estaHipotecada() end
    #def hipotecar() end
    #def precioTotalComprar() end
    #def propietarioEncarcelado() end
    #def sePuedeEdificarCasa() end
    #def sePuedeEdificarHotel() end
    #def venderTitulo() end
    #def asignarTituloPropiedad() end
    
    def to_s
      "Casilla{numeroCasilla=#{@numeroCasilla}, coste=#{@coste}, numHoteles=#{@numHoteles}, numCasas=#{@numCasas}, tipo=#{@tipo}, titulo=#{@titulo}";
    end
  end
end

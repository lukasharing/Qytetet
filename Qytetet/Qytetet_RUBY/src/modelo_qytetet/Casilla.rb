#encoding: utf-8
module ModeloQytetet
  class Casilla
    attr_accessor:titulo
    attr_reader :tipo, :numero_casilla
    def initialize(coste, numero_casilla, num_casas, num_hoteles, titulo, tipo)
      @coste = coste
      @numero_casilla = numero_casilla
      @num_casas = num_casas
      @num_hoteles = num_hoteles
      @titulo = titulo
      @tipo = tipo
      @propietario = nil
    end
    def self.newEspecial(_tipo, _numero)
      return new(0, _numero, 0, 0, nil, _tipo)
    end
    
    def self.newCalle(_titulo, _numero, _coste)
      return new(_coste, _numero, 0, 0,  _titulo, TipoCasilla::CALLE)
    end
    
    def get_precio_edificar
      return @titulo.precioedificar
    end
    
    def soy_edificable
      return (@tipo == TipoCasilla::CALLE)
    end
    def esta_hipotecada
      return @titulo.hipotecada
    end
    def tengo_propietario
      return @titulo.tengo_propietario
    end
    def asignar_propietario(_propietario)
        titulo.propietario = _propietario
        return titulo
    end
    
    def calcular_valor_hipoteca
      hipoteca_base = @titulo.hipoteca_base
      cantidad_recibida = hipoteca_base + (1 + (@num_casas * 0.5 + @num_hoteles).to_i)
      return cantidad_recibida
    end
    def cancelar_hipoteca
      # Debemos implementarlo, no se como se cancela una hipoteca en la vida real.
    end
    def cobrar_alquiler
      coste_alquiler_base = @titulo.alquiler_base
      coste_alquiler = coste_alquiler_base + (@num_casas * 0.5 + 2 * @num_hoteles).to_i
      return coste_alquiler
    end
    def edificar_casa
      @num_casas = @num_casas + 1
      coste_edificar_casa = precio_edificar
      return coste_edificar_casa
    end
    def edificar_hotel
      @num_hoteles = @num_hoteles + 1
      coste_edificar_hoteles = precio_edificar
      return coste_edificar_hoteles
    end
    def hipotecar
      @titulo.hipotecada = true
      cantidad_recibida = calcularValorHipoteca
      return cantidad_recibida
    end
    
    def precio_total_comprar
      # No se especifica NADA
      return 0
    end
    def propietario_encarcelado
      return @titulo.propietario_encarcelado
    end
    def se_puede_edificar_casa()
      return (@num_casas)
    end
    def se_puede_edificar_hotel()
      return (@num_hoteles < 4)
    end
    def vender_titulo
      @num_casas = 0
      @num_hoteles = 0
      @titulo = nil
      precio_compra = @coste + precioEdificar() * (@num_casas + @num_hoteles)
      precio_venta = precio_compra * (1 + @titulo.factor_revalorizacion()).to_i()
      return precio_venta
    end
    def asignarTituloPropiedad
      # No se especifica NADA
    end
    
    def to_s
      resultado = "La casilla (#{@numero_casilla}) ";
      if @tipo == TipoCasilla::CALLE
        resultado += "#{@titulo.nombre} y valorada en #{@coste} tiene #{@num_casas} casas y #{@num_hoteles} hoteles."
      else
        resultado += "es de tipo #{@tipo}";
      end
      return resultado
    end
    private :titulo=
    private_class_method :new
  end
end

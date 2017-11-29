module ModeloQytetet
  class TituloPropiedad
    attr_reader :nombre
    def initialize(nombre, alquiler_base, factor_revalorizacion, hipoteca_base, precio_edificar)
      @nombre = nombre;
      @hipotecada = false;
      @alquiler_base = alquiler_base;
      @factor_revalorizacion = factor_revalorizacion;
      @hipoteca_base  = hipoteca_base;
      @precio_edificar = precio_edificar;
      @propietario = nil
    end

    def tengo_propietario
      return (@propietario != nil)
    end
    def cobrar_alquiler(coste_alquiler)
      @propietario.modificar_saldo(-coste_alquiler)
    end
    def propietario_encarcelado
      return @propietario.encarcelado
    end
    def to_s
      "TituloPropiedad{nombre=#{@nombre}, hipotecada=#{@hipotecada}, alquilerBase=#{@alquiler_base}, factorRevalorizacion=#{@factor_revalorizacion}, hipotecaBase=#{@hipoteca_base}, precioEdificar=#{@precio_edificar}}";
    end
  end
end
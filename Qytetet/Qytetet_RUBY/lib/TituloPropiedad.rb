module ModeloQytetet
  class TituloPropiedad
    attr_reader :casilla, :propietario, :nombre, :hipotecada, :alquilerBase, :factorRevalorizacion, 
                :hipotecaBase, :precioEdificar
    def initialize(_nombre, _alquilerBase, _factorRevalorizacion, _hipotecaBase, _precioEdificar)
      @nombre               = _nombre;
      @hipotecada           = false;
      @alquilerBase         = _alquilerBase;
      @factorRevalorizacion = _factorRevalorizacion;
      @hipotecaBase         = _hipotecaBase;
      @precioEdificar       = _precioEdificar;
    end

    def to_s
      "TituloPropiedad{nombre=#{@nombre}, hipotecada=#{@hipotecada}, alquilerBase=#{@alquilerBase}, factorRevalorizacion=#{@factorRevalorizacion}, hipotecaBase=#{@hipotecaBase}, precioEdificar=#{@precioEdificar}}";
    end
    
    private
    attr_writer:propietario, :nombre, :hipotecada, :alquilerBase, :factorRevalorizacion
  end
end
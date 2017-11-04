require_relative "Sorpresa"
require_relative "Casilla"
module ModeloQytetet
  class Jugador
    attr_writer:saldo, :nombre, :encarcelado, :cartaLibertad, :casillaActual, :propiedades
    def initialize(nombre)
      @nombre = nombre
      @saldo = 0
      @encarcelado = false
      @propiedades = Array.new
      @casillaActual = nil
      @cartaLibertad = nil
    end
    
    def tengopropiedades
      (@propiedades.size() > 0)
    end
    
    def actualizarposicion(casilla) 
        unless casilla.numeroCasilla() == @casillaActual.numeroCasilla
            @casillaActual = casilla 
            return true
        else
            return false
        end
    end
    
    def comprartitulo
      (!casillaActual.tengoPropietario)
    end
    
    def devolvercartalibertad
        carta = @cartaLibertad
        @cartaLibertad = nil
        carta
    end
    
    def iracarcel(casilla)
        @casillaActual = casilla
        @encarcelado = true
    end
    
    def modificarsaldo(cantidad)
      @saldo += cantidad
    end
    
    
    def obtenercapital
      valorpropiedades = 0
      @propiedades.each do|e|
          edificios = e.casilla.numCasas + e.casilla.numCasas
          valorpropiedades += e.casilla.coste + edificios * e.precioEdificar
          if e.hipotecada
              valorpropiedades -= e.hipotecaBase;
          end
      end
      (@saldo + valorpropiedades)
    end
    
    def obtenerpropiedadeshipotecadas(hipotecada)
        @propiedades.select{ |e| e.hipotecada == hipotecada };
    end
    
    # def pagarCobrarPorCasaYHotel(int cantidad)end
    # def pagarLibertad(int cantidad)end
    # def puedoEdificarCasa(Casilla casilla)end
    # def puedoEdificarHotel(Casilla casilla) end
    # def puedoHipotecar(Casilla casilla) end
    # def puedoPagarHipoteca(Casilla casilla)end
    
    def puedovenderpropiedad(casilla)
      esdemipropiedad(casilla)
    end
    
    def tengocartalibertad
      @cartaLibertad != nil
    end
    #def venderPropiedad(casilla)end
    
    def cuantascasashotelestengo
        total = 0;
        @propiedades.each{ |e| total += e.casilla.numHoteles + e.casilla.numHoteles }
        total
    end
    
    def eliminardemispropiedades(casilla)
      mias = @propiedades.select{ |e| e.nombre == _casilla.tituloPropiedad.nombre }
      if mias.size > 0
        @propiedades.delete_at(@propiedades.index(mias[0]))
      end
    end
    
    def esdemipropiedad(_casilla)
      mias = @propiedades.select{ |e| e.nombre == _casilla.tituloPropiedad.nombre }
      mias.size > 0
    end
        
    def tengosaldo(_cantidad)
      @saldo >= _cantidad
    end
    
    def to_s
      "El jugador #{@nombre} con saldo #{@saldo} #{@encarcelado ? '' : "no "} se encuentra encarcelado";
    end
    
    private :tengosaldo
    attr_writer:saldo, :nombre, :encarcelado, :cartaLibertad, :casillaActual, :propiedades
  end
end
    
    

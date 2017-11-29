#encoding: utf-8
require_relative "Sorpresa"
require_relative "Casilla"
module ModeloQytetet
  class Jugador
    attr_accessor:casilla_actual, :encarcelado
    attr_reader :nombre
    def initialize(nombre)
      @nombre = nombre
      @saldo = 0
      @encarcelado = false
      @propiedades = Array.new
      @casilla_actual = nil
      @carta_libertad = nil
    end
    
    def tengo_propiedades
      return (@propiedades.size() > 0)
    end

    def tengo_carta_libertad
      return @carta_libertad != nil
    end
    
    def actualizar_posicion(casilla)
      tiene_propietario = true
      if casilla.numero_casilla < casilla_actual.numero_casilla
        modificar_saldo(ModeloQytetet::Qytetet.instance.SALDO_SALIDA)
      end
      @casilla_actual = casilla
      if casilla.soy_edificable
        tengo_propietario = casilla.tengo_propietario
        tiene_propietario = tengo_propietario
        if tengo_propietario
          propietario_encarcelado = casilla.propietario_encarcelado
          if !propietario_encarcelado
            coste_alquiler = casilla.cobrar_alquiler
            modificar_saldo(coste_alquiler)
          end
        end
      elsif casilla.tipo == ModeloQytetet::TipoCasilla::IMPUESTO
        coste = casilla.coste
        modificar_saldo(coste)
      end
      return tiene_propietario
    end
    
    def esta_bancarrota
      return (@saldo <= 0)
    end
    
    def comprar_titulo
      puedo_comprar = false
      if @casilla_actual.soy_edificable
        tengo_propietario = @casilla_actual.tengo_propietario
        if tengo_propietario
          coste_compra = @casilla_actual.coste
          if coste_compra <= @saldo
            titulo = @casilla_actual.asignar_propietario(self)
            @propiedades << titulo
            puedo_comprar = true
            modificarsaldo(coste_compra)
          end
        end
      end
      return puedo_comprar
    end
    
    def devolver_carta_libertad
        carta = @carta_libertad
        @carta_libertad = nil
        return carta
    end
    
    def ir_a_carcel(casilla)
        @casilla_actual = casilla
        @encarcelado = true
    end
    
    def modificar_saldo(cantidad)
      @saldo += cantidad
    end
    def obtener_capital
      valor_propiedades = 0
      @propiedades.each do|e|
          edificios = e.casilla.num_casas + e.casilla.num_hoteles
          valor_propiedades += e.casilla.coste + edificios * e.precio_edificar
          if e.hipotecada
            valor_propiedades -= e.hipoteca_base;
          end
      end
      return (@saldo + valor_propiedades)
    end
    
    def obtener_propiedades_hipotecadas(hipotecada)
        @propiedades.select{ |e| e.hipotecada == hipotecada };
    end
    
    def pagar_cobrar_por_casa_y_hotel(cantidad)
      numero_total = cuantas_casas_hoteles_tengo
      modificar_saldo(-numero_total * cantidad)
    end
    def pagar_libertad(cantidad)
      tengo_saldo = tengo_saldo(cantidad)
      if tengo_saldo
        modificar_saldo(-cantidad)
      end
      return tengo_saldo
    end
    def puedo_edificar_casa(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      if es_mia
        coste_edificar_casa = casilla.get_precio_edificar
        tengo_saldo = tengo_saldo(coste_edificar_casa)
        return tengo_saldo
      end
      return es_mia
    end
    def puedo_edificar_hotel(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      if es_mia
        coste_edificar_hotel = casilla.get_precio_edificar
        tengo_saldo = tengo_saldo(coste_edificar_hotel)
        return tengo_saldo
      end
      return es_mia
    end
    def puedo_hipotecar(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      return es_mia
    end
    def puedo_pagar_hipoteca(casilla)
      # No se especifica NADA;
    end
    def puedo_vender_propiedad(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      hipotecada = casilla.esta_hipotecada
      puedo_vender = es_mia && !hipotecada
      return puedo_vender
    end
    
    def tengocarta_libertad
      return (@cartaLibertad != nil)
    end
    def venderPropiedad(casilla)
      precio_venta = casilla.vender_titulo
      modificar_saldo(precio_venta)
    end
    def cuantas_casas_hoteles_tengo
        total = 0;
        @propiedades.each{ |e| total += e.casilla.numHoteles + e.casilla.numHoteles }
        total
    end
    
    def eliminar_de_mis_propiedades(casilla)
      mias = @propiedades.select{ |e| e.nombre == _casilla.tituloPropiedad.nombre }
      if mias.size > 0
        @propiedades.delete_at(@propiedades.index(mias[0]))
      end
    end
    
    def es_de_mi_propiedad(_casilla)
      return @propiedades.include?(casilla)
    end
        
    def tengo_saldo(_cantidad)
      return (@saldo >= _cantidad)
    end
    
    def to_s
      "El jugador #{@nombre} se encuentra en la casilla #{@casilla_actual.numero_casilla} con saldo #{@saldo}, este #{@encarcelado ? "" : "no "} se encuentra encarcelado"
    end
    private :cuantas_casas_hoteles_tengo, :eliminar_de_mis_propiedades, :es_de_mi_propiedad, :tengo_saldo
  end
end 

#encoding: utf-8
require_relative "Dado"
require_relative "Tablero"
require_relative "TipoSorpresa"
require_relative "Sorpresa"
require_relative "Jugador"
require_relative "MetodoSalirCarcel"
require 'singleton'
module ModeloQytetet
  class Qytetet
    include Singleton
    attr_reader :jugador_actual, :casilla_actual
    @@MAX_JUGADORES = 4;
    @@MAX_CARTAS = 10;
    @@MAX_CASILLAS = 20;
    @@PRECIO_LIBERTAD = 200;
    @@SALDO_SALIDA = 1000;
    def inicializar_juego(nombres)
      inicializar_jugadores(nombres)
      inicializar_tablero
      inicializar_cartas_sorpresas
      salida_jugadores
    end

    def inicializar_cartas_sorpresas
      @mazo = Array.new
      @mazo << Sorpresa.new("Has ganado la lotería!", +100, TipoSorpresa::PAGARCOBRAR)
      @mazo << Sorpresa.new("Has perdido tu cartera en la calle...", -100, TipoSorpresa::PAGARCOBRAR)

      # Añadimos las cartas de IRACASILLA (x3)
      @mazo << Sorpresa.new("Te vas a Paseo del prado", 18, TipoSorpresa::IRACASILLA)
      @mazo << Sorpresa.new("Te trasladan a la salida.", 0, TipoSorpresa::IRACASILLA)
      @mazo << Sorpresa.new("¡A la carcel!", 0, TipoSorpresa::IRACASILLA)
      #tablero.getCarcel().getNumeroCasilla()
      # Añadimos las cartas de PORCASAHOTEL (x2)
      @mazo << Sorpresa.new("Debes cobrar por tus propiedades", 10, TipoSorpresa::PORCASAHOTEL)
      @mazo << Sorpresa.new("Debes pagar por tus propiedades", -10, TipoSorpresa::PORCASAHOTEL)

      # Añadimos las cartas de PORJUGADOR (x2)
      @mazo << Sorpresa.new("Recibes de los demás jugadores", 20, TipoSorpresa::PORJUGADOR)
      @mazo << Sorpresa.new("Debes darles a los demás jugadores", -5, TipoSorpresa::PORJUGADOR)

      # Añadimos las cartas de SALIRCARCEL (x1)
      @mazo << Sorpresa.new("Hola Mundo", 10, TipoSorpresa::SALIRCARCEL);
      
      @mazo.shuffle
      @carta_actual = @mazo[0]
    end

    def inicializar_jugadores(nombres)
      @jugadores = Array.new
      nombres.each{ |e| @jugadores << Jugador.new(e) }
    end
    
    def inicializar_tablero
      @tablero = Tablero.new;
    end
    
    def siguiente_jugador
      @jugador_actual = @jugadores[(@jugadores.index(@jugador_actual) + 1) % @jugadores.size];
    end
    
    def salida_jugadores
      @jugadores.each do |e|
          e.casilla_actual = @tablero.obtener_casilla_numero(0);
          e.encarcelado = true;
          e.modificar_saldo(@@SALDO_SALIDA);
      end
      @jugadores.shuffle;
      @jugador_actual = @jugadores[0];
    end
    
    def obtener_ranking
      ranking = Array.new
      @jugadores.each{ |j|
        ranking << [j.nombre, j.obtener_capital()]
      }
      return ranking
    end
    
    def comprar_titulo_propiedad
      puedo_comprar = @jugador_actual.comprar_titulo
      return puedo_comprar
    end
    
    def encarcelar_jugador
      if @jugador_actual.tengo_carta_libertad
        carta = @jugador_actual.devolver_carta_libertad
        @mazo << carta
      else
        casilla_carcel = @tablero.carcel
        @jugador_actual.ir_a_carcel(casilla_carcel)
      end
    end
    
    def jugar
      valor_dado = ModeloQytetet::Dado.instance.tirar
      casilla_posicion = @jugador_actual.casilla_actual
      nueva_casilla = @tablero.obtener_nueva_casilla(casilla_posicion, valor_dado)
      tiene_propietario = @jugador_actual.actualizar_posicion(nueva_casilla)
      if !nueva_casilla.soy_edificable
        case nueva_casilla.tipo 
          when TipoCasilla::JUEZ
            encarcelar_jugador()
          when TipoCasilla::SORPRESA
            @carta_actual = @mazo[0]
        end
      end
      return tiene_propietario
    end
    
    def buscar_valores_positivo
      return @mazo.select {|e| e.valor > 0 }
    end

    def buscar_ir_a_casillas
      return @mazo.select {|e| e.tipo == TipoSorpresa::IRACASILLA }
    end

    def buscar_casillas(casilla)
      return @mazo.select {|e| e.tipo == casilla }
    end
    
    def intentar_salir_carcel(metodo)
      libre = false
      case metodo
        when MetodoSalirCarcel::TIRANDODADO
          valor_dado = ModeloQytetet::Dado.instance.tirar
          libre = valor_dado > 5
        when MetodoSalirCarcel::PAGANDOLIBERTAD
          tengo_saldo = @jugador_actual.pagar_libertad(@@PRECIO_LIBERTAD)
          libre = tengo_saldo
      end
      if libre
        @jugador_actual.encarcelado = false
      end
      return libre
    end
    
    def aplicar_sorpresa()
     tiene_propietario = true
     case @carta_actual
       when PAGARCOBRAR
         @jugador_actual.modificar_saldo(@carta_actual.valor);
       when IRACASILLA
         es_carcel = @tablero.es_casilla_carcel(@carta_actual.valor)
         if es_carcel
           encarcelar_jugador
         else
           nueva_casilla = @tablero.obtener_casilla_numero(@carta_actual.getValor())
           @jugador_actual.actualizar_posicion(nueva_casilla)
           if nueva_casilla.tipo == TipoCasilla::CALLE
            tiene_propietario = nueva_casilla.esta_hipotecada
           end
         end
       when PORCASAHOTEL
         @jugador_actual.pagar_cobrar_por_casa_y_hotel(@carta_actual.valor)
       when PORJUGADOR
         @jugadores.each{ |jugador|
           if ! jugador.eql?(@jugador_actual)
             @jugador_actual.modificar_saldo(@carta_actual.valor);
             @jugador.modificar_saldo(-@carta_actual.valor);
           end
         }
     end
     return tiene_propietario
    end
    
    def vender_propiedad(casilla)
      puedo_vender = false
      if casilla.soy_edificable
        puedo_vender = @jugador_actual.puedo_vender_propiedad(casilla)
        if puedo_vender
          @jugador_actual.vender_propiedad(casilla)
        end
      end
      return puedo_vender
    end
    
    def cancelar_hipoteca(casilla)
      # No se lo que se hace en la vida real para cancelar una hipoteca
      return false
    end
    
    def hipotecar_propiedad(casilla)
      puedo_edificar = false
      if casilla.soy_edificable
        se_puede_hipotecar = !casilla.esta_hipotecada
        if se_puede_hipotecar
          puedo_edificar = @jugador_actual.puedo_hipotecar(casilla)
          if puedo_edificar
            cantidad_recibida = casilla.hipotecar
            @jugador_actual.modificar_saldo(cantidad_recibida)
          end
        end
      end
      
      return puedo_edificar
    end
    
    def edificar_casa(casilla)
      puedo_edificar = false
       if casilla.soy_edificable
         se_puede_edificar = casilla.se_puede_edificar_casa
         if se_puede_edificar
           puedo_edificar = @jugador_actual.puedo_edificar_casa(casilla)
           if puedo edificar
             coste_edificar = casilla.edificar_casa()
             @jugador_actual.modificar_saldo(-coste_edificar)
           end
         end
       end
      return puedo_edificar
    end
    
    def edificar_hotel(casilla)
      puedo_edificar = false
       if casilla.soy_edificable
         se_puede_edificar = casilla.se_puede_edificar_hotel
         if se_puede_edificar
           puedo_edificar = @jugador_actual.puedo_edificar_hotel(casilla)
           if puedo edificar
             coste_edificar = casilla.edificar_hotel()
             @jugador_actual.modificar_saldo(-coste_edificar)
           end
         end
       end
      return puedo_edificar
    end
    
    def to_s
      info = "";

      info += "Cartas sorpresa: \n";
      @mazo.each{|e| info += e.to_s + "\n" }

      info += "Las casillas son las siguientess: \n" + @tablero.to_s

      info += "Los participantes son: \n";
      @jugadores.each{|e| info += e.to_s + "\n" }
      info;
    end
  end
end

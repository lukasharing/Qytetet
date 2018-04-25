#encoding: utf-8
require_relative '../modelo_qytetet/Qytetet'
require_relative 'VistaTextualQytetet'
module InterfazTextualQytetet
  class ControladorQytetet
    def initialize
      @juego = nil
      @jugador = nil
      @casilla = nil
      @vista = VistaTextualQytetet.new
    end
    
    def desarrollo_juego
      puede_jugar = true;
      finalizado = false;
      @vista.mostrar("------------------------------------")
      rs = "Es el turno de #{@jugador.nombre}, actualmente se encuentra en la casilla #{@casilla.numero_casilla}"
      if @casilla.tipo == ModeloQytetet::TipoCasilla::CALLE
        rs += " con nombre #{@casilla.titulo.nombre}"
      else
        rs += " de tipo #{@casilla.tipo}"
      end
      @vista.mostrar(rs)
      
      if @jugador.encarcelado
        valor = @vista.menu_salir_carcel
        metodo = nil
        if valor == 0
          metodo = ModeloQytetet::MetodoSalirCarcel::TIRANDODADO
        else
          metodo = ModeloQytetet::MetodoSalirCarcel::PAGANDOLIBERTAD
        end
        @juego.intentar_salir_carcel(metodo)
        if @jugador.encarcelado
          @vista.mostrar("Desgraciadamente el jugador sigue en la carcel.")
          puede_jugar = false
        else
          @vista.mostrar("Afortunadamente consiguio salir sin muchos problemas.");
        end
      end
      
      if puede_jugar
        no_tiene_propietario = @juego.jugar
        desplazamiento = @jugador.casilla_actual
        ra = "Tras lanzar el dado, el jugador se desplazo hacia "
        if desplazamiento.tipo == ModeloQytetet::TipoCasilla::CALLE
          ra += desplazamiento.titulo.nombre
        else
          ra += " la casilla de tipo #{desplazamiento.tipo}"
        end
        @vista.mostrar(ra)
        if !@jugador.encarcelado
          if !no_tiene_propietario
            comprar = @vista.elegir_quiero_comprar
            if comprar
              compra = @juego.comprar_titulo_propiedad
              if compra
                @vista.mostrar("El usuario compró #{desplazamiento.titulo.nombre}");
              else 
                @vista.mostrar("No se pudo realizar la compra.");
              end       
            end
            @vista.mostrar(@jugador.to_s)
            if @jugador.esta_bancarrota
              finalizado = true
            elsif @jugador.tengo_propiedades
              opcion = @vista.menu_gestion_inmobiliaria
              while opcion != 0 do
                posible = true
                case opcion
                  when 1
                    posible = @juego.edificar_casa(casilla)
                  when 2
                    posible = @juego.edificar_hotel(casilla)
                  when 3
                    posible = @juego.vender_propiedad(casilla)
                  when 4
                    posible = @juego.hipotecar_propiedad(casilla)
                  when 5
                    posible = @juego.cancelar_hipoteca(casilla)
                end
                if !posible
                  @vista.mostrar("No se pudo realizar la operacion");
                end
                opcion = @vista.menu_gestion_inmobiliaria
              end
            end
          end
        else
          @vista.mostrar(@jugador.to_s)
        end
      end
      
      if !finalizado 
        @juego.siguiente_jugador
        @jugador = @juego.jugador_actual
        @casilla = @jugador.casilla_actual
        if @jugador.esta_bancarrota
          finalizado = true
        else
          desarrollo_juego
        end
      end
      
      if finalizado
        @vista.mostrar("El juego ha acabado, un jugador esta en bancarrota.")
        ranking = @juego.obtener_ranking
        ranking.each{ |p|
          @vista.mostrar("#{p[0]} con un capital de #{p[1]}")
        }
      end
    end
    
    def elegir_propiedad(propiedades)
      nombres = Array.new
      propiedades.each{ |p| nombres << p.nombre }
      opcion = @vista.menu_elegir_propiedad(nombre)
      return propiedades[opcion]
    end
    
    def inicializacion_juego
      @juego = ModeloQytetet::Qytetet.instance
      nombres = @vista.obtener_nombre_jugadores
      @juego.inicializar_juego(nombres)
      @jugador = @juego.jugador_actual
      @casilla = @jugador.casilla_actual
      @vista.mostrar(@juego.to_s)
      desarrollo_juego
    end
    
    private :elegir_propiedad
  end
end
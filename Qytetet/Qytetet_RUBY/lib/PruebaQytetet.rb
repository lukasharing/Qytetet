#encoding: utf-8
require_relative "Tablero"
require_relative "TipoSorpresa"
require_relative "Sorpresa"
require_relative "Jugador"
module ModeloQytetet
  class PruebaQytetet
    
    @@MAX_JUGADORES = 4;
    @@MAX_CARTAS = 10;
    @@MAX_CASILLAS = 20;
    @@PRECIO_LIBERTAD = 200;
    @@SALDO_SALIDA = 1000;
    
    attr_reader :mazo, :tablero, :jugadores, :jugadorActual, :cartaActual
    def initialize
      nombres = ["Lukas", "Ana", "Alberto", "María"]
      inicializartablero
      inicializarcartassorpresas
      inicializarjugadores(nombres)
      salidajugadores
    end

    def inicializarcartassorpresas
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
    end

    def inicializarjugadores(nombres)
      @jugadores = Array.new
      nombres.each{ |e| @jugadores << Jugador.new(e) }
    end
    
    def inicializartablero
      @tablero = Tablero.new;
    end
    
    def siguientejugador
        @jugadorActual = @jugadores[(jugadores.index(@jugadorActual) + 1) % @jugadores.size];
    end
    
    def salidajugadores()
        @jugadores.each do |e|
            e.casillaActual = @tablero.obtenercasillanumero(0);
            e.encarcelado = false;
            e.modificarsaldo(7500);
        end
        @jugadores.shuffle;
        @jugadorActual = jugadores[0];
    end
    
    def buscarvalorespositivo
      return @mazo.select {|e| e.valor > 0 }
    end

    def buscariracasillas
      return @mazo.select {|e| e.tipo == TipoSorpresa::IRACASILLA }
    end

    def buscarcasillas(casilla)
      return @mazo.select {|e| e.tipo == casilla }
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
    
    private
    attr_writer :mazo, :tablero, :jugadores, :jugadorActual, :cartaActual
  end
  
  juego = PruebaQytetet.new
  puts juego.to_s
end

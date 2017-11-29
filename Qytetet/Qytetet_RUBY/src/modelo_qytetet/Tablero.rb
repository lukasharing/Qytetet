#encoding: utf-8
require_relative "TipoCasilla"
require_relative "TituloPropiedad"
require_relative "Casilla"
module ModeloQytetet
  class Tablero
    attr_reader:carcel
    def initialize
      inicializar
    end
    
    def inicializar
      @casillas = Array.new
      @casillas << Casilla.newEspecial(TipoCasilla::SALIDA, 0)
      casilla0 = TituloPropiedad.new("Av. del pan duro", 50, 10, 150, 250)
      @casillas << Casilla.newCalle(casilla0, 1, 100) 
      casilla1 = TituloPropiedad.new("Calle de la magdalena", 54, 10.8, 220, 290)
      @casillas << Casilla.newCalle(casilla1, 2, 150)
      casilla2 = TituloPropiedad.new("Calle de pizza con piña", 58, 11.6, 290, 330)
      @casillas << Casilla.newCalle(casilla2, 3, 200)
      @casillas << Casilla.newEspecial(TipoCasilla::SORPRESA, 4)
      @casillas << Casilla.newEspecial(TipoCasilla::JUEZ, 5)
      casilla3 = TituloPropiedad.new("Glorieta dulce de leche", 62, 12.4, 360, 370)
      @casillas << Casilla.newCalle(casilla3, 6, 250) 
      casilla4 = TituloPropiedad.new("Calle de la Coca-Cola", 66, 13.2, 430, 410)
      @casillas << Casilla.newCalle(casilla4, 7, 300)
      @casillas << Casilla.newEspecial(TipoCasilla::PARKING, 8)
      casilla5 = TituloPropiedad.new("Calle gran postre", 70, 14, 500, 450)
      @casillas << Casilla.newCalle(casilla5, 9, 350)
      @casillas << (@carcel = Casilla.newEspecial(TipoCasilla::CARCEL, 10))
      @casillas << Casilla.newEspecial(TipoCasilla::SORPRESA, 11)
      casilla6 = TituloPropiedad.new("Calle tiramisu", 74, 14.8, 570, 490)
      @casillas << Casilla.newCalle(casilla6, 12, 400) 
      casilla7 = TituloPropiedad.new("Glorieta del helado", 78, 15.6, 640, 530)
      @casillas << Casilla.newCalle(casilla7, 13, 450) 
      casilla8 = TituloPropiedad.new("Av. Tosta Rica", 82, 16.4, 710, 570)
      @casillas << Casilla.newCalle(casilla8, 14, 500) 
      @casillas << Casilla.newEspecial(TipoCasilla::IMPUESTO, 15)
      @casillas << Casilla.newEspecial(TipoCasilla::SORPRESA, 16)
      casilla9 = TituloPropiedad.new("Calle Te Paquistani", 86, 17.2, 780, 610)
      @casillas << Casilla.newCalle(casilla9, 17, 550) 
      casilla10 = TituloPropiedad.new("Calle galletas María", 90, 18, 850, 650)
      @casillas << Casilla.newCalle(casilla10, 18, 600)
      casilla11 = TituloPropiedad.new("Av. del postre caro", 94, 18.8, 920, 690)
      @casillas << Casilla.newCalle(casilla11, 19, 650)
    end
    
    def es_casilla_carcel(numero_casilla)
      return @casillas[numero_casilla].tipo == ModeloQytetet::TipoCasilla::CARCEL
    end
    def obtener_casilla_numero(numero_casilla)
      return @casillas[numero_casilla];
    end
    def obtener_nueva_casilla(casilla, desplazamiento)
      return @casillas[(casilla.numero_casilla + desplazamiento) % @casillas.size];
    end
    def to_s
        string_casillas = ""
        @casillas.map { |e| string_casillas += e.to_s + "\n" }
        string_casillas
    end
  end
end
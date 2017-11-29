#encoding: utf-8
module InterfazTextualQytetet
  class VistaTextualQytetet
    def comprobar_opcion(captura, min, max)
      valido = true
      begin
        opcion = Integer(captura)
        if opcion < min || opcion > max
          valido = false
          mostrar('el numero debe estar entre min y max')
        end
      rescue Exception => e
        valido = false
        mostrar('debes introducir un numero')
      end 
      valido
    end
    
    def menu_elegir_propiedad(lista_propiedades)
      menuGI = Array.new
      lista_propiedades.each{ |p, i| menuGI << [i, p] }
      salida = seleccion_menu(menuGI);
      return salida  
    end
    
    def menu_gestion_inmobiliaria
      mostrar( 'Elige la gestion inmobiliaria que deseas hacer')
      menuGI = [[0, 'Siguiente Jugador'],
                [1, 'Edificar casa'],
                [2, 'Edificar Hotel'],
                [3, 'Vender propiedad'],
                [4, 'Hipotecar Propiedad'],
                [5, 'Cancelar Hipoteca']];
      salida = seleccion_menu(menuGI);
      mostrar('has elegido')
      mostrar(salida)
      return salida
    end
    
    def elegir_quiero_comprar
      mostrar('El jugador actual puede comprar la calle actual:')
      menuGI = [[0, 'No comprar calle'],
                [1, 'Comprar calle']];
      salida = seleccion_menu(menuGI);
      return (salida == 0 ? false : true)
    end
    
    def menu_salir_carcel
      mostrar( 'Elige el metodo para salir de la carcel')
      menuSC=[[0, 'Tirando el dado'], [1, 'Pagando mi libertad']]
      salida=seleccion_menu(menuSC)
      mostrar( 'has elegido')
      mostrar(salida)
      salida
    end
    
    def obtener_nombre_jugadores
      nombres = Array.new
      valido = true; 
      begin
        self.mostrar("Escribe el numero de jugadores: (de 2 a 4):")
        lectura = gets.chomp
        valido = comprobar_opcion(lectura, 2, 4)
      end while(!valido)
      
      for i in 1..Integer(lectura)
        mostrar('Jugador:  '+ i.to_s)
        nombre = gets.chomp
        nombres << nombre
      end
      nombres
    end
    
    def mostrar(texto)
      puts texto
    end
    
    def seleccion_menu(menu)
      valido = true
      begin
        for m in menu
          mostrar( "#{m[0]}" + " : " + "#{m[1]}")
        end
        mostrar( "Elige un numero de opcion: ")
        captura = gets.chomp
        valido = comprobar_opcion(captura, 0, menu.length-1);
        if ! valido
          mostrar( "\n\n ERROR !!! \n\n Seleccion erronea. Intentalo de nuevo.\n\n")
        end
      end while (!valido)
      Integer(captura)  
    end
    
    private :comprobar_opcion, :seleccion_menu
  end
end
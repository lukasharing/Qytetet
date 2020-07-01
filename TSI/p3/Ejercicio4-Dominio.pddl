(define (domain Ejercicio4-Dominio)

    (:types
        Unidad
        Edificio
        Localizacion
        Recurso
        
        TipoUnidad
        TipoEdificio
        TipoRecurso
    )

    (:constants
        VCE Marine Segador                 - TipoUnidad
        CentroDeMando Barracon Extractor   - TipoEdificio
        Mineral Gas                        - TipoRecurso
    )

    (:predicates
        ; Localizacion
        (EnE ?e - Edificio ?l - Localizacion)
        (EnU ?u - Unidad   ?l - Localizacion)
        
        ; Construccion Unidad
        (Requiere ?tu - TipoUnidad ?tr - TipoRecurso)
        
        ; Arco
        (Camino ?l1 - Localizacion ?l2 - Localizacion)
        
        ; Asignación
        (Asignar ?r - Recurso ?l - Localizacion)
        (Extrayendo ?u - Unidad)
        (Necesita ?e - TipoEdificio ?r - TipoRecurso)
        
        ; Estado de un Edificio
        (Construido ?e - Edificio)
        
        (HayRecurso ?r - TipoRecurso)
        
        ; Unidad "Construida"
        (Reclutada ?u - Unidad)
        
        ; Es de Tipo.
        (EsTipoU ?u - Unidad   ?tu - TipoUnidad)
        (EsTipoE ?e - Edificio ?te - TipoEdificio)
        (EsTipoR ?r - Recurso  ?tr - TipoRecurso)
    )

    (:action Navegar
        :parameters (?u - Unidad ?l1 ?l2 - Localizacion)
        :precondition (and
            ; La unidad debe estar reclutada
            (Reclutada ?u)
            ; Buscamos una unidad no ocupada
            (not (Extrayendo ?u))
            ; Miramos la posicion de la unidad
            (EnU ?u ?l1)
            ; Comprobamos que haya un arco entre la unidad y la posicion solicitada
            (Camino ?l1 ?l2)
        )
        :effect (and
            ; Desplazamos la unidad
            (not (EnU ?u ?l1))
            (EnU ?u ?l2)
        )
    )
    
    (:action Asignar
        :parameters (?u - Unidad ?r - Recurso ?tr - TipoRecurso ?l - Localizacion)
        :precondition (and
            ; La unidad debe estar reclutada
            (Reclutada ?u)
            ; Comprobamos que la unidad es VCE
            (EsTipoU ?u VCE)
            ; Unidad no está en estado
            (not (Extrayendo ?u))
            ; Comprobamos la localizacion del recurso y su tipo
            (Asignar ?r ?l)
            
            ; Añadimos Reglas Para Gas, Para extraer necesitamos en la posicion un extractor.
            (EsTipoR ?r ?tr) ; Devolvemos el tipo, ya que ?tr no tiene por que ser el correspondiente a t.
            ; Gas => Existe un Extractor Construido en la misma localización
            (imply
                (EsTipoR ?r Gas)
                (exists (?e - Edificio)
                    (and 
                        (EsTipoE ?e Extractor)
                        (EnE ?e ?l)
                        (Construido ?e)
                    )
                )
            )
            ; Comprobamos que la unidad esté en la localización del recurso
            (EnU ?u ?l)
            
        )
        :effect (and
            ; Unidad en estado extrayendo y añadimos el recurso extraido
            (Extrayendo ?u)
            (HayRecurso ?tr)
        )
    )
    
    (:action Construir
        :parameters (?e - Edificio ?te - TipoEdificio ?u - Unidad ?l - Localizacion)
        :precondition (and
            ; Unidad debe estar en la Localización, disponible y reclutada
            (Reclutada ?u)
            (EnU ?u ?l)
            (not (Extrayendo ?u))
            ; Está obligado a que solo los VCE pueden contruir? No se indica, pero se supone
            (EsTipoU ?u VCE)
            
            ; La unidad está sobre queremos contruir el edificio
            (EnE ?e ?l)
            (not (Construido ?e))
            
            ; La localizacion no está ocupada (No existe un edificio contruido sobre este)
            (not (exists (?e2 - Edificio) 
                    (and
                        (Construido ?e2)
                        (EnE ?e2 ?l)
                    )
                )
            )
            
            ; Vemos que elemento es necesario para contruir el Edificio y que lo tenemos.
            (EsTipoE ?e ?te)
            
            ; Comprobamos que tenemos todos los recursos necesarios para construir
            (forall (?tr - TipoRecurso)
                ; Si necesita recurso entonces tiene que haberlo (A -> B)
                (imply (Necesita ?te ?tr) (HayRecurso ?tr))
            )
        )
        :effect (and
            ; Asignamos El Edificio en la Localización
            (Construido ?e)
        )
    )
    
    (:action Reclutar
        :parameters (?e - Edificio ?l - Localizacion ?u - Unidad ?tu - TipoUnidad)
        :precondition (and
            (or
                ; Miramos Si es Centro de Mando, puede generar VCE
                (and
                    (EsTipoE ?e CentroDeMando)
                    (EsTipoU ?u VCE)
                )
                ; Miramos Si es Barracon, Puede Generar Marine o Segador
                (and
                    (EsTipoE ?e Barracon)
                    (or
                        (EsTipoU ?u Marine)
                        (EsTipoU ?u Segador)
                    )
                )
            )
            
            ; Recopilamos la posicion del lugar donde se va a generar
            (EnE ?e ?l)
            ; Recopilamos en ?tu el tipo de ?tu, que lo usaremos para los recursos necesarios
            (EsTipoU ?u ?tu)
            ; Comprobamos que la unidad no ha sido reclutada
            (not (Reclutada ?u))
            
            ; Comprobamos que tenemos todos los recursos necesarios para construir dicha unidad
            (forall (?tr - TipoRecurso)
                ; Si necesita recurso entonces tiene que haberlo (A -> B == no(A) o B)
                (imply (Requiere ?tu ?tr) (HayRecurso ?tr))
            )
        )
        :effect (and
            ; Creamos Unidad En La localización del edificio
            (Reclutada ?u)
            (EnU ?u ?l)
        )
    )
)
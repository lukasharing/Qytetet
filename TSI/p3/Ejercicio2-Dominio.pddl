(define (domain Ejercicio2-Dominio)
    (:requirements :adl)

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
        VCE                                - TipoUnidad
        CentroDeMando Barracon Extractor   - TipoEdificio
        Mineral Gas                        - TipoRecurso
    )

    (:predicates
        ; Localizacion
        (EnE ?e - Edificio ?l - Localizacion)
        (EnU ?u - Unidad   ?l - Localizacion)
        
        ; Arco
        (Camino ?l1 - Localizacion ?l2 - Localizacion)
        
        ; Asignación
        (Asignar ?r - Recurso ?l - Localizacion)
        (Extrayendo ?u - Unidad)
        (Necesita ?e - TipoEdificio ?r - TipoRecurso)
        
        ; Estado de un Edificio
        (Construido ?e - Edificio)
        
        (HayRecurso ?r - TipoRecurso)
        
        ; Es de Tipo.
        (EsTipoU ?u - Unidad   ?tu - TipoUnidad)
        (EsTipoE ?e - Edificio ?te - TipoEdificio)
        (EsTipoR ?r - Recurso  ?tr - TipoRecurso)
    )

    (:action Navegar
        :parameters (?u - Unidad ?l1 ?l2 - Localizacion)
        :precondition (and
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
            ; Comprobamos que la unidad es VCE
            (EsTipoU ?u VCE)
            ; Unidad no está en estado
            (not (Extrayendo ?u))
            ; Comprobamos la localizacion del recurso y su tipo
            (Asignar ?r ?l)
            ; Comprobamos que la unidad esté en la localización del recurso
            (EnU ?u ?l)
            
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
        )
        :effect (and
            ; Unidad en estado extrayendo y añadimos el recurso extraido
            (Extrayendo ?u)
            (HayRecurso ?tr)
        )
    )
    
    (:action Construir
        :parameters (?e - Edificio ?te - TipoEdificio ?u - Unidad ?l - Localizacion ?tr - TipoRecurso)
        :precondition (and
            ; Unidad debe estar en la Localización y disponible
            (EnU ?u ?l)
            (not (Extrayendo ?u))
            
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
            (Necesita ?te ?tr)
            (HayRecurso ?tr)
        )
        :effect (and
            ; Asignamos El Edificio en la Localización
            (Construido ?e)
        )
    )
)
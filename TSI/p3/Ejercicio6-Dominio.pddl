(define (domain Ejercicio6-Dominio)

    (:types
        Unidad
        Edificio
        Localizacion
        Recurso
        Investigacion
        
        TipoUnidad
        TipoEdificio
        TipoRecurso
        TipoInvestigacion
    )

    (:constants
        VCE Marine Segador                                         - TipoUnidad
        CentroDeMando Barracon Extractor Bahia_Ingenieria Deposito - TipoEdificio
        Mineral Gas                                                - TipoRecurso
        Impulsor_Segador                                           - TipoInvestigacion
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
        
        ; Estado de un Edificio
        (Construido ?e - Edificio)
        
        ; Unidad "Construida"
        (Reclutada ?u - Unidad)
        
        ; Investigacion
        (Investigado ?i - Investigacion)
        
        ; Es de Tipo.
        (EsTipoU ?u - Unidad   ?tu - TipoUnidad)
        (EsTipoE ?e - Edificio ?te - TipoEdificio)
        (EsTipoR ?r - Recurso  ?tr - TipoRecurso)
        (EsTipoI ?i - Investigacion  ?ti - TipoInvestigacion)
    )

    ; Ahora los recursos son funciones numéricas
    (:functions
        ; Construccion Unidad
        (Requiere ?tu - TipoUnidad ?tr - TipoRecurso)
        
        ; Edificio
        (Necesita ?e - TipoEdificio ?r - TipoRecurso)
        
        ; Investigación
        (InvestigacionRequiere ?ti - TipoInvestigacion ?tr - TipoRecurso)
    
        ; Rercursos
        (MaximoRecurso)
        (HayRecurso ?r - TipoRecurso)
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
            (increase (HayRecurso ?tr) 20)
            ; Si nos pasamos del máximo lo "clampeamos"
            (when (> (HayRecurso ?tr) (MaximoRecurso)) (assign (HayRecurso ?tr) (MaximoRecurso)))
        )
    )
    
    (:action Desasignar
        :parameters (?u - Unidad)
        :precondition (and
            ; Comprobamos que la unidad esté ocupada
            (Extrayendo ?u)
        )
        :effect (and
            (not (Extrayendo ?u))
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
            (forall (?tr - TipoRecurso) (>= (HayRecurso ?tr) (Necesita ?te ?tr)))
        )
        :effect (and
            ; Asignamos El Edificio en la Localización
            (Construido ?e)
            
            ; Decrementamos los recursos que tenemos
            (forall (?tr - TipoRecurso) (decrease (HayRecurso ?tr) (Necesita ?te ?tr)))
            
            ; Depósito
            (when (EsTipoE ?e Deposito) (increase (MaximoRecurso) 100))
        )
    )
    
    (:action Reclutar
        :parameters (?e - Edificio ?l - Localizacion ?u - Unidad ?tu - TipoUnidad ?i - Investigacion)
        :precondition (and
            (or
                ; Miramos Si es Centro de Mando, puede generar VCE
                (and
                    (EsTipoE ?e CentroDeMando)
                    (EsTipoU ?u VCE)
                )
                ; Miramos Si es Barracon, Puede Generar Marine
                (and
                    (EsTipoE ?e Barracon)
                    (EsTipoU ?u Marine)
                )
                ; o Puede Generar Segador pero necesita un Impulsor de Segador
                (and
                    (EsTipoI ?i Impulsor_Segador)
                    (Investigado ?i)
                    (EsTipoE ?e Barracon)
                    (EsTipoU ?u Segador)
                )
            )
            
            ; Recopilamos la posicion del lugar donde se va a generar
            (EnE ?e ?l)
            ; Recopilamos en ?tu el tipo de ?tu, que lo usaremos para los recursos necesarios
            (EsTipoU ?u ?tu)
            ; Comprobamos que la unidad no ha sido reclutada
            (not (Reclutada ?u))
            
            ; Comprobamos que tenemos todos los recursos necesarios para construir dicha unidad
            (forall (?tr - TipoRecurso) (>= (HayRecurso ?tr) (Requiere ?tu ?tr)))
        )
        :effect (and
        
            ; Decrementamos los recursos que tenemos
            (forall (?tr - TipoRecurso) (decrease (HayRecurso ?tr) (Requiere ?tu ?tr)))
            ; Creamos Unidad En La localización del edificio
            (Reclutada ?u)
            (EnU ?u ?l)
        )
    )
    
    (:action Investigar
        :parameters (?e - Edificio ?i - Investigacion ?ti - TipoInvestigacion)
        :precondition (and
            ; No está investigada
            (not (Investigado ?i))
            
            ; Existe una bahía de ingeniería y está contruida
            (EsTipoE ?e Bahia_Ingenieria)
            (Construido ?e)
            
            ; Obtenemos el tipo de Investigacion requerida
            (EsTipoI ?i ?ti)
            
            ; Comprobamos que tenemos todos los recursos necesarios para construir dicha investigacion
            (forall (?tr - TipoRecurso) (>= (HayRecurso ?tr) (InvestigacionRequiere ?ti ?tr)))
        )
        
        :effect(and
            ; Decrementamos los recursos que tenemos
            (forall (?tr - TipoRecurso) (decrease (HayRecurso ?tr) (InvestigacionRequiere ?ti ?tr)))
            
            (Investigado ?i)
        )
    )
)
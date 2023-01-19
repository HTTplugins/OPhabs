Donde tienes que añadir una fruta cada vez que añades una nueva:
Primero tienes que hacerla y definir tanto una habilidad como un cast identification y un fruitIdentification 
    Ophabs.java: 
        Añadir un objeto habilidad de la fruta que hayas hecho en sección abilitieSystem
        Añadir el objeto creado al final del metodo fruitAssociation en la sección FruitSystem (Posiblemente añada un vector porq habria que ir modificando el constructor cada vez)
    
    FruitSystem:
        fruitAssociation:
            Constructor:
                Añadir al metodo del constructor la habilidad en donde haga falta ( varias veces )
            onPlayerItemConsume: Añadir en varios sitios
        loseFruit:
            en el onPlayerDeath

    commands:
        oph: en donde aparecen tambin todas las habilidades, hay q ponerlo 2 veces
    
    scoreboard: Dos veces, una en string y otra mas adelante
Si veis que falta algo mas añadidlo, se puede optimizar mas para quitar algunos pasos pero lo dejamos para el siguiente refactor


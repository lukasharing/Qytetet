
const leones_item = new Item(
    "Leones del Patio",
    `¿Sabías que no hay explicación para el patio de los leones?
    <br/> <br/> Una de las joyas del conjunto arquitectónico es el patio de los leones que ha terminado en febrero de ser restaurado. La fuente situada en su centro no tiene un significado claro. Los doce leones que la componen podrían tener un simbolismo astrológico, siendo cada león un signo zodiacal, los extremos del patio parecen estar ordenados en función de los 4 puntos cardinales y existe una inscripción que hace referencia al templo de Salomon. Además, parece claro que la fuente es un símbolo del poder del sultán dado a las referencias a la fuente de la vida y los 4 ríos del paraíso coránico. En resumen, todo son incógnitas para los estudiosos.`,
    "./assets/item/leones.jpg"
);

leones_item.world(100, 50);

const arrayanes_item = new Item(
    "Patio de Arrayanes",
    "El Patio de los Arrayanes se ha llamado de diversas formas a lo largo del tiempo. La actual denominación (al igual que la de Patio de los Mirtos) se debe a los macizos de arrayanes (o mirtos) cuyo color verde vivo contrasta con el piso de mármol blanco del patio, y que rodean al estanque central. También se le llamó Patio del Estanque o de la Alberca, precisamente por este estanque, de 34 metros por 7,10 metros, que divide el patio longitudinalmente y se abastece de agua gracias a dos pilas de mármol situadas en cada extremo. A ambos lados del patio se encuentran dos naves de aposentos y, en los lados menores, se levantan unos pórticos, sostenidos por columnas de capiteles cúbicos, de siete arcos semicirculares adornados con rombos calados e inscripciones de alabanza a Dios. El arco central es mayor que los otros seis, y presenta enjutas macizas con decoración de ataurique y capiteles de mocárabes. ",
    "./assets/item/arrayanes.jpg"
);

const generalife_item = new Item(
    "Generalife",
    "El Generalife es la villa con jardines habitada por los reyes musulmanes de Granada como lugar de descanso. Fue concebida como villa rural, donde jardines ornamentales, huertos y arquitectura se integraban, en las cercanías de la Alhambra. El origen del nombre está discutido. Algunos abogan por Yannat al-Arif como «huerta del arquitecto», o «jardín del arquitecto» aunque pudo significar «el más excelso jardín». Ese huerto real era común en las cortes hispano-árabes y es fruto de las reformas y añadidos que le aportaron los diferentes sultanes. Por sus elementos decorativos más antiguos, el palacio debió de construirse a finales del siglo XIII por el segundo sultán de la dinastía nazarí, Muhammad II (1273-1302). Siendo declarado, junto con la Alhambra, Patrimonio de la Humanidad por la Unesco en 1984.",
    "./assets/item/generalife.jpg"
);

const golden_room_item = new Item(
    "Cuarto Dorado",
    `Por la pequeña puerta con arco de herradura, dispuesta así para permitir el paso de una sola persona y controlar perfectamente el tránsito de una estancia a otra, se pasa al patio en el que el Sultán recibía en audiencia a sus súbditos en la Alhambra del siglo XIV.
    <br/> <br/>Al norte del patio, tras el característico pórtico de tres arcos se encuentra el Cuarto Dorado, cuya decoración original se debe a Muhammad V. Fue llamado así por la bella techumbre de madera que lo cubre, repintada y decorada, al igual que toda la estancia, en época de los Reyes Católicos, como testimonian sus escudos, el yugo y las flechas y la ventana central con parteluz y capitel mudéjar. Esta sala debía ser utilizada por los administradores y secretarios de la corte musulmana para anotar y ejecutar las sentencias del Sultán.
    <br/> <br/>Por debajo de la estancia corre el pasadizo de ronda que usaba la guardia de seguridad de los Palacios. Originalmente estaba sobre la muralla, a cielo abierto, pero las transformaciones y ampliaciones de los palacios en el siglo XIV lo dejaron oculto, al igual que la estructura original de este sector de la Alhambra.
    <br/> <br/>La planta superior de la sala, también modificada, albergó a la Emperatriz Isabel de Portugal el verano de 1526, y después a los Gobernadores y Alcaides de la Alhambra. `,
    "./assets/item/golden_room.jpg"
);

let items_dictionary = new Map();

items_dictionary.set("Patio de los Arrayanes", arrayanes_item);
items_dictionary.set("Leones del patio", leones_item);
items_dictionary.set("Generalife", generalife_item);
items_dictionary.set("Cuarto Dorado", golden_room_item);
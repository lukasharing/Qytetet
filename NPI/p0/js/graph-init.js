/* 1. Entrada Este */
const entrada_este = new Node("Entrada Este", "entrada-este");
entrada_este.set_marker(500, 160);
//entrada_este.set_gps(37.1751538, -3.5861705);
entrada_este.set_gps(37.1973126, -3.6244918);

/* 2. Calle Real de la Alhambra */
const calle_real = new Node("Calle Real de la Alhambra", "iglesia-palacio-central");
calle_real.set_marker(235, 340);
// calle_real.set_gps(37.1762785, -3.5895924);
calle_real.set_gps(37.1971932, -3.6243575000000003);

/* 3. Paseo Real de la Alhambra */
const paseo_real = new Node("Paseo Real de la Alhambra", "paseo-real");
paseo_real.set_marker(365, 195);
paseo_real.set_gps(37.1762785, -3.5895924);

/* 4. Jardines Adarves */
const jardines_adarves = new Node("Jardines Adarves", "jardines-adarves");
jardines_adarves.set_marker(125, 425);
jardines_adarves.set_gps(37.1750825, -3.5870668);

/* 5. Jardines Paraiso */
const jardines_paraiso = new Node("Jardines Paraiso", "jardines-paraiso");
jardines_paraiso.set_marker(400, 290);
jardines_paraiso.set_gps(37.1764132, -3.5870442);

/* 6. Patio Comares */
const patio_comares = new Node("Patio de Comares", "patio-comares");
patio_comares.set_marker(255, 425);
patio_comares.set_gps(37.1772072, -3.5907835);

/* 7. Patio Leones */
const patio_leones = new Node("Patio de los leones", "patio-leones");
patio_leones.set_marker(280, 400);
patio_leones.set_gps(37.1770712, -3.5899057);

// 3D
patio_leones.add(leones_item);

/* 8. Peinador de la reina */
const peinador_reina = new Node("Peinador de la reina", "peinador-reina");
peinador_reina.set_marker(293, 447);
peinador_reina.set_gps(37.1776579, -3.5905158);

/* 9. Sala de los reyes */
const sala_reyes = new Node("Sala de los reyes", "sala-reyes");
sala_reyes.set_marker(295, 400);
sala_reyes.set_gps(37.1770724, -3.5911767);

/* 10. Parador de san francisco */
const parador_san_francisco = new Node("Parador de San Francisco", "san-francisco");
parador_san_francisco.set_marker(390, 255);
//parador_san_francisco.set_gps(37.1758294, -3.5871992);
parador_san_francisco.set_gps(37.1973829, -3.6245545999999993);

/* 11. Torre de Comares */
const torre_comares = new Node("Torre de Comares", "torre-comares");
torre_comares.set_marker(260, 450);
torre_comares.set_gps(37.1772698, -3.5906263);

/* 12. Torre de las damas */
const torre_damas = new Node("Torre de las damas", "torre-damas");
torre_damas.set_marker(337, 412);
torre_damas.set_gps(37.1774488, -3.5895599);

/* 13. Torre de la vela */
const torre_vela = new Node("Torre de la vela", "torre-vela");
torre_vela.set_marker(78, 457);
torre_vela.set_gps(37.1770672, -3.5944138);

/* 14. Puerta del vino */
const puerta_vino = new Node("Puerta del vino", "puerta-vino");
puerta_vino.set_marker(170, 395);
puerta_vino.set_gps(37.176517, -3.5914556);

/* 15. Puerta del vino */
const carlos_quinto = new Node("Palacio Carlos Quinto", "carlos-quinto");
carlos_quinto.set_marker(230, 390);
carlos_quinto.set_gps(37.1768235, -3.5912582);


/* Connections */
entrada_este.connect(paseo_real, 180);
entrada_este.connect(jardines_paraiso, 0);
paseo_real.connect(calle_real, 90);
calle_real.connect(puerta_vino, 180);
puerta_vino.connect(carlos_quinto, 90);
carlos_quinto.connect(patio_comares, 90);
puerta_vino.connect(jardines_adarves, 180);
jardines_adarves.connect(torre_vela, 180);

jardines_paraiso.connect(torre_damas, 90);
torre_damas.connect(sala_reyes, 180);
jardines_paraiso.connect(sala_reyes, 180);
sala_reyes.connect(patio_leones, 180);
patio_leones.connect(patio_comares, 180);
patio_leones.connect(peinador_reina, 90);
patio_comares.connect(torre_comares, 90);
parador_san_francisco.connect(calle_real, 180);
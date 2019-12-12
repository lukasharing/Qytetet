/* UTILS */

const mod = (v, m) => ((v % m) + m) % m;
const toAngle = v => (Math.floor(v * 180 / Math.PI) + 360) % 360;
const toRadian = v => v * Math.PI / 180;
const angle2ID = v => Math.ceil(Math.floor(v / 45) * .5) % 4;
const distance = (v1, v2) => Math.sqrt(Math.pow(v1[0] - v2[0], 2) + Math.pow(v1[1] - v2[1], 2) + Math.pow(v1[2] - v2[2], 2));


window.callbacks = [];
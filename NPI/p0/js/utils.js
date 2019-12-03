/* UTILS */

const mod = (v, m) => ((v % m) + m) % m;
const toAngle = v => (Math.floor(v * 180 / Math.PI) + 360) % 360;
const toRadian = v => v * Math.PI / 180;
const angle2ID = v => Math.ceil(Math.floor(v / 45) * .5) % 4;

window.callbacks = [];
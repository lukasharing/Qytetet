//!function(a){var b=a.createElement("script");b.onload=function(){TouchEmulator()},b.src="//cdn.rawgit.com/hammerjs/touchemulator/0.0.2/touch-emulator.js",a.body.appendChild(b)}(document);

const AlambraApp = new App("AlambraApp");

window.onload = function(){

  AlambraApp.load().then(_ => {
    AlambraApp.init();
    this.callbacks.map(e => e(AlambraApp));
  });

};
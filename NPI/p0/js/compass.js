/* COMPASS */

class Compass {

    constructor(app){

        if(!!Compass.instance) return Compass.instance;

        Compass.instance = this;

        this._app = app;

        return this;
    };

    init(){

        if(window.DeviceOrientationEvent){

            if(typeof DeviceMotionEvent.requestPermission === 'function'){
                window.DeviceOrientationEvent.requestPermission().then(status => this.compass_permission(status === 'granted'));
            }else{
                this.compass_permission(true);
            }
        
        }else{
        
            this.compass_permission(false);
        
        }

    };

    compass_permission(granted){

        if(granted){
            window.addEventListener("deviceorientation", e => this.compass_orientation(e), false);
        }else{
            document.getElementById("compass").classList.add("none");
        }

    };

    compass_orientation(event){

        const compass = Number(event.alpha);
        
        const html = document.getElementById("compass-point");
        html.style.webkitTransform = `rotate(${compass}deg)`;
        html.style.transform = `rotate(${compass}deg)`;

        const letters = Array.from(document.querySelectorAll("#compass > span"));
        letters.forEach(e => e.classList.remove("on"));
        letters[angle2ID(compass)].classList.add("on");

    };

}
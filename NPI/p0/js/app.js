class App{

    constructor(name){
        
        if(!!App.instance) return App.instance;

        App.instance = this;

        this._name = name;

        /* Renderer */
        this._renderer = new Renderer(this);
        
        /* Map */
        this._minimap = new MiniMap(this);
        
        /* QR */
        this._qr      = new Qr(this);
        
        /* Compass */
        this._compass = new Compass(this);

        /* Cache */
        this._cache   = new Cache();

        /* Leap */
        this._leap    = new LeapController(this);

        /* Touch */
        this._start_touch = 0;
        this._touch = {x: 0, y: 0};

        return this;
    };

    /* Load to cache */
    load(){
        const cache_array = [];

        this._minimap.nodes().forEach(node => {

            const id_texture = node.id();
            cache_array.push({id: id_texture, src: `./assets/skyboxes/${id_texture}.jpg`, type: FILE.TEXTURE});
            cache_array.push({id: id_texture, src: `./assets/minis/${id_texture}.jpg`, type: FILE.IMAGE});
        
        });

        return this._cache.load(cache_array);
    };

    init(){
        
        /* Animation */
        document.getElementById("loading-bar").style.width = "100%";
        setTimeout(_ => {
        
            document.getElementById("loading-popup").classList.add("hide");
            this.popup_open("#tutorial0");
        
        }, 1000);

        /* Init */
        this._renderer.init();
        this._minimap.init();
        this._renderer.start();
        this._qr.init();
        this._compass.init();
        
        /* Add Events to any popup close element */
        this.init_touchevents();
        this.init_popup();

    };

    init_touchevents(){

        /* Delegation */
        document.getElementById("enviroment").addEventListener("touchstart", e => this.touchstart(e), false);
        document.getElementById("enviroment").addEventListener("touchmove", e => this.touchmove(e), false);
        document.getElementById("enviroment").addEventListener("touchend", e => this.touchend(e), false);

    };

    init_popup(){
        Array.from(document.querySelectorAll(".popup > .close")).map(elm => {
            
            /* If it has steps, move to the next element */
            if(elm.parentNode.hasAttribute("data-step")){
                elm.addEventListener("click", _ => this.next_tutorial(elm));
            /* If not, then close it */
            }else{
                elm.addEventListener("click", _ => this.popup_close(`#${ elm.parentElement.id }`));
            }
            
        });
    };

    /* Next Tutorial */
    next_tutorial(elm){
        const id_step = parseInt(elm.parentNode.dataset["step"]);
        this.popup_close(`#${ elm.parentElement.id }`);
        const next_popup = document.querySelector(`.popup[data-step="${id_step + 1}"]`);
        if(next_popup !== null){
            this.popup_open(`.popup[data-step="${id_step + 1}"]`);
        }
    };

    /* Touch Events */
    touchstart(event){

        event.preventDefault();

        /* Store Date */
        this._start_touch = (new Date()).getTime();

        /* Store always one finger */
        this._touch.x = event.touches[0].clientX;
        this._touch.y = event.touches[0].clientY;

        /* Check if two fingers are on the screen */
        if(event.touches.length == 2){

            document.body.classList.add("doubletouch");

            /* Create imaginary finger from the middle of both */
            this._touch.x = (event.touches[0].clientX + event.touches[1].clientX) * .5;
            this._touch.y = (event.touches[0].clientY + event.touches[1].clientY) * .5;

        }

    };

    touchmove(event){

        if(document.body.classList.contains("doubletouch") && event.touches.length == 2){

            /* Get Distance from the center of the screen */
            const DX = Math.abs(window.innerWidth * .5 - this._touch.x);
            const DY = Math.abs(window.innerHeight * .5 - this._touch.y);
            
            /* Drag at least one quarter of the screen */
            const UMBRAL_LONG = (Math.min(window.innerWidth, window.innerHeight) * .25);
            
            //event.touches[1] = {clientX: event.touches[0].clientX, clientY: event.touches[0].clientY - 50}

            /* Imaginary finger from the middle of both */
            const x_touch = (event.touches[0].clientX + event.touches[1].clientX) * .5;
            const y_touch = (event.touches[0].clientY + event.touches[1].clientY) * .5;
            
            /* Distance from the start and the current position */
            const dx = x_touch - this._touch.x, dy = y_touch - this._touch.y;
            const length = Math.sqrt(dx * dx + dy * dy) + 0.0001;
            const angle = toAngle(Math.atan2(-dy / length, dx / length));
            
            /* Change HTML elements */
            Array.from(document.querySelectorAll(".preview-button")).forEach(k => k.classList.remove("hover"));
            
            const image_id = angle2ID(angle);
            const element = document.querySelectorAll(".preview-button")[image_id];
            if(length >= UMBRAL_LONG && !element.classList.contains("blocked")){
              element.classList.add("hover");
            }
            
        }

    };

    touchend(event){

        event.preventDefault();

        const current_time = (new Date()).getTime();

        let body_class = document.body.classList;
        /* Check if two fingers */
        if(body_class.contains("doubletouch")){
            
            const direction = document.querySelector(".preview-button.hover");
            if(direction !== null){

                const id = parseInt(direction.dataset.orientation);
                this._minimap.move(this._renderer.azimuth_connection(id));
                document.querySelector(".preview-button.hover").classList.remove("hover");
                
            }
            body_class.remove("doubletouch");

        
        /* Check if fast touch (less than 200ms)  => Raycasting*/
        }else if(current_time - this._start_touch <= 200){

            const canvas = document.getElementById("enviroment");
            
            /* Screen position */
            const x = (this._touch.x / canvas.clientWidth) * 2 - 1;
            const y = -(this._touch.y / canvas.clientHeight) * 2 + 1;

            /* THREE JS Cast Ray, If hit, show info */
            const objects = this._renderer.cast(x, y);
            if(objects.length > 0){
                this.show_info(objects[0]);
            }

        }

    };


    /* Popups */
    
    popup_close(tag){

        this._renderer.start();
        document.querySelector(tag).classList.add("hide");
        document.body.classList.remove("isOnPopup");

    };

    popup_open(tag){

        this._renderer.stop();
        document.querySelector(tag).classList.remove("hide");
        document.body.classList.add("isOnPopup");

    };

    /* Add Information to the Information Preview Article */
    show_info(element){
    
        document.querySelector("#information-preview > article").innerHTML = element.html();
        this.popup_open("#information-preview");
    
    };

}
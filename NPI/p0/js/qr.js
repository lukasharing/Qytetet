/* QR */

class Qr{

    /* Contructor. Inicializa el scanner que nos servirá para inicializar la cámara, pararla y
       leer los códigos QR */
    constructor(app){
        if(!!Qr.instance) return Qr.instance;

        Qr.instance = this;

        /* Scanner */
        let scanner = new Instascan.Scanner({
            video: document.getElementById('preview'),
            mirror: false
        });
        

        this._app = app;
        this._scanner = scanner;

        return this;
    };

    /* Método que asigna al botón QR una función un evento para que cuando se haga click en
       el, se abra el popup y se inicialice la cámara */
    init(){
        document.querySelector("#qr-button").addEventListener('click', _ => {
      
            this._app.popup_open("#qr-preview");
            this.start_scanner();
    
        });
    
        document.querySelector("#qr-preview > .close").addEventListener('click', _ => this.stop_scanner());
          
    }

    /* Método para inicializar la cámara. Obtenenemos la cámara trasera si es posible. En caso de que
       el dispositivo no disponga de camaras, salimos.
       Una vez obtenida la cámara llamamos al metodo read_qrcode para empezar a leer*/
    start_scanner(){
        
        Instascan.Camera.getCameras().then(cameras => {
            if(cameras.length > 0){
                
                const back_camera = cameras.find(e => e.name !== null && e.name.indexOf("back") >= 0);
                this._scanner.start(back_camera === undefined ? cameras[0] : back_camera);
                

            }else{

                document.getElementById("qr-button").classList.add("none");

            }

        });
    
        this.read_qrcode();
    };
    
    /* Método para parar la cámara */
    stop_scanner(){
    
        this._scanner.stop();
    
    };
    

    /* Método para leer el código QR. El código contendrá de manera codificada un json que
       parsearemos y obtendremos un objeto con dos atributos:
            - El tipo de QR (localización o información).
            - La locacización o la información.

       Una vez obtenido esto, saltará un mensaje por pantalla que nos informará del tipo de
       QR que hemos leido y que podremos aceptar o rechazar si queremos que se realice la
       acción. Si es de tipo localización, nos moverá a dicho lugar y si, es de tipo información,
       nos mostrará dicha información.
    */
    read_qrcode() {

        this._scanner.addListener('scan', (content) => {

            try{
                
                let obj = JSON.parse(content);
                switch(obj.type){
                    case     "location": this.read_location_qr(obj.val);   break;
                    case "information" : this.read_information_qr(obj.val); break;
                    default: throw "Este código QR no es válido";
                };

            }catch(error){
                modal(Modal.ERROR, "QR no válido", error);
            }

        });    
    };

    /* Se ha leido un qr de localización */
    read_location_qr(value){

        let node = this._app._minimap.find_node(value);
        if(!!node){
            
            modal(Modal.BOTH, "QR de Localización", `¿Quiéres moverte a ${node.title()}?`, _ => {

                this._app._minimap.move(value);
                this.stop_scanner();
                this._app.popup_close("#qr-preview");

            });

        }else{ throw "Esta localización no existe."; }

    };

    /* Se ha leido un qr de información */
    read_information_qr(value){

        if(items_dictionary.has(value)){

            modal(Modal.BOTH, "QR de Información", `¿Quiéres obtener informacion sobre ${value}?`, _ => {

                this._app.popup_close("#qr-preview");
                this.stop_scanner();

                /* this._app._minimap.findItem(value) */
                this._app.show_info(items_dictionary.get(value));

            });

        }else{
            throw "Esta información no existe.";
        }

    };

};
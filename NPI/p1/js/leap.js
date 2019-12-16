class LeapController{

    constructor(app){

        this._app = app;

        this._last = '';
        this._time = 0;

    };

    init(){

        Leap.loop().use("boneHand", {
            
            scene: this._app._renderer._foreground_scene,
            camera: this._app._renderer._camera,
            opacity: 0.3,
            jointColor: new THREE.Color(0xffffff)

        });
        
        Leap.loopController.on("frame", frame => this.frame(frame));
    };

    frame(frame){

        
        document.body.classList.remove("doubletouch");
        this.hands = frame.hands;

        let next = '';
        if(this.hands.length === 1 && this.hands[0]){
            next = this.one_hand();
        }else if(this.hands.length === 2 && this.hands[0] && this.hands[1]){
            next = this.two_hands();
        }else return;

        this._time = next !== this._last ? 0 : (this._time + 1);
        this._last = next;
    }

    one_hand(){

        const hand = this.hands[0];
        const palm_pos  = hand.palmPosition;
        palm_pos[1] = 0.;

        const centers     = hand.fingers.map(finger => distance(palm_pos, [finger.dipPosition[0], 0., finger.dipPosition[2]]));

        const grab        = hand.grabStrength;

        const intersections = [[], [], [], [], []];
         
        //    0 = THUMB
        //    1 = INDEX
        //    2 = MIDDLE
        //    3 = RING
        //    4 = PINKY
        
        const RADIUS = 40;
        hand.fingers.forEach((finger1, i) =>
            hand.fingers.forEach((finger2, j) => 
                intersections[i][j] = (finger1.invalid || finger2.invalid) ? 1000. : distance(finger1.dipPosition, finger2.dipPosition)
            )
        )

        const collide_fingers = (i, u) => intersections[i].reduce((_a, _v, _i) => _a + (_i == i ? 0 : (_v < u)), 0);
        const center_fingers = (v, u) => v.map(e => e < u).reduce((i, a) => i | a, true);

        // If popup, only close event
        if(document.body.classList.contains("isOnPopup")){
            if(grab > 0.9){
                // Close Popup
                if(this._time > 50 && this._last === 'c'){
                    if(centers[0] < 30.){ // E
                        
                        const popup  = document.querySelector(".popup:not(.hide)");
                        if(popup.dataset["step"] !== undefined){
                            this._app.next_tutorial(popup.querySelector(".close"));
                        }else{
                            this._app.popup_close(`#${popup.id}`);
                        }
                        return 'a'; // 'a' -> Action

                    }
                }
                return 'c'; // 'c' -> Close
            }
        }else{
            // If not popup
            console.log(grab)
            if(Math.abs(Math.abs(hand.roll()) - 1.6) < 0.25){
                // Qr (Camera Photo Like)
                if(this._time > 50 && this._last == 'q'){
                    if(collide_fingers(0, 50.) === 0){
                        this._app.popup_open("#qr-preview");
                        return 'a'; // 'a' -> Action
                    }
                }
                return 'q'; // 'q' -> QR
            }else{
                // Map (Palm facing up)
                if(this._time > 50 && this._last == 'm'){
                    if(Math.abs(hand.roll()) > 3.){
                        this._app.popup_open("#map-preview");
                        return 'a'; // 'a' -> Action
                    }
                }
                return 'm'; // 'm' -> Map
            
            // Hand normal is parallel to the plane xz
            }
        }

        return 'o'; // 'o' -> One Hand
    };

    two_hands(){
        // Only if popup is not active
        if(document.body.classList.contains("isOnPopup")) return;

        let hand1 = this.hands[0];
        let hand2 = this.hands[1];
        
        // Swap Hands, hand 1 -> Right, hand 2 -> Left
        if(hand1.type === "left"){
            hand1 = this.hands[1];
            hand2 = this.hands[0];
        }
        // Foward Movement Map HTML
        const front = document.querySelector(".preview-button:nth-child(2)");
        front.classList.remove("hover");
        
        // Projection hand into plane yx (h1, h2) Where h1 and h2 are left and right hand
        const palm1_plane_xy = hand1.palmPosition.map((i, j) => (j === 2) ? 0. : i);
        const palm2_plane_xy = hand2.palmPosition.map((i, j) => (j === 2) ? 0. : i);
        
        // ∇ (h1, h2) = grad
        const dpalm_plane_xy = palm1_plane_xy.map((e, i) => e - palm2_plane_xy[i]);

        // | grad |
        const dpalm_len = Math.sqrt(dpalm_plane_xy.reduce((a, b) => a + Math.pow(b, 2.), 0));
        
        // tan-1(grad) = θ
        let dpalm_ang = Math.atan2(dpalm_plane_xy[1], dpalm_plane_xy[0]);
        
        // Clamp Values
        dpalm_ang = Math.max(Math.min(Math.PI / 2, dpalm_ang), -Math.PI / 2);

        // Movement (Like doubletouch)
        document.body.classList.add("doubletouch");
        
        // Both hands are closed
        if(hand1.grabStrength > 0.99 && hand2.grabStrength > 0.99){
            
            // Hands not diagonal
            if(Math.abs(dpalm_ang) > 0.1){
                
                // TODO: Tilt Camera to the right
                //this._app._renderer._camera.rotation.x = Math.sign(dpalm_ang) * 0.5;
                this._app._renderer._controls.rotate(-dpalm_ang * 2. * Math.PI / 180);

                return 'd'; // 'd' -> Direction
            
            }
        
        /* One Hand is Closed, The other hand is Open */
        }else if(hand1.grabStrength > 0.99 && hand2.grabStrength < 0.1 || hand1.grabStrength < 0.1 && hand2.grabStrength > 0.99){
            
            front.classList.add("hover");
            if(this._time >= 50 && this._last === 'f'){
                
                this._app._minimap.move(this._app._renderer.azimuth_connection(1));
                
                return 'a'; // 'a' -> Action
            }

            return 'f'; // 'f' -> Foward
        }

        return 't'; // 't' -> Two hands
    };

}

window.callbacks.push(function(app){
    app._leap.init();
});
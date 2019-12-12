window.callbacks.push(function(app){

    app._renderer._leap.loop().use("boneHand", {
        
        scene: app._renderer._foreground_scene,
        opacity: 0.3,
        jointColor: new THREE.Color(0xffffff)

    });
    
    // https://image.freepik.com/vector-gratis/alfabeto-lenguaje-signos_23-2147896970.jpg
    app._renderer._leap.loopController.on("frame", frame => {
        if(!frame.hands.length > 0) return;

        const hand = frame.hands[0];
        
        if(!hand) return;
        
        /* 0. -> Open Hand, 1. -> Closed Hand  */
        
        const palm_pos  = hand.palmPosition;
        palm_pos[1] = 0.;

        const centers     = hand.fingers.map(finger => distance(palm_pos, [finger.dipPosition[0], 0., finger.dipPosition[2]]));
        

        const grab        = hand.grabStrength;
        const palm        = Math.abs(hand.roll());
        const verticality = hand.yaw();

        const intersections = [[], [], [], [], []];

        /*         
            0 = THUMB
            1 = INDEX
            2 = MIDDLE
            3 = RING
            4 = PINKY
        */
        const RADIUS = 40;
        hand.fingers.forEach((finger1, i) =>
            hand.fingers.forEach((finger2, j) => 
                intersections[i][j] = (finger1.invalid || finger2.invalid) ? 1000. : distance(finger1.dipPosition, finger2.dipPosition)
            )
        )

        const collide_fingers = (i, u) => intersections[i].reduce((_a, _v, _i) => _a + (_i == i ? 0 : (_v < u)), 0);
        const center_fingers = (v, u) => v.map(e => e < u).reduce((i, a) => i | a, true);

        if(grab > 0.999){
            if(centers[0] < 30.){
                console.log("E");
            }else{
                console.log("A");
            }
        }else if(grab < 0.001){
            
            //center < 40.
            if(centers[0] < 30.){
                console.log("B");
            }else{
                
                if(intersections[0][1] < 30.){
                    console.log("F");
                }else{
                    if(collide_fingers(1, 50.) === 0){
                        
                    }else{
                        console.log("D");
                    }
                }
            }
            
        }else{
            if(palm >= 1.45 && palm <= 1.85){
                if(collide_fingers(0, 50.) === 0){
                    console.log("C");
                }
            }
        }
        // Grab === 1.

        // B
        // Grab === 0

        // C 
        // Grab > 0 && < 1 
        // ??

        // D
        // Finger INDEX NO COLLISION

        // E 
        // Grab === 1
        // Finger THUMB COLLIDE AT LEAT WITH 3

        // F
        // Grab == 0
        // THUMB + INDEX COLLIDE

        // G
        // PALM (Perpendicular)
        // FINGER THUMB NO COLLIDE with INDICE
        // OTHER ALL TOUCHING 

        // H
        // 


        /*
        lineGeometry.vertices[0].fromArray(hand.indexFinger.tipPosition);
        lineGeometry.vertices[1].fromArray(hand.thumb.tipPosition);
    
        lineGeometry.verticesNeedUpdate = true;
        */
    });

})
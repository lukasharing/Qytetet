window.callbacks.push(function(app){
    
    app._renderer._leap.loop().use("boneHand", {
        
        targetEl: document.getElementById("hands"),
        opacity: 0.3,
        jointColor: new THREE.Color(0xffffff)

    }).use("proximity");
    
    app._renderer._leap.loopController.on("frame", frame => {
        if(!frame.hands.length > 0) return;

        const hand = frame.hands[0];
        
        if(!hand) return;
        console.log(2);
        /*
        lineGeometry.vertices[0].fromArray(hand.indexFinger.tipPosition);
        lineGeometry.vertices[1].fromArray(hand.thumb.tipPosition);
    
        lineGeometry.verticesNeedUpdate = true;
        */
    });

})
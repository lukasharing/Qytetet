/* RENDERER */

class Renderer{

  constructor(app){
    if(!!Renderer.instance) return Renderer.instance;

    Renderer.instance = this;

    this._element     = document.getElementById("enviroment");
    this._labels_html = document.createElement("div");
    this._labels_html.id = `${"enviroment"}_labels`;
    this._element.parentNode.appendChild(this._labels_html);

    this._app = app;
    
    this._raycast = new THREE.Raycaster();

    this._leap = Leap;

    this._stop = true;

    this._azimuth_angle = 0;

    return this;
  };
  
  init(){
    
    // Renderer
    this.create_renderer();
    
    // Camera 
    this.create_camera();

    // Control
    this.create_controls();
  
    this.create_scene();
    this.resize();

    window.addEventListener("resize", e => this.resize(e));

    
    /* Finger Chaging Visalizator compass */
    let self = this;
    this._controls.addEventListener("change", function(e){

      /* Get id from the azimuth angle */
      const degrees = toAngle(this.getAzimuthalAngle());
      const id = angle2ID(degrees);

      /* Check if different from the last id */
      if(self._azimuth_angle != id){
        
        /* Update and Store */
        self._azimuth_angle = id;
        self.update_direction();
      
      }
  
    });

  };

  /* 
    Returns from the current node 
    the next node walking in a 0 - 3 azimuth angle
    and from the current rotation device 
  */
  azimuth_connection(azimuth){
      return this._app._minimap._current_node.to(mod(azimuth + this._azimuth_angle, 4));
  };

  /*
    Update the two finger moving around feature to the new
    rotation (compass).
  */
  update_direction(){

    /* Helper for the 4 directions (Non-Visit list) */
    const used = [0, 1, 2, 3];
    /* Remove previous classes */
    used.forEach(e => document.querySelectorAll(".preview-button")[e].classList.remove("blocked"));

    /* For each directions possible from the current node */
    this._app._minimap._current_node.connections().forEach(connection => {
        
        /* Get the compass direction and put it counter-clockwise from the connection direction */
        const id = mod(connection.angle() - this._azimuth_angle, 4);

        /* Get the new preview image and the html element from that id */
        const image = this._app._cache.get(`image_${connection.to().id()}`);
        const element = document.querySelectorAll(".preview-button")[id];

        /* Modify it's html and remove from the non-visit list */
        element.innerHTML = '';
        element.appendChild(image);
        used.splice(used.indexOf(id), 1);

    });

    /* For the non-visited html items, add a class to prevent it from showing */
    used.forEach(e => document.querySelectorAll(".preview-button")[e].classList.add("blocked"));
  };

  /* Creates THREE JS Renderer */
  create_renderer(){

    const renderer = new THREE.WebGLRenderer({ canvas: this._element });
    renderer.autoClearColor = false;
    this._renderer = renderer;

  };

  /* Creates THREE JS Orbit Controls */
  create_controls(){
    
    const controls = new THREE.OrbitControls(this._camera, this._element);
    controls.enableZoom = false;
    controls.update();
    this._controls = controls;

  };

  /* Creates THREE JS Camera */
  create_camera(){
    
    const camera = new THREE.PerspectiveCamera(75., 2., .1, 200.);
    camera.position.z = 3;
    this._camera = camera;

  };

  /* Creates THREE JS scene */
  create_scene(){

    /* Create scene */
    const scene = new THREE.Scene();

    /* Add White Light to the scene */
    const light = new THREE.AmbientLight(0xFFFFFF);
    scene.add(light);

    /* Create background scene */
    const bgScene = new THREE.Scene();

    /* Create Shader for the Skybox material */
    const shader = THREE.ShaderLib.equirect;
    const material = new THREE.ShaderMaterial({
      fragmentShader: shader.fragmentShader,
      vertexShader: shader.vertexShader,
      uniforms: shader.uniforms,
      depthWrite: false,
      side: THREE.BackSide,
    });
    this._material = material;
    
    /* Create "Sky" box */
    const plane = new THREE.BoxBufferGeometry(2, 2, 2);
    /* Fix Material to that box */
    const bgMesh = new THREE.Mesh(plane, material);
    /* Add it into the bgscene */
    bgScene.add(bgMesh);

    this._background_mesh  = bgMesh;
    this._background_scene = bgScene;
    this._foreground_scene = scene;

  };

  /* Cast Ray using THREE JS */
  cast(x, y){

    const position = new THREE.Vector2(x, y);
    /* Cast from camera */
    this._raycast.setFromCamera(position, this._camera);
    
    /* Get Collision array  */
    let collide = this._raycast.intersectObjects(this._foreground_scene.children);

    /* Map only object */
    collide = collide.map(e => e.object);
    /* Find the Items associated to that THREE Object (Same id) */
    collide = collide.map(e => this._app._minimap._current_node._items.find(k => k._shape.uuid === e.uuid));

    return collide;
  };

  /* Change Scene */
  change_scene(){

    /* Change Shader Material to the new skybox */
    this._material.uniforms.tEquirect.value = this._app._cache.get(`texture_${this._app._minimap._current_node.id()}`);
    /* Set controls to 0 and update */
    this._controls.target.set(0, 0, 0);
    this._controls.update();
    /* Put new items from the node */
    this.put_items();
    /* Update two fingers Directions boxes */
    this.update_direction();
    
  };

  /* Resize window event that fixes aspect-ratio */
  resize(event){

    /* Fix Aspect-Ratio */
    this._renderer.setSize(this._element.clientWidth, this._element.clientHeight, false);
    this._camera.aspect = this._element.clientWidth / this._element.clientHeight;
    this._camera.updateProjectionMatrix();

  };

  /* Start drawing */
  start(){
    
    this._stop = false;
    requestAnimationFrame(time => this.render(time));

  };

  /* Stop Drawing */
  stop(){

    this._stop = true;

  };

  /* Put all items from the current node into the foreground scene */
  put_items(){
    
    /* Clear previous Items and tags */
    //this._foreground_scene.children.forEach(e => console.log(e));
    while(this._foreground_scene.children.length > 1){
      this._foreground_scene.children.pop();
      this._labels_html.firstChild.remove();
    }
    
    /* Draw all items from the current node */
    this._app._minimap._current_node._items.forEach(item => {
      
      if(item._visible){

        /* Put Item in the foreground scene */
        this._foreground_scene.add(item.shape);

        /* Put tags */
        const label = document.createElement("span");
        label.innerText = item.title();
        this._labels_html.appendChild(label);

      }

    });

  };

  /* Render the scene */
  render_scene(time){
    
    /* Temporal Vectors */
    const tempV = new THREE.Vector3();
    const tempW = new THREE.Vector3();
    const tempP = new THREE.Vector3();

    /* For each item in the foreground scene, re-draw it */
    const children = this._foreground_scene.children.filter(e => e.name.indexOf("hand") < 0 && e.name != "");
    for(let i = 0; i < children.length; ++i){
      
      /* Elements should always face to the camera */
      const element = children[i];
      element.lookAt(this._camera.position);
      
      /* Clone vector */
      element.getWorldPosition(tempV);
      tempP.copy(tempV);

      /* Project Element vector into the camera */
      tempV.project(this._camera);

      /* Put the label in that projection position */
      const span = this._labels_html.children[i - 1];
      const x = (tempV.x *  .5 + .5) * this._element.clientWidth;
      const y = (tempV.y * -.5 + .5) * this._element.clientHeight;
      
      /* Get Angle direction */
      this._camera.getWorldDirection(tempW);
      const dot = tempW.dot(tempP);

      /* Hide if the vector is facing towards the camera */
      span.style.display = dot >= 0 ? `block` : `none`;
      span.style.transform = `translate(-50%, -50%) translate(${x}px,${y}px)`;
    }

  };

  /* Render the visualizator:
    Background, Foreground and Items
  */
  render(time){
    
    this.render_scene(time);

    /* Put the Center of the background_mesh to the camera position */
    this._background_mesh.position.copy(this._camera.position);

    /* Render Background scene */
    this._renderer.render(this._background_scene, this._camera);
    /* Render Foreground scene */
    this._renderer.render(this._foreground_scene, this._camera);
    
    if(!this._stop){  
      requestAnimationFrame(time => this.render(time));
    }

  };

};
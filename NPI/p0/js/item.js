class Item{

    constructor(title, description, image){

        this._title       = title;
        this._description = description;
        this._image       = image;

        this._angle   = 0;
        this._size    = 0;
        this._visible = false; 
    };

    title(){ return this._title; };

    /* 
        Create an element for the scene and position
    */
    world(angle, size){
        
        angle = toRadian(angle);
        /* Create THREE Cube for the Item */
        const cube = new THREE.Mesh(
            new THREE.BoxGeometry(size, size, 1.),
            new THREE.MeshPhongMaterial({ color: 0xFF0000, opacity: 0.5, transparent: true })
        );
        cube.rotation.set(0, angle, 0);
        cube.position.set(
            Math.cos(angle) * 100.,
            0,
            Math.sin(angle) * 100.
        );
        
        cube.name = "obj_scene";

        this._shape = cube;
        this._visible = true;
    };

    get shape(){ return this._shape; };

    /* Returns HTML */
    html(){

        return `
            <h2>${ this._title }</h2>
            <div class="scrollable">
                ${this._image != null ? `<figure><img src="${this._image}" alt="${ this._title }" /></figure>` : ''}
                <p>
                    ${ this._description }
                </p>
            </div>
        `;

    };
}
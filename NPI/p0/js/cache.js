/* Cache System */
const FILE = {
    TEXTURE: 1,
    IMAGE: 2
};

/* Cache Singleton */
class Cache{

    constructor(callback){
        if(!!Cache.instance) return Cache.instance;

        Cache.instance = this;

        this._length = 0;
        this._loaded = 0;
        this._cache  = new Map();

        return this;
    };

    /*
        Add Element to cache with a prefix depending on the type of process:
        texture process : texture prefix
        image process : image prefix
    */
    add(prefix, id, obj){
        this._cache.set(`${prefix}_${id}`, obj);
        document.getElementById("loading-bar").style.width = `${++this._loaded / this._length * 100.}%`;
    };

    /* 
        Returns an element from the cache with the same name
    */
    get(name){ return this._cache.get(name); };

    /*
        It process an Image from it's path
    */
    image(path){
        return new Promise((resolve, reject) => {

            const image = new Image();
            image.onload = _ => {
                this.add("image", path.id, image);
                resolve();
            };
            image.src = path.src;
      
        });
    };

    /*
        It process a Texture from it's path
    */
    texture(path){
        return new Promise((resolve, reject) => {
      
            const loader = new THREE.TextureLoader();
            const texture = loader.load(path.src);
            texture.magFilter = THREE.LinearFilter;
            texture.minFilter = THREE.LinearFilter;
            this.add("texture", path.id, texture);
            resolve();
        
      });
    };

    /*
        Load from array {type, id, path} into the cache
        depending on the type it will process an Image or a Texture
    */
    load(array){

        const loaded = [];

        this._length = array.length;
        array.forEach(e => {
            for(let i = 0; (e.type >> i) > 0; ++i){
                switch(e.type & (1 << i)){
                    case   FILE.IMAGE: loaded.push(this.image(e));   break;
                    case FILE.TEXTURE: loaded.push(this.texture(e)); break;
                }
            }
        });

        return Promise.all(loaded);

    };

}
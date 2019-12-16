/* Mini Map */

const markers = {
    
    RED: new L.Icon({
        iconUrl: "https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|e85141&chf=a,s,ee00FFFF",
        shadowUrl: "http://www.geocodezip.com/mapIcons/marker_shadow.png",
        iconSize: [21, 34],
        iconAnchor: [11, 34],
        popupAnchor: [1, -34],
    }),

    BLUE: new L.Icon({
        iconUrl: "https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|abcdef&chf=a,s,ee00FFFF",
        shadowUrl: "http://www.geocodezip.com/mapIcons/marker_shadow.png",
        iconSize: [21, 34],
        iconAnchor: [11, 34],
        popupAnchor: [1, -34],
    })

}

//https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|abcdef&chf=a,s,ee00FFFF
class MiniMap{

    constructor(app){
        if(!!MiniMap.instance) return MiniMap.instance;

        MiniMap.instance = this;

        this._app = app;

        const map = L.map('map-preview', {
            crs: L.CRS.Simple,
            minZoom: 0,
            maxZoom: 2,
            zoomControl: false
        });

        const bounds = [[0,0], [513, 634]];
        const image = L.imageOverlay('./assets/map.jpg', bounds).addTo(map);
        map.fitBounds(bounds);

        this._image = image;
        this._map   = map;
        this._group = null;

        /* Initial map "entrada_este" */
        this._current_node = entrada_este;

        return this;
    }

    init(){
        this.move(this._current_node);

        /* Events */
        document.getElementById("map-button").addEventListener('click', _ => {
      
            this._app.popup_open("#map-preview");  
            this.draw_markers();
            
        });


        // Settings > Privacy > Location Services > Safari
        if (!navigator.geolocation){
            document.getElementById("localize").classList.add("none");
        }else{
            document.getElementById("localize").addEventListener("click", e => this.gps_localization(e));
        }
    };
    
    /*
        Finds the current node from the gps localization
    */
    gps_localization(){

        navigator.geolocation.getCurrentPosition( 

            position => { // Success

                const {latitude, longitude} = position.coords;

                let distances = this.nodes().map(e => new Object({node: e, dst: e.distance(latitude, longitude)}));
                distances.sort((a, b) => a.dst - b.dst);
                
                if(distances[0].dst >= 1.){ // > 1.km
                    modal(Modal.ERROR, "Exterior de la Alhambra", "¡Usted está fuera del recinto de la Alhambra!");
                }else{
                    this.move(distances[0].node);
                    this._app.popup_close("#map-preview");
                }

            },

            _ => {// Error
                modal(Modal.ERROR, "No se pudo acceder a su localización", "Ha ocurrido un error, quizás no tenga permiso o deba recargar la página."); 
            },

            // Options
            {
                enableHighAccuracy: true
            } 
        );

    }

    /*
        Finds from all nodes, the one with with the same id
    */
    find_node(id){
        return this.nodes().find(e => e.id() === id);
    };

    /* 
        Returns all the node from the graph, starting from current_node
    */
    nodes(){

        const nodes = [];

        const visited = new Map();
        const stack = [this._current_node];
        do{
            
            let current = stack.pop();

            nodes.push(current);
            visited.set(current.id(), 1);
            current._connections.forEach(connection => {
                const neighbour = connection._connection_b;
                if(!visited.has(neighbour.id())){
                    visited.set(neighbour.id(), 1);
                    stack.push(neighbour);
                }
            });

        }while(stack.length > 0);

        return nodes;

    };

    /*
        Moves to new node and change the scene, you could pass
        String : It finds a Object Class Node with that id
        Node : Object Class to move
    */
    move(v){
        if(v === null) return;

        if(typeof v === 'string'){
            v = this.find_node(v);
        }

        this._current_node = v;
        this._app._renderer.change_scene();
    
    };

    /*
        Returns Current Node
    */
    current(){ return this._current_node; };
    
    /*
        Remove previous grouod and
        draw all markers and path into a the new group
        finally it prints it into the map
    */
    draw_markers(){

        if(this._group != null){
            this._map.removeLayer(this._group);
        }

        const nodes = this.nodes();
        const group = L.layerGroup();
        nodes.forEach(node => {

            L.marker(node.get_marker(), {icon: markers[node.id() === this._current_node.id() ? "RED" : "BLUE"]})
            .bindTooltip(node.title(), {permanent: true})
            .on("click", _ => {

                modal(Modal.BOTH, "Localización", `¿Quiéres moverte a ${ node.title() }?`, _ => {
                    
                    this.move(node);
                    this._app.popup_close("#map-preview");

                });

            })
            .addTo(group);
            node._connections.forEach(connection => {
                
                const neighbour = connection._connection_b;
                L.polyline([node.get_marker(), neighbour.get_marker()]).addTo(this._map);

            });

        });

        this._group = group;
        this._group.addTo(this._map);
    };

};

    
/* MAP */

class Node{
    constructor(title, id){
        this._connections = [];
        this._title = title;
        this._id = id;
        
        this._marker = null;
        this._gps = null;
        
        this._items = [];
    };

    /* Add Item to Items List */
    add(item){ this._items.push(item); };
    /* Getter/Setter GPS  */
    set_gps(lat, lng){ this._gps = {lat: lat, lng: lng}; } 
    get_gps(){ return this._gps; };
    
    /* Getter/Setter Marker */
    set_marker(x, y){ this._marker = [y, x]; };
    get_marker(){ return this._marker; };

    /* Returns the Id of a node */
    id(){ return this._id; };

    /* Returns the title */
    title(){ return this._title; };

    /*
        returns distance from the current Node 
        to a given latitude / longitude
    */
    distance(lat, long){

        const rad = Math.PI / 180.;
        const lt1 = lat * rad, lt2 = this._gps.lat * rad;
        const dlt = lt1 - lt2;
        const dln = (long - this._gps.lng) * rad;
        const dst = 1. - Math.cos(dlt) + Math.cos(lt1) * Math.cos(lt2) * (1. - Math.cos(dln));
        // 2 * R; R = 6371 km Radio Terrestre
        const distance = 12742 * Math.asin(Math.sqrt(0.5 * dst));

        return distance;
        
        /* 
            Optimization: As gps points are too near from one another, in a very big sphere, they can be approximated by euclidean distance
            By empyrical we get around ~0.01 Difference between one method and the other

            return Math.sqrt(Math.pow(lat - this._gps.lat, 2) + Math.pow(long - this._gps.lng, 2));
        */
    };

    /* Connects to another node by an angle
        0 -> North (000 - 090) degrees
        1 -> East  (090 - 180) degrees
        2 -> South (180 - 270) degrees
        3 -> West  (270 - 360) degrees
    */
    connect(node, angle){
        
        let id0 = angle2ID(angle), id1 = angle2ID((angle + 180) % 360);
        this._connections[id0] = new Connection(this, node, id0);
        node._connections[id1] = new Connection(node, this, id1);

    };

    /* Returns all the connections */
    connections(){ return this._connections; };
    /* Returns the node from an angle (direction) */
    to(id){ return this._connections[id].to(); }
}

class Connection{
    constructor(a, b, angle){
        this._connection_a = a;
        this._connection_b = b;
        this._angle_walk = angle;
    };

    /* Returns From Node */
    from(){ return this._connection_a; };
    /* Return To Node */
    to(){ return this._connection_b; };
    /* Return Angle From - To */
    angle(){ return this._angle_walk; };
}
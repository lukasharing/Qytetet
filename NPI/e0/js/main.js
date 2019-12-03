
const fingers_dragging = [];

const start_dragging_finger = function(e, i){

    e.target.classList.add("dragging");
    fingers_dragging.push({elm: e.target, id: i});

};

const end_dragging_finger = function(e, i){

    e.target.classList.remove("dragging");
    fingers_dragging.splice(fingers_dragging.indexOf(e => e.id === i), 1);    

};

let created = false;
const html_area = document.getElementById("touches");

html_area.addEventListener("touchstart", function(e){

    if(!created){
        
        html_area.innerHTML = '';

        Array.from(e.touches).forEach((touch, i) => {
            
            const finger = document.createElement("div");
            finger.classList.add("finger");
            finger.style.left = `${touch.clientX}px`;
            finger.style.top = `${touch.clientY}px`;
            finger.addEventListener("touchstart", e => start_dragging_finger(e, i), false);
            finger.addEventListener("touchend", e => end_dragging_finger(e, i), false);

            html_area.appendChild(finger);

        });
    
    }

}, false);

html_area.addEventListener("touchmove", function(e){

    if(created){

        html_area.classList.remove("removeFinger");

        fingers_dragging.forEach((finger, i) => {

            finger = finger.elm;

            finger.classList.remove("remove");

            const x = e.touches[i].clientX;
            const y = e.touches[i].clientY;

            finger.style.left = `${x}px`;
            finger.style.top = `${y}px`;
            
            const hw = window.innerWidth * .5, hh = window.innerHeight * .5;
            const dx = Math.abs(x - hw), dy = Math.abs(y - hh);

            if(dx >= hw - 50. || dy >= hh - 50.){
                finger.classList.add("remove");
                html_area.classList.add("removeFinger"); 
            }

        });
    }

}, false);

html_area.addEventListener("touchend", function(){

    html_area.classList.remove("removeFinger");
    Array.from(document.querySelectorAll("#touches > .finger.remove")).forEach(e => e.remove());
    created = html_area.children.length > 0;

}, false);
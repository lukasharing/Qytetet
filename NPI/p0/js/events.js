/* EVENTS */

const Modal = {
  ACCEPT: 0,
  BOTH: 1,
  ERROR: -1
};

const accept = document.getElementById("modal-accept");
const cancel = document.getElementById("modal-cancel");

accept.addEventListener("click", _ => document.getElementById("modal-area").classList.add("hide"), false);
cancel.addEventListener("click", _ => document.getElementById("modal-area").classList.add("hide"), false);

/* Stores last events to clean them form the Event Queue when new are set */
let last_eventAccept = null;
let last_eventCancel = null;

/* Open new typed modal:
  1. Accept : Accept Button
  2. Error: Close Button
  3. BOTH: Accept and Close Button
  Custom title and description
  With callbacks for each type of events
*/
const modal = function(type = Modal.ACCEPT, title = "", description = "", event_accept = null, event_cancel = null){
  
  /* HTML */
  document.getElementById("modal-area").className = '';
  document.querySelector("#modal > p").innerHTML = description;
  document.querySelector("#modal > h3").innerText = title;

  /* Events */
  accept.removeEventListener("click", last_eventAccept, false);
  cancel.removeEventListener("click", last_eventCancel, false);
  accept.addEventListener("click", event_accept, false);
  cancel.addEventListener("click", event_cancel, false);

  /* Hide buttons depending on the modal type */
  switch(type){
    case Modal.ACCEPT:
      document.getElementById("modal-area").classList.add("accept");
    break;
    case Modal.BOTH:
      document.getElementById("modal-area").classList.add("both");
    break;
    
    case Modal.ERROR:
      document.getElementById("modal-area").classList.add("error");
    break;
  };

  /* Store current events */
  last_eventAccept = event_accept;
  last_eventCancel = event_cancel;

};

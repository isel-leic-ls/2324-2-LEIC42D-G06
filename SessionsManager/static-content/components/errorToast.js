import { div } from '../tags.js';


export function errorToast(message) {
    const overlay =
      div({ class: 'popupOverlay' },
          div({ class: 'popup' },
              div({ class: 'content' }, message)
          )
      );
    const mainContent = document.getElementById('mainContent');
    mainContent.appendChild(overlay);

    const timeout = 1500; //Remove popup after timeout
    setTimeout(() => { mainContent.removeChild(overlay); }, timeout);
}
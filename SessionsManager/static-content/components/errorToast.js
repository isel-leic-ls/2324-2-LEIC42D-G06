// Try to get this to work with our DSL
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

    // Remove popup after timeout
    const timeout = 1500;
    setTimeout(() => { mainContent.removeChild(overlay); }, timeout);
}
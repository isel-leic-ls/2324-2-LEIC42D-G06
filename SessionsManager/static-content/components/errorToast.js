import { div } from "../tags.js";

// Try to get this to work with our DSL
export function errorToast(message) {

  // Create overlay
  const mainContent = document.getElementById('mainContent');
  const overlay = document.createElement('div');
  overlay.className = 'popupOverlay';

  // Create popup
  const popup = document.createElement('div');
  popup.className = 'popup';

  // Create content element
  const content = document.createElement('div');
  content.className = 'content';
  content.innerText = message;

  // Append content to popup
  popup.appendChild(content);

  // Append popup to overlay
  overlay.appendChild(popup);

  // Append overlay to body
  mainContent.appendChild(overlay);

  // Remove popup after 4 seconds
  setTimeout(() => {
    mainContent.removeChild(overlay);
  }, 2500);
}
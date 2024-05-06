import { div, button } from '../tags.js';

export function closeModal() {
    var modal = document.querySelector('.modal');
    modal.remove();
}

export function openModal(content) {
    const modal = div({ class: 'modal' },
        div({ class: 'modal-content' },
            content,
            button({ class: 'cancel-btn', onClick: closeModal }, "Cancel")
        )
    );
    document.body.appendChild(modal);
    modal.style.display = 'block';
}
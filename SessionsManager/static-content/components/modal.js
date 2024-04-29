export function closeModal() {
    var modal = document.querySelector('.modal');
    modal.remove();
  }

export function openModal(content) {
  var modal = document.createElement('div');
  modal.classList.add('modal');

  var modalContent = document.createElement('div');
  modalContent.classList.add('modal-content');

  // add the content to the modal
  modalContent.appendChild(content);

  var cancelButton = document.createElement('button');
  cancelButton.innerHTML = 'Cancel';
  cancelButton.classList.add('cancel-btn');
  cancelButton.addEventListener('click', closeModal);

  modalContent.appendChild(cancelButton);
  modal.appendChild(modalContent);
  document.body.appendChild(modal);
  modal.style.display = 'block';
}
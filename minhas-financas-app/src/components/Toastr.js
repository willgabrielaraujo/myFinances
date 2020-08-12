import toastr from "toastr"

toastr.options = {
  "closeButton": true,
  "debug": false,
  "newestOnTop": false,
  "progressBar": true,
  "positionClass": "toast-top-right",
  "preventDuplicates": true,
  "onclick": null,
  "showDuration": "300",
  "hideDuration": "1000",
  "timeOut": "5000",
  "extendedTimeOut": "1000",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}

export function showAlert(title, message, type) {
  toastr[type](message, title)
}

export function errorAlert(message) {
  showAlert('Error', message, 'error')
}

export function successAlert(message) {
  showAlert('Success', message, 'success')
}

export function warningAlert(message) {
  showAlert('Warning', message, 'warning')
}
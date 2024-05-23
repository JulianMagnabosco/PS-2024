import { Injectable } from '@angular/core';
import Swal from "sweetalert2";

const cSwal = Swal.mixin({
  customClass: {
    actions: "d-flex w-100 justify-content-evenly",
    confirmButton: "btn btn-primary px-4",
    cancelButton: "btn btn-danger px-4"
  },
  buttonsStyling: false
})
export function cAlert(type:'error'|'success',text:string){
  return cSwal.fire({
    title: type=='error'? 'Error':'Éxito',
    text: text,
    icon: type
  });
}
export function cConfirm(text:string){
  return cSwal.fire({
    title: "Aviso!",
    text: text,
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Si",
    cancelButtonText: "No"
  });
}

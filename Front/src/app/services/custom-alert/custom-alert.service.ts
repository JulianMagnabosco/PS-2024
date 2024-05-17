import { Injectable } from '@angular/core';
import Swal from "sweetalert2";

const cSwal = Swal.mixin({
  customClass: {
    confirmButton: "btn btn-primary",
    cancelButton: "btn btn-danger"
  },
  buttonsStyling: false
})
export function cAlert(type:'error'|'success',text:string){
  return cSwal.fire({
    title: type.toUpperCase(),
    text: text,
    icon: type
  });
}

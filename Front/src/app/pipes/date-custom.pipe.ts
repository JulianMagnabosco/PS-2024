import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateCustom',
  standalone: true
})
export class DateCustomPipe implements PipeTransform {

  transform(value: number, ...args: unknown[]): unknown {
    let months:string[]=["Enero",
      "Febrero",
      "Marzo",
      "Abril",
      "Mayo",
      "Junio",
      "Julio",
      "Agosto",
      "Septiembre",
      "Octubre",
      "Noviembre",
      "Diciembre",]

    return months[value-1];
  }

}

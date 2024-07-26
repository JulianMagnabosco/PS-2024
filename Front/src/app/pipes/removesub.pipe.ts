import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'removesub',
  standalone: true
})
export class RemovesubPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): unknown {
    return value.replace("_"," ");
  }

}

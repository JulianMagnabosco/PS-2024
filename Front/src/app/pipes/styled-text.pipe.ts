import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'styledText',
  standalone: true
})
export class StyledTextPipe implements PipeTransform {

  transform(value: string, ...args: number[]): unknown {

    return value.replace(/(^[\s]+)/g,"")
      .replace(/([\s]+$)/g,"")
      .replace(/\r\n|\r|\n/g, '<br>')
      .replace(/(\#\w+)/g,
      '<span class="fw-bold">$1</span>'
    );
  }

}

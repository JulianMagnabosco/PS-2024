import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  loading = false;
  constructor() { }
  start(){this.loading=true}
  end(){this.loading=false}
}

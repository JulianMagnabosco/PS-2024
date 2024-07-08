import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {LoadingService} from "../loading/loading.service";
import {EChartsOption} from "echarts";

@Injectable({
  providedIn: 'root'
})
export class StadisticsService {

  private baseUrl = "api/stats/";
  coolTheme:EChartsOption  = {
    color: [
      '#fc7300',
      '#fc7300',
      '#ff9100',
      '#ffbf70',
      '#fae563',
      '#ffc400',
      '#d52e2e',
      '#fc8f00',
      '#ff2a04',
      '#fdc433',
    ],

    title: {
      // fontWeight: 'normal',
      // color: '#8d0000',
    },

    visualMap: {
      color: ['#fc7300', '#a2d4e6'],
    },

    toolbox: {
      // color: ['#fc7300', '#fc7300', '#fc7300', '#fc7300'],
    },

    tooltip: {
      backgroundColor: 'rgba(0,0,0,0.5)',
      axisPointer: {
        // Axis indicator, coordinate trigger effective
        type: 'line', // The default is a straight line： 'line' | 'shadow'
        lineStyle: {
          // Straight line indicator style settings
          color: '#fc7300',
          type: 'dashed',
        },
        crossStyle: {
          color: '#fc7300',
        },
        shadowStyle: {
          // Shadow indicator style settings
          color: 'rgba(200,200,200,0.3)',
        },
      },
    },

    // Area scaling controller
    dataZoom: {
      // dataBackgroundColor: '#eee', // Data background color
      fillerColor: 'rgba(144,197,237,0.2)', // Fill the color
      // handleColor: '#fc7300', // Handle color
    },

    timeline: {
      lineStyle: {
        color: '#fc7300',
      },
      controlStyle: {
        color: '#fc7300',
        borderColor: 'fc7300',
      },
    },

    candlestick: {
      itemStyle: {
        color: '#fc7300',
        color0: '#a2d4e6',
      },
      lineStyle: {
        width: 1,
        color: '#fc7300',
        color0: '#a2d4e6',
      },
      areaStyle: {
        color: '#fc7300',
        color0: '#ff2a04',
      },
    },

    chord: {
      padding: 4,
      itemStyle: {
        color: '#fc7300',
        borderWidth: 1,
        borderColor: 'rgba(128, 128, 128, 0.5)',
      },
      lineStyle: {
        color: 'rgba(128, 128, 128, 0.5)',
      },
      areaStyle: {
        color: '#ff2a04',
      },
    },

    graph: {
      itemStyle: {
        color: '#fc7300',
      },
      linkStyle: {
        color: '#2a2073',
      },
    },

    map: {
      itemStyle: {
        color: '#c12e34',
      },
      areaStyle: {
        color: '#ddd',
      },
      label: {
        color: '#c12e34',
      },
    },

    gauge: {
      axisLine: {
        lineStyle: {
          color: [
            [0.2, '#dddddd'],
            [0.8, '#fc7300'],
            [1, '#f5ccff'],
          ],
          width: 8,
        },
      },
    },};
  constructor(private client: HttpClient, private loadService:LoadingService) {
  }

  get loading(){
    return this.loadService.loading
  }

  getUserStadistics(year: any):Observable<any>{
    return this.client.get(this.baseUrl + "users/"+year);
  }
  getPubsStadistics(year: any, month:any):Observable<any>{
    return this.client.get(this.baseUrl + "pubs/"+year+"/"+month);
  }
  getSellsStadistics(type:string,date1: string,date2: string):Observable<any>{
    return this.client.get(this.baseUrl + "sells?date1="+date1+"&date2="+date2+"&type="+type);
  }
}

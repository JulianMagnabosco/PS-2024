import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxEchartsDirective, ThemeOption} from "ngx-echarts";
import type {EChartsOption} from "echarts";
import {Subscription} from "rxjs";
import {StadisticsService} from "../../../services/stadistics/stadistics.service";

@Component({
  selector: 'app-user-stadistics',
  templateUrl: './user-stadistics.component.html',
  styleUrl: './user-stadistics.component.css'
})
export class UserStadisticsComponent implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  years:number[]=[2023];

  options:EChartsOption={};

  constructor(public service: StadisticsService) {
  }
  ngOnInit(): void {
    let yeardiff = new Date().getFullYear()-this.years[0];
    let i=1;
    for (let y in [].constructor(yeardiff)){
      this.years.push(this.years[0]+i);
      i++;
    }
    this.charge(2023);
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  charge(year:number) {
    const xAxisData:string[] = [];
    const data1:number[]  = [];
    const data2:number[]  = [];

    this.subs.add(
      this.service.getUserStadistics(year.toString()).subscribe({
        next: value => {
          for (let i = 0; i < value["stats"][0]["series"].length; i++) {
            xAxisData.push(value["stats"][0]["series"][i]["name"]);
            data1.push(value["stats"][0]["series"][i]["value"]);
            data2.push(value["stats"][1]["series"][i]["value"]);
          }
        }
      })
    )



    this.options = {
      // tooltip: {
      //   // formatter: params => {
      //   //   return '<div style="width:300px; height: 400px">working</div>';
      //   // },
      //   formatter: this.getTooltipFormatter(),
      //   confine: true,
      // },
      legend: {
        align: 'auto',
        bottom: 10,
        data: ['Usuarios diarios', 'Usuarios totales'],
      },
      tooltip: {
        trigger: 'item',
        formatter: '<div class="text-white">{a} <br/>{b} : {c}</div>',
      },
      xAxis: {
        data: xAxisData,
        axisLabel: {
          inside: false,
          color: '#000000',
        },
        axisTick: {
          show: false,
        },
        axisLine: {
          show: false,
        },
        z: 10,
      },
      yAxis: {},
      series: [
        {
          type: 'line',
          name: 'Usuarios diarios',
          data: data1,
        },
        {
          type: 'line',
          name: 'Usuarios totales',
          data: data2,
        },
      ],
    };
  }




}

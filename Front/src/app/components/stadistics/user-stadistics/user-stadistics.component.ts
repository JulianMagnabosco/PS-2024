import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxEchartsDirective, ThemeOption} from "ngx-echarts";
import type {EChartsOption} from "echarts";
import {Subscription} from "rxjs";
import {StadisticsService} from "../../../services/stadistics/stadistics.service";

@Component({
  selector: 'app-user-stadistics',
  standalone: true,
    imports: [
        NgxEchartsDirective
    ],
  templateUrl: './user-stadistics.component.html',
  styleUrl: './user-stadistics.component.css'
})
export class UserStadisticsComponent implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  options:any;

  constructor(public service: StadisticsService) {
  }
  ngOnInit(): void {
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  charge() {
    const xAxisData = [];
    const data1 = [];
    const data2 = [];

    for (let i = 0; i < 5; i++) {
      xAxisData.push('category' + i);
      data1.push((Math.sin(i / 5) * (i / 5 - 10) + i / 6) * 5);
      data2.push((Math.cos(i / 5) * (i / 5 - 10) + i / 6) * 5);
    }

    this.options = {
      // tooltip: {
      //   // formatter: params => {
      //   //   return '<div style="width:300px; height: 400px">working</div>';
      //   // },
      //   formatter: this.getTooltipFormatter(),
      //   confine: true,
      // },
      xAxis: {
        data: xAxisData,
      },
      yAxis: {},
      series: [
        {
          type: 'line',
          barCategoryGap: '0%',
          data: data1,
        },
        {
          type: 'line',
          barCategoryGap: '0%',
          data: data2,
        },
      ],
    };
  }




}

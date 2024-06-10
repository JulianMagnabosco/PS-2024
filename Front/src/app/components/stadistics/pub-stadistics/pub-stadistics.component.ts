import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import type {EChartsOption} from "echarts";
import {StadisticsService} from "../../../services/stadistics/stadistics.service";

@Component({
  selector: 'app-pub-stadistics',
  templateUrl: './pub-stadistics.component.html',
  styleUrl: './pub-stadistics.component.css'
})
export class PubStadisticsComponent implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  months:number[]=[];
  years:number[]=[];

  selectedYear:number=2023;
  selectedMonth:number=1;

  nodata:string='c';

  options:EChartsOption={};

  constructor(public service: StadisticsService) {
  }
  ngOnInit(): void {
    let m=1
    for (let i of [].constructor(12)){
      this.months.push(m as number);
      m++
    }

    let y=2023
    let yeardiff = new Date().getFullYear()-y;
    for (let i of [].constructor(yeardiff+1)){
      this.years.push(y as number);
      y++
    }
    // this.selectedYear=new Date().getFullYear()
    // this.selectedMonth=new Date().getMonth()
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  charge() {
    const xAxisData:string[] = [];
    const data1:any[]  = [];

    this.subs.add(
      this.service.getPubsStadistics(this.selectedYear.toString(),this.selectedMonth.toString())
        .subscribe({
        next: value => {
          if(value["nodata"]){
            this.nodata="y"
            return
          }
          this.nodata="n"
          for (let i = 0; i < value["stats"][0]["series"].length; i++) {
            xAxisData.push(value["stats"][0]["series"][i]["name"]);
            data1.push(value["stats"][0]["series"][i]);
          }

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
              data: xAxisData,
            },
            tooltip: {
              trigger: 'item',
              formatter: '<div class="text-white">{b} : {c} ({d}%)</div>',
            },

            series: [
              {
                name: 'area',
                type: 'pie',
                radius: '55%',
                roseType: 'radius',
                data: data1,
              },
            ],
          };
        }
      })
    )

  }
}

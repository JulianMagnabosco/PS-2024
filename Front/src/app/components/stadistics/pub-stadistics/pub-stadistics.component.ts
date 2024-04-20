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

  months:{year:number,month:number}[]=[{year:2023,month:1}];
  selected:{year:number,month:number}=this.months[0];

  nodata:string='c';

  options:EChartsOption={};

  constructor(public service: StadisticsService) {
  }
  ngOnInit(): void {
    let yeardiff = new Date().getFullYear()-this.months[0].year;
    console.log(yeardiff);
    let i=0;
    for (let y of [].constructor(yeardiff+1)){
      let j=0;
      for (let m of [].constructor(12)){
        let newval = {
          year: this.months[0].year +i,
          month:this.months[0].month+j
        }
        j++;
        if(newval.month == this.months[0].month && newval.year == this.months[0].year) {
          continue
        };
        this.months.push(newval);
      }
      i++;
    }
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  charge() {
    const xAxisData:string[] = [];
    const data1:any[]  = [];

    console.log(this.selected)
    this.subs.add(
      this.service.getPubsStadistics(this.selected.year.toString(),this.selected.month.toString())
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
                radius: [30, 110],
                roseType: 'area',
                data: data1,
              },
            ],
          };
        }
      })
    )

  }
}

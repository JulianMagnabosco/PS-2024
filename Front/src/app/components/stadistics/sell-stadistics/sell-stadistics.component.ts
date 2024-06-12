import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import type {EChartsOption} from "echarts";
import {StadisticsService} from "../../../services/stadistics/stadistics.service";

@Component({
  selector: 'app-sell-stadistics',
  templateUrl: './sell-stadistics.component.html',
  styleUrl: './sell-stadistics.component.css'
})
export class SellStadisticsComponent implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  maxDate:string="";
  minDate:string="";

  firstDate: string ;
  lastDate: string ;

  nodata:string="c";

  options:EChartsOption={};

  constructor(public service: StadisticsService) {
    let datenow= new Date(Date.now());
    this.lastDate= datenow.toISOString().split("T")[0]
    datenow.setDate(datenow.getDate()-90)
    this.firstDate= datenow.toISOString().split("T")[0]
    this.onChange()
  }
  ngOnInit(): void {
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  onChange(){
    let l = new Date(this.lastDate);
    l.setDate(l.getDate()-180)
    this.minDate=l.toISOString().split("T")[0]

    l = new Date(this.firstDate);
    l.setDate(l.getDate()+180)
    this.maxDate=l.toISOString().split("T")[0]
  }

  charge() {
    let firstDate=this.firstDate+"T00:00:01";
    let lastDate=this.lastDate+"T23:59:59";

    const xAxisData:string[] = [];
    const data1:number[]  = [];

    this.subs.add(
      this.service.getSellsStadistics(firstDate,lastDate).subscribe({
        next: value => {

          if(value["nodata"]){
            this.nodata="y"
            return
          }
          this.nodata="n"
          for (let i = 0; i < value["stats"][0]["series"].length; i++) {
            xAxisData.push(value["stats"][0]["series"][i]["name"]);
            data1.push(value["stats"][0]["series"][i]["value"]);
          }

          this.options = {

            // tooltip: {
            //   // formatter: params => {
            //   //   return '<div style="width:300px; height: 400px">working</div>';
            //   // },
            //   formatter: this.getTooltipFormatter(),
            //   confine: true,
            // },
            tooltip: {
              trigger: 'item',
              formatter: '<div class="text-white"> {b} : {c} Ventas</div>',
            },
            xAxis: {
              data: xAxisData,
              axisLabel: {
                inside: false,
                rotate: 45,
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
            yAxis: {
            },
            series: [
              {
                type: 'bar',
                name: 'Cantidad',
                data: data1,
                barWidth: '50%',
              },
            ],
          };
        }
      })
    )
  }
}

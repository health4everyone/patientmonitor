!function(i){
	"use strict";
	function e(){
		this.$body=i("body"),
		this.charts=[]
	}
	e.prototype.initCharts=function(){
		window.Apex={
			chart:{
				parentHeightOffset:0,
				toolbar:{
					show:!1
				}
			},
			grid:{
				padding:{
					left:0,
					right:0
				}
			},
			colors:["#727cf5","#0acf97","#fa5c7c","#ffbc00"]
		};

		var e=new Date,
		t=function(e,t){
			var ss = new Date();
			ss.setDate(ss.getDate()-14);
			for(var a=new Date(t,e,1),o=[],r=0;a.getMonth()===e&&r<15;){
				var s=new Date(a);
				o.push(ss.getDate()+" "+ss.toLocaleString("en-us",{month:"short"})),
				a.setDate(a.getDate()+1),
				ss.setDate(ss.getDate()+1)
				r+=1
			}
			return o
		}
		
		(e.getMonth()+1,e.getFullYear()),a=["#727cf5","#0acf97","#fa5c7c","#ffbc00"],
		o=i("#values-received-overview").data("colors");
		o&&(a=o.split(","));
		var valuesCategories=[];
		var valuesSeries=[];
		var r={
			chart:{
				height:309,
				type:"area"
			},
			dataLabels:{enabled:!1},
			stroke:{curve:"smooth",width:4},
			series:[{name:"Values received"}],
			zoom:{enabled:!1},
			legend:{show:!1},
			colors:a,
			xaxis:{type:"string",tooltip:{enabled:!1},axisBorder:{show:!1},labels:{}},
			yaxis:{
				labels:{
					formatter:function(e){
						return e;
					},
					offsetX:-15
				}
			},
			fill:{type:"gradient",gradient:{type:"vertical",shadeIntensity:1,inverseColors:!1,opacityFrom:.45,opacityTo:.05,stops:[45,100]}}
		};
		var chartV = new ApexCharts(document.querySelector("#values-received-overview"),r);
		chartV.render();
		$.getJSON('/dashboard/data', function(dt) {
			chartV.updateSeries([{
				data: dt.chartData 
			}]);
			i("#total-values-count").text(dt.totalValues)
			var arrowClass='mdi-arrow-up-bold';
			if(dt.dailyValuesPercent<0){
				i("#daily-values-percent").removeClass("text-success");
				arrowClass='mdi-arrow-down-bold';
				i("#daily-values-percent").addClass("text-danger");
			}
			i("#daily-values-percent").html("<span class=\"mdi "+arrowClass+"\"></span> "+dt.dailyValuesPercent+"%</span>")
			i("#total-users-count").text(dt.totalUsers);
			i("#total-devices-count").text(dt.totalDevices);
		});

},
e.prototype.init=function(){
	i("#dash-daterange").daterangepicker({singleDatePicker:!0}),
	this.initCharts(),
	window.setInterval(function(){
		var e=Math.floor(600*Math.random()+150);
		i("#active-users-count").text(e),
		i("#active-views-count").text(Math.floor(Math.random()*e+200))
	},2e3)
},
i.AnalyticsDashboard=new e,
i.AnalyticsDashboard.Constructor=e
}(window.jQuery),
function(){
	"use strict";
	window.jQuery.AnalyticsDashboard.init()
}();
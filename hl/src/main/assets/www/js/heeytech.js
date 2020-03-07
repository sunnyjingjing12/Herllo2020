

$(function(){
	var androidData = my.getData();
	var obj = JSON.parse(androidData);
	var hosptail = obj.hosptail;
	var id = obj.id;
	var phone = obj.phone;
	$("#hosptialName").empty().text(hosptail);
	reloadUserBaseInfo(phone);
	reloadUserTests(id);
	reloadUserTrains(id);

});

//加载用户基本信息
function  reloadUserBaseInfo(phone){
	var url  = "http://www.heeytech.cn/Ls_Server_web/mstest/findSyntheticAnalysisByUser"
		$.ajax({
			url : url,
			type : "get",	
			contentType: "application/json; charset=utf-8",
			dataType:"jsonp",
			data:{
				phone:phone
			},
			jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
			success:function(data) {
				$("#userName").empty().text(data.userName);
			},
			error:function(data){
			}
		});
}


//加载用户测试记录
function  reloadUserTests(id){
	var firstMuscles = new Array();
	var secondMuscles = new Array();
	var testMusclesTime = new Array();
	var caseCode = "A";
	var maxPower = 0;
	var url  = "http://www.heeytech.cn/Ls_Server_web/mstest/findSyntheticAnalysisByTest"
		$.ajax({
			url : url,
			type : "get",	
			contentType: "application/json; charset=utf-8",
			dataType:"jsonp",
			data:{
				uId:id
			},
			jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
			success:function(data) {
				var datas = new  Array();
				datas= data;
				maxPower = datas[0].maxPower;
				caseCode = datas[0].caseName;

				for(var i = datas.length; i>0; i--){
					firstMuscles[datas.length-i] = datas[i-1].oneMuscle;
					secondMuscles[datas.length-i] = datas[i-1].twoMuscle;
					testMusclesTime[datas.length-i] = dateLong2String(datas[i-1].recordDate);
				}

				var lastChils = new Array();
				lastChils = datas[0].chlids;
				var lastlevelI = new  Array();
				var lastlevelII = new  Array();
				for(var j = 0 ; j < lastChils.length; j++) {
					lastlevelI[j] = lastChils[j].cOneMuscle == 0 ? 0 : lastChils[j].cOneMuscle > 0 ? -lastChils[j].cOneMuscle : lastChils[j].cOneMuscle;
					lastlevelII[j] =  lastChils[j].cTwoMuscle;
				}
				var firstMuscle = firstMuscles[firstMuscles.length-1];
				firstMuscle = firstMuscle == "0" ? "1" :firstMuscle;
				var secondMuscle = secondMuscles[secondMuscles.length-1];
				$("#userMaxPower").empty().text("最大肌力 : "+maxPower+"N");
				$("#trainCode").empty().text("训练方案 : "+caseCode+"方案");
				$("#firstMuscle").empty().text("第一肌力 : "+firstMuscle+"级");
				$("#secondMuscle").empty().text("第二肌力 : "+secondMuscle+"级");
				$("#nowTime").empty().text(getToday());
				var analysis_1 = datas[0].tester.split("；")[0]+"；"+datas[0].tester.split("；")[1]+"。";
				$("#analysisResult").empty().text("盆底肌最大肌力"+maxPower+"N；" + analysis_1+" ");
				$("#analysisResult1").empty().text("Ⅰ类肌肌力不足易引发压力性尿失禁，阴道松驰，盆腔器官脱垂，性功能障碍，反复泌尿感染，便秘等临床病症；Ⅱ类肌肌力不足易引发尿失禁，性冷淡和性体验下降，子宫等脏器脱垂，粪失禁等病症。");
				$("#analysisResult2").empty().text("肌力等级<=3级表明盆底肌明显异常。坚持主动盆底康复训练，有助于恢复盆底肌力水平。");
				switch(firstMuscle){
				case 1:
					$('#firstMuscleimg').attr('src',"./images/icon_leveli_1.png");
					break;
				case 2:
					$('#firstMuscleimg').attr('src',"./images/icon_leveli_2.png");
					break;
				case 3:
					$('#firstMuscleimg').attr('src',"./images/icon_leveli_3.png");
					break; 
				case 4:
					$('#firstMuscleimg').attr('src',"./images/icon_leveli_4.png");
					break;
				case 5:
					$('#firstMuscleimg').attr('src',"./images/icon_leveli_5.png");
					break;
				default:
					break;
				}

				switch(secondMuscle){
				case 1:
					$('#secondMuscleimg').attr('src',"./images/icon_levelii_1.png");
					break;
				case 2:
					$('#secondMuscleimg').attr('src',"./images/icon_levelii_2.png");
					break;
				case 3:
					$('#secondMuscleimg').attr('src',"./images/icon_levelii_3.png");
					break; 
				case 4:
					$('#secondMuscleimg').attr('src',"./images/icon_levelii_4.png");
					break;
				case 5:
					$('#secondMuscleimg').attr('src',"./images/icon_levelii_5.png");
					break;
				default: 
					break;
				}

				// 绘制最大肌力表
				var app1 = echarts.init(document.getElementById('main2'));

				option1 = {
						tooltip : {
							formatter: "{a} <br/>{b} : {c}N"
						},
						toolbox: {
							feature: {
								restore: {},
								saveAsImage: {}
							}
						},
//						lable:{
//					        normal:{
//					            textStyle:{
//					                fontsize:'20px'
//					            }
//					        }
//					    },
						series: [
						         {
						        	 name: '最大肌力',
						        	 type: 'gauge',
						        	 radius: '100%',
						        	 max:0,
						        	 detail: {formatter:'\n{value}N'},
						        	 data: [{value: maxPower}],
						        	 textStyle:{
							                fontsize:'12px'
							            }
						         }
						         ]
				};
				app1.setOption(option1);	

				//绘制最近一次肌力测试肌力等级

				var app = echarts.init(document.getElementById('main1'));
				app.title = '正负条形图';
				option = {
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
							"textStyle": {
								"fontSize": 12
							},
							data:['II类肌', 'I类肌']
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: '4%',
							top: '15%',
							containLabel: true
						},
						xAxis : [
						         {
						        	 type : 'value'
						         }
						         ],
						         yAxis : [
						                  {
						                	  type : 'category',
						                	  axisTick : {show: false},
						                	  data : ['1级','2级','3级','4级','5级']
						                  }
						                  ],
						                  series : [
						                            {
						                            	name:'I类肌',
						                            	type:'bar',
						                            	label: {
						                            		normal: {
						                            			show: true,
						                            			formatter: function (value) {
						                            				if(value.data<0){
						                            					return -value.data;
						                            				}
						                            			},
						                            		}
						                            	},
						                            	data:lastlevelI
						                            },
						                            {
						                            	name:'II类肌',
						                            	type:'bar',
						                            	stack: '总量',
						                            	label: {
						                            		normal: {
						                            			show: true,

						                            		}
						                            	},
						                            	data:lastlevelII
						                            }
						                            ]
				};
				app.setOption(option);

				//绘制肌力测试5次记录
				var myChart = echarts.init(document.getElementById('main'));
				myChart.title = '肌力测试5次记录';

				var colors = ['#5793f3', '#d14a61', '#675bba'];

				option = {
						color: colors,
						tooltip: {
							trigger: 'none',
							axisPointer: {
								type: 'cross'
							}
						},
						legend: {
							"textStyle": {
								"fontSize": 12
							},
							data:['I类肌', 'II类肌']
						},
						grid: {
							top: 35,
							bottom: 25
						},
						xAxis: [
						        {
						        	type: 'category',
						        	axisTick: {
						        		alignWithLabel: true
						        	},
						        	axisLine: {
						        		onZero: false,
						        		lineStyle: {
						        			color: colors[1]
						        		}
						        	},
						        	axisPointer: {
						        		label: {
						        			formatter: function (params) {
						        				return 'I 类肌  ' + params.value
						        				+ (params.seriesData.length ? '：' + params.seriesData[0].data : '')+"级";
						        			}
						        		}
						        	},
						        	data: testMusclesTime,
						        	axisLabel:{
						        		interval:0,//横轴信息全部显示
						        	}
						        },
						        {
						        	type: 'category',
						        	axisTick: {
						        		alignWithLabel: true
						        	},
						        	axisLine: {
						        		onZero: false,
						        		lineStyle: {
						        			color: colors[0]
						        		}
						        	},
						        	axisPointer: {
						        		label: {
						        			formatter: function (params) {
						        				return 'II 类肌  ' + params.value
						        				+ (params.seriesData.length ? '：' + params.seriesData[0].data : '')+"级";
						        			}
						        		}
						        	},
						        	data: testMusclesTime,
						        	show:false
						        }
						        ],
						        yAxis: [
						                {
						                	type: 'value'
						                }
						                ],
						                series: [
						                         {
						                        	 name:'I类肌',
						                        	 type:'line',
						                        	 xAxisIndex: 1,
						                        	 smooth: true,
						                        	 data: firstMuscles
						                         },
						                         {
						                        	 name:'II类肌',
						                        	 type:'line',
						                        	 smooth: true,
						                        	 data: secondMuscles
						                         }
						                         ]
				};
				myChart.setOption(option);
			},
			error:function(data){
			}
		});
}


function reloadUserTrains(id){
	var url  = "http://www.heeytech.cn/Ls_Server_web/mstest/findSyntheticAnalysisByTrain"

		$.ajax({
			url : url,
			type : "get",	
			contentType: "application/json; charset=utf-8",
			dataType:"jsonp",
			data:{
				uId:id
			},
			jsonp: "callback",  //传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名,默认为callback
			success:function(data) {
				var trainScore = new Array();
				var trainTime = new Array();
				var traindate = new Array();
				var datas = new Array();
				datas = data;
				for(var i = datas.length; i>0 ;i--) {
					var ts =  datas[i-1].trainScore > 100 ? 100 : datas[i-1].trainScore;
					trainScore[datas.length-i] = ts;
					trainTime[datas.length-i] = datas[i-1].trainTime;
					traindate[datas.length-i] = dateLong2String(datas[i-1].trainDate);
				}

				//绘制训练记录曲线
				var app2 = echarts.init(document.getElementById('main3'));
				app2.title = '训练趋势图';

				option2 = {
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						toolbox: {
							feature: {
								dataView: {show: false, readOnly: false},
								magicType: {show: true, type: ['line', 'bar']},
								restore: {show: true},
								saveAsImage: {show: false}
							}
						},
						legend: {
							"textStyle": {
								"fontSize": 12
							},
							data:['训练时间','训练分数']
						},
						grid:{
			                top:'25%',//距上边距
			              },
						
						xAxis: [
						        {
						        	type: 'category',
						        	data: traindate,
						        	axisPointer: {
						        		type: 'shadow'
						        	}
						        }
						        ],
						        yAxis: [
						                {
						                	type: 'value',
						                	name: '时间',
						                	min: 0,
						                	max: 25,
						                	interval: 2.5,
						                	axisLabel: {
						                		formatter: '{value} min'
						                	}
						                },
						                {
						                	type: 'value',
						                	name: '分数',
						                	min: 0,
						                	max: 100,
						                	interval: 10,
						                	axisLabel: {
						                		formatter: '{value}'
						                	}
						                }
						                ],
						                series: [

						                         {
						                        	 name:'训练时间',
						                        	 type:'bar',
						                        	 data:trainTime
						                         },
						                         {
						                        	 name:'训练分数',
						                        	 type:'line',
						                        	 yAxisIndex: 1,
						                        	 data:trainScore
						                         }
						                         ]
				};
				app2.setOption(option2);
			},
			error:function(data){

			}
		});
}

function getToday(){
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}

//在android代码中调用此方法
function showInfoFromJava(msg){
	alert("来自客户端的信息："+msg);
}

function dateLong2String(time){
	var date = new Date(time);
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	month = month < 10 ? "0"+month:month;
	day = day < 10 ? "0"+day:day;
	return year+"-"+month+"-"+day;
};

<template>
	<div class="mod-role">
		<el-row :gutter="40" class="panel-group" style="margin-left: 40px;">
			<div style="margin-bottom: 0px;">
			<el-col :xs="10" :sm="10" :lg="10" class="card-panel-col">
				<div class="card-panel" style="background-color: #6495ED;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-goods"></i>订单总数
						</div>
						<count-to :start-val="0" :end-val="count.sumOrderNum" :duration="2600"
							class="card-panel-num" />
					</div>
				</div>
			</el-col>
			
			<el-col :xs="10" :sm="10" :lg="10" class="card-panel-col">
				<div class="card-panel" style="background-color: #DAA520;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-marketing"></i>订单总额
						</div>
						<count-to :start-val="0" :end-val="count.sumOrderMoney" :duration="3000"
							class="card-panel-num" />
					</div>
				</div>
			</el-col>
			</div>
			
			<el-col :xs="5" :sm="5" :lg="5" class="card-panel-col">
				<div class="card-panel" style="background-color: #CD5C5C;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-goods"></i>今日订单总数
						</div>
						<count-to :start-val="0" :end-val="count.todayOrderNum" :duration="2600"
							class="card-panel-num" />
					</div>
				</div>
			</el-col>

			<el-col :xs="5" :sm="5" :lg="5" class="card-panel-col">
				<div class="card-panel" style="background-color: #FFD700;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-marketing"></i>今日订单总额
						</div>
						<count-to :start-val="0" :end-val="count.todayOrderMoney" :duration="3000"
							class="card-panel-num" />
					</div>
				</div>
			</el-col>

			<el-col :xs="5" :sm="5" :lg="5" class="card-panel-col">
				<div class="card-panel" style="background-color: #7FFF00;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-help"></i>昨日订单总数
						</div>
						<count-to :start-val="0" :end-val="count.yesterdayOrderNum" :duration="2600"
							class="card-panel-num" />
					</div>
				</div>
			</el-col>

			<el-col :xs="5" :sm="5" :lg="5" class="card-panel-col">
				<div class="card-panel" style="background-color: #FF1493;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-release"></i>昨日订单总额
						</div>
						<count-to :start-val="0" :end-val="count.yesterdayOrderMoney" :duration="3000"
							class="card-panel-num" />
					</div>
				</div>
			</el-col>

		</el-row>
		<div class="chart-container">
			<el-row>
				<el-col :span="24">
					<div id="chartColumn" style="width:100%; height:400px;"></div>
				</el-col>

				<el-col :span="24">
					<div id="chartLine" style="width:100%; height:400px;"></div>
				</el-col>

				<el-col :span="24">
					<div id="chartColumnFor" style="width:100%; height:400px;"></div>
				</el-col>
			</el-row>
		</div>
	</div>
</template>

<script>
	import CountTo from 'vue-count-to'
	import echarts from 'echarts'
	export default {
		components: {
			CountTo
		},
		data() {
			return {
				//统计数据
				count: {
					todayOrderNum: 1000,
					todayOrderMoney: 1000,
					yesterdayOrderNum: 1000,
					yesterdayOrderMoney: 1000,
					sumOrderNum: 1000,
					sumOrderMoney: 1000
				},
				dataList: [],
				//柱状图数据1
				myChart1Title: [],
				//柱状图数据2
				myChart1Num: [],
				//折线图数据1
				myChart2Title: [],
				//折线图数据2
				myChart2Num: [],
				//折线图数据3
				myChart2Money: [],
				//柱状图
				chartColumn: null,
				//折线图
				chartLine: null,

				hotelTypeMoney: [],
				//柱状图数据3
				chartColumnFor: null,
				//柱状图数据3
				myChartTitle3: [],
				//柱状图数据3
				myChartNum3: [],
				moneyNum: 0

			};
		},

		methods: {
			setChart1() {
				for (var i = 0; i < 7; i++) {
					this.myChart1Title[i] = this.dataList[i].dayTime;
					this.myChart1Num[i] = this.dataList[i].dayMoney;
				}
				this.drawColumnChart();
			},
			setChart2() {
				for (var i = 0; i < 7; i++) {
					this.myChart2Title[i] = this.dataList[i].dayTime;
					this.myChart2Num[i] = this.dataList[i].dayNum;
					this.myChart2Money[i] = this.dataList[i].dayMoney;
				}
				this.drawLineChart();
			},

			setChart3() {
				var forItemNum = this.moneyNum
				console.log(forItemNum)
				for (var i = 0; i < forItemNum; i++) {
					this.myChartTitle3[i] = this.hotelTypeMoney[i].title;
					this.myChartNum3[i] = this.hotelTypeMoney[i].hotelTypeMoney;
				}
				this.drawForItem3();
			},

			//柱状图
			drawColumnChart() {
				this.chartColumn = echarts.init(document.getElementById('chartColumn'))
				this.chartColumn.setOption({
					title: {
						text: '销量统计'
					},
					tooltip: {},
					xAxis: {
						data: this.myChart1Title
					},
					yAxis: {
						max: 10000,
						min: 0,
						type: 'value',
						minInterval: 10,
						//每次增加几个
						interval: 1000
					},
					series: [{
						name: '金额',
						type: 'bar',
						data: this.myChart1Num
					}]
				})
			},
			//折线图
			drawLineChart() {
				this.chartLine = echarts.init(document.getElementById('chartLine'))
				this.chartLine.setOption({
					title: {
						text: '销量统计'
					},
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['订单金额', '订单数量']
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: {
						data: this.myChart2Title
					},
					yAxis: {
						max: 10000,
						min: 0,
						type: 'value',
						minInterval: 10,
						//每次增加几个
						interval: 1000
					},
					series: [{
							name: '数量',
							type: 'line',
							data: this.myChart2Num
						},
						{
							name: '金额',
							type: 'line',
							data: this.myChart2Money
						},
					]
				})
			},
			//柱状图3
			drawForItem3() {
				this.chartColumnFor = echarts.init(document.getElementById('chartColumnFor'))
				this.chartColumnFor.setOption({
					title: {
						text: '分类销售'
					},
					tooltip: {},
					xAxis: {
						data: this.myChartTitle3
					},
					yAxis: {
						max: 10000,
						min: 0,
						type: 'value',
						minInterval: 10,
						//每次增加几个
						interval: 1000
					},
					series: [{
						name: '金额',
						type: 'bar',
						data: this.myChartNum3
					}]
				})
			},
			drawCharts() {
				this.setChart1()
				this.setChart2()
				this.setChart3()
			},
			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				this.$http({
					url: this.$http.adornUrl("/yx_order/order_count"),
					method: "get",
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						this.count.todayOrderNum = data.page.todayOrderNum;
						this.count.todayOrderMoney = data.page.todayOrderMoney;
						this.count.yesterdayOrderNum = data.page.yesterdayOrderNum;
						this.count.yesterdayOrderMoney = data.page.yesterdayOrderMoney;
						this.count.sumOrderNum = data.page.sumOrderNum;
						this.count.sumOrderMoney = data.page.sumOrderMoney;
						// this.dataList = data.page.orderCountList;
						data.page.orderCountList.forEach((item) => {
							this.dataList.push({
								dayNum: item.dayNum,
								dayTime: item.dayTime,
								dayMoney: item.dayMoney,
							});
						});
						this.moneyNum = 0;
						data.page.yxHotelTypeList.forEach((item) => {
							// console.log(item, 1110)
							this.hotelTypeMoney.push({
								title: item.title,
								hotelTypeMoney: item.hotelTypeMoney,
							});
							this.moneyNum++;
						});
						// console.log(data.page, 1212)
						this.drawCharts()
					}
					this.dataListLoading = false;
				});
			},
		},
		mounted: function() {
			this.getDataList();


		},
		updated: function() {
			this.getDataList();


		}
	};
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
	.panel-group {
		margin-top: 18px;

		.card-panel-col {
			margin-bottom: 32px;
		}

		.card-panel {
			height: 108px;
			cursor: pointer;
			font-size: 12px;
			position: relative;
			overflow: hidden;
			color: #FFFFFF;
			box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
			border-color: rgba(0, 0, 0, .05);
			background-color: #a1a3a6;
			border-radius: 10px;

			.icon-people {
				color: #40c9c6;
			}

			.icon-message {
				color: #36a3f7;
			}

			.icon-money {
				color: #f4516c;
			}

			.icon-shopping {
				color: #34bfa3
			}

			.card-panel-icon-wrapper {
				float: left;
				margin: 14px 0 0 14px;
				padding: 16px;
				transition: all 0.38s ease-out;
				border-radius: 6px;
			}

			.card-panel-icon {
				float: left;
				font-size: 48px;
			}

			.card-panel-description {

				font-weight: bold;
				margin: 26px;



				.card-panel-text {
					line-height: 18px;
					color: #FFFFFF;
					font-size: 16px;
					margin-bottom: 12px;
				}

				.card-panel-num {
					font-size: 20px;
				}
			}
		}
	}

	@media (max-width:550px) {
		.card-panel-description {
			display: none;
		}

		.card-panel-icon-wrapper {
			float: none !important;
			width: 100%;
			height: 100%;
			margin: 0 !important;

			.svg-icon {
				display: block;
				margin: 14px auto !important;
				float: none !important;
			}
		}
	}

	.chart-container {
		background-color: white;
		height: 100%;
		width: 100%;
	}

	.el-col {
		padding: 30px;
	}
</style>

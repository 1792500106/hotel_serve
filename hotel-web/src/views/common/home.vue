<template>
	<div class="mod-home">
		<el-row :gutter="40" class="panel-group">
			<el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
				<div class="card-panel" style="background-color: #0081FF;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-user-solid"></i>用户总数
						</div>
						<count-to :start-val="0" :end-val="count.sumUserNum" :duration="2600" class="card-panel-num" />
					</div>
				</div>
			</el-col>

			<el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
				<div class="card-panel" style="background-color: #b54334;">
					<div class="card-panel-description">
						<div class="card-panel-text">
							<i class="el-icon-s-order"></i>剩余房间
						</div>
						<count-to :start-val="0" :end-val="count.sumTaskNum" :duration="3000" class="card-panel-num" />
					</div>
				</div>
			</el-col>

		</el-row>

		<el-row :gutter="40" class="panel-group-banner">
			<el-col :xs="12" :sm="12" :lg="12" class="card-panel-banner">
				<el-carousel height="300px">
					<el-carousel-item v-for="item in bannerList" :key="item">
						<img :src="item.photos" alt="">
						
					</el-carousel-item>
				</el-carousel>
			</el-col>
			<el-col :xs="12" :sm="12" :lg="12" class="card-panel-banner">
				<el-card class="box-card" style="height:300px;">
					<div slot="header" class="clearfix">
						<div style="padding-bottom: 10px;">最新公告：{{notice.title}}</div>
						<div>发布时间：{{notice.createdTime}}</div>
					</div>
					<div  class="text item">
						{{notice.content }}
					</div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>
<script>
	import CountTo from 'vue-count-to'
	export default {
		components: {
			CountTo
		},
		data() {
			return {
				//统计数据
				count: {
					sumTaskNum: 1000,
					sumUserNum: 1000
				},
				bannerList: [],
				notice: {
					title: "",
					content: "",
					createdTime: "",
				}
			};
		},

		activated() {
			this.getCountData();
		},
		methods: {
			getCountData() {
				this.$http({
					url: this.$http.adornUrl(`/sys/user/get_user_count`),
					method: "get"
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						this.count.sumUserNum = data.count.sumUserNum;
						this.count.sumTaskNum = data.count.sumTaskNum;

						this.notice.title = data.count.noticeTile;
						this.notice.content = data.count.noticeContent;
						this.notice.createdTime = data.count.noticeTime;

						data.count.bannerList.forEach((item) => {
							this.bannerList.push({
								id: item.id,
								title: item.title,
								photos: item.photos,
							});
						});


					}
				});
			},
		},
	}
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
				margin-left: 40%;


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

	.el-carousel__item h3 {
		color: #475669;
		font-size: 14px;
		opacity: 0.75;
		line-height: 150px;
		margin: 0;
	}

	.el-carousel__item:nth-child(2n) {
		background-color: #99a9bf;
	}

	.el-carousel__item:nth-child(2n+1) {
		background-color: #d3dce6;
	}
</style>

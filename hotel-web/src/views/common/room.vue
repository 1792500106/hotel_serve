<template>
	<div class="site-wrapper">

		<div class="line">
			<el-menu :default-active="activeIndex2" class="el-menu-demo" mode="horizontal" @select="handleSelect"
				background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
				<el-menu-item index="1" @click="$router.push({ name: 'login' })" style="margin-left: 30px;">首页
				</el-menu-item>
				<el-menu-item index="2" @click="$router.push({ name: 'room' })">房间</el-menu-item>
				<el-menu-item index="3" @click="$router.push({ name: 'login-in' })"
					style="float: right; margin-right: 30px;">登录</el-menu-item>
			</el-menu>
		</div>
		<div class="content">
			<div class="mod-role" style="margin-left: 5%;">
				<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
					<el-form-item>
						<el-input v-model="dataForm.title" placeholder="请输入房间名称查询" clearable></el-input>
					</el-form-item>
					<el-form-item>
						<el-select v-model="dataForm.typeId" placeholder="类型" clearable>
							<el-option v-for="item in typeListReserve" :key="item.id" :label="item.title"
								:value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select v-model="dataForm.sort" placeholder="排序" clearable>
							<el-option label="价格升序" :value="1"></el-option>
							<el-option label="价格降序" :value="2"></el-option>
							<el-option label="热度" :value="3"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-button @click="getDataList()">查询</el-button>
					</el-form-item>
				</el-form>
				<el-row>
					<el-col :span="4" v-for="(o, index) in dataList" :key="o" class="homeMain ">
						<el-card :body-style="{ padding: '5px' }">
							<img :src="o.photos" class="image">
							<div style="padding: 14px;">
								<span>{{o.homeName}}</span>
								<div class="bottom clearfix">
									<span class="newPrice">价格：{{o.newPrice}}</span><br />
								</div>
								<div class="buttonTrue">
									<el-button type="text" @click="$router.push({ name: 'login-in' })">预定</el-button>
									<el-button type="text" @click="hotletDetailHandle(o.id)">详情</el-button>
								</div>
							</div>
						</el-card>
					</el-col>
				</el-row>

			</div>

		</div>
		<div class="footer">
			<span>©2022 酒店预定系统 </span>
		</div>
		<hotel-comments v-if="hotelCommentsVisible" ref="HotelComments" @refreshDataList="getDataList">
		</hotel-comments>
	</div>
</template>

<script>
	import HotelComments from "./user-comments";
	export default {
		components: {
			HotelComments
		},
		data() {
			return {
				activeIndex: '1',
				activeIndex2: '1',
				dataForm: {
					title: "",
				},
				//分类列表
				typeListReserve: [],
				dataList: [],
				pageIndex: 1,
				pageSize: 10,
				totalCount: 0,
				dataListLoading: false,
				dataListSelections: [],
				hotelCommentsVisible: false,
			}
		},
		created() {
			this.getTypeList();
			this.getDataList();
		},
		methods: {
			hotletDetailHandle(id) {
				this.hotelCommentsVisible = true;
				this.$nextTick(() => {
					this.$refs.HotelComments.visible = true;
					this.$refs.HotelComments.init(id);
				});
			},
			//获取分类列表
			getTypeList() {
				this.$http({
					url: this.$http.adornUrl(`/yx_home/type_list`),
					method: "get"
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						data.list.forEach((item) => {
							this.typeListReserve.push({
								id: item.id,
								title: item.title
							});
						});
					}
				});
			},
			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				this.$http({
					url: this.$http.adornUrl("/yx_home/reserve_list"),
					method: "get",
					params: this.$http.adornParams({
						page: this.pageIndex,
						limit: this.pageSize,
						title: this.dataForm.title,
						typeId: this.dataForm.typeId,
						sort: this.dataForm.sort,
					}),
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						this.dataList = data.page.list;
						this.totalCount = data.page.totalCount;
					} else {
						this.dataList = [];
						this.totalCount = 0;
					}
					this.dataListLoading = false;
				});
			},

			handleSelect(key, keyPath) {
				console.log(key, keyPath);
			}
		}
	}
</script>

<style lang="scss">
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

	.content {

		margin: 30px 30px;
	}

	.homeMain {
		margin: 10px 10px;
	}

	.oldPrice {
		text-decoration: line-through
	}

	.newPrice {
		color: red;
		padding-left: 5px;
		font-size: 15px;
	}

	.buttonTrue {
		padding-left: 30%;
		margin-top: 13px;
		line-height: 12px;
	}

	.button {
		padding: 0;
		float: right;
	}

	.image {
		width: 100%;
		display: block;
	}

	.clearfix {
		padding: 15px 0 0 0;
	}

	.footer {
		margin-left: 48%;
		position: fixed;
		bottom: 20px;
	}
</style>

<template>
	<div class="mod-role">
		<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
			<el-form-item>
				<el-input v-model="dataForm.title" placeholder="请输入房间名称查询" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.typeId" placeholder="类型" clearable>
					<el-option v-for="item in typeListReserve" :key="item.id" :label="item.title" :value="item.id">
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
							<!-- <span class="oldPrice">原价：{{o.oldPrice}}</span> -->
							<span class="newPrice">价格：{{o.newPrice}}</span><br />
						</div>
						<div class="buttonTrue">
							<el-button type="text" v-if="isAuth('user:reserve:save')" @click="userReserveHandle(o.id)">
								预定</el-button>
							<el-button type="text" v-if="isAuth('admin:reserve:save')"
								@click="adminReserveHandle(o.id)">客服开房</el-button>
							<el-button type="text"
								@click="hotletDetailHandle(o.id)">详情</el-button>
						</div>
					</div>
				</el-card>
			</el-col>
		</el-row>

		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
		<admin-or-update v-if="adminOrUpdateVisible" ref="AdminOrUpdate" @refreshDataList="getDataList">
		</admin-or-update>
		<hotel-comments v-if="hotelCommentsVisible" ref="HotelComments" @refreshDataList="getDataList">
		</hotel-comments>
	</div>
</template>

<script>
	import AddOrUpdate from "./reserve-add-or-update";
	import AdminOrUpdate from "./admin-add-or-update";
	import HotelComments from "./hotel-comments";
	export default {
		components: {
			AddOrUpdate,
			AdminOrUpdate,
			HotelComments
		},
		data() {
			return {
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
				addOrUpdateVisible: false,
				adminOrUpdateVisible: false,
				hotelCommentsVisible: false,
			};
		},

		activated() {
			this.getDataList();
			this.getTypeList();
		},
		methods: {
			//获取分类列表
			getTypeList() {
				this.$http({
					url: this.$http.adornUrl(`/yx_hotel/type_list`),
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
					url: this.$http.adornUrl("/yx_hotel/reserve_list"),
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
			// 每页数
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 多选
			selectionChangeHandle(val) {
				this.dataListSelections = val;
			},
			// 用户订房
			userReserveHandle(id) {
				// console.log(1111);
				this.addOrUpdateVisible = true;
				this.$nextTick(() => {
					this.$refs.addOrUpdate.visible = true;
					this.$refs.addOrUpdate.init(id);
				});
			},
			// 管理员订房
			adminReserveHandle(id) {
				// console.log(1111);
				this.adminOrUpdateVisible = true;
				this.$nextTick(() => {
					this.$refs.AdminOrUpdate.visible = true;
					this.$refs.AdminOrUpdate.init(id);
				});
			},
			hotletDetailHandle(id) {
				this.hotelCommentsVisible = true;
				this.$nextTick(() => {
					this.$refs.HotelComments.visible = true;
					this.$refs.HotelComments.init(id);
				});
			},
		},
	};
</script>

<style>
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

	.clearfix:before,
	.clearfix:after {
		display: table;
		content: "";
	}

	.clearfix:after {
		clear: both
	}
</style>

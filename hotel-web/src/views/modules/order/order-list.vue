<template>
	<div class="mod-role">
		<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
			<el-form-item>
				<el-input v-model="dataForm.title" placeholder="请输入房间号查询" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-input v-model="dataForm.toOrderId" placeholder="请输入订单号查询" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.status" placeholder="状态" clearable>
					<el-option label="未付款" :value="0"></el-option>
					<el-option label="已付款未入住" :value="1"></el-option>
					<el-option label="已退款" :value="2"></el-option>
					<el-option label="已完结" :value="3"></el-option>
					<el-option label="已付款已入住" :value="3"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.typeId" placeholder="类型" clearable>
					<el-option v-for="item in typeList" :key="item.id" :label="item.title" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
			style="width: 100%">
			<!-- <el-table-column type="selection" header-align="center" align="center" width="50">
			</el-table-column> -->
			<el-table-column prop="orderId" header-align="center" align="center" label="订单ID">
			</el-table-column>
			<el-table-column prop="userName" header-align="center" align="center" label="用户姓名">
			</el-table-column>
			<el-table-column prop="userCard" header-align="center" align="center" label="用户身份证">
			</el-table-column>
			<el-table-column prop="homeNum" header-align="center" align="center" label="房间号">
			</el-table-column>
			<el-table-column prop="startTime" header-align="center" align="center" label="入住时间">
			</el-table-column>
			<el-table-column prop="endTime" header-align="center" align="center" label="离房时间">
			</el-table-column>
			<el-table-column prop="price" header-align="center" align="center" label="价格">
			</el-table-column>
			<el-table-column prop="statue" header-align="center" align="center" label="状态">
				<template slot-scope="scope">
					<div style="color: red;">{{ setUserType(scope.row.status) }}</div>
				</template>
			</el-table-column>
			<el-table-column prop="userId" header-align="center" align="center" label="类型">
				<template slot-scope="scope">
					<div style="color: red;">{{ scope.row.userId ==0 ? '客服订房' : '用户自行订房' }}</div>
				</template>
			</el-table-column>
			<el-table-column prop="remarks" header-align="center" align="center" label="备注">
			</el-table-column>
			<el-table-column prop="trips" header-align="center" align="center" label="行程">
			</el-table-column>
			<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
				<template slot-scope="scope">
					<span v-if="scope.row.status==4">
						<el-button type="primary" size="small" v-if="isAuth('msg:order:check')"
							@click="checkHandle(scope.row.id)" class="qure-btn">离房</el-button>
					</span>
					<span v-if="scope.row.status!=3&&scope.row.status!=2&&scope.row.status!=0">
						<el-button type="success" size="small" v-if="isAuth('msg:order:extend')"
							@click="extendHandle(scope.row)" class="qure-btn">延长时间</el-button>
					</span>
					<span v-if="scope.row.status==1">
						<el-button type="warning" size="small" v-if="isAuth('msg:order:determine')"
							@click="adminDetermineHandle(scope.row.id)" class="qure-btn">办理入住</el-button>
					</span>
					<span v-if="scope.row.status==1||scope.row.status==0">
						<el-button type="danger" size="small" v-if="isAuth('msg:order:quit')"
							@click="adminQuitHandle(scope.row.id)" class="qure-btn">退房</el-button>
					</span>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
			:page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalCount"
			layout="total, sizes, prev, pager, next, jumper">
		</el-pagination>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
	</div>
</template>

<script>
	import AddOrUpdate from "./extend-add-or-update";
	export default {
		components: {
			AddOrUpdate,
		},
		data() {
			return {
				dataForm: {
					title: "",
				},
				//分类列表
				typeList: [],
				dataList: [],
				pageIndex: 1,
				pageSize: 10,
				totalCount: 0,
				dataListLoading: false,
				dataListSelections: [],
				addOrUpdateVisible: false,
			};
		},

		activated() {
			this.getDataList();
			this.getTypeList();
		},
		methods: {

			getTypeList() {
				this.$http({
					url: this.$http.adornUrl(`/yx_hotel/type_list`),
					method: "get"
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						data.list.forEach((item) => {
							this.typeList.push({
								id: item.id,
								title: item.title
							});
						});
					}
				});
			},
			//显示类型
			setUserType(item) {
				switch (item) {
					case 0: {
						return "未付款";
					}
					case 1: {
						return "已付款未入住";
					}
					case 2: {
						return "已退款";
					}
					case 3: {
						return "已完结";
					}
					case 4: {
						return "已付款已入住";
					}
				}
			},

			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				this.$http({
					url: this.$http.adornUrl("/yx_order/list"),
					method: "get",
					params: this.$http.adornParams({
						page: this.pageIndex,
						limit: this.pageSize,
						title: this.dataForm.title,
						status: this.dataForm.status,
						toOrderId: this.dataForm.toOrderId,
						typeId: this.dataForm.typeId,
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
			// 延长时间
			extendHandle(row) {
				this.addOrUpdateVisible = true;
				this.$nextTick(() => {
					this.$refs.addOrUpdate.visible = true;
					this.$refs.addOrUpdate.init(row);
				});
			},
			// 离房
			checkHandle(id) {
				var ids = id ? [id] : this.dataListSelections.map((item) => item.id);
				this.$confirm(
						`确定对[id=${ids.join(",")}]进行[${id ? "离房" : "批量离房"}]操作?`,
						"提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						}
					)
					.then(() => {
						this.$http({
							url: this.$http.adornUrl("/yx_order/check"),
							method: "post",
							data: this.$http.adornData(ids, false),
						}).then(({
							data
						}) => {
							if (data && data.code === 200) {
								this.$message({
									message: "操作成功",
									type: "success",
									duration: 1500,
									onClose: () => this.getDataList(),
								});
							} else {
								this.$message.error(data.msg);
							}
						});
					})
					.catch(() => {});
			},
			// 管理员退房
			adminQuitHandle(id) {
				var ids = id;
				this.$confirm(
						`确定要退房操作吗?`,
						"提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						}
					)
					.then(() => {
						this.$http({
							url: this.$http.adornUrl("/yx_order/order_quit"),
							method: "post",
							data: this.$http.adornData(ids, false),
						}).then(({
							data
						}) => {
							if (data && data.code === 200) {
								this.$message({
									message: "操作成功",
									type: "success",
									duration: 1500,
									onClose: () => this.getDataList(),
								});
							} else {
								this.$message.error(data.msg);
							}
						});
					})
					.catch(() => {});
			},
			// 管理员办理入住手续
			adminDetermineHandle(id) {
				var ids = id;
				this.$confirm(
						`确定要办理入住手续吗?`,
						"提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						}
					)
					.then(() => {
						this.$http({
							url: this.$http.adornUrl("/yx_order/order_determine"),
							method: "post",
							data: this.$http.adornData(ids, false),
						}).then(({
							data
						}) => {
							if (data && data.code === 200) {
								this.$message({
									message: "操作成功",
									type: "success",
									duration: 1500,
									onClose: () => this.getDataList(),
								});
							} else {
								this.$message.error(data.msg);
							}
						});
					})
					.catch(() => {});
			},
		},
	};
</script>
<style>
	.qure-btn {
		padding: 10px 10px;
		margin: 5px;
	}
</style>

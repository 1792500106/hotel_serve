<template>
	<div class="mod-role">
		<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
			<el-form-item>
				<el-input v-model="dataForm.title" placeholder="请输入房间号查询" clearable></el-input>
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
			<el-table-column prop="homeName" header-align="center" align="center" label="房间名">
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
			<el-table-column prop="remarks" header-align="center" align="center" label="备注">
			</el-table-column>
			<el-table-column prop="trips" header-align="center" align="center" label="行程">
			</el-table-column>
			<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
				<template slot-scope="scope">
					<span v-if="scope.row.status!=3&&scope.row.status!=2&&scope.row.status!=0">
						<el-button type="text" size="small" v-if="isAuth('user:home:extend')"
							@click="extendHandle(scope.row)">延长时间</el-button>
					</span>
					<span v-if="scope.row.status==1">
						<el-button type="text" size="small" v-if="isAuth('user:home:quit')"
							@click="orderQuitHandle(scope.row.id)" style="color: red;">退房</el-button>
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
		},
		methods: {

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
					url: this.$http.adornUrl("/yx_personal/user_home"),
					method: "get",
					params: this.$http.adornParams({
						page: this.pageIndex,
						limit: this.pageSize,
						title: this.dataForm.title,
						status: this.dataForm.status
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
				console.log(1111);
				this.addOrUpdateVisible = true;
				this.$nextTick(() => {
					this.$refs.addOrUpdate.visible = true;
					this.$refs.addOrUpdate.init(row);
				});
			},
			// 用户退房
			orderQuitHandle(id) {
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
							url: this.$http.adornUrl("/yx_personal/user_order_quit"),
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

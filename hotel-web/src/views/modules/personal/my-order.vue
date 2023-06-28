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
			<el-table-column prop="id" header-align="center" align="center" width="80" label="ID">
			</el-table-column>
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
			<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
				<template slot-scope="scope">
					<el-button type="text" size="small" v-if="isAuth('my:order:delete')"
						@click="deleteHandle(scope.row.id)">删除</el-button>
					<span v-if="scope.row.status==3">
						<el-button type="text" size="small" v-if="isAuth('my:order:comments')"
							@click="addOrUpdateHandle(scope.row.id)">评论</el-button>
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
	import AddOrUpdate from "./comments-add";
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
					url: this.$http.adornUrl("/yx_personal/user_order"),
					method: "get",
					params: this.$http.adornParams({
						page: this.pageIndex,
						limit: this.pageSize,
						title: this.dataForm.title
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
			// 新增 / 修改
			addOrUpdateHandle(id) {
				console.log(1111);
				this.addOrUpdateVisible = true;
				this.$nextTick(() => {
					this.$refs.addOrUpdate.visible = true;
					this.$refs.addOrUpdate.init(id);
				});
			},
			// 删除
			deleteHandle(id) {
				var ids = id ? [id] : this.dataListSelections.map((item) => item.id);
				this.$confirm(
						`确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
						"提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						}
					)
					.then(() => {
						this.$http({
							url: this.$http.adornUrl("/yx_personal/user_order_delete"),
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

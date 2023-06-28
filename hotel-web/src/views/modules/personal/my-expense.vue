<template>
	<div class="mod-role">
		<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
			<el-form-item>
				<el-input v-model="dataForm.title" placeholder="请输入标题查询" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
				<el-button v-if="isAuth('msg:expense:delete')" type="danger" @click="deleteHandle()"
					:disabled="dataListSelections.length <= 0">批量删除</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
			style="width: 100%">
			<el-table-column type="selection" header-align="center" align="center" width="50">
			</el-table-column>
			<el-table-column prop="id" header-align="center" align="center" width="80" label="ID">
			</el-table-column>
			<el-table-column prop="userName" header-align="center" align="center" label="用户姓名">
			</el-table-column>
			<el-table-column prop="title" header-align="center" align="center" label="标题">
			</el-table-column>
			<el-table-column prop="status" header-align="center" align="center" label="类型">
			</el-table-column>
			<el-table-column prop="price" header-align="center" align="center" label="金额">
			</el-table-column>
			<el-table-column prop="createdTime" header-align="center" align="center" label="添加时间">
			</el-table-column>
			<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
				<template slot-scope="scope">
					<el-button type="text" size="small" v-if="isAuth('msg:expense:delete')"
						@click="deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
			:page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalCount"
			layout="total, sizes, prev, pager, next, jumper">
		</el-pagination>
	</div>
</template>

<script>
	
	export default {
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
			};
		},

		activated() {
			this.getDataList();
		},
		methods: {
			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				this.$http({
					url: this.$http.adornUrl("/yx_expense/list"),
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
							url: this.$http.adornUrl("/yx_expense/delete"),
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

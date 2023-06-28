<template>
	<div class="mod-role">
		<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
			<el-form-item>
				<el-input v-model="dataForm.title" placeholder="请输入房间名称查询" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.typeId" placeholder="类型" clearable>
					<el-option v-for="item in typeList" :key="item.id" :label="item.title" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
				<el-button v-if="isAuth('msg:hotel:save')" type="primary" @click="addOrUpdateHandle()">新增
				</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
			style="width: 100%">
			<!-- <el-table-column type="selection" header-align="center" align="center" width="50">
			</el-table-column> -->
			<el-table-column prop="id" header-align="center" align="center" width="80" label="ID">
			</el-table-column>
			<el-table-column prop="homeNum" header-align="center" align="center" label="房间编号">
			</el-table-column>
			<el-table-column prop="homeName" header-align="center" align="center" label="房间名称">
			</el-table-column>
			<el-table-column prop="photos" header-align="center" align="center" label="房间照片">
				<template slot-scope="{ row }">
					<img :src="row.photos" class="table-avatar" />
				</template>
			</el-table-column>
			<el-table-column prop="newPrice" header-align="center" align="center" label="现价" class="nowPriceCss">
			</el-table-column>
			<el-table-column prop="statue" header-align="center" align="center" label="状态">
				<template slot-scope="scope">
					<div style="color: red;">{{ setUserType(scope.row.status) }}</div>
				</template>
			</el-table-column>
			<el-table-column prop="createdTime" header-align="center" align="center" label="添加时间">
			</el-table-column>
			<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
				<template slot-scope="scope">
					<el-button type="text" size="small" v-if="isAuth('msg:hotel:update')"
						@click="addOrUpdateHandle(scope.row.id)">修改</el-button>
					<el-button type="text" size="small" v-if="isAuth('msg:hotel:delete')"
						@click="deleteHandle(scope.row.id)">删除</el-button>
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
	import AddOrUpdate from "./hotel-add-or-update";
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
						return "空房";
					}
					case 1: {
						return "已预定";
					}
					case 2: {
						return "脏房";
					}
				}
			},
			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				this.$http({
					url: this.$http.adornUrl("/yx_hotel/list"),
					method: "get",
					params: this.$http.adornParams({
						page: this.pageIndex,
						limit: this.pageSize,
						title: this.dataForm.title,
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
							url: this.$http.adornUrl("/yx_hotel/delete"),
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
	.table-avatar {
		width: 60%;
		height: 60%;
	}

	.nowPriceCss {
		color: red;
	}
</style>

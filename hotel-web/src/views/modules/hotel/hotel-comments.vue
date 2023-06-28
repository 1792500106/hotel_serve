<template>
	<el-dialog :title="!dataForm.id ? '详情' : '详情'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<div class="hotel-detail">
			<span>房间名称：{{hotelDetail.homeName}}</span>
			<span style=" padding-left: 10px;">房间号：{{hotelDetail.homeNum}}</span>
			<span style="color: red; padding-left: 10px;">房间价格：{{hotelDetail.newPrice}}</span>
		</div>
		<div class="mod-role">
			<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
				<el-form-item>
					<el-input v-model="dataForm.title" placeholder="请输入内容查询" clearable></el-input>
				</el-form-item>
				<el-form-item>
					<el-button @click="getDataList()">查询</el-button>
				</el-form-item>
			</el-form>
			<el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
				style="width: 100%">
				<!-- <el-table-column type="selection" header-align="center" align="center" width="50">
				</el-table-column> -->
				<el-table-column prop="userName" header-align="center" align="center" label="用户姓名">
				</el-table-column>
				<el-table-column prop="hotelName" header-align="center" align="center" label="房间名称">
				</el-table-column>
				<el-table-column prop="content" header-align="center" align="center" label="评论内容">
				</el-table-column>
				<el-table-column prop="createdTime" header-align="center" align="center" label="评论时间">
				</el-table-column>
			</el-table>
			<el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
				:current-page="pageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalCount"
				layout="total, sizes, prev, pager, next, jumper">
			</el-pagination>
		</div>
	</el-dialog>
</template>

<script>
	export default {

		data() {
			return {
				visible: true,
				dataForm: {
					title: "",
				},
				hotelDetail: "",
				dataList: [],
				pageIndex: 1,
				pageSize: 10,
				totalCount: 0,
				dataListLoading: false,
				dataListSelections: [],
			};
		},

		activated() {
			// this.getDataList();
		},
		methods: {

			init(id) {
				// this.dataForm.title = ""
				this.dataForm.id = id || 0;

				if (this.dataForm.id != 0) {
					this.dataListLoading = true;
					this.visible = true;
					this.$http({
						url: this.$http.adornUrl("/yx_hotel/hotel_detail"),
						method: "get",
						params: this.$http.adornParams({
							page: this.pageIndex,
							limit: this.pageSize,
							title: this.dataForm.title,
							hotelId: this.dataForm.id
						}),
					}).then(({
						data
					}) => {
						if (data && data.code === 200) {
							this.dataList = data.page.pageUtils.list;
							this.hotelDetail = data.page.yxHotel;
							this.totalCount = data.page.pageUtils.totalCount;
						} else {
							this.dataList = [];
							this.totalCount = 0;
						}
						this.dataListLoading = false;
					});
				}
			},

			// 获取数据列表
			getDataList() {
				var id = this.dataForm.id
				this.init(id);
				// this.dataListLoading = true;
				// this.$http({
				// 	url: this.$http.adornUrl("/yx_comments/list"),
				// 	method: "get",
				// 	params: this.$http.adornParams({
				// 		page: this.pageIndex,
				// 		limit: this.pageSize,
				// 		title: this.dataForm.title
				// 	}),
				// }).then(({
				// 	data
				// }) => {
				// 	if (data && data.code === 200) {
				// 		this.dataList = data.page.list;
				// 		this.totalCount = data.page.totalCount;
				// 	} else {
				// 		this.dataList = [];
				// 		this.totalCount = 0;
				// 	}
				// 	this.dataListLoading = false;
				// });
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

		},
	};
</script>
<style>
	.hotel-detail {
		margin-bottom: 20px;
		font-size: 16px;
	}
</style>

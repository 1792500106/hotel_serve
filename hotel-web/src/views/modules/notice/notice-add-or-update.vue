<template>
	<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">
			<el-form-item label="公告标题" prop="title">
				<el-input v-model="dataForm.title" placeholder="清输入公告标题"></el-input>
			</el-form-item>
			<el-form-item label="是否重要" prop="myHot">
				<el-radio v-model="dataForm.myHot" :label="0">否</el-radio>
				  <el-radio v-model="dataForm.myHot" :label="1">是</el-radio>
			</el-form-item>
			<el-form-item label="公告内容" prop="content">
				<el-input type="textarea" v-model="dataForm.content" placeholder="清输入公告内容"></el-input>
			</el-form-item>
		</el-form>
		<span slot="footer" class="dialog-footer">
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmit()">确定</el-button>
		</span>
	</el-dialog>
</template>

<script>
	export default {
		data() {
			return {
				visible: true,
				roleList: [],
				dataForm: {
					id: 0,
					title: "",
					content: "",
					myHot: '1'
				},
				dataRule: {
					title: [{
						required: true,
						message: "标题不能为空",
						trigger: "blur"
					}],
					content: [{
						required: true,
						message: "内容不能为空",
						trigger: "blur"
					}],
				},
			};
		},
		methods: {
			init(id) {
				this.dataForm.id = id || 0;

				if (this.dataForm.id != 0) {
					this.visible = true;
					this.$http({
						url: this.$http.adornUrl(`/yx_notice/info/${this.dataForm.id}`),
						method: "get",
						params: this.$http.adornParams(),
					}).then(({
						data
					}) => {
						if (data && data.code === 200) {
							this.dataForm.title = data.yxNotice.title;
							this.dataForm.content = data.yxNotice.content;
							this.dataForm.myHot = data.yxNotice.myHot;
						}
					});
				} else {
					this.dataForm.title = "";
					this.dataForm.content = "";
					this.dataForm.myHot = '1';
				}
			},
			// 表单提交
			dataFormSubmit() {
				this.$refs["dataForm"].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(
								`/yx_notice/${!this.dataForm.id ? "save" : "update"}`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								title: this.dataForm.title,
								content: this.dataForm.content,
								myHot: this.dataForm.myHot,
							}),
						}).then(({
							data
						}) => {
							if (data && data.code === 200) {
								this.$message({
									message: "操作成功",
									type: "success",
									duration: 1500,
									onClose: () => {
										this.visible = false;
										this.$emit("refreshDataList");
									},
								});
							} else {
								this.$message.error(data.msg);
							}
						});
					}
				});
			},
		},
	};
</script>

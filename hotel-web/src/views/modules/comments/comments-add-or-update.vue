<template>
	<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">
			<el-form-item label="内容" prop="content">
				<el-input v-model="dataForm.content" placeholder="请输入" clearable></el-input>
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
					content: "",
				},
				dataRule: {
					content: [{
						required: true,
						message: "不能为空",
						trigger: "blur"
					}]
				},
			};
		},
		methods: {
			init(id) {
				this.dataForm.id = id || 0;

				if (this.dataForm.id != 0) {
					this.visible = true;
					this.$http({
						url: this.$http.adornUrl(`/yx_comments/info/${this.dataForm.id}`),
						method: "get",
						params: this.$http.adornParams(),
					}).then(({
						data
					}) => {
						if (data && data.code === 200) {
							this.dataForm.content = data.yxComments.content;
						}
					});
				} else {
					this.dataForm.content = "";
				}
			},
			// 表单提交
			dataFormSubmit() {
				this.$refs["dataForm"].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(
								`/yx_comments/${!this.dataForm.id ? "save" : "update"}`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								content: this.dataForm.content,
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

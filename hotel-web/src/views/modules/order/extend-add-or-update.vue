<template>
	<el-dialog :title="!dataForm.id ? '延长住房时间' : '延长住房时间'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">
			<el-form-item label="延长时间" prop="endTime">
				<el-date-picker v-model="dataForm.endTime" type="date" placeholder="选择日期" format="yyyy-MM-dd"
					value-format="yyyy-MM-dd HH:mm:ss">
				</el-date-picker>
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
                oldTime: '',
				dataForm: {
					id: 0,
					endTime: "",
				},
				dataRule: {
					endTime: [{
						required: true,
						message: "时间不能为空",
						trigger: "blur"
					}]
				},
			};
		},
		methods: {
            init(row) {
                this.dataForm.id = row.id || 0;
                this.oldTime = row.endTime
            },
			// 表单提交
			dataFormSubmit() {
				this.$refs["dataForm"].validate((valid) => {
					if (valid) {
                        if (this.dataForm.endTime < this.oldTime) {
                            this.$message.error("延长日期必须大于当前退房日期");
                            return
                        }
						this.$http({
							url: this.$http.adornUrl(
								`/yx_order/admin_extend`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								endTime: this.dataForm.endTime,
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

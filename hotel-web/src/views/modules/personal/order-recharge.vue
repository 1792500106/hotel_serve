<template>
	<div>
		<div class="my-money">
			<span style="color: red;">我的余额：{{myMoney}}</span>
		</div>
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">
			<el-form-item label="充值金额" prop="money">
				<el-radio v-model="dataForm.money" label="50">50元</el-radio>
				<el-radio v-model="dataForm.money" label="100">100元</el-radio>
				<el-radio v-model="dataForm.money" label="200">200元</el-radio>
				<el-radio v-model="dataForm.money" label="500">500元</el-radio>
			</el-form-item>
		</el-form>
		<span slot="footer" class="dialog-footer">
			<el-button type="primary" @click="dataFormSubmit()">确定</el-button>
		</span>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dataForm: {
					money: "50",
				},
				myMoney: 0
			};
		},
		activated() {
			this.getUserInfo();
		},
		methods: {
			// 获取数据列表
			getUserInfo() {
				this.$http({
					url: this.$http.adornUrl(`/yx_personal/user_info`),
					method: "get",
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						this.myMoney = data.user.money;
					}
				});
			},
			// 表单提交
			dataFormSubmit() {

				this.$confirm(
						`确定要进行充值吗?`,
						"提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						}
					)
					.then(() => {
						this.$refs["dataForm"].validate((valid) => {
							if (valid) {
								this.$http({
									url: this.$http.adornUrl(
										`/yx_order/user_recharge`
									),
									method: "post",
									data: this.$http.adornData({
										money: this.dataForm.money,
									}),
								}).then(({
									data
								}) => {
									if (data && data.code === 200) {
                                        const divForm = document.getElementsByTagName('div')
                                        if (divForm.length) {
                                            document.body.removeChild(divForm[0])
                                        }
                                        const div = document.createElement('div')
                                        div.innerHTML = data.url // data就是接口返回的form 表单字符串
                                        document.body.appendChild(div)
                                        document.forms[0].setAttribute('target', '_blank') // 新开窗口跳转
                                        document.forms[0].submit()
										// this.$message({
										// 	message: "操作成功",
										// 	type: "success",
										// 	duration: 1500,
										// 	onClose: () => {
										// 		this.visible = false;
										// 		this.$emit("refreshDataList");
										// 	},
										// });
									} else {
										this.$message.error(data.msg);
									}
								});
							}
						});
					})
					.catch(() => {});
			},
		},
	};
</script>
<style>
	.my-money {
		margin-bottom: 10px;
		margin-left: 10px;
	}
</style>

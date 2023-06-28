<template>
	<el-dialog :title="!dataForm.id ? '订房' : '订房'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="100px">
			<el-form-item label="用户姓名" prop="userName">
				<el-input v-model="dataForm.userName" placeholder="请输入" clearable></el-input>
			</el-form-item>
			<el-form-item label="用户身份证" prop="userCard">
				<el-input v-model="dataForm.userCard" placeholder="请输入" clearable></el-input>
			</el-form-item>
			<el-form-item label="时间" prop="startTimes">
				<el-date-picker v-model="dataForm.startTimes" type="daterange" range-separator="至"
					start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
					style="width: 420px;" @blur="jiSuanMoney()">
				</el-date-picker>
			</el-form-item>
			<el-form-item label="行程表" prop="trips">
				<el-input v-model="dataForm.trips" placeholder="请输入" clearable></el-input>
			</el-form-item>
			<el-form-item label="备注" prop="remarks">
				<el-input v-model="dataForm.remarks" placeholder="请输入" clearable></el-input>
				<span style="color: red;">注意：客服订房，需要收取现金或微信支付宝，系统只统计订单。</span>
			</el-form-item>
		</el-form>
		<div class="forMoneyClass" v-if="isShowMoney">
			<span>总价： {{hotelMoney}}</span>
		</div>
		<span slot="footer" class="dialog-footer">
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmit()">确定</el-button>
		</span>
	</el-dialog>
</template>

<script>
	export default {
		data() {
            const rulesCode = (rule, value, callback) => {
                if (
                    !/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/.test(
                        value
                    )
                ) {
                    callback("身份证信息错误");
                } else {
                    callback();
                }
            };
			return {
				visible: true,
				roleList: [],
				dataForm: {
					id: 0,
					startTimes: "",
					remarks: "",
					userName: "",
					userCard: "",
					trips: ""
				},
				hotelMoney: 0,
				isShowMoney: false,
				dataRule: {
					startTimes: [{
						required: true,
						message: "时间不能为空",
						trigger: "blur"
					}],
					userName: [{
						required: true,
						message: "姓名不能为空",
						trigger: "blur"
					}],
					userCard: [{
						required: true,
						message: "身份证不能为空",
						trigger: "blur"
					},
                        {validator: rulesCode, trigger: "blur", message: "身份证号格式错误"},
					]
				},
			};
		},
		methods: {
			init(id) {
				this.dataForm.id = id || 0;
			},
			// 表单提交
			dataFormSubmit() {
				this.$refs["dataForm"].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(
								`/yx_hotel/admin_reserve`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								startTimes: this.dataForm.startTimes,
								remarks: this.dataForm.remarks,
								userName: this.dataForm.userName,
								userCard: this.dataForm.userCard,
								trips: this.dataForm.trips,
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
			jiSuanMoney() {
				this.$http({
					url: this.$http.adornUrl(
						`/yx_hotel/hotel_money`
					),
					method: "post",
					data: this.$http.adornData({
						id: this.dataForm.id || undefined,
						startTimes: this.dataForm.startTimes,
						// remarks: this.dataForm.remarks,
						// trips: this.dataForm.trips,
					}),
				}).then(({
					data
				}) => {
					if (data && data.code === 200) {
						this.hotelMoney = data.forMoney.hotelMoney
						this.isShowMoney = true
					} else {
						this.$message.error(data.msg);
					}
				});
			}
		},
	};
</script>
<style>
	.forMoneyClass {
		margin-left: 20%;
		font-size: 20px;
		color: red;
	}
</style>

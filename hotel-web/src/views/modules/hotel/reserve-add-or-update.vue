<template>
	<el-dialog :title="!dataForm.id ? '订房' : '订房'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="100px">
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
			return {
				visible: true,
				roleList: [],
				dataForm: {
					id: 0,
					startTimes: "",
					remarks: "",
					trips: ""
				},
				hotelMoney: 0,
				isShowMoney: false,
				dataRule: {
					startTimes: [{
						required: true,
						message: "时间不能为空",
						trigger: "blur"
					}]
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
								`/yx_hotel/user_reserve`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								startTimes: this.dataForm.startTimes,
								remarks: this.dataForm.remarks,
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

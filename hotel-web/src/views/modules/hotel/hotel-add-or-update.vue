<template>
	<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">

			<el-row>
				<el-col :span="12">
					<el-form-item label="房间编号" prop="homeNum">
						<el-input v-model="dataForm.homeNum" placeholder="请输入"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="房间名称" prop="homeName">
						<el-input v-model="dataForm.homeName" placeholder="请输入"></el-input>
					</el-form-item>
				</el-col>
			</el-row>

			<el-row>
				<el-col :span="12">
					<el-form-item label="房间分类" prop="typeId">
						<el-select v-model="dataForm.typeId" placeholder="请选择分类">
							<el-option v-for="item in typeList" :key="item.id" :label="item.title" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="现价" prop="newPrice">
						<el-input v-model="dataForm.newPrice" placeholder="请输入"></el-input>
					</el-form-item>
				</el-col>
			</el-row>

			<el-row>
				<el-form-item label="照片" prop="photos">
					<el-upload class="avatar-uploader" :action="uploadPath" :limit="1" list-type="picture-card"
						:on-preview="handlePictureCardPreview" :on-remove="handleRemove" :on-success="onSuccess">
						<img v-if="dataForm.photos" :src="dataForm.photos" class="avatar" />
						<i class="el-icon-plus"></i>
					</el-upload>
					</el-upload>
				</el-form-item>
			</el-row>

		</el-form>
		<span slot="footer" class="dialog-footer">
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmit()">确定</el-button>
		</span>
	</el-dialog>
</template>

<script>
	import {
		uploadPath
	} from "@/utils/file";
	export default {
		data() {
			return {
				visible: true,
				roleList: [],
				//分类列表
				typeList: [],
				//图片上次路径
				uploadPath,
				//上传图片路径
				dialogImageUrl: '',
				//是否放大显示图片
				dialogVisible: false,
				dataForm: {
					id: 0,
					typeId: 1,
					homeNum: "",
					homeName: "",
					newPrice: "",
					photos: "",
				},
				dataRule: {
					typeId: [{
						required: true,
						message: "分类不能为空",
						trigger: "blur"
					}],
					homeNum: [{
						required: true,
						message: "房间编号不能为空",
						trigger: "blur"
					}],
					homeName: [{
						required: true,
						message: "房间名称不能为空",
						trigger: "blur"
					}],
					oldPrice: [{
						required: true,
						message: "原价不能为空",
						trigger: "blur"
					}],
					newPrice: [{
						required: true,
						message: "现价不能为空",
						trigger: "blur"
					}]
				},
			};
		},
		/**
		 * 页面加载调用 */
		created() {
			this.getTypeList();
		},
		methods: {
			//图片上传成功后的操作
			onSuccess(response, file, fileList) {
				console.log(response, file);
				this.dataForm.photos = response.url
			},
			handleRemove(file, fileList) {
				this.dataForm.photos = file.url
				console.log(file, fileList);
			},
			//图片放大显示
			handlePictureCardPreview(file) {
				this.dialogImageUrl = file.url;
				this.dialogVisible = true;
			},

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
								title: item.title,
							});
						});
					}
				});
			},

			init(id) {
				this.dataForm.id = id || 0;
				if (this.dataForm.id != 0) {
					this.visible = true;
					this.$http({
						url: this.$http.adornUrl(`/yx_hotel/info/${this.dataForm.id}`),
						method: "get",
						params: this.$http.adornParams(),
					}).then(({
						data
					}) => {
						if (data && data.code === 200) {
							this.dataForm.typeId = data.yxHotel.typeId;
							this.dataForm.homeNum = data.yxHotel.homeNum;
							this.dataForm.homeName = data.yxHotel.homeName;
							this.dataForm.newPrice = data.yxHotel.newPrice;
							this.dataForm.photos = data.yxHotel.photos;
						}
					});
				} else {
					this.dataForm.typeId = "";
					this.dataForm.homeNum = "";
					this.dataForm.homeName = "";
					this.dataForm.newPrice = "";
					this.dataForm.photos = "";
				}
			},
			// 表单提交
			dataFormSubmit() {
				this.$refs["dataForm"].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(
								`/yx_hotel/${!this.dataForm.id ? "save" : "update"}`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								typeId: this.dataForm.typeId,
								homeNum: this.dataForm.homeNum,
								homeName: this.dataForm.homeName,
								oldPrice: this.dataForm.oldPrice,
								newPrice: this.dataForm.newPrice,
								photos: this.dataForm.photos
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
<style>
	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}
</style>

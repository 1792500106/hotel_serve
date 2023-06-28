<template>
	<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible"
		:append-to-body="true">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">
			<el-form-item label="标题" prop="title">
				<el-input v-model="dataForm.title" placeholder="清输入标题"></el-input>
			</el-form-item>
			<el-form-item label="排序" prop="sort">
				<el-input minLength="1" maxlength="8" oninput="value=value.replace(/[^\d.]/g,'')"
					v-model="dataForm.sort" placeholder="请输入" clearable></el-input>
					<span style="color: red;">数字越大排序在前！</span>
			</el-form-item>
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
				//图片上次路径
				uploadPath,
				//上传图片路径
				dialogImageUrl: '',
				//是否放大显示图片
				dialogVisible: false,
				dataForm: {
					id: 0,
					title: "",
					photos: "",
					sort: 0,
				},
				dataRule: {
					title: [{
						required: true,
						message: "标题不能为空",
						trigger: "blur"
					}],
					sort: [{
						required: true,
						message: "排序不能为空",
						trigger: "blur"
					}],
				},
			};
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
			
			init(id) {
				this.dataForm.id = id || 0;

				if (this.dataForm.id != 0) {
					this.visible = true;
					this.$http({
						url: this.$http.adornUrl(`/yx_banner/info/${this.dataForm.id}`),
						method: "get",
						params: this.$http.adornParams(),
					}).then(({
						data
					}) => {
						if (data && data.code === 200) {
							this.dataForm.title = data.yxBanner.title;
							this.dataForm.photos = data.yxBanner.photos;
							this.dataForm.sort = data.yxBanner.sort;
						}
					});
				} else {
					this.dataForm.title = "";
					this.dataForm.photos = "";
					this.dataForm.sort = 1;
				}
			},
			// 表单提交
			dataFormSubmit() {
				this.$refs["dataForm"].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(
								`/yx_banner/${!this.dataForm.id ? "save" : "update"}`
							),
							method: "post",
							data: this.$http.adornData({
								id: this.dataForm.id || undefined,
								title: this.dataForm.title,
								photos: this.dataForm.photos,
								sort: this.dataForm.sort,
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
<style >
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

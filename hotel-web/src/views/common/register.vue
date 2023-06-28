<template>
	<div class="site-wrapper site-page--login">
		<div class="site-content__wrapper bg-img">
			<div class="site-content">
				<div class="brand-info">
					<h2 class="brand-info__text">酒店预约系统</h2>
				</div>
				<div class="login-main">
					<h3 class="login-title">用户注册</h3>
					<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
						status-icon>
						<el-form-item label="账号" prop="username">
							<el-input v-model="dataForm.username" placeholder="登录账号"></el-input>
						</el-form-item>
						<el-form-item label="密码" prop="password">
							<el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
						</el-form-item>
						<el-form-item>
							<el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">注册</el-button>
						</el-form-item>
						<el-button type="text" size="small" @click="$router.push({ name: 'login' })">直接登录</el-button>
						<el-button type="text" size="small" @click="$router.push({ name: 'login' })">主页</el-button>
					</el-form>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dataForm: {
					username: '',
					password: '',
					salt: '',
				},
				dataRule: {
					username: [{
						required: true,
						message: '帐号不能为空',
						trigger: 'blur'
					}],
					password: [{
							required: true,
							message: '密码不能为空',
							trigger: 'blur'
						},
						{
							min: 6,
							max: 20,
							message: '长度在 6 到 20 个字符',
							trigger: 'blur'
						}
					],
				}
			}
		},
		created() {

		},
		methods: {
			// 提交表单
			dataFormSubmit() {

				this.$refs["dataForm"].validate(valid => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl('/sys/user/register'),
							method: "post",
							data: this.$http.adornData(this.dataForm)
						}).then(({
							data
						}) => {
							if (data && data.code === 200) {
								this.$message({
									message: "操作成功",
									type: "success",
									duration: 1500,
								});
								this.$router.replace({
									name: 'login'
								})
							} else {
								this.$message.error(data.msg);
							}
						});
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.bg-img {
		background-image: url('bg.jpg');
		background-size: 100% 100%;
	}

	.site-wrapper.site-page--login {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		background-color: rgba(38, 50, 56, 0.5);
		overflow: hidden;

		&:before {
			position: fixed;
			top: 0;
			left: 0;
			z-index: -1;
			width: 100%;
			height: 100%;
			content: "";
			background-color: #fa8bff;
			background-image: linear-gradient(45deg,
					#fa8bff 0%,
					#2bd2ff 52%,
					#2bff88 90%);
			background-size: cover;
		}

		.site-content__wrapper {
			position: absolute;
			top: 0;
			right: 0;
			bottom: 0;
			left: 0;
			padding: 0;
			margin: 0;
			overflow-x: hidden;
			overflow-y: auto;
			background-color: transparent;
		}

		.site-content {
			min-height: 100%;
			padding: 30px 500px 30px 30px;
		}

		.brand-info {
			margin: 220px 100px 0 90px;
			color: #fff;
		}

		.brand-info__text {
			margin: 0 0 22px 0;
			font-size: 48px;
			font-weight: 400;
			text-transform: uppercase;
		}

		.brand-info__intro {
			margin: 10px 0;
			font-size: 16px;
			line-height: 1.58;
			opacity: 0.6;
		}

		.login-main {
			position: absolute;
			top: 0;
			right: 0;
			padding: 150px 60px 180px;
			width: 470px;
			min-height: 100%;
			background-color: #fff;
		}

		.login-title {
			font-size: 16px;
		}

		.login-captcha {
			overflow: hidden;

			>img {
				width: 100%;
				cursor: pointer;
			}
		}

		.login-btn-submit {
			width: 100%;
			margin-top: 38px;
		}
	}
</style>

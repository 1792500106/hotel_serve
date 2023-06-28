<template>
    <div>
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
                 label-width="80px">
            <h2>个人资料设置</h2>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="真实姓名" prop="nickName">
                        <el-input v-model="dataForm.nickName" placeholder="请输入真实姓名"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="身份证号" prop="userCard">
                        <el-input v-model="dataForm.userCard" placeholder="请输入身份证号"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="手机号" prop="mobile">
                        <el-input v-model="dataForm.mobile" placeholder="请输入手机号"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="dataForm.email" placeholder="请输入邮箱"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <span slot="footer" class="dialog-footer">
			<el-button type="primary" @click="dataFormSubmit()">确定</el-button>
		</span>
    </div>
</template>

<script>
    import {
        isEmail,
        isMobile
    } from '@/utils/validate'

    export default {
        data() {
            var validateEmail = (rule, value, callback) => {
                if (!isEmail(value)) {
                    callback(new Error('邮箱格式错误'))
                } else {
                    callback()
                }
            }
            var validateMobile = (rule, value, callback) => {
                if (!isMobile(value)) {
                    callback(new Error('手机号格式错误'))
                } else {
                    callback()
                }
            }

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
                dataForm: {
                    userId: 0,
                    nickName: '',
                    userCard: '',
                    mobile: '',
                    email: ''
                },
                dataRule: {
                    nickName: [{
                        required: true,
                        message: '用户名不能为空',
                        trigger: 'blur'
                    }],
                    userCard: [{
                        required: true,
                        message: '身份证不能为空',
                        trigger: 'blur'
                    },
                        {validator: rulesCode, trigger: "blur", message: "身份证号格式错误"},
                    ],

                    email: [{
                        required: true,
                        message: '邮箱不能为空',
                        trigger: 'blur'
                    },
                        {
                            validator: validateEmail,
                            trigger: 'blur'
                        }
                    ],
                    mobile: [{
                        required: true,
                        message: '手机号不能为空',
                        trigger: 'blur'
                    },
                        {
                            validator: validateMobile,
                            trigger: 'blur'
                        }
                    ]
                }
            };
        },
        activated() {
            this.init();
        },
        methods: {
            init() {
                this.$http({
                    url: this.$http.adornUrl(`/sys/user/msg_info`),
                    method: "get",
                }).then(({
                             data
                         }) => {
                    if (data && data.code === 200) {
                        this.dataForm.userId = data.userMsg.userId
                        this.dataForm.nickName = data.userMsg.nickName
                        this.dataForm.sex = data.userMsg.sex
                        this.dataForm.mobile = data.userMsg.mobile
                        this.dataForm.email = data.userMsg.email
                        this.dataForm.idCard = data.userMsg.idCard
                        this.dataForm.address = data.userMsg.address
                        this.dataForm.userCard = data.userMsg.userCard
                    }
                });
            },
            // 表单提交
            dataFormSubmit() {
                this.$refs["dataForm"].validate((valid) => {
                    if (valid) {
                        this.$http({
                            url: this.$http.adornUrl(
                                `/sys/user/msg_update`
                            ),
                            method: "post",
                            data: this.$http.adornData({
                                'userId': this.dataForm.userId,
                                'nickName': this.dataForm.nickName,
                                'sex': this.dataForm.sex,
                                'mobile': this.dataForm.mobile,
                                'email': this.dataForm.email,
                                'idCard': this.dataForm.idCard,
                                'address': this.dataForm.address,
                                'userCard': this.dataForm.userCard,
                            }),
                        }).then(({
                                     data
                                 }) => {
                            if (data && data.code === 200) {
                                this.$message({
                                    message: "操作成功",
                                    type: "success",
                                    duration: 1500,
                                    onClose: () => this.init(),
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

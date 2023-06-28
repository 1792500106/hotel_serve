<template>
  <div class="site-wrapper">
    <div class="line">
      <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#8C7853"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item
          index="1"
          @click="$router.push({ name: 'login' })"
          style="margin-left: 30px"
          >首页</el-menu-item
        >
        <el-menu-item index="2" @click="$router.push({ name: 'room' })"
          >房间</el-menu-item
        >
        <el-menu-item
          index="3"
          @click="$router.push({ name: 'login-in' })"
          style="float: right; margin-right: 30px"
          >登录</el-menu-item
        >
      </el-menu>
    </div>
    <div class="content">
      <el-row :gutter="40" class="panel-group-banner">
        <el-col :xs="12" :sm="12" :lg="12" class="card-panel-banner">
          <el-carousel height="400px">
            <el-carousel-item v-for="item in bannerList" :key="item">
              <img :src="item.photos" alt="" />
            </el-carousel-item>
          </el-carousel>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="12" class="card-panel-banner">
          <el-card class="box-card" style="height: 400px">
            <div slot="header" class="clearfix">
              <div style="padding-bottom: 10px">
                最新公告：{{ notice.title }}
              </div>
              <div>发布时间：{{ notice.createdTime }}</div>
            </div>
            <div class="text item">
              {{ notice.content }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <div class="footer">
      <span>酒店房间预定系统 </span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      bannerList: [],
      notice: {
        title: "",
        content: "",
        createdTime: "",
      },
    };
  },
  created() {
    this.getCountData();
  },
  methods: {
    getCountData() {
      this.$http({
        url: this.$http.adornUrl(`/yx_home/get_user_count`),
        method: "get",
      }).then(({ data }) => {
        if (data && data.code === 200) {
          this.notice.title = data.count.noticeTile;
          this.notice.content = data.count.noticeContent;
          this.notice.createdTime = data.count.noticeTime;

          data.count.bannerList.forEach((item) => {
            this.bannerList.push({
              id: item.id,
              title: item.title,
              photos: item.photos,
            });
          });
        }
      });
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style lang="scss">
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.content {
  margin: 30px 50px;
}

.footer {
  margin-left: 48%;
  position: fixed;
  bottom: 20px;
}
</style>

<style lang="less">
@import "./home.less";
</style>

<template>
  <div class="home-main">
    <Row :gutter="10">
      <Col :md="20" :lg="20">
        <Row class-name="home-page-row1" :gutter="10">
          <Col :md="24" :lg="24" :style="{ marginBottom: '10px' }">
            <Card>
              <Row type="flex" class="user-infor">
                <Col span="8">
                  <Row
                    class-name="made-child-con-middle"
                    type="flex"
                    align="middle"
                  >
                    <img class="avator-img" src="../../../images/moli.jpg" />
                  </Row>
                </Col>
                <Col span="16" style="padding-left:6px;">
                  <Row
                    class-name="made-child-con-middle"
                    type="flex"
                    align="middle"
                  >
                    <div>
                      <b class="card-user-infor-name">代码生成器</b
                      ><a
                        href="http://doc.dc.citycloud.com.cn/project-5/doc-11/"
                        target="_blank"
                        >（使用手册）</a
                      >
                      <p>welcome admin~</p>
                    </div>
                  </Row>
                </Col>
              </Row>
              <div class="line-gray"></div>
              <!-- <Row class="margin-top-8">
                <Col span="8"
                  ><p class="notwrap">欢迎使用代码生成器平台:</p>
                  </Col
                >
              </Row> -->
            </Card>
          </Col>
        </Row>
      </Col>
    </Row>
  </div>
</template>

<script>
export default {
  name: "home",
  components: {},
  data() {
    return {};
  },
  methods: {}
};
</script>

<template>
  <div class="home">
    <!-- 3. 使用组件 -->
    <!-- 左侧菜单 - 区分文件类型 -->
    <SideMenu class="home-left"></SideMenu>
    <!-- 右侧内容区 -->
    <div class="home-right">
      <!-- 面包屑导航栏 - 显示文件路径 -->
      <BreadCrumb></BreadCrumb>
      <!-- 表格组件 - 文件展示区 -->
      <FileTable></FileTable>
    </div>
  </div>
</template>

<script>
import SideMenu from './components/SideMenu.vue' //  1.引入左侧菜单组件
import BreadCrumb from './components/BreadCrumb.vue' //  引入面包屑导航栏
import FileTable from './components/FileTable.vue' //  引入文件表格展示区

export default {
  name: 'Home',
  components: {
    SideMenu, //  2. 注册组件
    BreadCrumb,
    FileTable
  },
  data() {
    return {}
  }
}
</script>
<style lang="stylus" scoped>
.home {
  // 使用flex布局 菜单居左固定宽度 右侧内容区域自适应宽度
  display: flex;

  .home-left {
    box-sizing: border-box;
  }

  .home-right {
    box-sizing: border-box;
    width: calc(100% - 200px);
    padding: 8px 24px;
    flex: 1;
  }
}
</style>
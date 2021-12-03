<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="fileName" label="文件名"> </el-table-column>
    <el-table-column prop="extendName" label="类型" width="100"> </el-table-column>
    <el-table-column prop="fileSize" label="大小" width="60"> </el-table-column>
    <el-table-column prop="uploadTime" label="修改日期" width="180"> </el-table-column>
    <el-table-column label="操作" width="60"> </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: 'FileTable',
  data() {
    return {
      // 表格数据先模拟
      tableData: [
        {
          fileName: 'markdown样式文件',
          extendName: 'markdown',
          fileSize: '10KB',
          uploadTime: '2020-10-28 16:33:33'
        },
        {
          fileName: '项目源码',
          extendName: 'zip',
          fileSize: '7MB',
          uploadTime: '2020-12-28 20:00:50'
        }
      ]
    }
  }
}
</script>
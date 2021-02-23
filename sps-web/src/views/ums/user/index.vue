<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          type="primary"
          @click="handleSearchList()"
          size="small"
        >
          查询搜索
        </el-button>
        <el-button
          style="float: right; margin-right: 15px"
          @click="handleResetSearch()"
          size="small"
        >
          重置
        </el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form
          :inline="true"
          :model="listQuery"
          size="small"
          label-width="140px"
        >
          <el-form-item label="输入搜索：">
            <el-input
              v-model="listQuery.keyword"
              class="input-width"
              placeholder="用户名"
              clearable
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        size="mini"
        class="btn-add"
        @click="handleAdd()"
        style="margin-left: 20px"
        >添加</el-button
      >
    </el-card>
    <div class="table-container">
      <el-table
        ref="userTable"
        :data="list"
        style="width: 100%"
        v-loading="listLoading"
        border
      >
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="用户名" align="center">
          <template slot-scope="scope">{{ scope.row.username }}</template>
        </el-table-column>
        <el-table-column label="电话" align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>
        <el-table-column label="性别" align="center">
          <template slot-scope="scope">
            {{ scope.row.gender === 1 ? "男" : "女" }}
          </template>
        </el-table-column>
        <el-table-column label="年龄" align="center">
          <template slot-scope="scope">{{ scope.row.age }}</template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">{{
            scope.row.createTime | formatDateTime
          }}</template>
        </el-table-column>
        <el-table-column label="是否启用" width="140" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleStatusChange(scope.$index, scope.row)"
              :active-value="1"
              :inactive-value="0"
              v-model="scope.row.status"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleSelectRole(scope.$index, scope.row)"
              >分配角色
            </el-button>
            <el-button
              size="mini"
              type="text"
              @click="handleUpdate(scope.$index, scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              @click="handleDelete(scope.$index, scope.row)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :current-page.sync="listQuery.pageNum"
        :page-size="listQuery.pageSize"
        :page-sizes="[10, 15, 20]"
        :total="total"
      >
      </el-pagination>
    </div>
    <el-dialog
      :title="isEdit ? '编辑用户' : '添加用户'"
      :visible.sync="dialogVisible"
      width="28%"
      :before-close="handleClose"
    >
      <el-form
        :model="user"
        :rules="userRules"
        ref="userForm"
        label-width="150px"
        size="small"
      >
        <el-form-item label="用户名：" prop="username">
          <el-input
            name="username"
            v-model="user.username"
            style="width: 250px"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input
            name="password"
            v-model="user.password"
            type="password"
            style="width: 250px"
          ></el-input>
        </el-form-item>
        <el-form-item label="电话：" prop="phone">
          <el-input
            name="phone"
            v-model="user.phone"
            style="width: 250px"
          ></el-input>
        </el-form-item>
        <el-form-item label="年龄：">
          <el-input v-model="user.age" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="性别：">
          <el-radio v-model="user.gender" :label="0">女</el-radio>
          <el-radio v-model="user.gender" :label="1">男</el-radio>
        </el-form-item>
        <el-form-item label="是否启用：">
          <el-radio-group v-model="user.status">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCancel()" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <el-dialog title="分配角色" :visible.sync="allocDialogVisible" width="30%">
      <el-select
        v-model="roleIds"
        multiple
        placeholder="请选择"
        size="small"
        style="width: 80%"
      >
        <el-option
          v-for="item in allRoleList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allocDialogVisible = false" size="small"
          >取 消</el-button
        >
        <el-button
          type="primary"
          @click="handleAllocDialogConfirm()"
          size="small"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { fetchList, createUser, updateUser, updateStatus, deleteUser, getRoleByUser, allocRole, existUsername, existPhone } from '@/api/login'
import { fetchAllRoleList } from '@/api/role'
import { formatDate } from '@/utils/date'
import { validUsername, validPhone } from '@/utils/validate'

const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  keyword: null
}
const defaultuser = {
  id: null,
  username: null,
  password: null,
  phone: null,
  gender: null,
  age: null,
  status: 1,
  createTime: null
}
export default {
  name: 'userList',
  data() {
    let validateUsernameExist = (rule, value, callback) => {
      if (value.length == 0 && value == null) {
        callback()
      } else {
        existUsername(value).then(res => {
          if (!validUsername(value)) {
            callback(new Error('用户名长度应大于3位'))
          } else {
            if (res.data != null) {
              callback(new Error('该用户名已被占用'))
            }
          }
          callback()
        })
      }

    }
    let validatePhoneExist = (rule, value, callback) => {
      if (value.length == 0 && value == null) {
        callback()
      } else {
        existPhone(value).then(res => {
          if (!validPhone(value)) {
            callback(new Error('手机号格式不正确'))
          } else {
            if (res.data != null) {
              callback(new Error('该手机号已被占用'))
            }
          }
          callback()
        })
      }
    }
    let validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      userRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsernameExist }],
        phone: [{ required: true, trigger: 'blur', validator: validatePhoneExist }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      listLoading: false,
      dialogVisible: false,
      user: Object.assign({}, defaultuser),
      isEdit: false,
      allocDialogVisible: false,
      roleIds: [],
      allRoleList: [],
      userId: null
    }
  },
  created() {
    this.getList()
    this.getAllRoleList()
  },
  filters: {
    formatDateTime(time) {
      if (time == null || time === '') {
        return 'N/A'
      }
      let date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  methods: {
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery)
    },
    handleSearchList() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageNum = 1
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    handleAdd() {
      this.dialogVisible = true
      this.isEdit = false
      this.user = Object.assign({}, defaultuser)
    },
    handleStatusChange(index, row) {
      this.$confirm('是否要修改该状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(row.id, { status: row.status }).then(response => {
          this.$message({
            type: 'success',
            message: '修改成功!'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消修改'
        })
        this.getList()
      })
    },
    handleDelete(index, row) {
      this.$confirm('是否要删除该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(row.id).then(response => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getList()
        })
      })
    },
    handleUpdate(index, row) {
      this.dialogVisible = true
      this.isEdit = true
      this.user = Object.assign({}, row)
      this.user.password = null
    },
    handleDialogConfirm() {
      this.$confirm('是否要确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.isEdit) {
          updateUser(this.user.id, this.user).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            })
            this.dialogVisible = false
            this.getList()
          })
        } else {
          createUser(this.user).then(response => {
            this.$message({
              message: '添加成功！',
              type: 'success'
            })
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleAllocDialogConfirm() {
      this.$confirm('是否要确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = new URLSearchParams()
        params.append("userId", this.userId)
        params.append("roleIds", this.roleIds)
        allocRole(params).then(response => {
          this.$message({
            message: '分配成功！',
            type: 'success'
          })
          this.allocDialogVisible = false
        })
      })
    },
    handleSelectRole(index, row) {
      this.userId = row.id
      this.allocDialogVisible = true
      this.getRoleListByUser(row.id)
    },
    handleClose(done) {
      this.$refs['userForm'].resetFields()
      done()
    },
    handleCancel(formName) {
      this.$refs['userForm'].resetFields()
      this.dialogVisible = false
    },
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.listLoading = false
        this.list = response.data.list
        this.total = response.data.total
      })
    },
    getAllRoleList() {
      fetchAllRoleList().then(response => {
        this.allRoleList = response.data
      })
    },
    getRoleListByUser(userId) {
      getRoleByUser(userId).then(response => {
        let roleIds = response.data
        this.roleIds = []
        if (roleIds != null && roleIds.length > 0) {
          for (let i = 0; i < roleIds.length; i++) {
            this.roleIds.push(roleIds[i].id)
          }
        }
      })
    }
  }
}
</script>
<style></style>



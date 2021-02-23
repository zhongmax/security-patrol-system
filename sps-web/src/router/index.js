import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'home',
      component: () => import('@/views/home/index'),
      meta: { title: '首页', icon: 'home' }
    }]
  }
]

export const asyncRouterMap = [
  {
    path: '/ums',
    component: Layout,
    redirect: '/ums/user',
    name: 'ums',
    meta: { title: '权限', icon: 'ums' },
    children: [
      {
        path: 'user',
        name: 'user',
        component: () => import('@/views/ums/user/index'),
        meta: { title: '用户列表', icon: 'ums-user' }
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/views/ums/role/index'),
        meta: { title: '角色列表', icon: 'ums-role' }
      },
      {
        path: 'allocMenu',
        name: 'allocMenu',
        component: () => import('@/views/ums/role/allocMenu'),
        meta: { title: '分配菜单' },
        hidden: true
      },
      {
        path: 'allocPermission',
        name: 'allocPermission',
        component: () => import('@/views/ums/role/allocPermission'),
        meta: { title: '分配资源' },
        hidden: true
      },
      {
        path: 'menu',
        name: 'menu',
        component: () => import('@/views/ums/menu/index'),
        meta: { title: '菜单列表', icon: 'ums-menu' }
      },
      {
        path: 'addMenu',
        name: 'addMenu',
        component: () => import('@/views/ums/menu/add'),
        meta: { title: '添加菜单' },
        hidden: true
      },
      {
        path: 'updateMenu',
        name: 'updateMenu',
        component: () => import('@/views/ums/menu/update'),
        meta: { title: '修改菜单' },
        hidden: true
      },
      {
        path: 'permission',
        name: 'permission',
        component: () => import('@/views/ums/permission/index'),
        meta: { title: '权限列表', icon: 'ums-permission' }
      },
      {
        path: 'permissionCategory',
        name: 'permissionCategory',
        component: () => import('@/views/ums/permission/categoryList'),
        meta: { title: '资源分类' },
        hidden: true
      }
    ]
  },
  {
    path: '/pms',
    component: Layout,
    redirect: '/pms/point',
    name: 'pms',
    meta: { title: '巡查', icon: 'pms-patrol' },
    children: [{
      path: 'point',
      name: 'point',
      component: () => import('@/views/pms/point/index'),
      meta: { title: '巡查点列表', icon: 'pms-point' }
    },
    {
      path: 'stand',
      name: 'stand',
      component: () => import('@/views/pms/product/add'),
      meta: { title: '巡查标准', icon: 'pms-stand' }
    },
    {
      path: 'shift',
      name: 'shift',
      component: () => import('@/views/pms/product/update'),
      meta: { title: '巡查排班', icon: 'pms-shift' }
    },
    {
      path: 'sign',
      name: 'sign',
      component: () => import('@/views/pms/productCate/index'),
      meta: { title: '巡查签到情况', icon: 'pms-sign' }
    },
    {
      path: 'abnormal',
      name: 'abnormal',
      component: () => import('@/views/pms/productCate/add'),
      meta: { title: '巡查异常情况', icon: 'pms-abnormal' }
    }
    ]
  },
  {
    path: '/sms',
    component: Layout,
    redirect: '/sms/performance',
    name: 'sms',
    meta: { title: '汇总', icon: 'sms-summary' },
    children: [
      {
        path: 'performance',
        name: 'performance',
        component: () => import('@/views/sms/flash/index'),
        meta: { title: '员工绩效统计', icon: 'sms-performance' }
      },
      {
        path: 'analysis',
        name: 'analysis',
        component: () => import('@/views/sms/flash/sessionList'),
        meta: { title: '巡查情况汇总', icon: 'sms-analysis' }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})


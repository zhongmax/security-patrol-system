import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/perm/list',
    method: 'get',
    params: params
  })
}

export function createPermission(data) {
  return request({
    url: '/perm/create',
    method: 'post',
    data: data
  })
}

export function updatePermission(id, data) {
  return request({
    url: '/perm/update/' + id,
    method: 'post',
    data: data
  })
}

export function deletePermission(id) {
  return request({
    url: '/perm/delete/' + id,
    method: 'post'
  })
}

export function fetchAllPermissionList() {
  return request({
    url: '/perm/listAll',
    method: 'get'
  })
}

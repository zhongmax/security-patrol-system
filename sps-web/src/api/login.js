import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/user/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo() {
  return request({
    url: '/user/info',
    method: 'get',
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function fetchList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params: params
  })
}

export function createUser(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data: data
  })
}

export function updateUser(id, data) {
  return request({
    url: '/user/update/' + id,
    method: 'post',
    data: data
  })
}

export function updateStatus(id, params) {
  return request({
    url: '/user/updateStatus/' + id,
    method: 'post',
    params: params
  })
}

export function deleteUser(id) {
  return request({
    url: '/user/delete/' + id,
    method: 'post'
  })
}

export function getRoleByUser(id) {
  return request({
    url: '/user/role/' + id,
    method: 'get'
  })
}

export function allocRole(data) {
  return request({
    url: '/user/role/update',
    method: 'post',
    data: data
  })
}

export function existUsername(username) {
  return request({
    url: '/user/existUsername/' + username,
    method: 'get',
  })
}

export function existPhone(phone) {
  return request({
    url: '/user/existPhone/' + phone,
    method: 'get',
  })
}

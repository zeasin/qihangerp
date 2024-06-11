import request from '@/utils/request'

// 查询淘宝退款订单列表
export function listRefund(query) {
  return request({
    url: '/jd-api/afterSale/list',
    method: 'get',
    params: query
  })
}

// 查询售后详细
export function getRefund(id) {
  return request({
    url: '/jd-api/afterSale/' + id,
    method: 'get'
  })
}

export function pullRefund(data) {
  return request({
    url: '/jd-api/afterSale/pull_list',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/jd-api/afterSale/push_oms',
    method: 'post',
    data: data
  })
}

import request from '@/utils/request'

// 查询订单发货列表（备货清单）
export function listShipping(query) {
  return request({
    url: '/shipping/stock_ship_list',
    method: 'get',
    params: query
  })
}

export function orderItemSpecIdUpdate(data) {
  return request({
    url: '/shipping/order_item_spec_id_update',
    method: 'post',
    data: data
  })
}

// 生成出库单（备货清单生成出库单）
export function generateStockOutEntry(data) {
  return request({
    url: '/shipping/generate_stock_out_entry',
    method: 'post',
    data: data
  })
}

// 分配给供应商发货
export function distributeSupplierShip(data) {
  return request({
    url: '/shipping/supplier_ship_dist',
    method: 'post',
    data: data
  })
}

// 订单待出库列表
export function listOrderStockOutEntry(query) {
  return request({
    url: '/shipping/order_stock_out_entry_list',
    method: 'get',
    params: query
  })
}

// 订单待出库明细列表
export function listOrderStockOutEntryItem(query) {
  return request({
    url: '/shipping/order_stock_out_entry_item_list',
    method: 'get',
    params: query
  })
}

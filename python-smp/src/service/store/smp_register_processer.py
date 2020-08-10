# coding=utf-8
import json
# 链接中台fmp系统
import time

import pymysql
import requests


# 动态生成参数中的唯一变量
def change_vr_key(key_list, param):
    for key in key_list:
        for data in param:
            new_key = "axin_{}".format(int(round(time.time() * 1000)))
            time.sleep(1)
            del data[key]
            data[key] = new_key


# 电商中台用户域处理器
class SmpVrProcesser(object):
    # 接口地址
    store_vrdatainit_path = "/admin/v1/data/init"
    order_dispatcher_path = "/admin/v1/order/dispatcher"

    host = "mysql-k8s.shbeta.ke.com"  # 数据库主机地址
    port = 3308
    user = "dianmian"  # 数据库用户名
    passwd = "3664505"  # 数据库密码
    database = "store_pms"

    def __init__(self, env):
        self.env = env
        self.mysql = pymysql.connect(
            host=self.host,
            port=self.port,
            user=self.user,
            password=self.passwd,
            database=self.database
        )

    # 后台初始化门店数据与创建摄影订单
    def store_vr_data_init(self, param):
        return requests.post(self.env + self.store_vrdatainit_path, data=json.dumps(param),
                             headers={"Content-Type": "application/json;charset=UTF-8"})

    # 单条触发派单逻辑
    def order_dispatcher(self, param):
        return requests.post(self.env + self.order_dispatcher_path, data=json.dumps(param),
                             headers={"Content-Type": "application/json;charset=UTF-8"})

    # mysql初始化操作
    def init_mysql_data(self, param):
        for data in param:
            sql = "INSERT INTO `store_pms`.`t_store_vr`(`store_code`, `name`, `vr_name`, `brand_id`, `brand_name`, `province`, `province_name`, `city`, `city_name`, `area`, `area_name`, `address`, `store_property`, `store_property_name`, `store_type`, `latitude`, `longitude`, `state`, `cuser`, `ctime`, `muser`, `mtime`, `house_area`, `sign_date`, `station_num`, `sign_room_num`, `vr_url`, `pic_url`, `pic_first`, `floor_num`, `joint_acceptance_date`, `showcase_area`, `door_area`, `owner_name`, `owner_phone`) VALUES ('{}', 'axin的小店', 'axin的小店', 20, '链家', '110000', '北京', '110000', '北京', '110000', '和平区', '沈阳市和平区沙岗西路112号，114号，116号1层', 1005, '大店', '优店', 66.0000000, 66.0000000, 1, 1000000021330256, '2020-04-15 13:33:48', 1000000010072066, '2020-06-04 16:54:55', 111.40, '1000-01-01 00:00:00', 66, 66, 'https://realsee.com/lianjia/dnovE3BZnv636lRK/XNv0MmWM6a42TOhjhVtm3MlYTNLkE1Vl/', 'http://storage.lianjia.com/shandian/c3d55e2e-f9e7-49bf-a5f8-e491e6f67e41.jpg,http://storage.lianjia.com/shandian/7cf9b10e-89f4-47b7-866c-6d1a2a930323.jpg', 'http://storage.lianjia.com/shandian/c3d55e2e-f9e7-49bf-a5f8-e491e6f67e41.jpg', 66, '2019-10-22', 66.00, 66.00, '刘鑫', '14685471313')".format(data["storeCode"])
            cursor = self.mysql.cursor()
            try:
                cursor.execute(sql)
                self.mysql.commit()
            except:
                self.mysql.rollback()
            self.mysql.close()

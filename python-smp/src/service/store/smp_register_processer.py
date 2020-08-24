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
            sql = "INSERT INTO `store_pms`.`t_store`(`store_code`, `name`, `brand_id`, `sign_status`, `province`, `city`, `area`, `address`, `store_property`, `image_finish`, `latitude`, `longitude`, `group_id`, `state`, `cuser`, `ctime`, `muser`, `mtime`, `war_area`, `province_area`, `house_area`, `store_number`, `month_rent`, `sign_date`, `payer_name`, `payer_phone`, `close_desc`, `close_proof`, `area_pic_type`, `release_time`, `intention_date`, `data_from`, `startup_fund_time`, `ehr_code`, `monthly_repayment`, `repayment_periods`, `project_status`, `close_status`, `close_image`, `bd_name`, `bd_phone`, `bd_networking_time`, `contract_effet_date`, `contract_subject_type`, `other_contract_subject`, `monthly_repay_date`, `first_repay_date`, `last_repay_date`, `contract_type`, `is_assessment`) VALUES ('{}', 'axin的小店', 243, 7002, '110000', '110000', '', '北京市贝壳找房', 1003, 8001, 29.6929870, 106.6106010, 87, 1, 0, '2019-09-03 20:23:47', 1000000010099438, '2020-03-12 17:40:36', '西部', '', 50.00, 'CQ_14_258570', 0.00, '2019-08-31 00:00:00', '', '', '', '', 3, '1970-01-01 00:00:00', NULL, 0, '2019-09-18 13:36:25', '\'\'', 0, 0, 32002, 0, NULL, '张鑫', '13983961603', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0);".format(data["storeCode"])
            cursor = self.mysql.cursor()
            try:
                sql_result = cursor.execute(sql)
                self.mysql.commit()
            except:
                self.mysql.rollback()
            self.mysql.close()

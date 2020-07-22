# coding=utf-8
from src.service.user.smp_register_processer import *

env = "http://172.16.23.4:8080"
env_local = "http://localhost:6666"

# tenantCode = "youpin"
tenantCode = "jiayou"

ucid = ""
accountType = "uc"
bizAccount = "1000000026029855"

is_batch_query = True

# 电商中台 - 用户查询自动化脚本
if __name__ == '__main__':
    processer = SmpUserProcesser(env_local)
    api_result = processer.query_user_info(ucid, accountType, bizAccount, tenantCode)
    print(api_result.text)

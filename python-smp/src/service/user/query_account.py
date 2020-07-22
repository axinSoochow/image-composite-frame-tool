# coding=utf-8
from src.service.user.smp_register_processer import *

env = "http://172.16.23.4:8080"
env_local = "http://localhost:6666"

# tenantCode = "youpin"
tenantCode = "jiayou"
# accountType = "commerce-seller"
accountType = "uc"


ucid = ""
smpId = ""
bizAccount = "1000000026029855"

ucids = []
smpIds = []
bizAccounts = ["1000000026029855", "1000000026013467"]

is_batch_query = True

# 电商中台 - 用户查询自动化脚本
if __name__ == '__main__':
    processer = SmpUserProcesser(env_local)
    if not is_batch_query:
        api_result = processer.query_account_info(ucid, smpId, bizAccount, tenantCode, accountType)
        print(api_result.text)
        quit()
    api_result = processer.batch_account_info(ucids, smpIds, tenantCode, bizAccounts, accountType)
    print(api_result.text)

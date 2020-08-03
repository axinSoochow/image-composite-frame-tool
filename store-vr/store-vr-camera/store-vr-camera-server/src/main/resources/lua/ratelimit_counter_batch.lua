-- 遍历传递进来的keys和变量数组，批量自增和设置过期时间
for index,key in ipairs(KEYS) do
	redis.call("INCR",key)
	redis.call('EXPIRE', key, ARGV[index])
end

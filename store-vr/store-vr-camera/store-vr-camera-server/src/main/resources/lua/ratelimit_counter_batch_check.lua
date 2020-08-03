for index,key in ipairs(KEYS) do
	-- 先判断Key是否存在
	if redis.call("EXISTS",key) == 1 then
		-- 判断当前数字是否大于限制的次数
	   if tonumber(redis.call("GET",key)) >= tonumber(ARGV[index]) then
	   	 -- will convert to redis string
	   	 return "false"
	   end
	end
end
return "true";
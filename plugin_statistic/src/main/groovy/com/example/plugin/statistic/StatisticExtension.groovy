package com.example.plugin.statistic

class StatisticExtension {
    List<Map<String, Object>> buryPoint = new ArrayList<>()

    //类名称白名单
    public List<String> classWhiteListRegex = new ArrayList<>();
    //方法hook调用实现类
    public String impl = ""
    public long time = -1
}
/*
 *
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.netflix.loadbalancer;

import java.util.List;

/**
 * A rule that skips servers with "tripped" circuit breaker and picks the
 * server with lowest concurrent requests.
 * <p>
 * This rule should typically work with {@link ServerListSubsetFilter} which puts a limit on the 
 * servers that is visible to the rule. This ensure that it only needs to find the minimal 
 * concurrent requests among a small number of servers. Also, each client will get a random list of 
 * servers which avoids the problem that one server with the lowest concurrent requests is 
 * chosen by a large number of clients and immediately gets overwhelmed.
 *
 * IRule: 最终的目的是选择出一个特定的服务
 * loadBalancer: 负载均衡器，含有服务列表，以及所有服务的统计信息
 * stats: 服务的统计信息
 *
 * IRule包含loadBalancer，loadBalancer包含servers和stats
 * 
 * @author awang
 *
 */
public class BestAvailableRule extends ClientConfigEnabledRoundRobinRule {

    private LoadBalancerStats loadBalancerStats;
    
    @Override
    public Server choose(Object key) {
        if (loadBalancerStats == null) {
            return super.choose(key);
        }
        //获取所有的服务列表
        List<Server> serverList = getLoadBalancer().getAllServers();

        //初始值设置为int的最大值
        int minimalConcurrentConnections = Integer.MAX_VALUE;
        long currentTime = System.currentTimeMillis();
        Server chosen = null;

        //遍历所特有服务
        for (Server server: serverList) {
            //获取服务的统计信息
            ServerStats serverStats = loadBalancerStats.getSingleServerStat(server);

            //如果服务没有熔断
            if (!serverStats.isCircuitBreakerTripped(currentTime)) {

                //获取服务当前的连接数
                int concurrentConnections = serverStats.getActiveRequestsCount(currentTime);

                //如果当前服务的连接数小于上一次遍历的服务的连接数（如果没有上一次，则与int最大值比较），这个if分支可以选出当前连接数最小的服务
                if (concurrentConnections < minimalConcurrentConnections) {
                    //重新赋值，使用较小的连接数赋值
                    minimalConcurrentConnections = concurrentConnections;
                    //选出当前连接数最小的服务
                    chosen = server;
                }
            }
        }

        //如果所有服务都被熔断，则退化为轮询策略
        if (chosen == null) {
            return super.choose(key);
        } else {
            return chosen;
        }
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if (lb instanceof AbstractLoadBalancer) {
            loadBalancerStats = ((AbstractLoadBalancer) lb).getLoadBalancerStats();            
        }
    }
    
    

}

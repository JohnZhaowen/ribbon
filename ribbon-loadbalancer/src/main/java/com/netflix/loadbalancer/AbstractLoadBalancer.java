/*
*
* Copyright 2013 Netflix, Inc.
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
 * AbstractLoadBalancer contains features required for most loadbalancing
 * implementations.
 * 
 * An anatomy of a typical LoadBalancer consists of:
 * 1. A List of Servers (nodes) that are potentially bucketed based on a specific criteria.
 * 2. A Class that defines and implements a LoadBalacing Strategy via <code>IRule</code>
 * 3. A Class that defines and implements a mechanism to determine the suitability/availability of the nodes/servers in the List.
 *
 * 获取stats
 * 获取服务列表
 * 获取单个服务
 * 
 * @author stonse
 * 
 */
public abstract class AbstractLoadBalancer implements ILoadBalancer {

    /**
     * 将服务按状态分成三组：所有，UP服务组，非UP服务组
     */
    public enum ServerGroup{
        ALL,
        STATUS_UP,
        STATUS_NOT_UP        
    }
        
    /**
     * delegate to {@link #chooseServer(Object)} with parameter null.
     * key为null时选择到的服务
     */
    public Server chooseServer() {
    	return chooseServer(null);
    }

    
    /**
     * List of servers that this Loadbalancer knows about
     * 根据服务跟组获取服务列表
     *
     *
     * @param serverGroup Servers grouped by status, e.g., {@link ServerGroup#STATUS_UP}
     */
    public abstract List<Server> getServerList(ServerGroup serverGroup);
    
    /**
     * Obtain LoadBalancer related Statistics
     * 获取
     */
    public abstract LoadBalancerStats getLoadBalancerStats();    
}

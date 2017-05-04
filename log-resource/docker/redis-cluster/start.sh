#!/usr/bin/env bash

for i in {0..5}
do
    docker run -d -p 700$i:6379 --name redis$i hub.c.163.com/library/redis redis-server --appendonly yes --bind 0.0.0.0 --cluster-enabled yes --cluster-config-file nodes.conf --cluster-node-timeout 15000
done


# ./redis-trib.rb create --replicas 1 192.168.0.2:6379 192.168.0.3:6379 192.168.0.4:6379 192.168.0.5:6379 192.168.0.6:6379 192.168.0.7:6379


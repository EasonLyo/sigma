# SIGMA

👆[中文文档](./README_zh.md)

A high performances API gateway based on Vertx(Netty), can execute on native image.

# Feature List

1. **Route**
2. **Upstream**
3. **reverse-proxy**
4. **Load balance**

5. **plugin**

# Roadmap

![ROADMAP](./image/SIGMA-ROADMAP.png)

# Milestone

- 2024-10-08 the version 0.1.0-alpha is done.

# Benchmark

## Benchmark Environments

Apple M1 Pro(10 vCPUs, 16 GB memory)

## Benchmark Test for reverse proxy

Only use sigma as the reverse proxy server,include path rewrite plugin,with no logging,or other plugins enabled.

## QPS

Because of M1 Pro CPU arch , it dont have Hyper-Threading tech, so, The test use 4 core for wrk, 4 core for nginx or sigma reverse proxy, and 2 core for upstream, upstream is only return a simple json response:

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```
### Upstream(on port 8888 and 8889)

```wiki
~ % wrk -t8 -c2000 -d30s http://localhost:8888
Running 30s test @ http://localhost:8888
  8 threads and 2000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.60ms    2.70ms 129.17ms   95.30%
    Req/Sec    17.01k     5.87k   31.22k    68.83%
  4070138 requests in 30.09s, 322.17MB read
  Socket errors: connect 1756, read 161, write 0, timeout 0
Requests/sec: 135264.76
Transfer/sec:     10.71MB
```

### Nginx(on port 8081):

```wiki
~ % wrk -t8 -c2000 -d1m http://localhost:8081
Running 1m test @ http://localhost:8081
  8 threads and 2000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.74ms    2.32ms 135.17ms   95.69%
    Req/Sec     8.20k     2.99k   16.62k    67.24%
  3908370 requests in 1.00m, 547.91MB read
  Socket errors: connect 1756, read 227, write 15, timeout 0
  Non-2xx or 3xx responses: 28
Requests/sec:  65119.80
Transfer/sec:      9.13MB
```

### Sigma(on port 80, path rewrite /test/* to /):

```wiki
~ % wrk -t8 -c2000 -d1m http://localhost/test/benchmark   
Running 1m test @ http://localhost/test/benchmark
  8 threads and 2000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.53ms    2.13ms 137.37ms   88.09%
    Req/Sec     8.55k     2.83k   14.77k    70.42%
  4087162 requests in 1.00m, 654.83MB read
  Socket errors: connect 1756, read 189, write 0, timeout 0
Requests/sec:  68079.91
Transfer/sec:     10.91MB
```
> Sigma is just a little bit faster than Nginx in some situation, but it's still very fast and we have a lot of room to improve.

# Contributing

We welcome contributions to Sigma! If you have any ideas, suggestions, or bug reports, please feel free to open an issue or submit a pull request.
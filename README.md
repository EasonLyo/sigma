# SIGMA

👆[中文文档](./README_zh.md)

> WARNING: [Eclipse Vert.x 5 candidate 1 released!](https://vertx.io/blog/eclipse-vert-x-5-candidate-1-released/), we plan to upgrade SIGMA vertx version, some method or code will change.

##  Introduction

Sigma is a high performances API gateway based on Vertx(Netty), can execute on native image.

### [Based on vertx](https://vertx.io/)
Vertx is a reactive web framework on JVM,  it provides many of out-of-the-box feature.

Thanks to the Vertx ecosystem, Sigma can achieve extremely high performance at a minimal cost.

Here is some vertx module sigma used:
- vertx core
- vertx web
- vertx http proxy
- vertx io_uring

### [Vertx io_uring](https://vertx.io/docs/vertx-io_uring-incubator/java/)
> The new io_uring interface added to the Linux Kernel 5.1 is a high I/O performance scalable interface for fully asynchronous Linux syscalls.

> Vert.x io_uring is based on Netty io_uring, more details on the GitHub project.

U can see more detail in [GitHub Project](https://github.com/netty/netty-incubator-transport-io_uring).

**With thd help of Vertx io_uring,sigma have high IO performance.**

### [Native transports](https://netty.io/wiki/native-transports.html)

> Vert.x can run with native transports (when available) on BSD (OSX) and Linux, these JNI transports add features specific to a particular platform, generate less garbage, and generally improve performance when compared to the NIO based transport.

**Sigma has improved data transfer speed with the help of Native Transport, although it can only work on some Linux and BSD systems, fortunately most services run on these systems.**

### [GraalVM native image](https://www.graalvm.org/latest/reference-manual/native-image/)

> Native Image is a technology to compile Java code ahead-of-time to a binary—a native executable. A native executable includes only the code required at run time, that is the application classes, standard-library classes, the language runtime, and statically-linked native code from the JDK.

When running applications as native executable files, compared to traditional Java running methods, it will have advantages such as faster startup speed, smaller packaging size, and smaller runtime memory usage.

**Sigma can be executed  as native executable files ,and it will adapt to the cloud native era better.**

**WARNING:**

By checking [GitHub actions runner image](https://github.com/actions/runner-images?tab=readme-ov-file) pre-installed software and [GraalVM Native image prerequisites](https://www.graalvm.org/latest/reference-manual/native-image/#prerequisites) requirements in GitHub actions, we found that the Windows 11 SDK is missing from the windows latest image of Github actions runner. If you need to use Sigma binary files for the Windows platform, please compile them yourself according to the documentation or deploy Sigma using Jar.

### [JAVA virtual thread](https://openjdk.org/jeps/444)

> Virtual threads are not faster threads — they do not run code any faster than platform threads. They exist to provide scale (higher throughput), not speed (lower latency). There can be many more of them than platform threads, so they enable the higher concurrency needed for higher throughput according to Little's Law.

For IO intensive tasks, the load is usually not limited by the CPU, and in this case, even if the number of threads exceeds the number of cores, throughput cannot be improved.

> Virtual threads help to improve the throughput of typical server applications precisely because such applications consist of a great number of concurrent tasks that spend much of their time waiting.

Unlike the OS threading model (which typically manages threads through thread pools to reduce the overhead of creating threads and reusing threads), virtual threads provide higher throughput while not offering lower latency.

**Sigma have a high concurrency performance with the help of Vertx's support for virtual thread.**

## Feature List

1. **Route**
2. **Upstream**
3. **reverse-proxy**
4. **Load balance**
5. **plugin**

## Example Usage

### binary
To begin, download the latest program for your operating system and architecture from the Release page.

Next, place the sigma binary and configuration file on your server.

Finally, edit the configuration.

#### Access your computer in a LAN network via SSH

##### Step 1 : modify `sigma.json` file.

As we can see, it has 5 part to edit. The part of `proxy-server` is a proxy server protocol config, u can just use it by default.

`port: 80`:  means sigma http proxy server will bind on port 80.

```json
{
  "proxy-server": {
    "http": {
      "id": "http-proxy-server",
      "server-id": "http-proxy-server",
      "port": 80
    }
  }
}
```
The part of `proxy-client` is a http proxy server config, we don't need to modify except we want to adjust it to improve some performances.  
```json
{
  "proxy-client": {
    "max-pool-size-per-server": 256
  }
}
```
The part of router is a main config which decide what request the proxy will handler.

`proxy-server-id`: it config this router is belonged to which proxy server.

`route`: it define an array of http request path to handle.

`proxy-pass-id`: This configuration determines which proxy pass rule it will submit the request to.
```json
{
  "router": [
    {
      "id": "router-first",
      "enable": true,
      "proxy-server-id": "http-proxy-server",
      "route": [
        {
          "id": "router-first-route-first",
          "enable": true,
          "path": "/test/*",
          "allow-method": [],
          "consumes": [],
          "produces": [],
          "proxy-pass-id": "proxy-pass-1"
        }
      ]
    }
  ]
}
```
The part of `proxy-pass` is a main config, this configuration determines how to handle requests received by the proxy server.

`upstream-id`: this proxy requst will send to which upsteam.

```json
{
  "proxy-pass": [
    {
      "id": "proxy-pass-1",
      "upstream-id": "upstream-static-service-1"
    }
  ]
}
```

The part of `upstream` is a main config, this configuration define the server discoverty method , load balance rule, and upstream nodes.

This is a simple example, it means we will send proxy request to `http://localhost:8888` by `round-robin`, and the upsteam list is a static config.
```json
{
  "upstream": [
    {
      "id": "upstream-static-service-1",
      "discovery": "static-config",
      "rule-strategy": "round-robin",
      "nodes": [
        "http://localhost:8888"
      ]
    }
  ]
}
```

#### step2 : start sigma

```shell
./sigma run net.oooops.sigma.Sigma -conf ${absolute path of sigma.json file} -options ${${absolute path of options.json file}} -instance 1 
```

### jar

#### step1
The step 1 are similar to the binary step 1.

#### 

```shell
java -jar sigma-vX.X.X-fat.jar ./sigma run net.oooops.sigma.Sigma -conf ${absolute path of sigma.json file} -options ${${absolute path of options.json file}} -instance 1
```

## Roadmap

![ROADMAP](./image/SIGMA-ROADMAP-V0.1.0-ALPHA.png)

## Milestone

- 2024-10-08 the version 0.1.0-alpha is done.

## Benchmark

### Benchmark Environments

Apple M1 Pro(10 vCPUs, 16 GB memory)

### Benchmark Test for reverse proxy

Only use sigma as the reverse proxy server,include path rewrite plugin,with no logging,or other plugins enabled.

### QPS

Because of M1 Pro CPU arch , it dont have Hyper-Threading tech, so, The test use 4 core for wrk, 4 core for nginx or sigma reverse proxy, and 2 core for upstream, upstream is only return a simple json response:

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```
#### Upstream(on port 8888 and 8889)

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

#### Nginx(on port 8081):

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

#### Sigma(on port 80, path rewrite /test/* to /):

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
Sigma is just a little bit faster than Nginx in some situation, but it's still very fast and we have a lot of room to improve.

## Contributing

We welcome contributions to Sigma! If you have any ideas, suggestions, or bug reports, please feel free to open an issue or submit a pull request.
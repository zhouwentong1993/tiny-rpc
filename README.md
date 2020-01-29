# tiny-rpc
简版 RPC 框架，基于 Netty 实现。

## todo list

- [ ] 增加客户端连接超时以及重试功能，connect(long timeout)
- [ ] 增加多种编解码方式供客户端使用
  - [ ] JSON
  - [ ] XML
  - [ ] JBoss Marshalling
- [ ] 增加探活，参见 Netty pro
- [ ] 增加限流功能
  - [ ] 基于时间窗口的限流
  - [ ] 基于信号量的限流
  - [ ] 基于 LimitLatch 的限流
# truenewx/ik-analyzer
基于org.wltea:ik-analyzer:2012_u6 （原始地址：https://code.google.com/archive/p/ik-analyzer/ ）的适配lucene-9.2的改造。

与原版相比，做了如下调整：

- 改造为maven工程，*.dic文件移至src/main/resources/META-INF/dic目录中
- 拆分为ik-analyzer-core和ik-analyzer-lucene两部分，ik-analyzer-core为不依赖lucene的核心部分，ik-analyzer-lucene为依赖lucene的适配部分
- 采用jdk-11和lucene-9.2
- 移除了对solr的支持，未来可能加上

# 真相
侦探爱好者破案辅助 App
# CaseAnalyst
Android App for detective thinking

## 底层数据库变化
在[33eed9c](https://github.com/YuHayring/CaseAnalyst/commit/33eed9c6ddf15971071b116e06f174367fd0a0c2)提交之前，本 App 采用的是 Java 实例本地序列化文件进行存储，在此次提交之后，将使用 neo4j 存储案件信息。由于neo4j 无法在安卓平台上运行，此 app 将通过 okhttp3 + retrofit2 + rxjava2 连接到 neo4j HTTP服务器。
## DataStruct Changed
Before [33eed9c](https://github.com/YuHayring/CaseAnalyst/commit/33eed9c6ddf15971071b116e06f174367fd0a0c2) commit, this app used Java instance serialization file for storage. After this commit, neo4j will be used to store case information. Since neo4j can't run on Android platform, this app will connect to neo4j HTTP server by okhttp3 + retrofit2 + rxjava2.

### 告示
我曾经使用了网上的一些代码，那写代码并没有标它注用了哪种开源许可或我已忘记那写代码从哪里来导致无法找到其使用的开源许可。若你在我的仓库中发现了你的代码并且与你使用的开源许可违背的行为，请与我联系。

### Notice
I used some code on the Internet, but I didn't mark which open-source license it used or I forgot where the code came from, so I couldn't find the open-source license it used. If you find your code in my repository and violate the open source license you use, please contact me.


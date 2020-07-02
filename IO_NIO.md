

这里的IO关键在网络IO，一般磁盘IO使用Filechannel读写时，filechannel是没有setblockingconfig方法的。
##阻塞式IO的实现

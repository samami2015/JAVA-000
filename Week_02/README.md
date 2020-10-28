学习笔记

	串行GC STW时间最长，而且STW时间随堆内存增大而增大，因为当内存可用空间较大时，GC触发的频率会降低，但每次需要回收的对象会增大
	并行GC 默认GC线程数为系统核心数，STW时间较串行GC短，STW时间也会随堆内存增大而增大
	CMSGC  只有初始标记阶段和最终标记阶段STW，其余阶段与应用线程并行执行，因此STW时间较并行GC短，然而实际运行时间比并行GC长，因此消耗的资源也比并行GC多，STW时间也会随堆内存增大而增大
	G1GC   STW时间最短，由(G1 Evacuation Pause 预测停顿)触发Yang GC，由(G1 Humongous Allocation 大型对象分配)触发mix GC，由(Allocation Failure 分配失败)触发Full GC，Full GC时退化为串行GC，STW时间较长
	
	可用堆内存较小时，不同GC方式的差别不大，可以尝试不同GC方式，查看GC消耗时间，依据应用需求选择，如对吞吐量要求更高，可以选择并行GC，如果对STW时间要求比较高而并行GC无法达到要求，则选用G1GC并通过MaxGCPauseMillis控制STW时间
	
	堆内存较大时，并行GCSTW时间会增大，可以优先考虑G1GC
	
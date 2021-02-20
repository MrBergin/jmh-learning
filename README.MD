# JMH Playground

## What is this project?

I've never had the chance to play around with JMH before, but always wanted to try something with it. This project's
motivation is practice using JMH comparing the usage of an `Either` type versus throwing an `Exception`. I chose this
because I've always been taught exceptions as a control flow are bad because:

1. unchecked exceptions don't force the caller to deal with error cases, this is fine because usually an author's intent
   of an unchecked exception is something they don't expect the caller to be able to handle.

2. checked exceptions solve 1. in that they force you to deal with exceptions, but fall over when you come to want to
   compose function calls and use lambdas.

3. exceptions have a performance cost associated with them.

In my personal experience, I've felt 1. and 2. when writing programs, but I've always just taken people's word for it on
point 3.

## What benchmarks are done here?

Given point 3. above I want to answer the question:

What performs better for error handling in Project Reactor? Kotlin Arrow's `Either<Throwable, *>`, or throwing
the `Exception`?

The test is pretty simple: have an error raised during a `Mono` by either throwing an `Exception`, or using an `Either`
 with an `Either.fold(Mono::error, Mono::just)` and `Mono.flatMap`.

## Other parameters?

In addition to comparing returning an `Either` vs throwing an `Exception`, this benchmark also compares what happens if
we skip filling in the stack trace, and vary the stack depth.

## A better analysis

[This article](https://shipilev.net/blog/2014/exceptional-performance/#_conclusion) is fantastic and does a great job of
analysing exception performance, even if I didn't fully understand it. This article analyses different rates of
failure, and demonstrates how exceptions are rather performant if your code mostly takes the happy path,
whereas my benchmarking here is analysing performance when every single call fails. 

## So... the results?

Caveat #1: I have no idea what I'm doing

Caveat #2: Take these results with a sack of salt, how much of your program's time is spent error handling depends on
your program, if only 0.00001% of your program's time is spent handling errors, then performance wins here are obviously
not your greatest concern. My personal preference is to strive for code which is easy to read and express your intent
with.

Here's results on my machine with an Intel(R) Core(TM) i5-7600K CPU @ 3.80GHz:

| Benchmark                              | Mode  |Cnt   |   Score  | Error  | Units  |
| -------------------------------------- | ----- | ---- | --------- |-------| ------ |
| benchmark_0depth_either_filledin       | thrpt |      |  733.329 |        | ops/ms |
| benchmark_0depth_either_notfilledin    | thrpt |      | 8080.171 |        | ops/ms |
| benchmark_0depth_throws_filledin       | thrpt |      |  704.310 |        | ops/ms |
| benchmark_0depth_throws_notfilledin    | thrpt |      | 4696.197 |        | ops/ms |
| benchmark_100depth_either_filledin     | thrpt |      |  163.231 |        | ops/ms |
| benchmark_100depth_either_notfilledin  | thrpt |      | 4611.828 |        | ops/ms |
| benchmark_100depth_throws_filledin     | thrpt |      |   99.465 |        | ops/ms |
| benchmark_100depth_throws_notfilledin  | thrpt |      |  233.676 |        | ops/ms |
| benchmark_5depth_either_filledin       | thrpt |      |  595.407 |        | ops/ms |
| benchmark_5depth_either_notfilledin    | thrpt |      | 7922.554 |        | ops/ms |
| benchmark_5depth_throws_filledin       | thrpt |      |  561.267 |        | ops/ms |
| benchmark_5depth_throws_notfilledin    | thrpt |      | 2088.981 |        | ops/ms |

Again I'd like to stress caveat #1, I'm just rolling with the first plan that came to my head.

With that in mind, I wasn't really surprised about the results.

## What did I learn from this?

* I have no confidence I truly know what I'm doing, but was pleased to see what I thought would happen, happened, the
  best case
  `benchmark_0depth_either_notfilledin` has nearly 100 times throughput as the worst case
  `benchmark_100depth_throws_filledin`
* Using Either consistently outperforms throwing
* Avoiding filling in a stack trace has a significant improvement on throughput, especially for using Either
* Increased stack depth doesn't impact Either too much, but has a significant impact on throwing an Exception. My guess
  is extra work has to be done when throwing when the "distance" between the throw and catch is greater?

## Next steps

I want to better understand what a good performance benchmark is, I just went with the first thing that came to my head,
and I have no idea if the conditions under which I ran these tests was fair.

In addition to this, my test case for Either vs Exception is only a relative comparison, I'd really like to set up a
benchmark that mimics a real world example (e.g. API calls with bad request bodies being handled with exceptions vs
either)
to see just how much this performance really matters (if it matters at all).

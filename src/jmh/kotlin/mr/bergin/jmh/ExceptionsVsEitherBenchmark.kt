package mr.bergin.jmh

import arrow.core.Either
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open class ExceptionsVsEitherBenchmark {

    @Benchmark
    fun benchmark_100depth_throws_filledin() = handleExceptionInMono {
        benchmark(100.toUInt()) {
            throw FilledInException()
        }
    }

    @Benchmark
    fun benchmark_5depth_throws_filledin() = handleExceptionInMono {
        benchmark(5.toUInt()) {
            throw FilledInException()
        }
    }

    @Benchmark
    fun benchmark_0depth_throws_filledin() = handleExceptionInMono {
        benchmark {
            throw FilledInException()
        }
    }

    @Benchmark
    fun benchmark_100depth_throws_notfilledin() = handleExceptionInMono {
        benchmark(100.toUInt()) {
            throw NotFilledInException()
        }
    }

    @Benchmark
    fun benchmark_5depth_throws_notfilledin() = handleExceptionInMono {
        benchmark(5.toUInt()) {
            throw NotFilledInException()
        }
    }

    @Benchmark
    fun benchmark_0depth_throws_notfilledin() = handleExceptionInMono {
        benchmark {
            throw NotFilledInException()
        }
    }

    @Benchmark
    fun benchmark_100depth_either_filledin() = handleEitherInMono {
        benchmark(100.toUInt()) {
            Either.left(FilledInException())
        }
    }

    @Benchmark
    fun benchmark_5depth_either_filledin() = handleEitherInMono {
        benchmark(5.toUInt()) {
            Either.left(FilledInException())
        }
    }

    @Benchmark
    fun benchmark_0depth_either_filledin() = handleEitherInMono {
        benchmark {
            Either.left(FilledInException())
        }
    }

    @Benchmark
    fun benchmark_100depth_either_notfilledin() = handleEitherInMono {
        benchmark(100.toUInt()) {
            Either.left(NotFilledInException())
        }
    }

    @Benchmark
    fun benchmark_5depth_either_notfilledin() = handleEitherInMono {
        benchmark(5.toUInt()) {
            Either.left(NotFilledInException())
        }
    }

    @Benchmark
    fun benchmark_0depth_either_notfilledin() = handleEitherInMono {
        benchmark {
            Either.left(NotFilledInException())
        }
    }
}
